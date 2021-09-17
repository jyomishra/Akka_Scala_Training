//Functional Programming
val incrementer = new ((Int) => Int) {
  override def apply(i: Int): Int = i + 1
}

val anIncrementer = (x : Int) => x + 1
val incremented = incrementer(42)
anIncrementer(42)

List(1,2,3).map(incrementer).map(anIncrementer).map(println)


