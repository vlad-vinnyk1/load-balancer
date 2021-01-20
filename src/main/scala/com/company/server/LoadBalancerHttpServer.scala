package com.company.server

import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get, onComplete, path}
import akka.stream.scaladsl.Sink
import com.company.akka.AkkaContext
import com.company.balancer.LoadBalancer
import com.company.logger.Logger
import com.typesafe.config.ConfigFactory

import scala.concurrent.Promise

object LoadBalancerHttpServer extends AkkaContext with Logger with LoadBalancerHttpServerLogHelper {
  private val log = Logging(system, this)
  private val loadBalancer = LoadBalancer()
  private val loadBalancerConfig = ConfigFactory.load.getConfig("load-balancer")
  private val getFortuneUrl = loadBalancerConfig.getString("route-registry.get-fortune.resource-url")

  def main(args: Array[String]): Unit = {
    val route =
      path("get-fortune") {
        log.debug(START_GET_FORTUNATE_LOG_MESSAGE)
        get {
          val promise = Promise[HttpResponse]
          loadBalancer.queue.offer(HttpRequest(uri = getFortuneUrl) -> promise)
          onComplete(promise.future) {
            case util.Success(f) =>
              log.debug(SUCCESS_GET_FORTUNATE_LOG_MESSAGE)
              complete(f)
            case util.Failure(ex) =>
              log.error(FAILURE_GET_FORTUNATE_LOG_MESSAGE + ex.getMessage)
              complete(ex)
          }
        }
      }

    Http().bind(
      interface = loadBalancerConfig.getString("interface"),
      port = loadBalancerConfig.getInt("port"),
    ).to(Sink.foreach { conn =>
      conn.flow.join(route).run()
    }).run()
  }
}
