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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Handles the User Table directly.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class DbmsServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(
      DbmsServlet.class.getName());

  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.sendRedirect(Constants.DBMS_PAGE_URL);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String action = req.getParameter("action");
    log.entering("AccountServlet", "doPost", action);

    if ("newUser".equals(action)) {
      newUser(req, resp);
    } else if ("deleteUser".equals(action)) {
      deleteUser(req, resp);
    } else if ("editUser".equals(action)) {
      editUser(req, resp);
    } else {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  private void editUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    DbError err = null;
    User user = ORMUtil.parseUser(req, "edit_");
    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("DBMS Edit User: ").append(user.toString());
      log.info(buf.toString());
    }
    if (user.getBirthdayDay() == null || user.getBirthdayDay().isEmpty()) {
      user.setBirthdayDay("0");
    }
    if (user.getBirthdayMonth() == null || user.getBirthdayMonth().isEmpty()) {
      user.setBirthdayMonth("0");
    }
    if (user.getBirthdayYear() == null || user.getBirthdayYear().isEmpty()) {
      user.setBirthdayYear("0");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()) {
      err = DbError.INVALID_EMAIL;
    } else if (!UserDAO.isEmailRegistered(user.getEmail())) {
      err = DbError.EMAIL_NOT_FOUNT;
    } else if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
      err = DbError.EMPTY_FIRSTNAME;
    } else if (user.getLastName() == null || user.getLastName().isEmpty()) {
      err = DbError.EMPTY_LASTNAME;
    } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
      err = DbError.EMPTY_PWD;
    } else if (!Validator.isValidDay(user.getBirthdayDay())) {
      err = DbError.INVALID_DAY;
    } else if (!Validator.isValidMonth(user.getBirthdayMonth())) {
      err = DbError.INVALID_MONTH;
    } else if (!Validator.isValidYear(user.getBirthdayYear())) {
      err = DbError.INVALID_YEAR;
    }
    if (err == null) {
      UserDAO.update(user);
      log.info("Successfully update user [" + user.getEmail() + "] by DBMS.");
      resp.sendRedirect(Constants.DBMS_PAGE_URL);
    } else {
      StringBuilder buf = new StringBuilder();
      buf.append(Constants.DBMS_PAGE_URL);
      String parameters = getParameterUrl(req, req.getParameterNames());
      if (parameters != null && !parameters.isEmpty()) {
        buf.append("?").append(parameters);
      }
      buf.append("&tab=edit&error=").append(err.name());
      resp.sendRedirect(buf.toString());
    }
  }

  private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String email = req.getParameter("email");
    DbError err = null;
    if (email == null || email.isEmpty()) {
      err = DbError.INVALID_EMAIL;
    } else if (!UserDAO.isEmailRegistered(email)) {
      err = DbError.EMAIL_NOT_FOUNT;
    } else {
      UserDAO.delete(email);
      log.info("Successfully delete user [" + email + "] by DBMS.");
    }
    if (err == null) {
      resp.sendRedirect(Constants.DBMS_PAGE_URL);
    } else {
      resp.sendRedirect(Constants.DBMS_PAGE_URL + "?error=" + err.name());
    }
  }

  private void newUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    DbError err = null;
    User user = ORMUtil.parseUser(req, "");
    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("DBMS Create User: ").append(user.toString());
      log.info(buf.toString());
    }
    if (user.getBirthdayDay() == null || user.getBirthdayDay().isEmpty()) {
      user.setBirthdayDay("0");
    }
    if (user.getBirthdayMonth() == null || user.getBirthdayMonth().isEmpty()) {
      user.setBirthdayMonth("0");
    }
    if (user.getBirthdayYear() == null || user.getBirthdayYear().isEmpty()) {
      user.setBirthdayYear("0");
    }
    if (user.getEmail() == null || user.getEmail().isEmpty()
        || !Validator.isValidEmail(user.getEmail())) {
      err = DbError.INVALID_EMAIL;
    } else if (UserDAO.isEmailRegistered(user.getEmail())) {
      err = DbError.EMAIL_REGISTERED;
    } else if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
      err = DbError.EMPTY_FIRSTNAME;
    } else if (user.getLastName() == null || user.getLastName().isEmpty()) {
      err = DbError.EMPTY_LASTNAME;
    } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
      err = DbError.EMPTY_PWD;
    } else if (!Validator.isValidDay(user.getBirthdayDay())) {
      err = DbError.INVALID_DAY;
    } else if (!Validator.isValidMonth(user.getBirthdayMonth())) {
      err = DbError.INVALID_MONTH;
    } else if (!Validator.isValidYear(user.getBirthdayYear())) {
      err = DbError.INVALID_YEAR;
    }
    if (err == null) {
      User createdUser = UserDAO.create(user);
      if (createdUser == null) {
        err = DbError.CREATE_USER_FAIL;
      }
    }
    if (err == null) {
      UserDAO.update(user);
      log.info("Successfully create user [" + user.getEmail() + "] by DBMS.");
      resp.sendRedirect(Constants.DBMS_PAGE_URL);
    } else {
      StringBuilder buf = new StringBuilder();
      buf.append(Constants.DBMS_PAGE_URL);
      String parameters = getParameterUrl(req, req.getParameterNames());
      if (parameters != null && !parameters.isEmpty()) {
        buf.append("?").append(parameters);
      }
      buf.append("&tab=insert&error=").append(err.name());
      resp.sendRedirect(buf.toString());
    }
  }

  private static List<String> OMIT_PARAMS = Arrays.asList(
      new String[] { "tab" });

  @SuppressWarnings("rawtypes")
  private String getParameterUrl(HttpServletRequest req, Enumeration names) {
    StringBuilder buf = new StringBuilder();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      if (!OMIT_PARAMS.contains(name)) {
        try {
          buf.append("&").append(name).append("=").
              append(URLEncoder.encode(req.getParameter(name), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
          log.severe(e.getMessage());
          e.printStackTrace();
        }
      }
    }
    return buf.toString();
  }
}
