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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import videoshop.AbstractWebIntegrationTests;

/**
 * @author Oliver Gierke
 */
public class CatalogControllerIntegrationTests extends AbstractWebIntegrationTests {

	@Autowired CatalogController controller;

	/**
	 * Sample integration test using fake HTTP requests to the system and using the expectations API to define
	 * constraints.
	 */
	@Test
	public void sampleMvcIntegrationTest() throws Exception {

		mvc.perform(get("/blurayCatalog")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}

	/**
	 * Integration test for an individual controller.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void sampleControllerIntegrationTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.blurayCatalog(model);

		assertThat(returnedView, is("discCatalog"));

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("catalog");
		assertThat(object, is(iterableWithSize(9)));
	}
}
