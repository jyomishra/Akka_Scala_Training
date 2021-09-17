package recap

import akka.actor.ActorSystem

object PlayGround extends App {
   val actorSystem = ActorSystem("MySystem")
   println(actorSystem.name)
}
