package actor_study.xii_patterns.fsm

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

case class Initialize(inventory: Map[String, Int], prices: Map[String, Int])
case class RequestProduct(product: String)
case class Instruction(instruction: String) // message the vending machine show on screen
case class ReceiveMoney(amount: Int)
case class Deliver(product: String)
case class GiveBackChange(amount: Int)
case class VendingError(reason: String)
case object ReceiveMoneyTimeout
/**
 * Vending Machine
 */
class VendingMachine extends Actor with ActorLogging{
  override def receive: Receive = idle

  def idle: Receive = {
    case Initialize(inventory, prices) => context.become(operational(inventory, prices))
    case _ => sender() ! VendingError("MachineNotInitialized")
  }

  def operational(inventory: Map[String, Int], prices: Map[String, Int]): Receive = {
    case RequestProduct(product) =>
      inventory.get(product) match {
        case None => sender() ! VendingError("ProductNotFound")
        case Some(value) =>
          val price = prices(product)
          sender() ! Instruction(s"Please insert $price for product $product")
          context.become(waitForMoney(inventory, prices, product, 0, startReceiveMoneyTimeoutSchedule(), sender()))
      }
  }

  def waitForMoney(inventory: Map[String, Int], prices: Map[String, Int],
                   product: String, initialMoney: Int, moneyTimeoutScheduler: Cancellable,
                   requester: ActorRef): Receive = {
    case ReceiveMoneyTimeout =>
      requester ! VendingError("ReceiveMoneyTimeout")
      if(initialMoney > 0) requester ! GiveBackChange(initialMoney)
      context.become(operational(inventory, prices))
    case ReceiveMoney(amount) =>
      moneyTimeoutScheduler.cancel()
      val price = prices(product)
      var change = amount + initialMoney - price
      if(change < 0) {
        change = Math.abs(change)
        sender() ! Instruction(s"Please insert remaining $change for product $product")
        context.become(waitForMoney(inventory, prices, product, amount + initialMoney, startReceiveMoneyTimeoutSchedule(), sender()))
      } else {
        sender() ! Instruction(s"Please collect your product $product and change $change")
        sender() ! Deliver(product)
        if (change > 0) {
          sender() ! GiveBackChange(change)
        }
        val newStock = inventory(product) - 1
        val newInventory = inventory + (product -> newStock)
        context.become(operational(newInventory, prices))
      }
  }

  def startReceiveMoneyTimeoutSchedule(): Cancellable = context.system.scheduler.scheduleOnce(1 second) {
    self ! ReceiveMoneyTimeout
  }
}
