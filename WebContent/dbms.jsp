<%-- Copyright 2011 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.DbError" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.User" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.UserDAO" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ColumnInfo" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ORMUtil" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%
User loggedInUser = (User)session.getAttribute(Constants.SESSION_KEY_LOGIN_USER);

List<User> users = UserDAO.getAllUsers();
List<ColumnInfo> columns = ORMUtil.getDeclaredColumns(User.class);
String tab = request.getParameter("tab");
DbError error = null;
String err = request.getParameter("error");
if (err != null && !err.isEmpty()) {
  try {
    error = DbError.valueOf(err);
  } catch(Exception e) {
  }
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<title>Demo Relying Party site using GIT</title>
<link rel="icon" href="image/git.png" type="image/png">
<link type=text/css rel=stylesheet href="stylesheet/rp.css" />
<script type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript">
  function setEditUser(user) {
    for (var p in user) {
      var val = user[p];
      jQuery('#edit_' + p).val(val);
    }
    jQuery('#edit').click();
  }

  jQuery(function() {
    jQuery('.tabs li').click(function(){
      jQuery('.tabs li').removeClass('tab-sel');
      jQuery(this).addClass('tab-sel');
      jQuery('.tab-panel').removeClass('tab-panel-sel');
      var id = jQuery(this).attr('id') + '-panel';
      jQuery('#' + id).addClass('tab-panel-sel');
    });<%
    if (tab != null && !tab.isEmpty()) {
      %>jQuery('#<%= tab %>').click();<%
    } %>
  });
</script>
<style type="text/css">
.error {
  background-color: yellow;
  color: red;
  font-size: 14px;
  font-weight: bold;
  padding: 3px;
  text-align: left;
  width: 900px;
}
</style>
</head>
<body>
<center>
<div style="width:780px;">
  <table cellpadding="2" cellspacing="0" border=0 style="padding-left:50px;align:left">
    <tr>
    <td valign="top" width="1%">
      <a href="/">
        <img src="image/rp_logo.png" style="border:0px;width:90px;height:60px;">
      </a>
    </td>
    <td width="98%" style="padding-left: 20px; font-size: 20px; color: #1C94C4;">
        Demo RP Site using <span style="color: #0000CC; font-weight: bold;">GIT</span>
    </td>
    </tr>
  </table>
</div>
<br>
<% if (error != null) { %>
  <div class="error"><%= error.getMessage() %></div>
<% } %>
<ul class="tabs">
  <li class="tab-sel" id="browse">
    <a href="javascript: void 0;">Browse Users</a>
  </li>
  <li id="insert">
    <a href="javascript: void 0;">Insert User</a>
  </li>
  <li id="edit">
    <a href="javascript: void 0;">Edit User</a>
  </li>
</ul>

<div id="browse-panel" class="tab-panel tab-panel-sel">
<script type="text/javascript">
  function deleteUser(email) {
    jQuery('#deleteEmail').val(email);
    jQuery('#deleteBtn').click();
  }
</script>
<form action="dbms" method="POST">
  <input type=hidden name="action" value="deleteUser">
  <input type=hidden name="email" id="deleteEmail" value="">
  <input type=submit value="delete" id="deleteBtn" style="display:none">
</form>
<table border=1 cellpadding=5 style="table-layout:fixed;">
  <tr><td title="id">id</td><td title="email">email</td>
<% for (int j = 0; j < columns.size(); j++) {
  %><td title="<%= columns.get(j).getName() %>"><%= columns.get(j).getName() %></td><%
} %>
  <td title="Action">Action</td>
  </tr>
<% for (int i = 0; i < users.size(); i++) {
  User user = users.get(i);%>
  <tr>
    <td title="<%= user.getId() %>"><%= user.getId() %></td>
    <td title="<%= user.getEmail() %>">
      <div style="width:100px;word-wrap:break-word; overflow:hidden;"><%= user.getEmail() %></div>
    </td>
    <% for (int j = 0; j < columns.size(); j++) {
      String column = columns.get(j).getName();
      Object value = columns.get(j).getField().get(user);
      if ("photourl".equalsIgnoreCase(column) && value != null && !(((String)value).isEmpty())) {
        %><td title="<%= value %>"><img src="<%= value %>" style="width:42px"></td><%
      } else {
        %><td title="<%= value %>"><%= value %></td><%
      }
    } %>
  <td><a href="javascript:deleteUser('<%= user.getEmail() %>')">Delete</a><br />
  <a href="javascript:setEditUser(<%= user.toString() %>);">Edit</a>
  </td>
  </tr>
<% } %>
</table>
</div>

<div class="tab-panel" id="insert-panel">
<form action="dbms" method="POST">
  <input type=hidden name="action" value="newUser">
  <table cellpadding=5>
  <% 
    String newEmail = request.getParameter("email");
    if (newEmail == null) {
      newEmail = "";
    }
  %>
  <tr><td style="text-align:right;">email</td><td><input type=text name='email' value='<%= newEmail %>'></td>
  <% int k = 1;
    boolean hasTr = true;
    for (int i = 0; i < columns.size(); i++) {
      if (k == 0) {
        %><tr><%
        hasTr = true;
      }
      String name = columns.get(i).getName();
      String val = request.getParameter(name);
      if (val == null) {
        val = "";
      }
      %><td style="text-align:right;"><%= name %></td>
      <td><input type=text name='<%= name %>' value='<%= val %>'></td><%
      k++;
      if (k == 3) { 
        k = 0;
        %><tr><%
        hasTr = false;
      }
      if (i == columns.size() - 1 && hasTr) {
        %></tr><%
      }
    } %>
  </table>
  <input type=submit value="Create New User">
</form>
<hr>
<ul style="text-align: left; color: #aaa;">
<li>Valid Month values: empty, 0-12.</li>
<li>Valid Day values: empty, 0-31.</li>
<li>Valid Year values: empty, 1901-2011.</li>
</ul>
</div>


<div class="tab-panel" id="edit-panel">
<form action="dbms" method="POST">
  <input type=hidden name="action" value="editUser">
  <p style="text-align: left; color: #aaa; font-size:14px;">You can click the 'Edit' link in the first tab to copy values into the text boxes.</p>
  <table cellpadding=5>
  <tr>
    <% 
      String editId = request.getParameter("edit_id");
      if (editId == null) {
        editId = "";
      }
      String editEmail = request.getParameter("edit_email");
      if (editEmail == null) {
        editEmail = "";
      }
    %>
    <td style="text-align:right;">id</td><td><input type=text name='edit_id' id='edit_id' value='<%= editId %>' readonly style="background-color:#eee;"></td>
    <td style="text-align:right;">email</td><td><input type=text name='edit_email' id='edit_email' value='<%= editEmail %>' readonly style="background-color:#eee;"></td>
  <% k = 2;
    hasTr = true;
    for (int i = 0; i < columns.size(); i++) {
      if (k == 0) {
        %><tr><%
        hasTr = true;
      }
      String name = columns.get(i).getName();
      String val = request.getParameter("edit_" + name);
      if (val == null) {
        val = "";
      }
      %><td style="text-align:right;"><%= name %></td>
      <td><input type=text name='edit_<%= name %>' value='<%= val %>' id='edit_<%= name %>'></td><%
      k++;
      if (k == 3) { 
        k = 0;
        %><tr><%
        hasTr = false;
      }
      if (i == columns.size() - 1 && hasTr) {
        %></tr><%
      }
    } %>
  </table>
  <input type=submit value="Update User">
</form>
<hr>
<ul style="text-align: left; color: #aaa;">
<li>Valid Month values: empty, 0-12.</li>
<li>Valid Day values: empty, 0-31.</li>
<li>Valid Year values: empty, 1901-2011.</li>
</ul>
</div>

<div class="tips">This page should be only accessed by administrators.
<br />This tool is not well tested, used for the GIT integration tutorial only. </div>
</center>
</body>
</html>
