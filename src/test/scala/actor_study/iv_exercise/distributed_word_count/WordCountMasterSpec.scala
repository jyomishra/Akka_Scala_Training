package actor_study.iv_exercise.distributed_word_count

import actor_study.iv_exercise.distributed_word_count.WordCountMaster.{GetTotalWordCount, Initialize, Register, RegisterAck, WordCountTask}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration.DurationInt

class WordCountMasterSpec extends TestKit(ActorSystem("TestProbeSuite"))
  with AnyWordSpecLike with ImplicitSender with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A Master Actor" should {
    "initialize children" in {
      val master = system.actorOf(Props[WordCountMaster], "master")
      master ! Initialize(5)
      expectNoMessage(1.second)
    }

    "initialize children and send some task" in {
      val master = system.actorOf(Props[WordCountMaster], "master1")
      master ! Initialize(5)
      master ! WordCountTask("Akka is awesome!!")
      master ! WordCountTask("Akka is awesome!!")
      master ! WordCountTask("Akka is awesome!!")
      Thread.sleep(1000)
      master ! GetTotalWordCount

      expectMsg(9)
    }

    "register children" in {
      val master = system.actorOf(Props[WordCountMaster], "master2")
      val child1 = TestProbe("child1")
      master ! Register(IndexedSeq(child1.ref))

      expectMsg(RegisterAck)
    }

    "register children and children should receive task" in {
      val master = system.actorOf(Props[WordCountMaster], "master3")
      val child1 = TestProbe("child1")
      master ! Register(IndexedSeq(child1.ref))
      master ! WordCountTask("Akka is awesome!!")
      expectMsg(RegisterAck)
      child1.expectMsg(WordCountTask("Akka is awesome!!"))
    }
  }


}
