package com.company.server

import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get, onComplete, path}
import akka.stream.scaladsl.Sink
import com.company.akka.AkkaContext
import com.company.balancer.AkkaLoadBalancer
import com.company.logger.Logger
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.concurrent.Promise

object LoadBalancerHttpServer extends AkkaContext with Logger with LoadBalancerHttpServerLogHelper {
  private val log = Logging(system, this)
  private val loadBalancerConfig = ConfigFactory.load.getConfig("load-balancer")
  private val routeRegistryConfig = loadBalancerConfig.getConfig("route-registry")
  private val getFortuneUrl = routeRegistryConfig.getString("get-fortune.resource-url")

  private val loadBalancer = AkkaLoadBalancer(
    ports = routeRegistryConfig.getIntList("ports").asScala.toList,
    host = routeRegistryConfig.getString("host"),
    bufferSize = routeRegistryConfig.getInt("buffer-size")
  )

  def main(args: Array[String]): Unit = {
    val route =
      path("get-fortune") {
        log.debug(START_GET_FORTUNATE_LOG_MESSAGE)
        get {
          val getFortuneResponse = loadBalancer.getFortune(HttpRequest(uri = getFortuneUrl) -> Promise[HttpResponse])
          onComplete(getFortuneResponse) {
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
