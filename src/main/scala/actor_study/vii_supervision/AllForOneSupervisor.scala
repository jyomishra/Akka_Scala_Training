package actor_study.vii_supervision

import akka.actor.{AllForOneStrategy, OneForOneStrategy, SupervisorStrategy}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}

class AllForOneSupervisor extends Supervisor {

  // apply for all the user actors in the system
  override val supervisorStrategy: SupervisorStrategy = AllForOneStrategy() {
    case _:NullPointerException => Restart
    case _:IllegalArgumentException => Stop
    case _:UnsupportedOperationException => Escalate
    case _:RuntimeException => Resume
    case _:Exception => Escalate
  }

}
