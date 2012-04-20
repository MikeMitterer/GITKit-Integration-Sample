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

package com.google.apps.easyconnect.demos.easyrpbasic.web.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if a email is valid.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class Validator {
  private static final String EMAIL_REGEX = "\\w+(\\.\\w+)*@(\\w+(\\.\\w+)+)";
  private static final List<String> MONTH_VALUES;
  private static final List<String> DAY_VALUES;
  private static final List<String> YEAR_VALUES;
  static {
    List<String> values = new ArrayList<String>(13);
    for (int i = 0; i < 13; i++) {
      values.add(String.valueOf(i));
    }
    MONTH_VALUES = values;

    values = new ArrayList<String>(32);
    for (int i = 0; i < 32; i++) {
      values.add(String.valueOf(i));
    }
    DAY_VALUES = values;

    values = new ArrayList<String>(112);
    values.add("0");
    for (int i = 2011; i > 1900; i--) {
      values.add(String.valueOf(i));
    }
    YEAR_VALUES = values;
  }

  /**
   * Checks if a email is valid.
   * 
   * @param email the email address to be checked
   * @return ture for valid, false otherwise
   */
  public static boolean isValidEmail(String email) {
    return (email != null) && email.matches(EMAIL_REGEX);
  }

  public static boolean isValidDay(String day) {
    return day != null && DAY_VALUES.contains(day);
  }

  public static boolean isValidMonth(String month) {
    return month != null && MONTH_VALUES.contains(month);
  }

  public static boolean isValidYear(String year) {
    return year != null && YEAR_VALUES.contains(year);
  }
}
