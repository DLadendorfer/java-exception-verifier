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
 * Performs type-based checks on thrown throwables.
 *
 * <p>This delegate is used internally by {@link ExceptionAssertion} to verify:
 *
 * <ul>
 *   <li>that the thrown throwable is exactly of a given type
 *   <li>that the thrown throwable is a subtype of a given type
 *   <li>that the thrown throwable wraps (causes) a throwable of a given type
 *   <li>that any throwable in the cause-chain matches exactly a given type (recursive)
 * </ul>
 *
 * <p>The class is annotated with {@link org.jspecify.annotations.NullMarked}, meaning that all
 * references are non-null unless explicitly annotated otherwise.
 *
 * <p>Assertion failures are delegated to {@link AbstractFailureDelegate} to ensure consistent error
 * signaling across the library.
 *
 * @author Daniel Ladendorfer
 */
@NullMarked
@AllArgsConstructor
public class ExceptionTypeCheckDelegate extends AbstractFailureDelegate {

  /** A message used if the type check cannot be performed because the throwable is null */
  private static final String EXPECTED_TYPE_MISMATCH_THROWABLE_NULL =
      "Expected throwable of type %s but got null";

  /** A message used when the concrete type of throwable does not match the expectation. */
  private static final String EXPECTED_TYPE_MISMATCH = "Expected throwable of type %s but got %s";

  /** A message used when the subtype of a throwable does not match the expectation. */
  private static final String EXPECTED_SUBTYPE_MISMATCH = "Expected a subtype of %s but got %s";

  /** A message used when the throwable should have a wrapped cause but the cause is null. */
  private static final String EXPECTED_WRAPPED_BUT_WAS_NULL =
      "Expected a wrapped throwable (cause) of type %s but the cause was null for throwable: %s";

  /** A message used when the expected throwable is not found in the cause-chain. */
  private static final String EXPECTED_WRAPPED_THROWABLE_IN_CAUSE_CHAIN =
      "Expected a wrapped throwable (anywhere in the cause-chain) of type %s but none was found.";

  private final ExceptionAssertion exceptionAssertion;

  /**
   * Asserts that the thrown exception is exactly of the expected type.
   *
   * <p>This check does not consider subtypes — the class must match exactly.
   *
   * @param expected the expected exception type
   * @param throwable the thrown exception to check
   * @return the same {@code ExceptionAssertion} instance for fluent chaining
   * @throws AssertionError if the thrown exception is not exactly of the expected type
   */
  public ExceptionAssertion throwsExactly(
      Class<? extends Throwable> expected, @Nullable Throwable throwable) {
    if (throwable == null) {
      fail(EXPECTED_TYPE_MISMATCH_THROWABLE_NULL.formatted(expected.getName()));
    } else if (!throwable.getClass().equals(expected)) {
      fail(EXPECTED_TYPE_MISMATCH.formatted(expected.getName(), throwable.getClass().getName()));
    }
    return exceptionAssertion;
  }

  /**
   * Asserts that the thrown exception is a subtype of the expected type.
   *
   * <p>This includes the expected type itself.
   *
   * @param expected the expected superclass or superinterface of the exception
   * @param throwable the thrown exception to check
   * @return the same {@code ExceptionAssertion} instance for fluent chaining
   * @throws AssertionError if the thrown exception is not a subtype of the expected type
   */
  public ExceptionAssertion throwsSubtypeOf(
      Class<? extends Throwable> expected, @Nullable Throwable throwable) {
    if (throwable == null) {
      fail(EXPECTED_TYPE_MISMATCH_THROWABLE_NULL.formatted(expected.getName()));
    } else if (!expected.isAssignableFrom(throwable.getClass())) {
      fail(EXPECTED_SUBTYPE_MISMATCH.formatted(expected.getName(), throwable.getClass().getName()));
    }
    return exceptionAssertion;
  }

