// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.internal.delegates;

import static org.junit.jupiter.api.Assertions.fail;

import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;

@NullMarked
@AllArgsConstructor
public class ExceptionTypeCheckDelegate {

  private static final String EXPECTED_TYPE_MISMATCH = "Expected exception of type %s but got %s";
  private static final String EXPECTED_SUBTYPE_MISMATCH = "Expected a subtype of %s but got %s";
  private final ExceptionAssertion exceptionAssertion;

  public ExceptionAssertion throwsExactly(
      Class<? extends Throwable> expected, Throwable throwable) {
    if (!throwable.getClass().equals(expected)) {
      fail(EXPECTED_TYPE_MISMATCH.formatted(expected.getName(), throwable.getClass().getName()));
    }
    return exceptionAssertion;
  }

  public ExceptionAssertion throwsSubtypeOf(
      Class<? extends Throwable> expected, Throwable throwable) {
    if (!expected.isAssignableFrom(throwable.getClass())) {
      fail(EXPECTED_SUBTYPE_MISMATCH.formatted(expected.getName(), throwable.getClass().getName()));
    }
    return exceptionAssertion;
  }
}
