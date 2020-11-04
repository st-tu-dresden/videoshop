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
package videoshop.customer;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class CustomerController {

	private final CustomerManagement customerManagement;

	CustomerController(CustomerManagement customerManagement) {

		Assert.notNull(customerManagement, "CustomerManagement must not be null!");

		this.customerManagement = customerManagement;
	}

	// (｡◕‿◕｡)
	// Über @Valid können wir die Eingaben automagisch prüfen lassen, ob es Fehler gab steht im BindingResult,
	// dies muss direkt nach dem @Valid Parameter folgen.
	// Siehe außerdem videoshop.model.validation.RegistrationForm
	// Lektüre: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html
	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}

		// (｡◕‿◕｡)
		// Falles alles in Ordnung ist legen wir einen Customer an
		customerManagement.createCustomer(form);

		return "redirect:/";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/customers")
	@PreAuthorize("hasRole('BOSS')")
	String customers(Model model) {

		model.addAttribute("customerList", customerManagement.findAll());

		return "customers";
	}
}
