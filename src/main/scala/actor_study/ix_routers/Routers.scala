package actor_study.ix_routers

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Terminated}
import akka.routing._
/*
  Supported Routing logics :
  1. Round-Robin
  2. Random
  3. Smallest Mailbox first
  4. Broadcast
  5. Scatter-gather first
  6. Tail-chopping
  7. Consistent-hashing
 */
object Routers extends App {

  class ManualLeader extends Actor {

    /** 1. Manual Router **/
    private val workers = for(i <- 1 to 5) yield {
      // step 1: create workers
      val worker = context.actorOf(Props[Worker], s"wrk_$i")
      context.watch(worker)
      ActorRefRoutee(worker)
    }
    // step 2: Define router
    private val router = Router(RoundRobinRoutingLogic(), workers)

    override def receive: Receive = onMessage(router)

    private def onMessage(router: Router): Receive = {
      // Step 4: Handle terminate msg of workers
      case Terminated(ref) =>
        context.become(onMessage(router.removeRoutee(ref)))
        context.unwatch(ref)
        val worker = context.actorOf(Props[Worker])
        context.watch(worker)
        context.become(onMessage(router.addRoutee(worker)))
      //Step 3: route msg to workers
      case msg => router.route(msg, sender())
    }
  }

  class Worker extends Actor with ActorLogging{
    override def receive: Receive = {
      case msg => log.info(s"Got message : $msg")
    }
  }

  val system = ActorSystem("RouterSystem")

  val manualLeader = system.actorOf(Props[ManualLeader], "leader")

  /**
   * Method #2 -> A router actor with its own children
   * Pool router
   */
  private val poolLeader =system.actorOf(RoundRobinPool(5).props(Props[Worker]),"poolLeader")
  (1 to 10).foreach(i => {
    poolLeader ! s"Hello message $i from world "
  })

  poolLeader ! Broadcast("Hello world!!")

  /**
   * Method #3 -> A router actor with actor created elsewhere
   * Group router
   */
  //in other part of application
  val workerList = (1 to 5).map(i => system.actorOf(Props[Worker], s"worker_$i"))
  //need their path
  val workerPaths = workerList.map(_.path.toString)

  val groupLeader = system.actorOf(RoundRobinGroup(workerPaths).props())
  (1 to 10).foreach(i => {
    groupLeader ! s"Hello message to group $i from world "
  })

  // PoisonPill, Kill are not routed
  // Add, Remove and Get routee can be done from router actor
}
