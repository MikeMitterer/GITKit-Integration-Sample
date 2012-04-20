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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a field to map to a column in the database.
 * <ul>
 * <li>Currently only varchar datatype is supported.</li>
 * <li>The fields ('id', 'email') defined in AbstractUser needn't to be annotated.</li>
 * </ul>
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ORM {
  /**
   * The length of the varchar type.
   * @return the length of the varchar type
   */
  int length() default 255;

  /**
   * The default value for this field. TODO: not fully supported yet.
   * @return the default value for this field
   */
  String def() default "";
}
