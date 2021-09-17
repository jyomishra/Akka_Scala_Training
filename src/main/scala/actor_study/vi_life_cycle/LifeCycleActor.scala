package actor_study.vi_life_cycle

import actor_study.vi_life_cycle.LifeCycleActor.{StartChild, StopChild}
import akka.actor.{Actor, ActorLogging, PoisonPill, Props, Terminated}

object LifeCycleActor{
  case class StartChild(name: String)
  case class StopChild(name: String)
}

class LifeCycleActor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("In to pre start")
  override def postStop(): Unit = log.info("In to post stop")

  override def receive: Receive = {
    case StartChild(name) =>
      val child = context.actorOf(Props[LifeCycleActor], name)
      context.watch(child)
    case StopChild(name) =>
      val optActorRef = context.child(name)
      optActorRef.map(ref => ref ! PoisonPill)
    case Terminated(child) =>
      log.info(s"Terminating child : ${child.path.name}")
  }
}
