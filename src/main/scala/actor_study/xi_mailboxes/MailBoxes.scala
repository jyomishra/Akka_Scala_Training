package actor_study.xi_mailboxes

import akka.actor.ActorSystem.Settings
import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}
import akka.dispatch.{ControlMessage, PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.{Config, ConfigFactory}

object MailBoxes extends App {

  val system = ActorSystem("mailboxDemo", ConfigFactory.load().getConfig("mailboxDemo"))

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case msg => log.info(s"Message : $msg")
    }
  }

  /**
   * Interesting case #1 - Custom priority mailbox
   * P0 -> most important then
   * P1, P2, P3
   */
  // Step #1 => Mailbox Definition
  class SupportTicketPriorityMailbox(settings: Settings, config: Config)
    extends UnboundedPriorityMailbox(
      PriorityGenerator{
        case msg:String if msg.startsWith("[P0]") => 1
        case msg:String if msg.startsWith("[P1]") => 2
        case msg:String if msg.startsWith("[P2]") => 3
        case msg:String if msg.startsWith("[P3]") => 4
        case _ => 4
    })

  // Step #2 => Make it known to config
  // Step #3 => Attach dispatcher to actor

  val supportTicketLogger = system.actorOf(Props[SimpleActor].withDispatcher("support-ticket-dispatcher"))

  /*supportTicketLogger ! PoisonPill
  supportTicketLogger ! "[P2] This is a support message"
  supportTicketLogger ! "[P0] This is a support message"
  supportTicketLogger ! "[P1] This is a support message"
  supportTicketLogger ! "[P1] This is a support message"
  supportTicketLogger ! "[P0] This is a support message"
  supportTicketLogger ! "[P1] This is a support message"
*/
  /**
   * Interesting case #2 - Control-aware mailbox
   * We will use UnboundedControlAwareMailbox
   */

  // Step 1: Mark important message as control message
  case object ManagementTicket extends ControlMessage

  // Step 2: Attach actor to mailbox
  val controlAwareActor = system.actorOf(Props[SimpleActor].withMailbox("control-mailbox"))

  controlAwareActor ! "[P2] This is a support message"
  controlAwareActor ! "[P0] This is a support message"
  controlAwareActor ! "[P1] This is a support message"
  controlAwareActor ! ManagementTicket

}
