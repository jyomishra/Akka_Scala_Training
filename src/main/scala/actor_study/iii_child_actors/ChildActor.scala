package actor_study.iii_child_actors

import akka.actor.Actor

class ChildActor extends Actor{
  override def receive: Receive = {
    case message: String => println(s"${self.path} I got : $message")
  }
}
