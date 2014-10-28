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
