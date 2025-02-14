// Copyright (c) 2022 Digital Asset (Switzerland) GmbH and/or its affiliates. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.daml.metrics.akkahttp

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

object AkkaHttpMetricLabels {

  final val HostLabel = "host"
  final val HttpStatusLabel = "http_status"
  final val HttpVerbLabel = "http_verb"
  final val PathLabel = "path"

  def labelsFromRequest(request: HttpRequest): Seq[(String, String)] = {
    val baseLabels = Seq[(String, String)](
      (HttpVerbLabel, request.method.name),
      (PathLabel, request.uri.path.toString),
    )
    val host = request.uri.authority.host
    val labelsWithHost =
      if (host.isEmpty)
        baseLabels
      else
        ((HostLabel, host.address)) +: baseLabels
    labelsWithHost
  }

  def labelsFromResponse(
      response: HttpResponse
  ): Seq[(String, String)] = {
    Seq((HttpStatusLabel, response.status.intValue.toString))
  }

}
