package actor_study.xii_patterns.ask

import actor_study.xii_patterns.ask.AskPatternDemo.{AuthFailure, AuthSuccess, Read}
import actor_study.xii_patterns.ask.Authenticator.{AUTH_FAILURE_NOT_FOUND, AUTH_FAILURE_PASSWORD_INCORRECT, AUTH_FAILURE_SYSTEM}
import akka.actor.{ActorRef, Props}
import akka.pattern.{ask, pipe}

object PipedAuthenticator {
  def props(kvActor: ActorRef): Props = Props(new PipedAuthenticator(kvActor))
}

class PipedAuthenticator(kvActor: ActorRef) extends Authenticator(kvActor: ActorRef) {

  override def handleAuthentication(uname: String, pwd: String): Unit = {
    val originalSender = sender()

    val future = kvActor ? Read(uname)
    val pwdFuture = future.mapTo[Option[String]]
    val responseFuture = pwdFuture.map {
      case None => AuthFailure(AUTH_FAILURE_NOT_FOUND)
      case Some(value) =>
        if (pwd.equals(value)) AuthSuccess
        else AuthFailure(AUTH_FAILURE_PASSWORD_INCORRECT)
    }

    responseFuture.pipeTo(sender())
  }
}
