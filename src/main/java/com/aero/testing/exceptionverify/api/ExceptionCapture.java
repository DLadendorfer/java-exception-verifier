// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.api;

import static java.util.Objects.requireNonNull;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Internal utility for executing an operation and capturing any thrown {@link Throwable}.
 *
 * <p>This class provides a low-level mechanism used by exception-verification utilities to execute
 * code and return the resulting exception, or {@code null} if none was thrown. It is not intended
 * for direct use by application code. <br>
 *
 * {@snippet :
 * Throwable thrown = ExceptionCapture.capture(() -> performRiskyOperation());
 *
 *  if (thrown != null) {
 *      // handle or inspect the exception
 *      handle(thrown);
 *  }
 * }
 */
@NullMarked
@UtilityClass
final class ExceptionCapture {

  private static final String REQUIRE_NON_NULL_MESSAGE =
      "Cannot capture result of passed ThrowingRunnable, because it is 'null'";

  /**
   * Executes the given runnable and returns the thrown {@link Throwable}, or {@code null} if
   * execution completes normally.
   *
   * @param runnable the operation to execute
   * @return the caught throwable, or {@code null} if none was thrown
   */
  @Nullable
  static Throwable capture(ThrowingRunnable runnable) {
    requireNonNull(runnable, REQUIRE_NON_NULL_MESSAGE);

    try {
      runnable.run();
      return null;
    } catch (Throwable t) {
      return t;
    }
  }
}
