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

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.AbstractUser;
import com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ORM;
import com.google.apps.easyconnect.easyrp.client.basic.data.Account;

/**
 * This class can be automatically OR mapping to database.
 * <ul>
 * <li>Currently only varchar datatype is supported.</li>
 * <li>The fields ('id', 'email') defined in AbstractUser needn't to be annotated.</li>
 * </ul>
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class User extends AbstractUser implements Account {
  private static final long serialVersionUID = 1L;

  @ORM
  private String firstName;

  @ORM
  private String lastName;

  @ORM
  private String password;

  @ORM
  private String photoUrl;

  @ORM
  private String birthdayMonth;

  @ORM
  private String birthdayDay;

  @ORM
  private String birthdayYear;

  @ORM
  private String federated = "false";

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getBirthdayMonth() {
    return birthdayMonth;
  }

  public void setBirthdayMonth(String birthdayMonth) {
    this.birthdayMonth = birthdayMonth;
  }

  public String getBirthdayDay() {
    return birthdayDay;
  }

  public void setBirthdayDay(String birthdayDay) {
    this.birthdayDay = birthdayDay;
  }

  public String getBirthdayYear() {
    return birthdayYear;
  }

  public void setBirthdayYear(String birthdayYear) {
    this.birthdayYear = birthdayYear;
  }


  public boolean isFederated() {
    return "true".equalsIgnoreCase(this.federated);
  }

  public void setFederated(boolean federated) {
    this.federated = Boolean.toString(federated);
  }



  @Override
  public String getDisplayName() {
    return this.firstName + " " + this.lastName;
  }

}
