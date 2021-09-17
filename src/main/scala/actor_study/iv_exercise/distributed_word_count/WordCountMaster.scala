package actor_study.iv_exercise.distributed_word_count

import actor_study.iv_exercise.distributed_word_count.WordCountMaster.{GetTotalWordCount, Initialize, Print, Register, RegisterAck, WordCountReply, WordCountTask}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object WordCountMaster {
  case class Register(actorRefs: Seq[ActorRef])
  case class Initialize(nChildren: Int)
  case class WordCountTask(text: String)
  case class WordCountReply(count: Int)
  case class Print()
  case class GetTotalWordCount()
  case class RegisterAck()
}
class WordCountMaster extends Actor with ActorLogging{
  override def receive: Receive = {
    case Register(actorRefs) =>
      context.become(receiveWithChildren(actorRefs.toIndexedSeq,0, 0))
      sender() ! RegisterAck
    case Initialize(nChildren) =>
      log.info(s"${self.path} Creating $nChildren children")
      val childrenRefs = (0 until nChildren).map(i => context.actorOf(Props[WordCountWorker],"child"+i))
      context.become(receiveWithChildren(childrenRefs,0, 0))
    case _ => log.info("Please initialize master first")
  }

  def receiveWithChildren(childrenRefs: IndexedSeq[ActorRef], currentWorker: Int, totalCount: Int): Receive = {
    case WordCountTask(text) =>
      childrenRefs(currentWorker) ! WordCountTask(text)
      val nextWorker = if(currentWorker == childrenRefs.length - 1) 0 else currentWorker + 1
      context.become(receiveWithChildren(childrenRefs,nextWorker, totalCount))
    case WordCountReply(count) =>
      log.info(s"Got word count reply $count and total count $totalCount")
      context.become(receiveWithChildren(childrenRefs, currentWorker, totalCount+count))
    case Print => log.info(s"Total count of words : $totalCount")
    case GetTotalWordCount =>
      sender() ! totalCount
  }
}
