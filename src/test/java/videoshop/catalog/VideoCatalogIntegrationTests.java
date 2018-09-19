/*
 * Copyright 2013-2017 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;

import videoshop.AbstractIntegrationTests;
import videoshop.catalog.Disc;
import videoshop.catalog.Disc.DiscType;
import videoshop.catalog.VideoCatalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link VideoCatalog}.
 * 
 * @author Oliver Gierke
 * @author Andreas Zaschka
 */
class VideoCatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired VideoCatalog catalog;

	@Test
	void findsAllBluRays() {

		Iterable<Disc> result = catalog.findByType(DiscType.BLURAY);
		assertThat(result).hasSize(9);
	}

	/**
	 * @see #50
	 */
	@Test
	void discsDontHaveAnyCategoriesAssigned() {

		for (Disc disc : catalog.findByType(DiscType.BLURAY)) {
			assertThat(disc.getCategories()).isEmpty();
		}
	}
}
