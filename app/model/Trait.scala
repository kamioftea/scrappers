package model

sealed trait Trait {
  val name: String
  val description: String
  val resources: Map[String, Int]
  val tags: List[String]
}

case class WarbandTrait(name: String, description: String) extends Trait {
  override val resources: Map[String, Int] = Map()
  override val tags: List[String] = List()
}

case class MemberTrait(name: String,
                       description: String,
                       resources: Map[String, Int] = Map(),
                       tags: List[String] = List()) extends Trait

case class GearTrait(name: String,
                     description: String,
                     resources: Map[String, Int] = Map(),
                     tags: List[String] = List()) extends Trait
