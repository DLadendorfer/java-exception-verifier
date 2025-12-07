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
public class ExceptionMessageCheckDelegate {
  private static final String EXPECTED_EQUAL_MESSAGE = "Expected message '%s' but got '%s'";
  private final ExceptionAssertion exceptionAssertion;

  public ExceptionAssertion withMessage(@Nullable String expected, Throwable throwable) {
    String msg = throwable.getMessage();

    if (msg == null && expected == null) {
      return exceptionAssertion;
    }

    if (msg == null || !msg.equals(expected)) {
      fail(EXPECTED_EQUAL_MESSAGE.formatted(expected, msg));
    }

    return exceptionAssertion;
  }

  public ExceptionAssertion messageContains(String substring, Throwable throwable) {
    String msg = throwable.getMessage();
    if (msg == null || !msg.contains(substring)) {
      fail("Expected message containing '%s' but got '%s'".formatted(substring, msg));
    }
    return exceptionAssertion;
  }

  public ExceptionAssertion messageMatches(String regex, Throwable throwable) {
    String msg = throwable.getMessage();
    if (msg == null || !msg.matches(regex)) {
      fail("Expected message matching regex '%s' but got '%s'".formatted(regex, msg));
    }

    return exceptionAssertion;
  }
}
