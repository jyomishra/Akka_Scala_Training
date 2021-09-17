package actor_study.x_dispatchers

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object Dispatchers extends App {

  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case msg =>
        count += 1
        log.info(s"Got msg [$count] : $msg")
    }
  }

  val system = ActorSystem("demo")//, ConfigFactory.load().getConfig("dispatcherDemo"))

  val counterActors =
    (1 to 10).map(i => system.actorOf(Props[Counter].withDispatcher("my-dispatcher"), s"counter_$i"))

  val r = new Random()
  (1 to 1000).foreach(i => {
    //counterActors(r.nextInt(9)) ! i
  })

  class DBActor extends Actor with ActorLogging {

    implicit val executionContext : ExecutionContext = context.dispatcher

    override def receive: Receive = {
      case msg => Future {
        Thread.sleep(5000)
        log.info(s"Success : $msg")
      }
    }
  }

  val dbActor = system.actorOf(Props[DBActor])
  dbActor ! "the meaning of life is ---"
}
