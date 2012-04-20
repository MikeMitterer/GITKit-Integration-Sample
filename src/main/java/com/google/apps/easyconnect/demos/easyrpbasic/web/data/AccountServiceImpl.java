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

import org.json.JSONException;
import org.json.JSONObject;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountException;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;

public class AccountServiceImpl implements AccountService {
  private boolean supportAutoCreateAccount = true;

  public boolean isSupportAutoCreateAccount() {
    return supportAutoCreateAccount;
  }

  public void setSupportAutoCreateAccount(boolean supportAutoCreateAccount) {
    this.supportAutoCreateAccount = supportAutoCreateAccount;
  }

  @Override
  public boolean checkPassword(String email, String password) {
    return UserDAO.checkPassword(email, password);
  }

  @Override
  public Account getAccountByEmail(String email) {
    return UserDAO.getUserByEmail(email);
  }

  public void toFederated(String email) throws AccountException {
    User user = UserDAO.getUserByEmail(email);
    if (user == null) {
      throw new AccountException(AccountException.ACCOUNT_NOT_FOUND);
    }
    user.setFederated(true);
  }

  @Override
  public Account createFederatedAccount(JSONObject assertion) throws AccountException {
    if (!isSupportAutoCreateAccount()) {
      throw new AccountException(AccountException.ACTION_NOT_ALLOWED);
    }
    User user = new User();
    try {
      user.setEmail(assertion.getString("email"));
      if (assertion.has("firstName")) {
        user.setFirstName(assertion.getString("firstName"));
      } else {
        user.setFirstName("empty");
      }
      if (assertion.has("lastName")) {
        user.setLastName(assertion.getString("lastName"));
      } else {
        user.setLastName("empty");
      }
      if (assertion.has("profilePicture")) {
        user.setPhotoUrl(assertion.getString("profilePicture"));
      } else {
        user.setPhotoUrl("http://www.google.com/uds/modules/identitytoolkit/image/nophoto.png");
      }
      user.setFederated(true);
      user.setBirthdayDay("0");
      user.setBirthdayMonth("0");
      user.setBirthdayYear("0");
      User createdUser = UserDAO.create(user);
      return createdUser;
    } catch (JSONException e) {
      throw new AccountException(AccountException.UNKNOWN_ERROR);
    }
  }
}
