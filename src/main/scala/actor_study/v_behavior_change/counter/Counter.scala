package actor_study.v_behavior_change.counter

import Counter.{Decrement, Increment, Print}
import Counter.{Decrement, Increment, Print}
import akka.actor.{Actor, Props}

object Counter {

  case class Increment()
  case class Decrement()
  case class Print()

  def props: Props = Props[Counter]
}

class Counter extends Actor {
  override def receive: Receive = countReceive(0)

  def countReceive(currentCounter: Int): Receive = {
    case Increment => context.become(countReceive(currentCounter + 1))
    case Decrement => context.become(countReceive(currentCounter - 1))
    case Print => println(s"[Counter] my current counter is ${currentCounter}")
  }
}
