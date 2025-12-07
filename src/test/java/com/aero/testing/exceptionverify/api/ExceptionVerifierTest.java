package com.aero.testing.exceptionverify.api;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExceptionVerifierTest {

  @DisplayName(
      "Verify that throws exactly works as expected and that message contains works as expected")
  @Test
  void throwsExactly_MessageContains() {
    ThrowingRunnable throwingRunnable =
        () -> {
          throw new IllegalArgumentException("Argument must not be null");
        };

    ExceptionVerifier.assertThat(throwingRunnable)
        .throwsExactly(IllegalArgumentException.class)
        .messageContains("must not be null");
  }

  @DisplayName("Verify that does not throw works as expected.")
  @Test
  void doesNotThrow() {
    ExceptionVerifier.assertThat(() -> {}).doesNotThrow();
  }

  @DisplayName("Verify that the withCause verifier works as expected")
  @Test
  void withCause() {
    ThrowingRunnable throwingRunnable =
        () -> {
          throw new IllegalArgumentException(
              "Nested exception: IO Error", new IOException("No space left"));
        };

    ExceptionVerifier.assertThat(throwingRunnable)
        .throwsExactly(IllegalArgumentException.class)
        .messageContains("Nested exception")
        .withCause(IOException.class)
        .causeMessageContains("No space left");
  }

  @DisplayName("Assert that null is throwing")
  @Test
  void assertThatNull() {
    assertThrows(NullPointerException.class, () -> ExceptionVerifier.assertThat(null));
  }

  @DisplayName("With message is working as intended")
  @Test
  void withMessage() {
    ThrowingRunnable throwingRunnable =
        () -> {
          int a = 10 / 0;
        };

    ExceptionVerifier.assertThat(throwingRunnable)
        .throwsExactly(ArithmeticException.class)
        .withMessage("/ by zero");
  }
}
