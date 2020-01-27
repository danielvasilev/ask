package model.services

import play.api.libs.json.{JsValue, Reads}
import play.api.libs.ws.WSRequest

import scala.concurrent.{ExecutionContext, Future}

case class ServiceRequestExecutor[T](request: WSRequest)(implicit ec: ExecutionContext) {
  def execute(f: JsValue => T, logRequest: Boolean = false)(implicit reads: Reads[T]): Future[T] = {
    if (logRequest) println("Making initial movie request: " + request.uri.toString)
    request.get().map(_.json).map(json => f(json))
  }
}