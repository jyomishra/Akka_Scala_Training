package actor_study.xii_patterns.fsm

import akka.actor.Props
import akka.testkit.TestKit

import scala.language.postfixOps

class VendingMachineFSMSpec extends VendingMachineGeneralSpec {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A vending machine FSM" should {
    runTestSuit(Props[VendingMachineFSM])
  }
}