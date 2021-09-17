package actor_study.vi_life_cycle.restart

import actor_study.vi_life_cycle.restart.ParentActor.FailChild
import akka.actor.{ActorSystem, Props}

object Main extends App {

  val system = ActorSystem("lifecycle")

  val parentActor = system.actorOf(Props[ParentActor], "parent")

  parentActor ! FailChild
}
