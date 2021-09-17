import scala.util.Try

val partialFunction: PartialFunction[Int, Int] = {
  case 1 => 3
  case 2 => 6
  case 7 => 21
}

val pf = (x: Int) => x match {
  case 1 => 3
  case 2 => 6
  case 7 => 21
}

val function:(Int => Int) = partialFunction

val modList = List(1, 2, 3).map {
  case 1 => 3
  case 2 => 6
  case _ => 21
}

val lifted = partialFunction.lift // Int => Option[Int]

Try(List(1,2,3).map(partialFunction))
List(1,2,3).map(lifted)

val pfChain = partialFunction.orElse [Int, Int] {
  case 6 => 18
}

pfChain(6)
Try(pfChain(9))


val appendedList = List(1,2,3).::(5) ::: (List(6,7)).++(List(9))

type ReceiveFunc = PartialFunction[Any, Unit]

def receive: ReceiveFunc = {
  case 1 => println("hello")
  case _ => println("Confused")
}

receive(1)
receive(2)

