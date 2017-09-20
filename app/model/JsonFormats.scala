package model

import play.api.libs.json._

object JsonFormats {
  implicit def mapReads[K, V](implicit readsK: Reads[K], readsV: Reads[V]): Reads[Map[K, V]] = {
    case obj: JsObject =>
      obj.fields.foldLeft[JsResult[Map[K, V]]](JsSuccess(Map.empty[K, V])) ({
        case (e: JsError, _) =>
          e
        case (JsSuccess(acc, _), (key, value)) =>
          for {
            k <- readsK.reads(JsString(key))
            v <- readsV.reads(value)
          } yield acc + (k -> v)
      })
    case _ =>
      JsError("Map must be a json object")
  }

  implicit def mapWrites[K, V](implicit writesK: Writes[K], writesV: Writes[V]): Writes[Map[K,V]] =
    (m: Map[K, V]) =>
      m.map { case (k, v) => (writesK.writes(k), writesV.writes(v)) }
    .foldLeft[JsValue](JsObject.empty)({
      case (e: JsString, _) => e
      case (JsObject(map), (k: JsString, v: JsValue)) => JsObject(map + (k.value -> v))
      case _ => JsString("Failed to write map, the Key type must have a Writes that produces a JsString?")
    })

  implicit def mapFormat[K, V](implicit formatK: Format[K], formatV: Format[V]): Format[Map[K, V]] =
    Format(mapReads, mapWrites)
}
