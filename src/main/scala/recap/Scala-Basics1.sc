import scala.annotation.tailrec

val aCond = false

var aVar = 42
aVar = 11

//expression
val aCondVal = if(aCond) 42 else 65

val aCodeBlock = {
  if(aCond) 76 else 63
  56
}
// unit
val unit: Unit = println("Hello World!")

def aFunc(x: Int) = x + 1
aFunc(5)

val aList = 1 to 10
aList.map(i => i*2)

def factorial(n : Int): Int = {
  if( n ==0 ) return 1
  n * factorial(n-1)
}
factorial(5)

@tailrec
def tailRecFactorial(n : Int, acc : Int) : Int = {
  if(n == 0) return acc
  tailRecFactorial(n-1, n * acc)
}
tailRecFactorial(5, 1)








