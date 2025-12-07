// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.internal.delegates;

import static org.junit.jupiter.api.Assertions.fail;

import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@AllArgsConstructor
public class ExceptionCauseCheckDelegate {
  private static final String EXPECTED_CAUSE_IS_NULL =
      "Expected cause of type %s but cause was null.";
  private static final String EXPECTED_CAUSE_MISMATCH = "Expected cause %s but got %s";
  private static final String NO_CAUSE_EXPECTED = "No cause expected but got: %s";
  private static final String EXPECTED_CAUSE_MESSAGE_DOES_NOT_CONTAIN_SUBSTRING =
      "Expected cause message to contain '%s' but got: %s";
  private final ExceptionAssertion exceptionAssertion;

  public ExceptionAssertion withCause(
      @Nullable Class<? extends Throwable> expectedCause, Throwable throwable) {
    var cause = throwable.getCause();
    if (expectedCause == null && cause == null) {
      return exceptionAssertion;
    }

    if (expectedCause == null) {
      fail(NO_CAUSE_EXPECTED.formatted(cause.getClass().getName()));
    }

    if (cause == null) {
      fail(EXPECTED_CAUSE_IS_NULL.formatted(expectedCause.getName()));
    }
    if (!cause.getClass().equals(expectedCause)) {
      fail(EXPECTED_CAUSE_MISMATCH.formatted(expectedCause.getName(), cause.getClass().getName()));
    }

    return exceptionAssertion;
  }

  public ExceptionAssertion causeMessageContains(String substring, Throwable throwable) {
    var cause = throwable.getCause();
    if (cause == null || cause.getMessage() == null || !cause.getMessage().contains(substring)) {
      fail(
          EXPECTED_CAUSE_MESSAGE_DOES_NOT_CONTAIN_SUBSTRING.formatted(
              substring, cause == null ? "null" : cause.getMessage()));
    }

    return exceptionAssertion;
  }
}
