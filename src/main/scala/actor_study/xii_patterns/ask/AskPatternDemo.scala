package actor_study.xii_patterns.ask

object AskPatternDemo extends App {
  case class Read(key: String)
  case class Write(key: String, value: String)

  case class RegisterUser(username: String, password: String)
  case class Authenticate(username: String, password: String)
  case class AuthFailure(message: String)
  case object AuthSuccess

}
