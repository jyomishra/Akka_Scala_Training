package actor_study.vii_supervision.backoff_pattern

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{OneForOneStrategy, Props}
import akka.pattern.{BackoffOpts, BackoffSupervisor}

import scala.concurrent.duration.DurationInt
import scala.io.Source

object EagerFBPActor {
  def eagerFBPActorProps : Props = BackoffSupervisor.props(
    BackoffOpts.onStop(
      Props[EagerFBPActor],
      "simpleBackOffActor",
      1.second,
      30.seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    )
  )
}

class EagerFBPActor extends FileBasedPersistentActor {
  override def preStart(): Unit = {
    log.info("Eager actor starting")
    dataSource = Source.fromFile("src/main/resources/test/file/important1.txt")
  }
}
