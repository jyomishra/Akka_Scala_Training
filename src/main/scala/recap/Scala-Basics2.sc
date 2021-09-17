import scala.util.Try

class Animal
class Dog extends Animal

val aDog: Animal = new Dog

trait Carnivore {
  def eat(animal: Animal): Unit
}

class Crocodile extends Animal with Carnivore {
  override def eat(animal: Animal): Unit = println("Crunch Crunch!!")
}

val aCroc = new Crocodile

aCroc.eat(aDog)
aCroc eat aDog

val aCarnivore = new Carnivore {
  override def eat(animal: Animal): Unit = println("Chomp Chomp!!")
}

aCarnivore.eat(aDog)

abstract class MyList[+A]

object MyList

case class Person(name: String, age: Int)
val p = Person.apply("Jyo", 35)
println(Person.unapply(p))

val aTry = Try{
  try {
    throw new RuntimeException("I am innocent! I swear.")
  } catch {
    case r:RuntimeException => {
      println("I caught exception!")
      throw r
    }
  } finally {
    println("In finally")
  }
}

aTry.isSuccess








