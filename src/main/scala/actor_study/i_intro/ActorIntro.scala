package actor_study.i_intro

import actor_study.i_intro.WordCountActor.{Message, PrintCount}
import akka.actor.ActorSystem

object ActorIntro extends App {

  // part 1 create actor system
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  //part 2 create actor
  val firstActor = actorSystem.actorOf(WordCountActor.props(), "worldCountActor")

  firstActor ! Message("This is my first Sentence")
  firstActor ! Message("This is my second Sentence")
  firstActor ! Message("This is my third Sentence")

  firstActor ! PrintCount

  sys.exit()
}
