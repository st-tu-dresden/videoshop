/*
 * Copyright 2013-2018 the original author or authors.
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
package videoshop.orders;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for security setup.
 * 
 * @author Oliver Gierke
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityIntegrationTests {

	@Autowired MockMvc mvc;

	/**
	 * Trying to access a secured resource should result in a redirect to the login page.
	 * 
	 * @see #19
	 */
	@Test
	void redirectsToLoginPageForSecuredResource() throws Exception {

		mvc.perform(get("/orders")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string("Location", endsWith("/login")));
	}

	/**
	 * Trying to access the orders as boss should result in the page being rendered.
	 * 
	 * @see #35
	 */
	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void returnsModelAndViewForSecuredUriAfterAuthentication() throws Exception {

		mvc.perform(get("/orders")) //
				.andExpect(status().isOk()) //
				.andExpect(view().name("orders")) //
				.andExpect(model().attributeExists("ordersCompleted"));
	}
}
