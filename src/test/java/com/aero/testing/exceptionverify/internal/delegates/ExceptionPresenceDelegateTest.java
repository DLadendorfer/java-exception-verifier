package com.aero.testing.exceptionverify.internal.delegates;

import static org.junit.jupiter.api.Assertions.*;

import com.aero.testing.exceptionverify.IMethodSourceProvider;
import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link ExceptionPresenceDelegate}.
 *
 * @author Daniel Ladendorfer
 */
class ExceptionPresenceDelegateTest implements IMethodSourceProvider {

  @DisplayName(
      """
        ExceptionPresenceDelegate::throwsAny is failing and throwing
        a java.lang.AssertionError if null is passed.""")
  @Test
  void throwsAny_Null() {
    var presenceDelegate = new ExceptionPresenceDelegate(new ExceptionAssertion(null));
    assertThrows(AssertionError.class, () -> presenceDelegate.throwsAny(null));
  }

  @DisplayName(
      """
        ExceptionPresenceDelegate::doesNotThrow is not failing if null is passed.
        Subsequently the ExceptionAssertion is returned.""")
  @Test
  void doesNotThrow_Null() {
    var exceptionAssertion = new ExceptionAssertion(null);
    var presenceDelegate = new ExceptionPresenceDelegate(exceptionAssertion);
    assertEquals(exceptionAssertion, presenceDelegate.doesNotThrow(null));
  }

  @ParameterizedTest(
      name =
          """
        ExceptionPresenceDelegate::throwsAny is not failing if an instance of {0} is passed.
        Subsequently the ExceptionAssertion is returned.
        """)
  @MethodSource("throwableArgumentFactory")
  void throwsAny_WithThrowable(Throwable throwable) {
    var exceptionAssertion = new ExceptionAssertion(throwable);
    var presenceDelegate = new ExceptionPresenceDelegate(exceptionAssertion);
    assertEquals(exceptionAssertion, presenceDelegate.throwsAny(throwable));
  }

  @ParameterizedTest(
      name =
          """
        ExceptionPresenceDelegate::doesNotThrow is failing if an instance of {0} is passed.
        """)
  @MethodSource("throwableArgumentFactory")
  void doesNotThrow_WithThrowable(Throwable throwable) {
    var presenceDelegate = new ExceptionPresenceDelegate(new ExceptionAssertion(throwable));
    assertThrows(AssertionError.class, () -> presenceDelegate.doesNotThrow(throwable));
  }
}
