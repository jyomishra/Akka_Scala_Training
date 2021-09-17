package actor_study.ii_capabilities

import actor_study.ii_capabilities.SimpleActor.{EchoMessage, SayHiBack, SayHiTo, SpecialMessage}
import akka.actor.{Actor, ActorRef, Props}

object SimpleActor {
  case class SpecialMessage(msg: String)
  case class EchoMessage(msg : String)
  case class SayHiTo(msg: String, ref: ActorRef)
  case class SayHiBack(msg: String)
  def props: Props = Props(new SimpleActor())
}

class SimpleActor extends Actor{
  override def receive: Receive = {
    case msg: String => println(s"${context.self} I got a message : $msg")
    case num: Int => println(s"$self I got a number $num")
    case SpecialMessage(msg) => println(s"[Simple Actor] I got a special message $msg" )
    case EchoMessage(msg) => {
      println("[Simple Actor] Sending messing to self")
      self ! msg
    }
    case SayHiTo(msg, receiver) => {
      //println(s"[Simple Actor] Sending messing to ${receiver.path.name}")
      receiver ! SayHiBack(s"Hi, I am ${self.path.name}. Message : $msg")
    }
    case SayHiBack(msg) => {
      //println(s"[Simple Actor] Got message from ${context.sender().path.name} ------ $msg")
      sender() ! s"Hi ${context.sender().path.name}, I got your message  -${self.path.name}"
    }
  }
}

