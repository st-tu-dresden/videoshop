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
package videoshop;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.moduliths.docs.Documenter;
import org.moduliths.docs.Documenter.Options;
import org.moduliths.model.Module;
import org.moduliths.model.Modules;
import org.springframework.util.StringUtils;

/**
 * Unit test verifying project modularity.
 *
 * @author Oliver Gierke
 */
@TestInstance(Lifecycle.PER_CLASS)
class VideoshopModularityTests {

	private static final Class<?> APPLICATION_CLASS = VideoShop.class;
	private static final String BASE_PACKAGE = APPLICATION_CLASS.getSimpleName().toLowerCase(Locale.ENGLISH);

	Modules modules = Modules.of(APPLICATION_CLASS);
	Predicate<Module> isSalespointModule = it -> it.getBasePackage().getName().startsWith("org.salespoint");

	@Test
	void assertModularity() {
		modules.verify();
	}

	@Test // #120
	void writeComponentDiagrams() throws IOException {

		var options = Options.defaults() //
				.withColorSelector(this::getColorForModule) //
				.withDefaultDisplayName(this::getModuleDisplayName) //
				.withTargetOnly(isSalespointModule);

		var documenter = new Documenter(modules);
		documenter.writeModulesAsPlantUml(options);
		documenter.writeModuleCanvases();
		documenter.writeIndividualModulesAsPlantUml(options);
	}

	private Optional<String> getColorForModule(Module module) {

		var packageName = module.getBasePackage().getName();

		if (packageName.startsWith("org.salespoint")) {
			return Optional.of("#ddddff");
		} else if (packageName.startsWith(BASE_PACKAGE)) {
			return Optional.of("#ddffdd");
		} else {
			return Optional.empty();
		}
	}

	private String getModuleDisplayName(Module module) {

		return module.getBasePackage().getName().startsWith(BASE_PACKAGE) //
				? String.format("%s :: %s", APPLICATION_CLASS.getSimpleName(), StringUtils.capitalize(module.getDisplayName())) //
				: module.getDisplayName();
	}
}
