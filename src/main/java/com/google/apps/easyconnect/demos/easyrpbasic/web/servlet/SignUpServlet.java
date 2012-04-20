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

package com.google.apps.easyconnect.demos.easyrpbasic.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.DbError;
import com.google.apps.easyconnect.demos.easyrpbasic.web.data.User;
import com.google.apps.easyconnect.demos.easyrpbasic.web.data.UserDAO;
import com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ORMUtil;
import com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants;
import com.google.apps.easyconnect.demos.easyrpbasic.web.util.Validator;

/**
 * Handles the sign-up request to create legacy/federated accounts.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class SignUpServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SignUpServlet.class.getName());

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    User user = ORMUtil.parseUser(req, "");
    if (user.getBirthdayDay() == null || user.getBirthdayDay().isEmpty()) {
      user.setBirthdayDay("0");
    }
    if (user.getBirthdayMonth() == null || user.getBirthdayMonth().isEmpty()) {
      user.setBirthdayMonth("0");
    }
    if (user.getBirthdayYear() == null || user.getBirthdayYear().isEmpty()) {
      user.setBirthdayYear("0");
    }
    String confirm = req.getParameter("confirm");

    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Sign Up Request: ").append(user.toString()).append(",");
      buf.append("confirm=[").append(confirm).append("].");
      log.info(buf.toString());
    }

    List<DbError> errors = new ArrayList<DbError>();

    if (user.getEmail() == null || user.getEmail().isEmpty()
        || !Validator.isValidEmail(user.getEmail())) {
      errors.add(DbError.INVALID_EMAIL);
    } else if (UserDAO.isEmailRegistered(user.getEmail())) {
      errors.add(DbError.EMAIL_REGISTERED);
    }
    if (user.getPassword() == null || user.getPassword().isEmpty()) {
      errors.add(DbError.EMPTY_PWD);
    } else if (!user.getPassword().equals(confirm)) {
      errors.add(DbError.CONFIRM_MISMATCH);
    }
    if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
      errors.add(DbError.EMPTY_FIRSTNAME);
    }
    if (user.getLastName() == null || user.getLastName().isEmpty()) {
      errors.add(DbError.EMPTY_LASTNAME);
    }
    if (!Validator.isValidMonth(user.getBirthdayMonth())) {
      errors.add(DbError.INVALID_MONTH);
    }
    if (!Validator.isValidDay(user.getBirthdayDay())) {
      errors.add(DbError.INVALID_DAY);
    }
    if (!Validator.isValidYear(user.getBirthdayYear())) {
      errors.add(DbError.INVALID_YEAR);
    }
    if (errors.isEmpty()) {
      User createdUser = UserDAO.create(user);
      if (createdUser == null) {
        errors.add(DbError.CREATE_USER_FAIL);
      } else {
        log.info("Successfully create user: " + createdUser.getEmail());
        req.getSession(true).setAttribute(Constants.SESSION_KEY_LOGIN_USER, createdUser);
        resp.sendRedirect(Constants.HOME_PAGE_URL);
        return;
      }
    }
    if (log.isLoggable(Level.SEVERE)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Failed to create user: ");
      for (int i = 0; i < errors.size(); i++) {
        buf.append(errors.get(i).name()).append(", ");
      }
      log.severe(buf.toString());
    }
    log.severe("Failed to create user: " + errors.toArray());
    req.setAttribute("errors", errors);
    req.getRequestDispatcher(Constants.SIGNUP_PAGE_URL).forward(req, resp);
  }
}