  /**
   * Asserts that the thrown exception wraps (has as its direct cause) a throwable of the expected
   * type.
   *
   * <p>This check is not recursive — only the immediate cause is inspected.
   *
   * @param expected the expected type of the cause
   * @param throwable the thrown exception to inspect
   * @return the same {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no cause exists or the cause is not exactly the expected type
   */
  public ExceptionAssertion wrapsExactly(Class<? extends Throwable> expected, Throwable throwable) {

    var cause = throwable.getCause();

    if (cause == null) {
      fail(EXPECTED_WRAPPED_BUT_WAS_NULL.formatted(expected.getName(), throwable));
    } else if (!cause.getClass().equals(expected)) {
      fail(EXPECTED_TYPE_MISMATCH.formatted(expected.getName(), cause.getClass().getName()));
    }

    return exceptionAssertion;
  }

  /**
   * Asserts that somewhere in the cause-chain of the thrown exception there exists a throwable
   * whose type matches the expected type exactly.
   *
   * <p>This performs a recursive search through:
   *
   * <pre>
   * throwable.getCause(), cause.getCause(), ...
   * </pre>
   *
   * <p>The search ends once a matching cause is found or the end of the chain is reached.
   *
   * @param expected the exact type of exception expected somewhere in the cause-chain
   * @param throwable the thrown exception whose cause-chain is inspected
   * @return the same {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no matching cause exists in the entire cause-chain
   */
  public ExceptionAssertion wrapsExactlyRecursive(
      Class<? extends Throwable> expected, Throwable throwable) {

    Throwable current = throwable.getCause();

    while (current != null) {
      if (current.getClass().equals(expected)) {
        return exceptionAssertion;
      }
      current = current.getCause();
    }

    fail(EXPECTED_WRAPPED_THROWABLE_IN_CAUSE_CHAIN.formatted(expected.getName()));
    return exceptionAssertion; // unreachable
  }

  /**
   * Asserts that the thrown exception wraps (has as its direct cause) a throwable that is a subtype
   * of the expected type.
   *
   * <p>This check is not recursive — only the immediate cause is inspected.
   *
   * @param expected the expected type or supertype of the cause
   * @param throwable the thrown exception to inspect
   * @return the same {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no cause exists or the cause is not a subtype of the expected type
   */
  public ExceptionAssertion wrapsSubtypeOf(
      Class<? extends Throwable> expected, Throwable throwable) {

    var cause = throwable.getCause();

    if (cause == null) {
      fail(EXPECTED_WRAPPED_BUT_WAS_NULL.formatted(expected.getName(), throwable));
    } else if (!expected.isAssignableFrom(cause.getClass())) {
      fail(EXPECTED_SUBTYPE_MISMATCH.formatted(expected.getName(), cause.getClass().getName()));
    }

    return exceptionAssertion;
  }

  /**
   * Asserts that somewhere in the cause-chain of the thrown exception there exists a throwable
   * whose type is a subtype of the expected type.
   *
   * <p>This performs a recursive search through:
   *
   * <pre>
   * throwable.getCause(), cause.getCause(), ...
   * </pre>
   *
   * <p>The search ends once a matching cause is found or the end of the chain is reached.
   *
   * @param expected the type or supertype expected somewhere in the cause-chain
   * @param throwable the thrown exception whose cause-chain is inspected
   * @return the same {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no matching cause exists in the entire cause-chain
   */
  public ExceptionAssertion wrapsSubtypeOfRecursive(
      Class<? extends Throwable> expected, Throwable throwable) {

    Throwable current = throwable.getCause();

    while (current != null) {
      if (expected.isAssignableFrom(current.getClass())) {
        return exceptionAssertion;
      }
      current = current.getCause();
    }

    fail(EXPECTED_WRAPPED_THROWABLE_IN_CAUSE_CHAIN.formatted(expected.getName()));
    return exceptionAssertion; // unreachable
  }
}
