package actor_study.v_behavior_change.counter

import Counter.{Decrement, Increment, Print}
import Counter.{Decrement, Increment, Print}
import akka.actor.ActorSystem

object CounterTest extends App {

  val system = ActorSystem("counterSystem")

  val counterActor = system.actorOf(Counter.props, "counterActor")

  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Decrement
  counterActor ! Decrement
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Increment
  counterActor ! Print
}
