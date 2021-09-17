package actor_study.vii_supervision.backoff_pattern

import actor_study.vii_supervision.backoff_pattern.FileBasedPersistentActor.ReadFile
import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import akka.pattern.{BackoffOpts, BackoffSupervisor}

import scala.concurrent.duration.DurationInt
import scala.io.Source

object FileBasedPersistentActor {
  case class ReadFile()

  def failureBackoffProps : Props = BackoffSupervisor.props(
    BackoffOpts.onFailure(
      Props[FileBasedPersistentActor],
      "simpleBackOffActor",
      3.second,
      30.seconds,
      0.2
    )
  )

  def stopBackoffProps : Props = BackoffSupervisor.props(
    BackoffOpts.onStop(
      Props[FileBasedPersistentActor],
      "simpleBackOffActor",
      3.second,
      30.seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    )
  )
}

class FileBasedPersistentActor extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("Persistent actor starting")

  override def postStop(): Unit = log.info("Persistent actor stopped")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("Persistent actor restarting")
  }

  var dataSource: Source = null
  override def receive: Receive = {
    case ReadFile =>
      if(dataSource == null)
        dataSource = Source.fromFile("src/main/resources/test/file/important1.txt")
      log.info("I have just read some important with number of lines: "+dataSource.getLines().length)
  }
}
