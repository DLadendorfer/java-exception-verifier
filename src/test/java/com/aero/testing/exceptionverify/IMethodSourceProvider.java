// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify;

import java.io.IOError;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Interface for tests that require some generic {@link
 * org.junit.jupiter.params.provider.MethodSource}.
 *
 * @author Daniel Ladendorfer
 */
public interface IMethodSourceProvider {
  static Stream<Arguments> throwableArgumentFactory() {
    return Stream.of(
            new Throwable(),
            new Throwable("Throwable with message"),
            new Throwable(new NullPointerException()),
            new Throwable("Throwable with message and cause", new NullPointerException()),
            new Exception(),
            new Exception("Exception with message"),
            new Exception(new NullPointerException()),
            new Exception("Exception with message and cause", new NullPointerException()),
            new RuntimeException(),
            new RuntimeException("RuntimeException with message"),
            new RuntimeException(new NullPointerException()),
            new RuntimeException(
                "RuntimeException with message and cause", new NullPointerException()),
            new Error(),
            new Error("Error with message"),
            new Error(new NullPointerException()),
            new Error("Error with message and cause", new NullPointerException()),
            new NullPointerException(),
            new IllegalArgumentException(),
            new ArithmeticException(),
            new IllegalStateException(),
            new InterruptedException(),
            new IOException(),
            new StackOverflowError(),
            new OutOfMemoryError(),
            new IOError(new IOException()))
        .map(Arguments::of);
  }
}
