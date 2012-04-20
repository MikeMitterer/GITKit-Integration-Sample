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
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants;

/**
 * Handles the log out request.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(LogoutServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    log.entering("LogoutServlet", "doGet");

    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
      log.info("user logout!");
    }
    resp.sendRedirect(Constants.HOME_PAGE_URL);
  }
}
