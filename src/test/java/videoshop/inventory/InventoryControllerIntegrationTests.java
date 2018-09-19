/*
 * Copyright 2017 the original author or authors.
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
package videoshop.inventory;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import videoshop.AbstractWebIntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

/**
 * Integration tests for {@link InventoryController}.
 * 
 * @author Oliver Gierke
 * @soundtrack Dave Matthews Band - The Stone (DMB Live 25)
 */
class InventoryControllerIntegrationTests extends AbstractWebIntegrationTests {

	@Test // #75
	void preventsPublicAccessForStockOverview() throws Exception {

		mvc.perform(get("/stock")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}

	@Test // #75
	void stockIsAccessibleForAdmin() throws Exception {

		mvc.perform(get("/stock").with(user("boss").roles("BOSS"))) //
				.andExpect(status().isOk())//
				.andExpect(model().attributeExists("stock"));
	}
}
