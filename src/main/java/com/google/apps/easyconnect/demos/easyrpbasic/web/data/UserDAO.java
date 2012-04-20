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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ORMUtil;

public class UserDAO {
  private static final Logger log = Logger.getLogger(UserDAO.class.getName());

  private static final String SQL_DELETE_USER = "DELETE FROM APP.users WHERE "
      + "email='%1$s'";

  private static final String SQL_GET_USER = "SELECT * FROM APP.users WHERE "
      + "email='%1$s'";

  private static final String SQL_USRE_LOGIN = "SELECT * FROM APP.users WHERE "
      + "email='%1$s' AND password='%2$s'";

  private static final String SQL_GET_ALL_USER = "SELECT * FROM APP.users";

  public static void delete(String email) {
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = String.format(SQL_DELETE_USER, email);
      log.info(sql);
      statement.execute(sql);
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void update(User user) {
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = ORMUtil.getUpdateSql(user);
      log.info(sql);
      statement.execute(sql);
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
  }

  public static boolean isEmailRegistered(String email) {
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = String.format(SQL_GET_USER, email);
      log.info(sql);
      ResultSet rows = statement.executeQuery(sql);
      return rows.next();
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public static boolean checkPassword(String email, String password) {
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = String.format(SQL_USRE_LOGIN, email,
          password == null ? "" : password);
      log.info(sql);
      ResultSet rows = statement.executeQuery(sql);
      return rows.next();
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  public static User getUserByEmail(String email) {
    User user = null;
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = String.format(SQL_GET_USER, email);
      log.info(sql);
      ResultSet rows = statement.executeQuery(sql);
      if (rows.next()) {
        user = new User();
        ORMUtil.copyToUser(rows, user);
      }
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
    return user;
  }

  public static User create(User user) {
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      String sql = ORMUtil.getInsertSql(user);
      log.info(sql);
      statement.execute(sql);
      return getUserByEmail(user.getEmail());
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  public static List<User> getAllUsers() {
    List<User> users = new ArrayList<User>();
    try {
      Connection conn = ORMUtil.getConnection();
      Statement statement = conn.createStatement();
      ResultSet rows = statement.executeQuery(SQL_GET_ALL_USER);
      while (rows.next()) {
        User user = new User();
        ORMUtil.copyToUser(rows, user);
        users.add(user);
      }
    } catch (SQLException e) {
      log.severe(e.getMessage());
      e.printStackTrace();
    }
    return users;
  }
}
