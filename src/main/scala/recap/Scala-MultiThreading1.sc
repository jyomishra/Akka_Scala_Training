//creating a thread on jvm
val aThread = new Thread(() => {
  wait(500)
  println("Waited 500 millis")
})

aThread.getState
aThread.start()
aThread.join()

