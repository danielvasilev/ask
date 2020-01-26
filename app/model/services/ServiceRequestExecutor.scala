package model.services

import play.api.libs.json.{JsValue, Reads}
import play.api.libs.ws.WSRequest

import scala.concurrent.{ExecutionContext, Future}

case class ServiceRequestExecutor[T](request: WSRequest, f: JsValue => T)(implicit ec: ExecutionContext) {
  def execute(implicit reads: Reads[T]): Future[T] = {
    request.get().map(_.json).map(json => f(json))
  }
}