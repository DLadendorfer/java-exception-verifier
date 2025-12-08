package com.aero.testing.exceptionverify.internal.delegates;

import static org.junit.jupiter.api.Assertions.*;

import com.aero.testing.exceptionverify.IMethodSourceProvider;
import com.aero.testing.exceptionverify.api.ExceptionAssertion;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link ExceptionTypeCheckDelegate}.
 *
 * @author Daniel Ladendorfer
 */
@SuppressWarnings("DataFlowIssue")
class ExceptionTypeCheckDelegateTest implements IMethodSourceProvider {

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsExactly is failing and throwing
      a java.lang.AssertionError if the expected type is null.
      """)
  @Test
  void throwsExactly_ExpectedNull() {
    var thrown = new IllegalArgumentException();
    var assertion = new ExceptionAssertion(thrown);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(NullPointerException.class, () -> typeDelegate.throwsExactly(null, thrown));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsExactly is failing if the thrown exception is null.
      Subsequently a java.lang.AssertionError is thrown.
      """)
  @Test
  void throwsExactly_ThrownNull() {
    var assertion = new ExceptionAssertion(null);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(
        AssertionError.class,
        () -> typeDelegate.throwsExactly(IllegalArgumentException.class, null));
  }

  @ParameterizedTest(
      name =
          """
          ExceptionTypeCheckDelegate::throwsExactly is not failing when thrown is exactly {0}.
          Subsequently the ExceptionAssertion is returned.
          """)
  @MethodSource("throwableArgumentFactory")
  void throwsExactly_ExactMatch(Throwable throwable) {
    var assertion = new ExceptionAssertion(throwable);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, typeDelegate.throwsExactly(throwable.getClass(), throwable));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsExactly is failing if the thrown exception type
      does not match exactly.
      """)
  @Test
  void throwsExactly_WrongType() {
    var thrown = new IllegalArgumentException();
    var assertion = new ExceptionAssertion(thrown);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(
        AssertionError.class, () -> typeDelegate.throwsExactly(RuntimeException.class, thrown));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsSubtypeOf is failing and throwing
      a java.lang.AssertionError if the expected type is null.
      """)
  @Test
  void throwsSubtypeOf_ExpectedNull() {
    var thrown = new IllegalArgumentException();
    var assertion = new ExceptionAssertion(thrown);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(NullPointerException.class, () -> typeDelegate.throwsSubtypeOf(null, thrown));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsSubtypeOf is failing if the thrown exception is null.
      Subsequently a java.lang.AssertionError is thrown.
      """)
  @Test
  void throwsSubtypeOf_ThrownNull() {
    var assertion = new ExceptionAssertion(null);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(
        AssertionError.class, () -> typeDelegate.throwsSubtypeOf(RuntimeException.class, null));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsSubtypeOf is not failing when thrown is a subtype
      of the expected type.
      Subsequently the ExceptionAssertion is returned.
      """)
  @Test
  void throwsSubtypeOf_SubtypeMatch() {
    var thrown = new IllegalArgumentException(); // subtype of RuntimeException
    var assertion = new ExceptionAssertion(thrown);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, typeDelegate.throwsSubtypeOf(RuntimeException.class, thrown));
  }

  @DisplayName(
      """
      ExceptionTypeCheckDelegate::throwsSubtypeOf is failing when thrown is not a subtype
      of the expected type.
      """)
  @Test
  void throwsSubtypeOf_NoSubtypeMatch() {
    var thrown = new RuntimeException();
    var assertion = new ExceptionAssertion(thrown);
    var typeDelegate = new ExceptionTypeCheckDelegate(assertion);

    assertThrows(
        AssertionError.class,
        () -> typeDelegate.throwsSubtypeOf(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::throwsSubtypeOf fails with AssertionError
              if the thrown exception is NOT a subtype of the expected class.
              """)
  @Test
  void throwsSubtypeOf_NotSubtype() {
    var thrown = new IllegalArgumentException();
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(AssertionError.class, () -> delegate.throwsSubtypeOf(IOException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::throwsSubtypeOf is not failing
              if the thrown exception *is* a subtype of the expected class.
              Subsequently the ExceptionAssertion is returned.
              """)
  @Test
  void throwsSubtypeOf_IsSubtype() {
    var thrown = new IllegalArgumentException();
    var assertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, delegate.throwsSubtypeOf(RuntimeException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsExactly fails with AssertionError
              if the throwable has no cause.
              """)
  @Test
  void wrapsExactly_NoCause() {
    var thrown = new RuntimeException(); // no cause
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class, () -> delegate.wrapsExactly(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsExactly fails with AssertionError
              if the throwable's direct cause does NOT match exactly.
              """)
  @Test
  void wrapsExactly_Mismatch() {
    var thrown = new RuntimeException(new IOException());
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class, () -> delegate.wrapsExactly(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsExactly is not failing
              if the throwable's direct cause matches exactly.
              Subsequently the ExceptionAssertion is returned.
              """)
  @Test
  void wrapsExactly_Match() {
    var cause = new IllegalArgumentException();
    var thrown = new RuntimeException(cause);

    var assertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, delegate.wrapsExactly(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsExactlyRecursive fails with AssertionError
              if no cause in the entire cause-chain matches exactly the expected type.
              """)
  @Test
  void wrapsExactlyRecursive_NotFound() {
    var thrown = new RuntimeException(new IOException(new IllegalStateException()));
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class,
        () -> delegate.wrapsExactlyRecursive(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsExactlyRecursive is not failing
              if any cause in the cause-chain matches exactly the expected type.
              Subsequently the ExceptionAssertion is returned.
              """)
  @Test
  void wrapsExactlyRecursive_Found() {
    var target = new IllegalArgumentException();
    var thrown = new RuntimeException(new IOException(new RuntimeException(target)));

    var assertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, delegate.wrapsExactlyRecursive(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsSubtypeOf fails with AssertionError
              if the throwable has no cause.
              """)
  @Test
  void wrapsSubtypeOf_NoCause() {
    var thrown = new RuntimeException();
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class, () -> delegate.wrapsSubtypeOf(RuntimeException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsSubtypeOf fails with AssertionError
              if the direct cause is NOT a subtype of the expected class.
              """)
  @Test
  void wrapsSubtypeOf_NotSubtype() {
    var thrown = new RuntimeException(new IOException());
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class,
        () -> delegate.wrapsSubtypeOf(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsSubtypeOf is not failing
              if the direct cause is a subtype of the expected class.
              Subsequently the ExceptionAssertion is returned.
              """)
  @Test
  void wrapsSubtypeOf_IsSubtype() {
    var cause = new IllegalArgumentException();
    var thrown = new RuntimeException(cause);

    var assertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, delegate.wrapsSubtypeOf(RuntimeException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsSubtypeOfRecursive fails with AssertionError
              if no cause in the cause-chain is a subtype of the expected type.
              """)
  @Test
  void wrapsSubtypeOfRecursive_NotFound() {
    var thrown = new RuntimeException(new IOException(new IllegalStateException()));
    var exceptionAssertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(exceptionAssertion);

    assertThrows(
        AssertionError.class,
        () -> delegate.wrapsSubtypeOfRecursive(IllegalArgumentException.class, thrown));
  }

  @DisplayName(
      """
              ExceptionTypeCheckDelegate::wrapsSubtypeOfRecursive is not failing
              if any cause in the cause-chain is a subtype of the expected class.
              Subsequently the ExceptionAssertion is returned.
              """)
  @Test
  void wrapsSubtypeOfRecursive_Found() {
    var target = new IllegalArgumentException();
    var thrown = new RuntimeException(new IOException(new RuntimeException(target)));

    var assertion = new ExceptionAssertion(thrown);
    var delegate = new ExceptionTypeCheckDelegate(assertion);

    assertEquals(assertion, delegate.wrapsSubtypeOfRecursive(RuntimeException.class, thrown));
  }
}
