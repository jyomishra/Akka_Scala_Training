package testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import testing.BasicSpec.{BlackHole, SimpleActor}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class BasicSpec()
  extends TestKit(ActorSystem("MySpec"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A simple actor" should {
    "send back the same message" in {
      val echoActor = system.actorOf(Props[SimpleActor],"simpleActor")
      val msg = "Hello World!!"
      echoActor ! msg
      expectMsg(msg)
    }
  }

  "A blackhole actor" should {
    "send back the same message" in {
      val echoActor = system.actorOf(Props[BlackHole],"simpleActor")
      val msg = "Hello World!!"
      echoActor ! msg
      expectNoMessage(1.second)
    }
  }
}

object BasicSpec {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }

  class BlackHole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }
}
