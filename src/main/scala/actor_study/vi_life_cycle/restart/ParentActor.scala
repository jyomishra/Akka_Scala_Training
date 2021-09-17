package actor_study.vi_life_cycle.restart

import actor_study.vi_life_cycle.restart.ParentActor.{Fail, FailChild}
import akka.actor.{Actor, ActorLogging, Props, Terminated}

object ParentActor {
  case class Fail()
  case class FailChild()
}

class ParentActor extends Actor with ActorLogging{
  private val child = context.actorOf(Props[ChildActor], "child")

  override def receive: Receive = {
    case FailChild => child ! Fail
    case Terminated(child) =>
      log.info(s"Child died ${child.path.name}")
  }
}
