package com.company

import com.company.balancer.LoadBalancer

object Test {
  private val lb = LoadBalancer()

  def main(args: Array[String]): Unit = {
    //    val rq = List(
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //      HttpRequest(uri = "/get-fortune") -> Promise[HttpResponse],
    //    )
    //
    //    val promises = rq.map(_._2)
    //    rq.foreach(a => LoadBalancer().queue.offer(a))
    //    Thread.sleep(5.seconds.toMillis)
    //    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    //    promises.foreach(p => println(p.future))
    //    Thread.sleep(5.seconds.toMillis)
    //    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    //    promises.foreach(p => println(p.future))
    //    Thread.sleep(5.seconds.toMillis)
    //    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    //    promises.foreach(p => println(p.future))
    //    Thread.sleep(5.seconds.toMillis)
    //    println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
    //    promises.foreach(p => println(p.future))
  }

}
