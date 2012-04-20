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
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.orm.ORMUtil" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.HttpUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
if (session.getAttribute(Constants.SESSION_KEY_LOGIN_USER) != null) {
  session.removeAttribute(Constants.SESSION_KEY_LOGIN_USER);
}
String hostUrl = HttpUtil.getHostUrl(request);
User user = ORMUtil.parseUser(request, "");
if (user.getPhotoUrl() == null || user.getPhotoUrl().trim().isEmpty()) {
  user.setPhotoUrl(hostUrl + "/image/nophoto.png");
}

List<DbError> errors = new ArrayList<DbError>();
if (request.getAttribute("errors") != null) {
  errors = (ArrayList)request.getAttribute("errors");
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
<script type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<link type=text/css rel=stylesheet href="stylesheet/codesite.css" />
<link type=text/css rel=stylesheet href="stylesheet/rp.css" />
<style type="text/css">
body {
  margin: 15px 0 2px;
  background-color: #fff;
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
  <!-- BODY -->
  <table width="780" cellpadding="0" border=0>
  <tbody>
  <tr>
  <td width="1%">&nbsp;</td>
  <td height="98%" bgcolor="#FFFFFF" align="center">
    <div id="reg_box">
      <form method="post" action="/signup" name="regForm">
      <input type="hidden" name=photoUrl value="<%= user.getPhotoUrl() %>">
      <div id="reg_area">
        <div style="text-align: left; font-size: 15px; color: #1C94C4; font-weight: bold; padding: 8px;">
          Welcome to DemoRP site!
        </div>
        <div style="text-align: left; font-size: 13px; color: #999; padding: 0px 8px 20px 20px;">
          Please provide follow information to create your account.
        </div>
        <table cellpadding="1" cellspacing="0">
          <tbody>
            <tr style="vertical-align: top;">
              <td class="label"><label for="email">Your Email:</label></td>
              <td>
                <input id="email" name="email" value='<%= user.getEmail() == null ? "" : user.getEmail() %>' type="text">
                <% if (errors.contains(DbError.INVALID_EMAIL)) { %><div class="signup-error"><%= DbError.INVALID_EMAIL.getMessage() %></div>
                <% } else if (errors.contains(DbError.EMAIL_REGISTERED)) { %><div class="signup-error"><%= DbError.EMAIL_REGISTERED.getMessage() %></div><% } %>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="firstname">First Name:</label></td>
              <td>
                <input id="firstname" name="firstname" value='<%= user.getFirstName() == null ? "" : user.getFirstName() %>' type="text">
                <% if (errors.contains(DbError.EMPTY_FIRSTNAME)) { %><div class="signup-error"><%= DbError.EMPTY_FIRSTNAME.getMessage() %></div><% } %>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="lastname">Last Name:</label></td>
              <td>
                <input id="lastname" name="lastname" value='<%= user.getLastName() == null ? "" : user.getLastName() %>' type="text">
                <% if (errors.contains(DbError.EMPTY_LASTNAME)) { %><div class="signup-error"><%= DbError.EMPTY_LASTNAME.getMessage() %></div><% } %>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="password">Password:</label></td>
              <td>
                <input id="password" name="password" value="" type="password">
                <% if (errors.contains(DbError.EMPTY_PWD)) { %><div class="signup-error"><%= DbError.EMPTY_PWD.getMessage() %></div><% } %>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="confirm">Re-enter Password:</label></td>
              <td>
                <input id="confirm" name="confirm" value="" type="password">
                <% if (errors.contains(DbError.CONFIRM_MISMATCH)) { %><div class="signup-error"><%= DbError.CONFIRM_MISMATCH.getMessage() %></div><% } %>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label">Birthday:</td>
              <td>
                <select class="" id="birthdaymonth" name="birthdaymonth">
                  <option value="0">Month:</option><%
                  for (int i = 1; i <= 12; i++) {
                    %><option value="<%= i %>" <% if (String.valueOf(i).equals(user.getBirthdayMonth())) { %>selected<% } %>><%= i %></option><%
                  }%>
                </select>
                <select name="birthdayday" id="birthdayday">
                  <option value="0">Day:</option><%
                  for (int i = 1; i <= 31; i++) {
                    %><option value="<%= i %>" <% if (String.valueOf(i).equals(user.getBirthdayDay())) { %>selected<% } %>><%= i %></option><%
                  }%>
                </select>
                <select name="birthdayyear" id="birthdayyear">
                  <option value="0">Year:</option><%
                  for (int i = 2011; i >= 1901; i--) {
                    %><option value="<%= i %>" <% if (String.valueOf(i).equals(user.getBirthdayYear())) { %>selected<% } %>><%= i %></option><%
                  }%>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
        <p>
        <table><tr><td>
        <table cellspacing="0" cellpadding="0" border="0" class="signup-button" onclick="regForm.submit();">
          <tbody>
            <tr>
              <td class="signup-button-left"></td>
              <td class="signup-button-middle">
                <a class="signup-button-link" onclick="return false;">Create Account</a>
              </td>
              <td class="signup-button-right"></td>
            </tr>
          </tbody>
        </table>
        </td>
        <td style="font-size: 13px; color: #999;">
          Or <a href="/" style="font-size: 13px; color: #1C94C4;">Cancel</a>
        </td>
        </tr></table>
        </p>
      </div>
      </form>
    </div>
  <td width="1%">&nbsp;</td>
  </tr>
  <td colspan="3">&nbsp;</td>
  <tr>
  </tr>

  <tr>
  <td colspan="3" valign="top" align="center"><hr noshade size="1" color="#DDDDDD" style="margin-top:5px">
  <p>
   2011 DemoRP -
  <a href="javascript:void 0;">Terms of Service</a>
  -
  <a href="javascript:void 0;">Privacy Policy</a>
  -
  <a href="javascript:void 0;">Help Center</a>

  -
  <a href="javascript:void 0;">
  Getting Started Guide</a>
  </p></td>
  </tr>
  </tbody>
  </table>
  <!-- FOOTER -->
  <br>
</center>
</body>
</html>
