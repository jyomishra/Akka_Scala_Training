package actor_study.viii_scheduling.simple

import akka.actor.{Actor, ActorLogging, ActorSystem, Cancellable, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object SimpleScheduling extends App {

  class SelfClosingActor extends Actor with ActorLogging{

    var schedule = createTimeoutWindow()
    def createTimeoutWindow(): Cancellable =
      context.system.scheduler.scheduleOnce(1 second) {
        self ! "timeout"
      }

    override def receive: Receive = {
      case "timeout" =>
        log.info("Stopping myself")
        context.stop(self)
      case msg =>
        log.info(s"Received msg : $msg staying alive")
        schedule.cancel()
        schedule = createTimeoutWindow()
    }
  }

  val system = ActorSystem("schedulingTest")
  val selfClosingActor = system.actorOf(Props[SelfClosingActor],"selfClosingActor")

  system.scheduler.scheduleOnce(250 millis,
    selfClosingActor, "ping")

  system.scheduler.scheduleOnce(2 seconds,
    selfClosingActor, "pong")

}
