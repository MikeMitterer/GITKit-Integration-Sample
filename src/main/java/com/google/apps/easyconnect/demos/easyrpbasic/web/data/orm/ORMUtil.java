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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource40;

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.User;

/**
 * The util that can parse the Annotation and automatically do OR mapping.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class ORMUtil {
  private static final Logger log = Logger.getLogger(ORMUtil.class.getName());

  private static final String DB_NAME = "DB";
  private static final String TABLE_NAME = "users";
  private static final Class<User> USER_CLASS = User.class;
  private static final String SQL_ADD_COLUMN = "ALTER TABLE APP.users ADD \n"
      + "COLUMN %1$s VARCHAR(%2$s) DEFAULT '%3$s'";
  private static final String SQL_DROP_COLUMN = "ALTER TABLE APP.users DROP \n"
      + "COLUMN %1$s";

  private static final DataSource dataSource;
  static {
    EmbeddedDataSource40 ds = new EmbeddedDataSource40();
    ds.setDatabaseName(DB_NAME);
    ds.setCreateDatabase("create");
    dataSource = ds;
  }

  private static boolean tableSynced = false;

  private static Map<Class<? extends AbstractUser>, List<ColumnInfo>> cache =
      new HashMap<Class<? extends AbstractUser>, List<ColumnInfo>>();

  /**
   * Parses the annotation to get all mapping fields defined in 'klass'.
   * @param klass the Class to be parsed
   * @return a list for the OR mapping information
   */
  public static List<ColumnInfo> getDeclaredColumns(Class<? extends AbstractUser> klass) {
    if (cache.containsKey(klass)) {
      return cache.get(klass);
    }

    List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
    Field[] fields = klass.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      ORM map = fields[i].getAnnotation(ORM.class);
      if (map != null) {
        String name = fields[i].getName().toLowerCase();
        int length = map.length() <= 0 ? 255 : map.length();
        fields[i].setAccessible(true);
        columns.add(new ColumnInfo(fields[i], name, length, map.def()));
      }
    }
    columns = Collections.unmodifiableList(columns);
    cache.put(klass, columns);
    return columns;
  }

  /**
   * Get the columns need to be added by comparing the annotation in 'klass' with the columns in the
   * database.
   * @param klass the Class to be parsed
   * @param existColumns the columns that exist in the database
   * @return the columns need to be added
   */
  public static List<String> getAddedColumns(Class<? extends AbstractUser> klass,
      List<String> existColumns) {
    List<String> added = new ArrayList<String>();
    List<ColumnInfo> columns = getDeclaredColumns(klass);
    for (int i = 0; i < columns.size(); i++) {
      String column = columns.get(i).getName();
      if (existColumns == null || !existColumns.contains(column)) {
        added.add(column);
      }
    }
    return added;
  }

  /**
   * Get the columns need to be removed by comparing the annotation in 'klass' with the columns in
   * the database.
   * @param klass the Class to be parsed
   * @param existColumns the columns that exist in the database
   * @return the columns need to be removed
   */
  public static List<String>
      getRemovedColumns(Class<? extends AbstractUser> klass, List<String> existColumns) {
    List<String> removed = new ArrayList<String>();
    if (existColumns != null && !existColumns.isEmpty()) {
      removed.addAll(existColumns);
      List<ColumnInfo> columns = getDeclaredColumns(klass);
      for (int i = 0; i < columns.size(); i++) {
        String column = columns.get(i).getName();
        if (existColumns.contains(column)) {
          removed.remove(column);
        }
      }
    }
    return removed;
  }

  /**
   * Generates the SQL command to create the table for 'klass'.
   * @param klass the Class to be parsed
   * @return the SQL command to create the table for 'klass'
   */
  public static String getCreateTableSql(Class<? extends AbstractUser> klass) {
    StringBuilder buf = new StringBuilder();
    buf.append("CREATE TABLE APP.").append(TABLE_NAME).append(" (")
        .append("id INTEGER NOT NULL")
        .append("   PRIMARY KEY GENERATED ALWAYS AS IDENTITY")
        .append("   (START WITH 1, INCREMENT BY 1),")
        .append("email VARCHAR(255) NOT NULL");

    List<ColumnInfo> columns = getDeclaredColumns(klass);
    for (int i = 0; i < columns.size(); i++) {
      ColumnInfo info = columns.get(i);
      buf.append(", ").append(info.getName()).append(" VARCHAR(")
          .append(info.getLength()).append(") DEFAULT '")
          .append(info.getDefaultValue()).append("'");
    }
    buf.append(")");
    return buf.toString();
  }

  /**
   * Generates the SQL command to insert a new record.
   * @param user the new record
   * @return the SQL command to insert a new record
   */
  public static String getInsertSql(AbstractUser user) {
    StringBuilder buf = new StringBuilder();
    buf.append("INSERT INTO APP.").append(TABLE_NAME).append(" (")
        .append("id, email");

    List<ColumnInfo> columns = getDeclaredColumns(user.getClass());
    for (int i = 0; i < columns.size(); i++) {
      buf.append(", ").append(columns.get(i).getName());
    }
    buf.append(") VALUES (DEFAULT, '").append(user.getEmail()).append("'");
    for (int i = 0; i < columns.size(); i++) {
      Object value = columns.get(i).getDefaultValue();
      try {
        value = columns.get(i).getField().get(user);
      } catch (IllegalArgumentException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      }
      buf.append(", '").append(value).append("'");
    }
    buf.append(")");

    return buf.toString();
  }

  /**
   * Generates the SQL command to update a record.
   * @param user the record that need to be updated to
   * @return the SQL command to update a record
   */
  public static String getUpdateSql(AbstractUser user) {
    StringBuilder buf = new StringBuilder();
    buf.append("UPDATE APP.").append(TABLE_NAME).append(" SET ");

    List<ColumnInfo> columns = getDeclaredColumns(user.getClass());
    for (int i = 0; i < columns.size(); i++) {
      if (i != 0) {
        buf.append(", ");
      }
      buf.append(columns.get(i).getName()).append("=");
      Object value = columns.get(i).getDefaultValue();
      try {
        value = columns.get(i).getField().get(user);
      } catch (IllegalArgumentException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      }
      buf.append("'").append(value).append("'");
    }
    buf.append(" WHERE email='").append(user.getEmail()).append("'");
    return buf.toString();
  }

  /**
   * Gets a connect to the database.
   * @return a connect to the database
   * @throws SQLException if error occurs when access database
   */
  public static Connection getConnection() throws SQLException {
    Connection conn = dataSource.getConnection();
    if (!tableSynced) {
      tableSynced = true;
      if (!isUserTableExist(conn)) {
        log.info("The table doesn't exist, create a new one.");
        createUserTable(conn);
      } else {
        log.info("The table exist.");
        syncUserTable(conn);
      }
    }
    return conn;
  }

  /**
   * Checks whether the target table exists in database.
   * @param conn a connect to the database
   * @return true if table exist, false otherwise
   * @throws SQLException if error occurs when access database
   */
  private static boolean isUserTableExist(Connection conn) throws SQLException {
    DatabaseMetaData metadata = conn.getMetaData();
    String[] names = { "TABLE" };
    ResultSet tableNames = metadata.getTables(null, null, null, names);
    while (tableNames.next()) {
      if (TABLE_NAME.equalsIgnoreCase(tableNames.getString("TABLE_NAME"))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns exist column names in the target table.
   * @param conn a connect to the database
   * @return a list of exist column names
   * @throws SQLException if error occurs when access database
   */
  private static List<String> getUserTableColumns(Connection conn) throws SQLException {
    List<String> result = new ArrayList<String>();
    DatabaseMetaData metadata = conn.getMetaData();
    ResultSet cloumnNames = metadata.getColumns(null, null, TABLE_NAME.toUpperCase(), null);
    while (cloumnNames.next()) {
      String column = cloumnNames.getString("COLUMN_NAME").toLowerCase();
      if (!"id".equalsIgnoreCase(column) && !"email".equalsIgnoreCase(column)) {
        result.add(column);
      }
    }
    return result;
  }

  /**
   * Creates the user table in the database.
   * @param conn a connect to the database
   * @throws SQLException if error occurs when access database
   */
  private static void createUserTable(Connection conn) throws SQLException {
    Statement statement = conn.createStatement();
    String sql = getCreateTableSql(USER_CLASS);
    log.info(sql);
    statement.execute(sql);
  }

  /**
   * Finds the ColumnInfo for a column name.
   * @param columns the list of ColumnInfo to search in.
   * @param name the column name
   * @return the ColumnInfo for a column, or null if not found.
   */
  private static ColumnInfo findColumnInfo(List<ColumnInfo> columns, String name) {
    for (int i = 0; i < columns.size(); i++) {
      if (name.equals(columns.get(i).getName())) {
        return columns.get(i);
      }
    }
    return null;
  }

  /**
   * Sync the table definition in the database with the annotation in the User class.
   * @param conn a connect to the database
   * @throws SQLException if error occurs when access database
   */
  private static void syncUserTable(Connection conn) throws SQLException {
    List<ColumnInfo> columns = getDeclaredColumns(USER_CLASS);
    List<String> exist = getUserTableColumns(conn);
    List<String> removed = getRemovedColumns(USER_CLASS, exist);
    List<String> added = getAddedColumns(USER_CLASS, exist);
    if (removed.isEmpty() && added.isEmpty()) {
      return;
    }
    Statement statement = conn.createStatement();
    for (int i = 0; i < removed.size(); i++) {
      String sql = String.format(SQL_DROP_COLUMN, removed.get(i));
      log.info(sql);
      statement.addBatch(sql);
    }
    for (int i = 0; i < added.size(); i++) {
      ColumnInfo info = findColumnInfo(columns, added.get(i));
      if (info != null) {
        String sql = String.format(SQL_ADD_COLUMN, info.getName(), info.getLength(),
            info.getDefaultValue());
        log.info(sql);
        statement.addBatch(sql);
      }
    }
    statement.executeBatch();
  }

  /**
   * Generates a user object by parse a record in the database.
   * @param <T> the user class type
   * @param row a record in the database
   * @param user the object which will hold the result
   * @throws SQLException if some exception when access database
   */
  public static <T extends AbstractUser> void copyToUser(ResultSet row, T user) throws SQLException {
    user.setId(row.getInt("id"));
    user.setEmail(row.getString("email"));
    List<ColumnInfo> columns = getDeclaredColumns(user.getClass());
    for (int i = 0; i < columns.size(); i++) {
      Object value = row.getString(columns.get(i).getName());
      try {
        columns.get(i).getField().set(user, value);
      } catch (IllegalArgumentException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  /**
   * Parses a user object from a HTTP request. The parameter name for each field is prefix +
   * fieldname.
   * @param req a HTTP request
   * @param prefix the prefix that prepend each column name
   * @return a user object with fields from the HTTP request
   */
  public static User parseUser(HttpServletRequest req, String prefix) {
    User user = new User();
    String email = req.getParameter(prefix + "email");
    if (email != null) {
      email = email.toLowerCase().trim();
      user.setEmail(email);
    }

    List<ColumnInfo> columns = getDeclaredColumns(User.class);
    for (int i = 0; i < columns.size(); i++) {
      ColumnInfo info = columns.get(i);
      String value = req.getParameter(prefix + info.getName());
      value = (value == null) ? info.getDefaultValue() : value;
      try {
        info.getField().set(user, value);
      } catch (IllegalArgumentException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        log.severe(e.getMessage());
        e.printStackTrace();
      }
    }
    return user;
  }

  public static String toJson(AbstractUser user) {
    List<ColumnInfo> columns = getDeclaredColumns(user.getClass());
    StringBuilder buf = new StringBuilder();
    buf.append("{");
    buf.append("id: '").append(user.getId()).append("'");
    buf.append(", email: '").append(user.getEmail()).append("'");
    for (int i = 0; i < columns.size(); i++) {
      String name = columns.get(i).getName();
      Object value;
      try {
        value = columns.get(i).getField().get(user);
      } catch (Exception ex) {
        value = columns.get(i).getDefaultValue();
        ex.printStackTrace();
      }
      buf.append(", ").append(name).append(": '").append(value).append("'");
    }
    buf.append("}");
    return buf.toString();
  }
}
