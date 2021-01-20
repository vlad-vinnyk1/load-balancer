package com.company.server

trait LoadBalancerHttpServerLogHelper {

  private[server] val START_GET_FORTUNATE_LOG_MESSAGE = "Hit the `get-fortune` endpoint, proceed by routing to the worker nodes "

  private[server] val SUCCESS_GET_FORTUNATE_LOG_MESSAGE = "Request completed successfully returning result "

  private[server] val FAILURE_GET_FORTUNATE_LOG_MESSAGE = "Request completed with error, returning status "
}
