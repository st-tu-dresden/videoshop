/*
 * Copyright 2018-2019 the original author or authors.
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

import de.olivergierke.moduliths.test.ModuleTest;
import de.olivergierke.moduliths.test.ModuleTest.BootstrapMode;

import org.junit.jupiter.api.Test;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Module tests for catalog.
 *
 * @author Oliver Gierke
 */
@ModuleTest(mode = BootstrapMode.DIRECT_DEPENDENCIES)
class CatalogModuleTests {

	@Autowired VideoCatalog catalog;
	@Autowired ConfigurableApplicationContext context;

	@Test // #100
	void verifiesModuleBootstrapped() {

		AssertableApplicationContext assertable = AssertableApplicationContext.get(() -> context);

		assertThat(assertable).doesNotHaveBean(Accountancy.class);
	}
}
