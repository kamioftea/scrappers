package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Faction(name: String, description: String, rivalFactionName: String, aliases: Map[MemberType, Alias], factionTrait: WarbandTrait)

object FactionJson {
  import JsonFormats.mapFormat
  import TraitJson.warbandTraitFormat
  import MemberType.memberTypeFormat

  implicit val aliasReads: Reads[Alias] = {
    case arr: JsArray =>
      (for {
        s <- arr.value.headOption.collect({case s: JsString => s.value})
        p <- arr.value.drop(1).headOption.collect({case s: JsString => s.value})
      } yield JsSuccess(Alias(s, p))).getOrElse(JsError("Failed to parse alias: not a pair of strings"))
    case _ => JsError("Failed to parse alias: not a js array")
  }

  implicit val aliasWrites: Writes[Alias] = a => Json.arr(a.singular, a.plural)

  implicit val aliasFormat: Format[Alias] = Format(aliasReads, aliasWrites)

  implicit val factionFormat: Format[Faction] = (
    (JsPath \ "name").format[String]
      and (JsPath \ "description").format[String]
      and (JsPath \ "rivalFactionName").format[String]
      and (JsPath \ "aliases").format[Map[MemberType, Alias]]
      and (JsPath \ "trait").format[WarbandTrait]
    ) (Faction.apply, unlift(Faction.unapply))
}

case class Alias(singular: String, plural: String)

/*
{
    "name": "",
    "description": "",
    "rivalFactionName": "",
    "aliases": {
      "Commander": "",
      "Veteran": "",
      "Trooper": ""
    },
    "trait": {
      "name": "",
      "description": "",
      "resources": {},
      "tags": []
    }
  }
 */
