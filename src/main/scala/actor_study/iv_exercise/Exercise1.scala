package actor_study.iv_exercise

import actor_study.iv_exercise.BankAccount.GetStatement
import actor_study.iv_exercise.User.{DoDeposit, DoWithdraw}
import akka.actor.ActorSystem

object Exercise1 extends App {

  val system = ActorSystem("exerciseSystem")

  val bankAccount1 = system.actorOf(BankAccount.props, "b1")
  val user1 = system.actorOf(User.props(bankAccount1), "u1")

  user1 ! DoDeposit(100)
  user1 ! DoWithdraw(50)
  user1 ! DoWithdraw(150)
  user1 ! DoDeposit(150)
  user1 ! DoDeposit(150)
  user1 ! DoDeposit(-150)
  user1 ! DoDeposit(150)
  user1 ! DoWithdraw(150)

  user1 ! GetStatement
}
