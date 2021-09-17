package actor_study.ii_capabilities

import actor_study.ii_capabilities.SimpleActor.{EchoMessage, SayHiBack, SayHiTo, SpecialMessage}
import akka.actor.{Actor, ActorSystem}

object ActorCapabilities extends App {
  val system = ActorSystem.apply("actorCapabilityDemo")

  val actor = system.actorOf(SimpleActor.props, "simpleActor1")
  actor ! "Hello, Actor"

  // 1. Number can be of any type
  actor ! 42

  // 2. Can send any user defined type
  // 3. Message must be immutable
  // 4. Message must be serializable
  // 5. In practice use case class and case objects
  actor ! SpecialMessage("hello, world!!!")

  // 6. Actors have information about their context and about themselves
  // context.self == this in OOP
  actor ! EchoMessage("Echo")

  //7. Actor can reply to message
  val alice = system.actorOf(SimpleActor.props, "alice")
  val bob = system.actorOf(SimpleActor.props, "bob")

  alice ! SayHiTo("#####Hi######", bob)

  alice ! SayHiBack(" Hi ")
}
