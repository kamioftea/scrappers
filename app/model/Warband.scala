package model

import play.api.libs.json._

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

  implicit val memberTypeReads: Reads[MemberType] = {
    case JsString("Commander") => JsSuccess(Commander)
    case JsString("Veteran") => JsSuccess(Veteran)
    case JsString("Trooper") => JsSuccess(Trooper)
    case _ => JsError("MemberType expects one of Commander, Veteran, or Trooper")
  }

  implicit val memberTypeWrites: Writes[MemberType] = {
    case Commander => JsString("Commander")
    case Veteran => JsString("Veteran")
    case Trooper => JsString("Trooper")
  }

  implicit val memberTypeFormat: Format[MemberType] = Format(
    memberTypeReads,
    memberTypeWrites
  )
}

case class Gear(name: String, traits: Set[GearTrait])


