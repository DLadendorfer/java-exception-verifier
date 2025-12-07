// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.api;

import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NullMarked;

/**
 * Provides a fluent API for verifying that code under test throws expected exceptions.
 *
 * <p>This class is a utility class and should not be instantiated. It is designed to integrate with
 * testing frameworks like JUnit 5, allowing clean and readable exception assertions. <br>
 * Example usage: <br>
 *
 * {@snippet :
 * ExceptionVerifier.assertThat(() -> service.process(null))
 *     .throwsExactly(IllegalArgumentException.class)
 *     .messageContains("must not be null");
 * }
 *
 * @see ExceptionAssertion
 * @see ThrowingRunnable
 */
@NullMarked
@UtilityClass
public final class ExceptionVerifier {

  /**
   * Executes the given {@link ThrowingRunnable} and returns an {@link ExceptionAssertion} that can
   * be used to perform fluent checks on any thrown exception.
   *
   * @param runnable the operation to execute, which may throw any exception
   * @return an {@link ExceptionAssertion} for verifying the thrown exception, or for asserting that
   *     no exception was thrown
   */
  public static ExceptionAssertion assertThat(ThrowingRunnable runnable) {
    return new ExceptionAssertion(ExceptionCapture.capture(runnable));
  }
}
