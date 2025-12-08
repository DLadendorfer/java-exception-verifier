// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.internal.delegates;

import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A delegate responsible for verifying whether an exception was thrown or not.
 *
 * <p>This class is used internally by {@link ExceptionAssertion} to perform presence-based
 * exception checks such as:
 *
 * <ul>
 *   <li>asserting that any exception was thrown
 *   <li>asserting that no exception was thrown
 * </ul>
 *
 * <p>The delegate is annotated with {@link org.jspecify.annotations.NullMarked}, meaning all
 * parameters and return types are non-null by default unless explicitly marked with {@link
 * org.jspecify.annotations.Nullable}.
 *
 * <p>Assertion failures are delegated to {@link AbstractFailureDelegate} to ensure consistent error
 * signaling across the library.
 *
 * @author Daniel Ladendorfer
 */
@NullMarked
@AllArgsConstructor
public class ExceptionPresenceDelegate extends AbstractFailureDelegate {

  /** Message used when an exception was expected but none was thrown. */
  private static final String EXCEPTION_EXPECTED = "Expected an exception, but nothing was thrown.";

  /**
   * Message used when no exception was expected but one was thrown. The thrown exception's
   * description is formatted into the message.
   */
  private static final String NO_EXCEPTION_EXPECTED = "Expected no exception, but got: %s";

  private final ExceptionAssertion exceptionAssertion;

  /**
   * Asserts that an exception was thrown.
   *
   * <p>If the given {@code throwable} is {@code null}, this indicates that the executed code did
   * not throw an exception, and the assertion fails.
   *
   * @param throwable the captured exception, or {@code null} if none occurred
   * @return the same {@link ExceptionAssertion} instance for fluent chaining
   * @throws AssertionError if no exception was thrown
   */
  public ExceptionAssertion throwsAny(@Nullable Throwable throwable) {
    if (throwable == null) {
      fail(EXCEPTION_EXPECTED);
    }
    return exceptionAssertion;
  }

  /**
   * Asserts that no exception was thrown.
   *
   * <p>If the given {@code throwable} is not {@code null}, the assertion fails and includes the
   * thrown exception in the failure message.
   *
   * @param throwable the captured exception, or {@code null} if none occurred
   * @return the same {@link ExceptionAssertion} instance for fluent chaining
   * @throws AssertionError if an exception was thrown
   */
  public ExceptionAssertion doesNotThrow(@Nullable Throwable throwable) {
    if (throwable != null) {
      fail(NO_EXCEPTION_EXPECTED.formatted(throwable));
    }
    return exceptionAssertion;
  }
}
