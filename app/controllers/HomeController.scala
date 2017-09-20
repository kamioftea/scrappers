package controllers

import java.nio.file.Paths
import javax.inject._

import akka.actor.ActorSystem
import akka.stream.scaladsl.FileIO
import akka.stream.{ActorMaterializer, Materializer}
import model.{Faction, WarbandTrait}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit actorSystem: ActorSystem, ec: ExecutionContext) extends AbstractController(cc) {

  import model.FactionJson.factionFormat

  implicit val materializer: Materializer = ActorMaterializer()

  val factions: Future[Seq[Faction]] =
    FileIO
      .fromPath(Paths.get("conf/factions.json"))
      .runFold("")((acc, str) => acc + str.utf8String)
      .flatMap(str => Json.parse(str).validate[Seq[Faction]] match {
        case JsSuccess(fs, _) => Future.successful(fs)
        case JsError(e) => Future.failed(new Throwable(e.toString()))
      })

  factions.onComplete(println)

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    factions.map(fs => Ok(views.html.index(fs)))
  }
}
