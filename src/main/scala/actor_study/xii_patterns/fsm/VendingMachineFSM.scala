package actor_study.xii_patterns.fsm

import akka.actor.{ActorRef, Cancellable, FSM}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

trait VendingState
case object Idle extends VendingState
case object Operational extends VendingState
case object WaitForMoney extends VendingState

trait VendingData
case object Uninitialized extends VendingData
case class Initialized(inventory: Map[String, Int], prices: Map[String, Int]) extends VendingData
case class WaitForMoneyData(inventory: Map[String, Int], prices: Map[String, Int],
                            product: String, initialMoney: Int, requester: ActorRef) extends VendingData

/**
 *    state, data
      event => state and data can be changed.

      state = Idle
      data = Uninitialized

      event(Initialize(Map(coke -> 10), Map(coke -> 1)))
        =>
        state = Operational
        data = Initialized(Map(coke -> 10), Map(coke -> 1))


      event(RequestProduct(coke))
        =>
        state = WaitForMoney
        data = WaitForMoneyData(Map(coke -> 10), Map(coke -> 1), coke, 0, R)

      event(ReceiveMoney(2))
        =>
        state = Operational
        data = Initialized(Map(coke -> 9), Map(coke -> 1))
 */
class VendingMachineFSM extends FSM[VendingState, VendingData] {
  // We don't have a receive handler
  // We have Event(message, Data)
  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(Initialize(inventory, prices), Uninitialized) =>
      goto(Operational) using Initialized(inventory, prices)
    case _ =>
      sender() ! VendingError("MachineNotInitialized")
      stay()
  }

  when(Operational) {
    case Event(RequestProduct(product), Initialized(inventory, prices)) =>
      inventory.get(product) match {
        case None | Some(0) =>
          sender() ! VendingError("ProductNotFound")
          stay()
        case Some(_) =>
          val price = prices(product)
          sender() ! Instruction(s"Please insert $price for product $product")
          goto(WaitForMoney) using WaitForMoneyData(inventory, prices, product, 0, sender())
      }
  }

  when(WaitForMoney, stateTimeout = 1 second) {
    case Event(StateTimeout, WaitForMoneyData(inventory, prices, _, initialMoney, requester)) =>
      requester ! VendingError("ReceiveMoneyTimeout")
      if (initialMoney > 0) requester ! GiveBackChange(initialMoney)
      goto(Operational) using Initialized(inventory, prices)
    case Event(ReceiveMoney(amount), WaitForMoneyData(inventory, prices, product, initialMoney, requester)) =>
      val price = prices(product)
      var change = amount + initialMoney - price
      if (change < 0) {
        change = Math.abs(change)
        requester ! Instruction(s"Please insert remaining $change for product $product")
        stay() using WaitForMoneyData(inventory, prices, product, amount + initialMoney, sender())
      } else {
        requester ! Instruction(s"Please collect your product $product and change $change")
        requester ! Deliver(product)
        if (change > 0) {
          requester ! GiveBackChange(change)
        }
        val newStock = inventory(product) - 1
        val newInventory = inventory + (product -> newStock)
        goto(Operational) using Initialized(newInventory, prices)
      }
  }

  whenUnhandled{
    case Event(_, _) =>
      sender() ! VendingError("CommandNotFound")
      stay()
  }

  onTransition {
    case stateA -> stateB => log.info(s"Transitioning from $stateA to $stateB")
  }

  // If y ou don't initialize whole FSM Actor will not start
  initialize()
}
