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

package com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm;

import java.lang.reflect.Field;

/**
 * The mapping info for a column.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class ColumnInfo {
  private Field field;
  private String name;
  private int length;
  private String defaultValue;

  public ColumnInfo(Field field, String name, int length, String defaultValue) {
    this.field = field;
    this.name = name;
    this.length = length;
    this.defaultValue = defaultValue;
  }

  public Field getField() {
    return field;
  };

  public String getName() {
    return name;
  }

  public int getLength() {
    return length;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
