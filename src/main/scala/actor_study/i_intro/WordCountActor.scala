package actor_study.i_intro

import actor_study.i_intro.WordCountActor.{Message, PrintCount}
import akka.actor.{Actor, Props}

object WordCountActor {
  case class PrintCount()
  case class Message(msg: String)

  def props(): Props = Props(new WordCountActor())
}

class WordCountActor extends Actor {

  var count = 0;

  override def receive: Receive = {
    case m: Message => count += m.msg.split(" ").length
    case PrintCount => println(s"World count is : $count")
    case _ => println("Unknown message")
  }

}
