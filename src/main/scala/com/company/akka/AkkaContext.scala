package com.company.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

trait AkkaContext {
  private val actorSystemName = ConfigFactory.load.getString("load-balancer.actor-system-name")
  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
}
