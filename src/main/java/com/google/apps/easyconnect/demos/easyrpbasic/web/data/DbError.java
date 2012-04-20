/* Copyright 2011 Google Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.apps.easyconnect.demos.easyrpbasic.web.data;

/**
 * Predefined error types and their message texts.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public enum DbError {
  INVALID_EMAIL("Email address is not valid."),
  EMAIL_REGISTERED("Email already registered."),
  EMAIL_NOT_FOUNT("Email hasn't registered yet."),
  EMPTY_PWD("Password cannot be empty."),
  CONFIRM_MISMATCH("The confirm doesn't match the password."),
  EMPTY_FIRSTNAME("Firstname cannot be empty."),
  EMPTY_LASTNAME("Lastname cannot be empty."),
  INVALID_MONTH("Invalid birthday month value, should be an integer between" +
      " 0 and 12."),
  INVALID_DAY("Invalid birthday day value, should be an integer between" +
      " 0 and 31."),
  INVALID_YEAR("Invalid birthday year value, should be an integer between" +
      " 1901 and 2011, or 0."),
  CREATE_USER_FAIL("Failed to create the new user."),
  PWD_ERR("Parameter 'column' cannot be empty.");

  private String message;

  private DbError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
