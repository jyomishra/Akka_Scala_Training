package actor_study.xii_patterns.ask

import actor_study.xii_patterns.ask.AskPatternDemo.{AuthFailure, AuthSuccess, Authenticate, RegisterUser}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class AuthenticatorSpec extends TestKit(ActorSystem("AskPatternSpec"))
  with AnyWordSpecLike with BeforeAndAfterAll with ImplicitSender {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "An authenticator actor" should {
    val kvActor = system.actorOf(Props[KVActor])
    authenticatorTestSuite(Authenticator.props(kvActor))
  }

  "An piped authenticator" should {
    val kvActor = system.actorOf(Props[KVActor])
    authenticatorTestSuite(PipedAuthenticator.props(kvActor))
  }

  private def authenticatorTestSuite(props: Props) = {
    "be able to register and authenticate the users" in {

      val authenticator = system.actorOf(props)

      authenticator ! RegisterUser("Amogh", "chotu")
      authenticator ! Authenticate("Amogh", "chotu")

      expectMsg(AuthSuccess)
    }

    "send auth failure if wrong credentials are sent" in {
      val authenticator = system.actorOf(props)

      authenticator ! RegisterUser("Amogh", "chotu")
      authenticator ! Authenticate("Amogh", "chotu1")

      expectMsg(AuthFailure(Authenticator.AUTH_FAILURE_PASSWORD_INCORRECT))
    }

    "send auth failure if user is not registered" in {
      val authenticator = system.actorOf(props)

      authenticator ! Authenticate("Amogh1", "chotu1")

      expectMsg(AuthFailure(Authenticator.AUTH_FAILURE_NOT_FOUND))
    }
  }

}
