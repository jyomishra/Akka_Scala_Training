package actor_study.vii_supervision.backoff_pattern

import actor_study.vii_supervision.backoff_pattern.FileBasedPersistentActor.{ReadFile, failureBackoffProps, stopBackoffProps}
import akka.actor.{ActorSystem, Props}

object BackoffSupervisionPattern extends App {

  val system = ActorSystem("BackoffSupervision")
  /*val persistentActor = system.actorOf(failureBackoffProps,"persiActor")

  //persistentActor ! ReadFile

  val stopBackoffActor = system.actorOf(stopBackoffProps, "stopBackoffActor")
  stopBackoffActor ! ReadFile*/

  //val eagerActor = system.actorOf(Props[EagerFBPActor])
  // ActorInitializationException ==> Stop

  val eagerFBPActor = system.actorOf(EagerFBPActor.eagerFBPActorProps)
  // it will retry to start actor with exponential backoff
}
