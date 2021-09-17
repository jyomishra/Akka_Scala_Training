package actor_study.iv_exercise

import actor_study.iv_exercise.BankAccount.{Deposit, GetStatement, Withdraw}
import actor_study.iv_exercise.User.{DoDeposit, DoWithdraw, Statement}
import akka.actor.{Actor, ActorRef, Props}

object User {
  case class DoDeposit(amount: Int)
  case class DoWithdraw(amount: Int)
  case class Statement(stmt: List[String])
  def props(bankAccount: ActorRef): Props = Props(new User(bankAccount))
}

class User(bankAccount: ActorRef) extends Actor{
  var transactionId: Int = 0
  override def receive: Receive = {
    case DoDeposit(amount) => {
      transactionId += 1
      bankAccount ! Deposit(amount, transactionId)
    }
    case DoWithdraw(amount) => {
      transactionId += 1
      bankAccount ! Withdraw(amount, transactionId)
    }
    case GetStatement => bankAccount ! GetStatement
    case Statement(stmt) => stmt.foreach(println)
    case msg: String => println(s"Got message from ${sender().path.name}: $msg")
  }
}
