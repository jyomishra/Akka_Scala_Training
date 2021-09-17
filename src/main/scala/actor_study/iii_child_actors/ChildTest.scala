package actor_study.iii_child_actors

import actor_study.iii_child_actors.ParentActor.{CreateChild, TellChild}
import akka.actor.{ActorSystem, Props}

object ChildTest extends App {

  val system = ActorSystem("actorSystem")

  val parent = system.actorOf(Props[ParentActor], "parent")

  parent ! CreateChild("child")
  parent ! TellChild("hey Kid!!")

}
