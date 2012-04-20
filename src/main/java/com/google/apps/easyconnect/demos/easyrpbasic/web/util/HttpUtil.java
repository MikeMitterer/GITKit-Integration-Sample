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

package com.google.apps.easyconnect.demos.easyrpbasic.web.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
  public static String getHostUrl(HttpServletRequest req) {
    StringBuilder buf = new StringBuilder();
    buf.append(req.getScheme()).append("://").append(req.getServerName());
    if ((!"http".equalsIgnoreCase(req.getScheme()) || req.getServerPort() != 80)
        && (!"https".equalsIgnoreCase(req.getScheme()) || req.getServerPort() != 443)) {
      buf.append(":").append(req.getServerPort());
    }
    return buf.toString();
  }
}
