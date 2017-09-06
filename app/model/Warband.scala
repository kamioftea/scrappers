package model

case class Warband(faction: Faction, members: List[Member], traits: Set[WarbandTrait])

case class Member(memberType: MemberType, cbt: Int, cmd: Int, con: Int, traits: Set[MemberTrait], gear: Set[Gear], xp: Int)

sealed trait MemberType {
  val startingTraits: Int

  def create(implicit traitLookup: Map[String, MemberTrait]): Member
}

object MemberType {

  case object Commander extends MemberType {
    val startingTraits = 3

    override def create(implicit traitLookup: Map[String, MemberTrait]) =
      Member(Commander, 1, 1, 1, Set(traitLookup("Commander")), Set(), 0)
  }

  case object Veteran extends MemberType {
    val startingTraits = 2

    override def create(implicit traitLookup: Map[String, MemberTrait]) =
      Member(Veteran, 1, 1, 1, Set(), Set(), 0)
  }

  case object Trooper extends MemberType {
    val startingTraits = 1

    override def create(implicit traitLookup: Map[String, MemberTrait]) =
      Member(Trooper, 1, 1, 1, Set(), Set(), 0)
  }

}

case class Faction(name: String, description: String, rivalFactionName: String, aliases: Map[MemberType, String], factionTrait: Trait)

case class Gear(name: String, traits: Set[GearTrait])


