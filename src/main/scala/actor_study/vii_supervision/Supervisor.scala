package actor_study.vii_supervision

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}

class Supervisor extends  Actor {

   override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _:NullPointerException => Restart
    case _:IllegalArgumentException => Stop
    case _:UnsupportedOperationException => Escalate
    case _:RuntimeException => Resume
    case _:Exception => Escalate
  }

  override def receive: Receive = {
    case props: Props =>
      val childRef = context.actorOf(props)
      sender() ! childRef
  }
}
