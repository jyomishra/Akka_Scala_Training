package actor_study.xii_patterns.fsm

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, OneInstancePerTest}
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration.DurationDouble
import scala.language.postfixOps

class VendingMachineGeneralSpec extends TestKit(ActorSystem("VendingMachineSpec"))
  with AnyWordSpecLike with BeforeAndAfterAll
  with ImplicitSender with OneInstancePerTest {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def runTestSuit(props: Props): Unit = {
    "give error when not initialized" in {
      val vendingMachine = system.actorOf(props)
      println(vendingMachine.path.toString)
      vendingMachine ! RequestProduct("Chocolate")

      expectMsg(VendingError("MachineNotInitialized"))
    }

    "be initialized by passing inventory and price list" in {
      val vendingMachine = system.actorOf(props)
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 1))
      vendingMachine ! RequestProduct("coke")

      expectMsgType[Instruction]
    }

    "report error when product not available" in {
      val vendingMachine = system.actorOf(props)
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 1))
      vendingMachine ! RequestProduct("pepsi")

      expectMsg(VendingError("ProductNotFound"))
    }

    "throw a timeout if I don't insert money" in {
      val vendingMachine = system.actorOf(props)
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 1))
      vendingMachine ! RequestProduct("coke")

      expectMsg(Instruction(s"Please insert 1 for product coke"))

      within(1.5 second) {
        expectMsg(VendingError("ReceiveMoneyTimeout"))
      }
    }

    "throw a timeout and give back change if I don't insert full money" in {
      val vendingMachine = system.actorOf(props, "v1")
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 2))

      vendingMachine ! RequestProduct("coke")
      expectMsg(Instruction(s"Please insert 2 for product coke"))

      vendingMachine ! ReceiveMoney(1)
      expectMsg(Instruction(s"Please insert remaining 1 for product coke"))

      within(1.5 second) {
        expectMsg(VendingError("ReceiveMoneyTimeout"))
        expectMsg(GiveBackChange(1))
      }
    }

    "deliver a product if I insert the money" in {
      val vendingMachine = system.actorOf(props,"v2")
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 2))

      vendingMachine ! RequestProduct("coke")
      expectMsg(Instruction("Please insert 2 for product coke"))

      vendingMachine ! ReceiveMoney(2)
      expectMsg(Instruction("Please collect your product coke and change 0"))
      expectMsg(Deliver("coke"))
    }

    "deliver a product and give back change" in {
      val vendingMachine = system.actorOf(props, "v3")
      println(vendingMachine.path.toString)
      vendingMachine ! Initialize(Map("coke" -> 10), Map("coke" -> 2))

      vendingMachine ! RequestProduct("coke")
      expectMsg(Instruction("Please insert 2 for product coke"))

      vendingMachine ! ReceiveMoney(3)
      expectMsg(Instruction("Please collect your product coke and change 1"))
      expectMsg(Deliver("coke"))
      expectMsg(GiveBackChange(1))
    }
  }
}
