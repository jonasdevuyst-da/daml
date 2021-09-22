// Copyright (c) 2021 Digital Asset (Switzerland) GmbH and/or its affiliates. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.daml.platform.apiserver.error

import com.daml.error.{BaseError, ErrorCategory, ErrorCode, Explanation, Resolution}
import com.daml.logging.{ContextualizedLogger, LoggingContext}
import com.daml.platform.apiserver.error.ErrorGroups.ProtoDeserializationErrorGroup
import com.daml.platform.apiserver.error.ProtoDeserializationError.ProtoDeserializationFailure
import com.google.protobuf.InvalidProtocolBufferException

sealed trait ProtoDeserializationError extends Product with Serializable {
  def toAdminError(implicit
      logger: ContextualizedLogger,
      loggingContext: LoggingContext,
      correlationId: CorrelationId,
  ): BaseError =
    ProtoDeserializationFailure.Wrap(this)
}
object ProtoDeserializationError extends ProtoDeserializationErrorGroup {
  final case class BufferException(error: InvalidProtocolBufferException)
      extends ProtoDeserializationError
  // TODO error codes: Adapt, if necessary, the CryptoDeserializationError
  //  final case class CryptoDeserializationError(error: DeserializationError)   extends ProtoDeserializationError
  final case class TransactionDeserialization(message: String) extends ProtoDeserializationError
  final case class ValueDeserializationError(field: String, message: String)
      extends ProtoDeserializationError
  final case class StringConversionError(error: String) extends ProtoDeserializationError
  final case class UnrecognizedField(error: String) extends ProtoDeserializationError
  final case class UnrecognizedEnum(field: String, value: Int) extends ProtoDeserializationError
  final case class FieldNotSet(field: String) extends ProtoDeserializationError
  final case class NotImplementedYet(className: String) extends ProtoDeserializationError
  final case class TimestampConversionError(message: String) extends ProtoDeserializationError
  final case class TimeModelConversionError(message: String) extends ProtoDeserializationError
  final case class ValueConversionError(field: String, error: String)
      extends ProtoDeserializationError
  final case class SubmissionIdConversionError(message: String) extends ProtoDeserializationError
  final case class InvariantViolation(error: String) extends ProtoDeserializationError
  final case class UnknownGrpcCodeError(error: String) extends ProtoDeserializationError
  final case class OtherError(error: String) extends ProtoDeserializationError

  /** Common Deserialization error code
    *
    * USE THIS ERROR CODE ONLY WITHIN A GRPC SERVICE, PARSING THE INITIAL REQUEST.
    * Don't used it for something like transaction processing or reading from the database.
    */
  @Explanation(
    """This error indicates that an incoming administrative command could not be processed due to a malformed message."""
  )
  @Resolution("Inspect the error details and correct your application")
  object ProtoDeserializationFailure
      extends ErrorCode(
        id = "PROTO_DESERIALIZATION_FAILURE",
        ErrorCategory.InvalidIndependentOfSystemState,
      ) {
    case class Wrap(reason: ProtoDeserializationError)(implicit
        val logger: ContextualizedLogger,
        val loggingContext: LoggingContext,
        val correlationId: CorrelationId,
    ) extends BaseError.Impl(
          cause = "Deserialization of protobuf message failed",
          correlationId = correlationId.id,
        )
        with BaseError
  }

}