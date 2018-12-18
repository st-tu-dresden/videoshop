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
package videoshop.catalog;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration test for the {@link CatalogController} on the web layer, i.e. simulating HTTP requests.
 * 
 * @author Oliver Gierke
 */
@SpringBootTest
@AutoConfigureMockMvc
class CatalogControllerWebIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired CatalogController controller;

	/**
	 * Sample integration test using fake HTTP requests to the system and using the expectations API to define
	 * constraints.
	 */
	@Test
	void sampleMvcIntegrationTest() throws Exception {

		mvc.perform(get("/blurays")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}
}
