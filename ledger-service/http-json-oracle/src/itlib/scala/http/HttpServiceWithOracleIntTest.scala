// Copyright (c) 2022 Digital Asset (Switzerland) GmbH and/or its affiliates. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.daml.http

abstract class HttpServiceWithOracleIntTest(override val disableContractPayloadIndexing: Boolean)
    extends QueryStoreAndAuthDependentIntegrationTest
    with HttpServiceOracleInt {

  override final def testLargeQueries = disableContractPayloadIndexing

  override def staticContentConfig: Option[StaticContentConfig] = None

  override def wsConfig: Option[WebsocketConfig] = None
}

final class HttpServiceWithOracleIntTestCustomToken
    extends HttpServiceWithOracleIntTest(disableContractPayloadIndexing = false)
    with AbstractHttpServiceIntegrationTestFunsCustomToken

final class HttpServiceWithOracleIntTestNoPayloadIndexCustomToken
    extends HttpServiceWithOracleIntTest(disableContractPayloadIndexing = true)
    with AbstractHttpServiceIntegrationTestFunsCustomToken
