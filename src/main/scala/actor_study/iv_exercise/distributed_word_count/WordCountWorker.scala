package actor_study.iv_exercise.distributed_word_count

import actor_study.iv_exercise.distributed_word_count.WordCountMaster.{WordCountReply, WordCountTask}
import akka.actor.Actor

class WordCountWorker extends Actor {
  override def receive: Receive = {
    case WordCountTask(text) =>
      sender() ! WordCountReply(text.split(" ").length)
  }
}
