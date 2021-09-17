package actor_study.iv_exercise.distributed_word_count

import actor_study.iv_exercise.distributed_word_count.WordCountMaster.{Initialize, Print, WordCountTask}
import akka.actor.{ActorSystem, Props}

object Main extends App {

  val actorSystem = ActorSystem("wordCount")

  val master = actorSystem.actorOf(Props[WordCountMaster],"master")

  master ! Initialize(5)

  (0 to 9).foreach(_ => master ! WordCountTask("Akka is awesome!!"))

  Thread.sleep(1000)
  master ! Print
}
