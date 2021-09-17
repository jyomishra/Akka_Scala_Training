package actor_study.vi_life_cycle

import actor_study.vi_life_cycle.LifeCycleActor.{StartChild, StopChild}
import akka.actor.{ActorSystem, PoisonPill, Props}

object Main extends App {

  val system = ActorSystem("lifecycle")
  val parent = system.actorOf(Props[LifeCycleActor], "parent")

  parent ! StartChild("child1")
  parent ! StopChild("child1")

  parent ! StartChild("child2")
  parent ! StopChild("child2")

  //parent ! StartChild("child3")
  //parent ! StartChild("child4")
  //parent ! StartChild("child5")

  //parent ! PoisonPill
}
