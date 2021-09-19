package actor_study.xii_patterns.ask

import actor_study.xii_patterns.ask.AskPatternDemo.{Read, Write}
import akka.actor.{Actor, ActorLogging}

class KVActor extends  Actor with ActorLogging{
  override def receive: Receive = online(Map())

  def online(map: Map[String, String]): Receive = {
    case Read(key) =>
      log.info(s"Trying to read value for key $key")
      sender() ! map.get(key)

    case Write(key, value) =>
      log.info(s"Writing key : $key for value : $value")
      context.become(online(map + (key -> value)))
  }
}
