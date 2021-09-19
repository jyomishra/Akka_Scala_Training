package actor_study.xii_patterns

object StashDemo extends App {
  /**
   * ResouceActor
   *  - open => it can receive read or write request
   *  - otherwise postpone all read write request until state is open
   */

  case object Open
  case object Close
  case object Read
  case class Write(data: String)
}
