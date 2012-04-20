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
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.data.User" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants" %>
<%@ page import="com.google.apps.easyconnect.demos.easyrpbasic.web.util.HttpUtil" %>
<%@ page import="java.util.ArrayList" %>
<%
User user = (User)session.getAttribute(Constants.SESSION_KEY_LOGIN_USER);
if (user == null) {
  response.sendRedirect(Constants.HOME_PAGE_URL);
  return;
}
String hostUrl = HttpUtil.getHostUrl(request);
String photoUrl = user.getPhotoUrl();
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
<script type=text/javascript src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<link type=text/css rel=stylesheet href="stylesheet/codesite.css" />
<link type=text/css rel=stylesheet href="stylesheet/rp.css" />
<style type="text/css">
body {
  margin: 15px 0 2px;
  background-color: #fff;
}
</style>
<script type=text/javascript>
  function submitEditForm() {
    jQuery('#editForm').submit();
  };
  function onUpdateBtnClicked() {
   jQuery("#submitting").show();
   window.setTimeout(submitEditForm, 600);
  }
  function onDeleteBtnClicked() {
    jQuery('#delForm').submit();
  }

  jQuery(function() {
    jQuery("input[type='radio'][name='predefinedPhoto']").click(function() {
      jQuery('#photourl').val(jQuery(this).val());
    });
  });
</script>
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
      <div id="reg_area">
        <div style="text-align: left; font-size: 15px; color: #1C94C4; font-weight: bold; padding: 8px;">
          Welcome to DemoRP site!
        </div>
        <div style="text-align: left; font-size: 13px; color: #f00; padding: 0px 8px 20px 20px;">
          The actions in this page are for testing only. 
        </div>
        <form method="post" action="/account" name="editForm" id="editForm">
        <input type="hidden" name="action" value="edit">
        <table cellpadding="1" cellspacing="0">
          <tbody>
            <tr style="vertical-align: top;">
              <td class="label"><label for="email">Your Email:</label></td>
              <td>
                <input id="email" name="email" value='<%= user.getEmail() %>' type="text" readonly style="background-color: #ddd;">
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="firstname">First Name:</label></td>
              <td>
                <input id="firstname" name="firstname" value='<%= (user.getFirstName() == null ? "" : user.getFirstName()) %>' type="text">
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label"><label for="lastname">Last Name:</label></td>
              <td>
                <input id="lastname" name="lastname" value='<%= (user.getLastName() == null ? "" : user.getLastName()) %>' type="text">
              </td>
            </tr>
            <tr style="vertical-align: top;"><td class="label">Photo Url:</td>
              <td>
                <input type=text id='photourl' name=photourl value="<%= photoUrl %>" style="width:410px;"><br />
                <p style="color:#999;">Or select below predefined photos.</p>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/nophoto.png" id="nophoto" <% if ((hostUrl + "/image/nophoto.png").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="nophoto"><img src="<%= hostUrl %>/image/nophoto.png" /></label>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/user1.png" id="user1" <% if ((hostUrl + "/image/user1.png").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="user1"><img src="<%= hostUrl %>/image/user1.png" /></label>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/user2.png" id="user2" <% if ((hostUrl + "/image/user2.png").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="user2"><img src="<%= hostUrl %>/image/user2.png" /></label>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/user3.png" id="user3" <% if ((hostUrl + "/image/user3.png").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="user3"><img src="<%= hostUrl %>/image/user3.png" /></label>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/user4.jpg" id="user4" <% if ((hostUrl + "/image/user4.jpg").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="user4"><img src="<%= hostUrl %>/image/user4.jpg" /></label>
                <input type=radio name=predefinedPhoto value="<%= hostUrl %>/image/user5.jpg" id="user5" <% if ((hostUrl + "/image/user1.jpg").equals(photoUrl)) { %> checked <% } %> style="width:auto;">
                <label for="user5"><img src="<%= hostUrl %>/image/user5.jpg" /></label>
              </td>
            </tr>
            <tr style="vertical-align: top;">
              <td class="label">Birthday:</td>
              <td>
                <select class="" id="birthdaymonth" name="birthdaymonth" >
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
        </form>
        <p>
        <div id="submitting" style="display:none; background-color: yellow; font-weight: bold;">Send updating request to server...</div>
        <table><tr><td>
        <table cellspacing="0" cellpadding="0" border="0" class="signup-button" onclick="onUpdateBtnClicked();">
          <tbody>
            <tr>
              <td class="signup-button-left"></td>
              <td class="signup-button-middle">
                <a class="signup-button-link" onclick="return false;" title="Update account profile information" >Update Profile</a>
              </td>
              <td class="signup-button-right"></td>
            </tr>
          </tbody>
        </table>
        </td>
        <td>
        <form method="post" action="/account" name="delForm" id="delForm">
          <input type="hidden" name="action" value="delete">
        </form>
        <table cellspacing="0" cellpadding="0" border="0" class="signup-button" onclick="onDeleteBtnClicked();">
          <tbody>
            <tr>
              <td class="signup-button-left"></td>
              <td class="signup-button-middle">
                <a class="signup-button-link" onclick="return false;" title="Delete your account to test sign-up again" >Delete Account</a>
              </td>
              <td class="signup-button-right"></td>
            </tr>
          </tbody>
        </table>
        </td>
        <td style="font-size: 13px; color: #999;">
          Or <a href="/home.jsp" style="font-size: 13px; color: #1C94C4;">Cancel</a>
        </td>
        </tr></table>
        </p>
      </div>
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
