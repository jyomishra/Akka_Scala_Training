package actor_study.vi_life_cycle.restart

import actor_study.vi_life_cycle.restart.ParentActor.Fail
import akka.actor.{Actor, ActorLogging}

class ChildActor extends Actor with ActorLogging{

  override def preStart(): Unit = log.info("supervised actor started")
  override def postStop(): Unit = log.info("supervised actor stopped")

  override def postRestart(reason: Throwable): Unit =
    log.info(s"supervised actor restarted")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
    log.info(s"supervised actor restarting because of ${reason.getMessage}")

  override def receive: Receive = {
    case Fail =>
      log.warning("Child will fail now")
      throw new RuntimeException("I failed")
  }
}
