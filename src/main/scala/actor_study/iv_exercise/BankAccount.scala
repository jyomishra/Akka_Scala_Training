package actor_study.iv_exercise

import actor_study.iv_exercise.BankAccount.{Deposit, GetStatement, Withdraw}
import actor_study.iv_exercise.User.Statement
import akka.actor.{Actor, ActorRef, Props}

import scala.collection.mutable

object BankAccount {
  case class GetStatement()
  case class Deposit(amt: Int, tid: Int)
  case class Withdraw(amt: Int, tid: Int)
  def props: Props = Props[BankAccount]
}

class BankAccount extends Actor{

  var stmt: mutable.Queue[String] = mutable.Queue()
  var amount: Int = 0

  override def receive: Receive = {
    case GetStatement => sender() ! Statement(stmt.toList.slice(0,9))
    case Deposit(amt, tid) => {
      if(amt > 0) {
        amount += amt
        sender() ! s"TransactionId $tid: Success"
        stmt.enqueue(s"Deposit of amount $amt for TransactionId $tid: Success")
      } else {
        sender() ! s"TransactionId $tid: Failure"
        stmt.enqueue(s"Deposit of amount $amt for TransactionId $tid: Failure")
      }
    }
    case Withdraw(amt, tid) => {
      if(amt <= amount) {
        amount -= amt
        sender() ! s"TransactionId $tid: Success"
        stmt.enqueue(s"Withdraw of amount $amt for TransactionId $tid: Success")
      } else {
        sender() ! s"TransactionId $tid: Failure"
        stmt.enqueue(s"Withdraw of amount $amt for TransactionId $tid: Failure")
      }
    }
  }
}
