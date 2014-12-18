/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;

import videoshop.AbstractWebIntegrationTests;

/**
 * Integration tests for security setup.
 * 
 * @author Oliver Gierke
 */
public class WebSecurityIntegrationTests extends AbstractWebIntegrationTests {

	/**
	 * @see #19
	 */
	@Test
	public void redirectsToLoginPageForSecuredResource() throws Exception {

		mvc.perform(get("/orders")).//
				andExpect(status().isFound()).//
				andExpect(header().string("Location", endsWith("/login")));
	}

	/**
	 * @see #35
	 */
	@Test
	public void returnsModelAndViewForSecuredUriAfterAuthentication() throws Exception {

		mvc.perform(get("/orders").with(user("boss").roles("BOSS"))).//
				andExpect(status().isOk()).//
				andExpect(view().name("orders")).//
				andExpect(model().attributeExists("ordersCompleted"));
	}
}
