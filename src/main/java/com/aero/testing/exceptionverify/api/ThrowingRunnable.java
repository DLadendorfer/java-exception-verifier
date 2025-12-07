// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.api;

/**
 * A functional interface similar to {@link Runnable}, but whose {@link #run()} method is permitted
 * to throw any {@link Throwable}.
 *
 * <p>This interface is intended for use in testing utilities or other contexts where an executable
 * block may throw checked exceptions. <br>
 *
 * {@snippet :
 * ExceptionVerifier.assertThat(() -> performOperation());
 * }
 *
 * @see Runnable
 */
@FunctionalInterface
public interface ThrowingRunnable {
  @SuppressWarnings("java:S112") // most generic exception type required
  void run() throws Throwable;
}
