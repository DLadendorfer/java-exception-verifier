// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.api;

import com.aero.testing.exceptionverify.internal.delegates.*;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Provides a fluent API to assert that a thrown {@link Throwable} meets expected conditions.
 *
 * <p>This class is typically used together with {@link ExceptionVerifier#assertThat} in unit tests.
 *
 * <p>Example usage:<br>
 *
 * {@snippet :
 * ExceptionVerifier.assertThat(() -> service.process(null))
 *     .throwsExactly(IllegalArgumentException.class)
 *     .messageContains("must not be null");
 * }
 */
@NullMarked
public final class ExceptionAssertion {

  private static final String SUBSTRING_MUST_NOT_BE_NULL =
      "Cannot check if the message contains 'null'. This does not make any sense.";
  private static final String SUBSTRING_MUST_NOT_BE_EMPTY =
      "Cannot check if the message contains an empty string. This does not make any sense.";
  private static final String EXPECTED_TYPE_MUST_NOT_BE_NULL = "Expected type must not be null";

  /** The captured throwable. May be {@code null} if no exception was thrown. */
  private final @Nullable Throwable thrown;

  private final ExceptionPresenceDelegate presenceChecker;
  private final ExceptionTypeCheckDelegate typeChecker;
  private final ExceptionMessageCheckDelegate messageChecker;
  private final ExceptionCauseCheckDelegate causeChecker;

  /**
   * Creates a new {@code ExceptionAssertion} for the given throwable.
   *
   * @param thrown the exception to assert on; may be {@code null} if no exception occurred
   */
  public ExceptionAssertion(@Nullable Throwable thrown) {
    this.thrown = thrown;
    presenceChecker = new ExceptionPresenceDelegate(this);
    typeChecker = new ExceptionTypeCheckDelegate(this);
    messageChecker = new ExceptionMessageCheckDelegate(this);
    causeChecker = new ExceptionCauseCheckDelegate(this);
  }

  // --- presence ---

  /**
   * Asserts that any exception was thrown.
   *
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no exception was thrown
   */
  public ExceptionAssertion throwsAny() {
    return presenceChecker.throwsAny(thrown);
  }

  /**
   * Asserts that no exception was thrown.
   *
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if an exception was thrown
   */
  public ExceptionAssertion doesNotThrow() {
    return presenceChecker.doesNotThrow(thrown);
  }

  // --- type checks ---

  /**
   * Asserts that the thrown exception is exactly of the specified type.
   *
   * @param expected the expected exception class; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the thrown exception type does not match
   */
  public ExceptionAssertion throwsExactly(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.throwsExactly(expected, thrown);
  }

  /**
   * Asserts that the thrown exception is a subtype of the specified type.
   *
   * @param expected the expected superclass of the exception; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the thrown exception is not a subtype
   */
  public ExceptionAssertion throwsSubtypeOf(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.throwsSubtypeOf(expected, thrown);
  }

  /**
   * Asserts that the thrown exception wraps (as its direct cause) a throwable of the given type.
   *
   * <p>The cause must be present and must be a subtype of the expected type.
   *
   * @param expected the expected type or supertype of the direct cause; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the cause is missing or not a subtype of the expected type
   */
  public ExceptionAssertion wrapsSubtypeOf(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.wrapsSubtypeOf(expected, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that somewhere in the cause-chain of the thrown exception there exists a throwable
   * whose type is a subtype of the specified type.
   *
   * <p>The entire cause-chain is searched recursively: {@code getCause(), getCause().getCause(),
   * ...}
   *
   * @param expected the type or supertype expected in the cause-chain; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no matching cause exists in the entire cause-chain
   */
  public ExceptionAssertion wrapsSubtypeOfRecursive(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.wrapsSubtypeOfRecursive(expected, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that the thrown exception wraps (as its direct cause) a throwable exactly of the given
   * type.
   *
   * <p>Only the immediate cause is inspected. Subtypes do not match.
   *
   * @param expected the exact type of the direct cause; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the direct cause is missing or not exactly the expected type
   */
  public ExceptionAssertion wrapsExactly(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.wrapsExactly(expected, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that somewhere in the cause-chain of the thrown exception there exists a throwable
   * whose type matches exactly the specified type.
   *
   * <p>This search is recursive and stops once a matching cause is discovered.
   *
   * @param expected the exact throwable type expected somewhere in the cause-chain; must not be
   *     {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if no exactly matching cause exists in the chain
   */
  public ExceptionAssertion wrapsExactlyRecursive(Class<? extends Throwable> expected) {
    Objects.requireNonNull(expected, EXPECTED_TYPE_MUST_NOT_BE_NULL);
    presenceChecker.throwsAny(thrown);
    return typeChecker.wrapsExactlyRecursive(expected, Optional.ofNullable(thrown).orElseThrow());
  }

  // --- message checks ---

  /**
   * Asserts that the exception message equals the specified string.
   *
   * @param expected the expected message; can be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the message does not match
   */
  public ExceptionAssertion messageEquals(@Nullable String expected) {
    presenceChecker.throwsAny(thrown);
    return messageChecker.messageEquals(expected, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that the exception message contains the specified substring.
   *
   * @param substring the substring to search for; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the message does not contain the substring
   */
  public ExceptionAssertion messageContains(String substring) {
    requireNonNullAndNonEmpty(substring);

    presenceChecker.throwsAny(thrown);
    return messageChecker.messageContains(substring, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that the exception message matches the specified regular expression.
   *
   * @param regex the regex pattern; must not be {@code null} or empty
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the message does not match the regex
   */
  public ExceptionAssertion messageMatches(String regex) {
    requireNonNullAndNonEmpty(regex);

    presenceChecker.throwsAny(thrown);
    return messageChecker.messageMatches(regex, Optional.ofNullable(thrown).orElseThrow());
  }

  // --- cause checks ---

  /**
   * Asserts that the cause of the thrown exception is exactly the specified type.
   *
   * @param expectedCause the expected cause class; can be null
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the cause is missing or of the wrong type
   */
  public ExceptionAssertion withCause(@Nullable Class<? extends Throwable> expectedCause) {
    presenceChecker.throwsAny(thrown);
    return causeChecker.withCause(expectedCause, Optional.ofNullable(thrown).orElseThrow());
  }

  /**
   * Asserts that the cause message contains the specified substring.
   *
   * @param substring the substring to search for; must not be {@code null}
   * @return this {@code ExceptionAssertion} for fluent chaining
   * @throws AssertionError if the cause message does not contain the substring
   */
  public ExceptionAssertion causeMessageContains(String substring) {
    requireNonNullAndNonEmpty(substring);

    presenceChecker.throwsAny(thrown);
    return causeChecker.causeMessageContains(substring, Optional.ofNullable(thrown).orElseThrow());
  }

  private static void requireNonNullAndNonEmpty(String substring) {
    Objects.requireNonNull(substring, SUBSTRING_MUST_NOT_BE_NULL);
    if (substring.isEmpty()) {
      throw new IllegalArgumentException(SUBSTRING_MUST_NOT_BE_EMPTY);
    }
  }
}
