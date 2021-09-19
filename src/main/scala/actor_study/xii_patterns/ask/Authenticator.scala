package actor_study.xii_patterns.ask

import actor_study.xii_patterns.ask.AskPatternDemo.{AuthFailure, AuthSuccess, Authenticate, Read, RegisterUser, Write}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

object Authenticator {
  val AUTH_FAILURE_NOT_FOUND = "username not found"
  val AUTH_FAILURE_PASSWORD_INCORRECT = "password incorrect"
  val AUTH_FAILURE_SYSTEM = "system error"

  def props(kvActor: ActorRef): Props = Props(new Authenticator(kvActor))
}

class Authenticator(kVActor: ActorRef) extends Actor with ActorLogging{

  import Authenticator._

  // step 2 - logistics
  implicit val timeout: Timeout = Timeout(1 second)
  implicit val executionContext: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case RegisterUser(username, password) => kVActor ! Write(username, password)
    case Authenticate(username, password) => handleAuthentication(username, password)
  }

  def handleAuthentication(uname: String, pwd: String): Unit = {
    val originalSender = sender()

    val future = kVActor ? Read(uname)

    future.onComplete {
      case Success(None) =>originalSender ! AuthFailure(AUTH_FAILURE_NOT_FOUND)
      case Success(Some(value)) =>
        if(pwd.equals(value)) originalSender ! AuthSuccess
        else originalSender ! AuthFailure(AUTH_FAILURE_PASSWORD_INCORRECT)
      case Failure(exception) =>
        originalSender ! AuthFailure(AUTH_FAILURE_SYSTEM)
    }
  }

}
