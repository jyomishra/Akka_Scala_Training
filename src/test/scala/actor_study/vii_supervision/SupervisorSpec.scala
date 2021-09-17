package actor_study.vii_supervision

import actor_study.vii_supervision.FussyWordCount.Report
import akka.actor.{ActorRef, ActorSystem, Props, Terminated}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class SupervisorSpec extends TestKit(ActorSystem("SupervisionSpec"))
  with AnyWordSpecLike with BeforeAndAfterAll
  with ImplicitSender {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A supervisor" should {
    "resume its child in case of minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCount]
      val child = expectMsgType[ActorRef]
      child ! "I love Akka!!"
      child ! Report
      expectMsg(3)

      child ! "Akka is awesome because I am learning to think a whole new way"
      child ! Report
      expectMsg(3)
    }

    "restart its child in case of empty string" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCount]
      val child = expectMsgType[ActorRef]
      child ! "I love Akka!!"
      child ! Report
      expectMsg(3)

      child ! ""
      child ! Report
      expectMsg(0)
    }

    "terminate its child in case of major error" in {
      val supervisor = system.actorOf(Props[Supervisor])
      supervisor ! Props[FussyWordCount]
      val child = expectMsgType[ActorRef]
      watch(child)

      child ! "akka is easy"
      val termMsg = expectMsgType[Terminated]
      assert(termMsg.actor == child)
    }

    "escalate the error when it doesn't know wah to do" in {
      val supervisor = system.actorOf(Props[Supervisor],"sup1")
      supervisor ! Props[FussyWordCount]
      val child = expectMsgType[ActorRef]

      watch(child)
      child ! 42

      val termMsg = expectMsgType[Terminated]
      assert(termMsg.actor == child)
    }
  }

  "An all-for-one supervisor strategy" should {
     "apply all-for-one strategy" in {
       val supervisor = system.actorOf(Props[AllForOneSupervisor],"allforone")
       supervisor ! Props[FussyWordCount]
       val child1 = expectMsgType[ActorRef]

       supervisor ! Props[FussyWordCount]
       val child2 = expectMsgType[ActorRef]

       child2 ! "Testing supervision"
       child2 ! Report
       expectMsg(2)

       child1 ! ""
       Thread.sleep(500)

       child2 ! Report
       expectMsg(0)
     }
  }
}
