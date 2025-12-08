// -------------------------------------------------------------------------------
// Copyright (c) Ladendorfer Daniel.
// All Rights Reserved.  See LICENSE in the project root for license information.
// -------------------------------------------------------------------------------
package com.aero.testing.exceptionverify.internal.delegates;

import org.jspecify.annotations.NullMarked;

/**
 * Abstract base class for all delegates that can throw an {@link AssertionError}.
 *
 * @author Daniel Ladendorfer
 */
@NullMarked
public class AbstractFailureDelegate {

  /**
   * Creates and throws a newly created instance of {@link AssertionError}.
   *
   * @param message the reason of failure
   * @see AssertionError
   */
  protected void fail(String message) {
    throw new AssertionError(message);
  }
}
