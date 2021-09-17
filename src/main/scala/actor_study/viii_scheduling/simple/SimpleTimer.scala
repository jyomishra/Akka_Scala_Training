package actor_study.viii_scheduling.simple

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Timers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object SimpleTimer extends App {

  case object Start
  case object TimerKey
  case object Reminder
  case object Stop

  class TimerBasedHeartbeatActor extends Actor with ActorLogging with Timers {

    timers.startSingleTimer(TimerKey, Start, 500 millis)

    override def receive: Receive = {
      case Start =>
        log.info("Bootstrapping")
        timers.startTimerAtFixedRate(TimerKey, Reminder, 500 millis, 1 second)
      case Reminder =>
        log.info("I am alive")
      case Stop =>
        log.warning("Stopping!!!!")
        timers.cancel(TimerKey)
        timers.cancelAll()
        context.stop(self)
    }
  }

  val system = ActorSystem("TimerBasedHeartbeat")
  val heartbeatActor = system.actorOf(Props[TimerBasedHeartbeatActor])

  system.scheduler.scheduleOnce(5 second) {
    heartbeatActor ! Stop
  }

}
