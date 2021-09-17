package actor_study.iii_child_actors

import actor_study.iii_child_actors.ParentActor.{CreateChild, TellChild}
import akka.actor.{Actor, ActorRef, Props}
object ParentActor {
  case class CreateChild(name: String)
  case class TellChild(msg: String)
}

class ParentActor extends Actor{
  override def receive: Receive = {
    case CreateChild(name) => {
      println(s"${self.path} is creating a child")
      val childRef = context.actorOf(Props[ChildActor],name)
      context.become(withChild(childRef))
    }
  }

  def withChild(childRef: ActorRef): Receive = {
    case TellChild(msg) => childRef forward msg
  }
}
