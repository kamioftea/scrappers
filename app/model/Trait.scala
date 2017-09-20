package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed trait Trait {
  val name: String
  val description: String
  val resources: Map[String, Int]
  val tags: List[String]
}

case class WarbandTrait(name: String,
                        description: String,
                        resources: Map[String, Int] = Map(),
                        tags: List[String] = List()) extends Trait {
}

case class MemberTrait(name: String,
                       description: String,
                       resources: Map[String, Int] = Map(),
                       tags: List[String] = List()) extends Trait

case class GearTrait(name: String,
                     description: String,
                     resources: Map[String, Int] = Map(),
                     tags: List[String] = List()) extends Trait

object TraitJson {
  implicit val warbandTraitFormat: Format[WarbandTrait] = (
    (JsPath \ "name").format[String]
      and (JsPath \ "description").format[String]
      and (JsPath \ "resources").format[Map[String, Int]]
      and (JsPath \ "tags").format[List[String]]

    ) (WarbandTrait.apply, unlift(WarbandTrait.unapply))
}
