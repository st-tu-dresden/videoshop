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

import javax.validation.Valid;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;
import videoshop.model.validation.RegistrationForm;

@Controller
class ShopController {

	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;

	@Autowired
	public ShopController(UserAccountManager userAccountManager, CustomerRepository customerRepository) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
	}

	@RequestMapping({ "/", "/index" })
	public String index() {
		return "index";
	}

	// (｡◕‿◕｡)
	// Über @Valid können wir die Eingaben automagisch prüfen lassen, ob es Fehler gab steht im BindingResult,
	// dies muss direkt nach dem @Valid Parameter folgen.
	// Siehe außerdem videoshop.model.validation.RegistrationForm
	// Lektüre: http://docs.spring.io/spring/docs/3.2.4.RELEASE/spring-framework-reference/html/validation.html
	@RequestMapping("/registerNew")
	public String registerNew(@ModelAttribute("registrationForm") @Valid RegistrationForm registrationForm,
			BindingResult result) {

		if (result.hasErrors()) {
			return "register";
		}

		// (｡◕‿◕｡)
		// Falles alles in Ordnung ist legen wir einen UserAccount und einen passenden Customer an und speichern beides.
		UserAccount userAccount = userAccountManager.create(registrationForm.getName(), registrationForm.getPassword(),
				new Role("ROLE_CUSTOMER"));
		userAccountManager.save(userAccount);

		Customer customer = new Customer(userAccount, registrationForm.getAddress());
		customerRepository.save(customer);

		return "redirect:/";
	}

	@RequestMapping("/register")
	public String register(ModelMap modelMap) {
		modelMap.addAttribute("registrationForm", new RegistrationForm());
		return "register";
	}
}
