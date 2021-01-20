package com.company.balancer

import akka.NotUsed
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Keep, Merge, Sink, Source}
import akka.stream.{FlowShape, OverflowStrategy}
import com.company.akka.AkkaContext
import com.company.logger.Logger
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.concurrent.Promise
import scala.util.{Failure, Success}

case class LoadBalancer() extends AkkaContext with Logger {
  private val routeRegistryConfig = ConfigFactory.load.getConfig("load-balancer.route-registry")
  private val fortuneConfig = routeRegistryConfig.getConfig("get-fortune")

  private val ports = fortuneConfig.getIntList("ports").asScala.toList
  private val host = fortuneConfig.getString("host")
  private val bufferSize = routeRegistryConfig.getInt("buffer-size")

  private val poolFlows = ports.map(port => Http().cachedHostConnectionPool[Promise[HttpResponse]](host = host, port = port))
  private val loadBalancerFlow = balancer(poolFlows)

  val queue = Source.queue[(HttpRequest, Promise[HttpResponse])](bufferSize, OverflowStrategy.backpressure)
    .via(loadBalancerFlow)
    .toMat(Sink.foreach({
      case (Success(resp), p) => p.success(resp)
      case (Failure(e), p) => p.failure(e)
    }))(Keep.left)
    .run

  private def balancer[In, Out](workers: List[Flow[In, Out, Any]]): Flow[In, Out, NotUsed] = {
    import GraphDSL.Implicits._

    Flow.fromGraph(GraphDSL.create() { implicit b =>
      val balancer = b.add(Balance[In](workers.length, waitForAllDownstreams = false))
      val merge = b.add(Merge[Out](workers.length))

      for (worker <- workers) {
        balancer ~> worker.async ~> merge
      }

      FlowShape(balancer.in, merge.out)
    })
  }
}
