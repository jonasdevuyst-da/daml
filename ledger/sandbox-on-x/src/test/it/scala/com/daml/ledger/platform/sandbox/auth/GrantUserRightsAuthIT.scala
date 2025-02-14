// Copyright (c) 2022 Digital Asset (Switzerland) GmbH and/or its affiliates. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.daml.platform.sandbox.auth

import com.daml.ledger.api.v1.admin.{user_management_service => ums}
import com.daml.ledger.api.v1.admin.user_management_service.GrantUserRightsRequest

import scala.concurrent.Future

final class GrantUserRightsAuthIT extends AdminServiceCallAuthTests with UserManagementAuth {

  override def serviceCallName: String = "UserManagementService#GrantUserRights"

  private def adminPermission =
    ums.Right(ums.Right.Kind.ParticipantAdmin(ums.Right.ParticipantAdmin()))

  override def serviceCallWithToken(token: Option[String]): Future[Any] = {
    for {
      response <- createFreshUser(token)
      userId = response.user.getOrElse(sys.error("Could not load create a fresh user")).id
      _ <- stub(token).grantUserRights(
        GrantUserRightsRequest(
          userId = userId,
          rights = scala.Seq(adminPermission),
        )
      )
    } yield {}

  }

}
