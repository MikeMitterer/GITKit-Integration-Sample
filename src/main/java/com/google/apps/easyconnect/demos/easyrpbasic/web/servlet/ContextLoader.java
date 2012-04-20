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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.apps.easyconnect.demos.easyrpbasic.web.data.AccountServiceImpl;
import com.google.apps.easyconnect.demos.easyrpbasic.web.util.Constants;
import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;
import com.google.apps.easyconnect.easyrp.client.basic.session.RpConfig;
import com.google.apps.easyconnect.easyrp.client.basic.session.SessionBasedSessionManager;
import com.google.apps.easyconnect.easyrp.client.basic.session.SessionManager;

/**
 * Initializes the {@code Context} class when the application starts up.
 * 
 * @author guibinkong@google.com (Guibin Kong)
 */
public class ContextLoader implements ServletContextListener {
	@Override
	public void contextDestroyed(final ServletContextEvent evt) {
	}

	@Override
	public void contextInitialized(final ServletContextEvent evt) {
		initEasyRpContext();
	}

	private void initEasyRpContext() {
		final RpConfig config = new RpConfig.Builder().sessionUserKey(Constants.SESSION_KEY_LOGIN_USER)
				.homeUrl(Constants.HOME_PAGE_URL).signupUrl(Constants.SIGNUP_PAGE_URL).build();
		final AccountService accountService = new AccountServiceImpl();
		final SessionManager sessionManager = new SessionBasedSessionManager(config);
		Context.setConfig(config);
		Context.setAccountService(accountService);
		Context.setSessionManager(sessionManager);
		Context.setGoogleApisDeveloperKey("<Insert your API-key here>");
	}
}
