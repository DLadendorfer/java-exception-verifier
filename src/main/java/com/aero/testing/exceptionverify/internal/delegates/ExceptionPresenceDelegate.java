// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.internal.delegates;

import static org.junit.jupiter.api.Assertions.fail;

import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@AllArgsConstructor
public class ExceptionPresenceDelegate {

  private static final String EXCEPTION_EXPECTED = "Expected an exception, but nothing was thrown.";
  private static final String NO_EXCEPTION_EXPECTED = "Expected no exception, but got: %s";

  private final ExceptionAssertion exceptionAssertion;

  public ExceptionAssertion throwsAny(@Nullable Throwable throwable) {
    if (throwable == null) {
      fail(EXCEPTION_EXPECTED);
    }

    return exceptionAssertion;
  }

  public ExceptionAssertion doesNotThrow(@Nullable Throwable throwable) {
    if (throwable != null) {
      fail(NO_EXCEPTION_EXPECTED.formatted(throwable));
    }

    return exceptionAssertion;
  }
}
