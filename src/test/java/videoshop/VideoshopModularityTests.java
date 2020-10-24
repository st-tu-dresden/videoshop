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

	Modules modules = Modules.of(VideoShop.class);
	Predicate<Module> isSalespointModule = it -> it.getBasePackage().getName().startsWith("org.salespoint");

	@Test
	void assertModularity() {
		modules.verify();
	}

	@Test // #120
	void writeComponentDiagrams() throws IOException {

		Options options = Options.defaults() //
				.withColorSelector(this::getColorForModule) //
				.withDefaultDisplayName(this::getModuleDisplayName) //
				.withTargetOnly(isSalespointModule);

		Documenter documenter = new Documenter(modules);
		documenter.writeModulesAsPlantUml(options);

		modules.stream().filter(isSalespointModule.negate()) //
				.forEach(it -> documenter.writeModuleAsPlantUml(it, options));
	}

	private Optional<String> getColorForModule(Module module) {

		String packageName = module.getBasePackage().getName();

		if (packageName.startsWith("org.salespoint")) {
			return Optional.of("#ddddff");
		} else if (packageName.startsWith("videoshop")) {
			return Optional.of("#ddffdd");
		} else {
			return Optional.empty();
		}
	}

	private String getModuleDisplayName(Module module) {

		return module.getBasePackage().getName().startsWith("videoshop") //
				? "Videoshop :: ".concat(StringUtils.capitalize(module.getDisplayName())) //
				: module.getDisplayName();
	}
}
