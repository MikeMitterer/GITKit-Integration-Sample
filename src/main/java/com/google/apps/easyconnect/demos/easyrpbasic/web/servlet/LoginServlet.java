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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.UserDAO;
import com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants;

/**
 * Handles the login request.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(
      LoginServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws
      IOException {
    resp.sendRedirect(Constants.HOME_PAGE_URL);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws
      IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (log.isLoggable(Level.INFO)) {
      StringBuilder buf = new StringBuilder();
      buf.append("Login Request: email=[").append(email).append("],");
      buf.append("password=[").append(password).append("]");
      log.info(buf.toString());
    }
    if (email == null || email.isEmpty()) {
      resp.sendRedirect(Constants.HOME_PAGE_URL + "?error=PWD_ERR");
      return;
    }
    if (UserDAO.checkPassword(email, password)) {
      req.getSession().setAttribute(Constants.SESSION_KEY_LOGIN_USER,
          UserDAO.getUserByEmail(email));
      log.info("User [" + email + "] logged in.");
      resp.sendRedirect(Constants.HOME_PAGE_URL);
    } else {
      resp.sendRedirect(Constants.HOME_PAGE_URL + "?error=PWD_ERR&email=" + email);
    }
  }
}
