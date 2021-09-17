import scala.concurrent.Future
import scala.language.implicitConversions

// implicits
implicit val timeout = 3000
def setTimeout(f : () => Unit)(implicit timeout: Int) = f()

setTimeout(() => println("timeout"))

// implicit conversion
case class Person(name: String) {
  def greet = s"Hi, my name is $name"
}

//companion object
object Person {
  implicit val personOrdering: Ordering[Person] =
    Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
}

implicit def strToPerson(name: String): Person = Person(name)
"Jyotirmay".greet

implicit class Dog(name: String) {
  def bark = println("Bhou bhou!!")
}

"Tommy".bark

implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
List(1,2,3,4,5,6).sorted

import scala.concurrent.ExecutionContext.Implicits.global
val future = Future {
  println("Hello from future!!")
}

List(Person("bob"), Person("Alice")).sorted

