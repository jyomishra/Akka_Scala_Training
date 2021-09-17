package actor_study.vii_supervision

import actor_study.vii_supervision.FussyWordCount.Report
import akka.actor.Actor

object FussyWordCount {
  case class Report()
}

class FussyWordCount extends Actor {
  var words = 0

  override def receive: Receive = {
    case Report => sender() ! words
    case "" => throw new NullPointerException("sentence empty")
    case sentence: String =>
      if(sentence.length >20)
        throw new RuntimeException("sentence too big")
      else if(!Character.isUpperCase(sentence(0)))
        throw new IllegalArgumentException("sentence must start with capital")
      else
        words += sentence.split(" ").length
    case _ => throw new Exception("can receive only string")
  }
}
