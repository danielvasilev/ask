package model.services

import akka.http.scaladsl.model.Uri
import play.api.libs.json.{JsValue, Reads}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

case class ServiceRequestExecutor[T](uri: Uri, ws: WSClient)(implicit ec: ExecutionContext) {
  def execute(f: JsValue => T)(implicit reads: Reads[T]): Future[T] = {
    val request = ws.url(uri.toString)
    request.get().map(_.json).map(json => f(json))
  }
}