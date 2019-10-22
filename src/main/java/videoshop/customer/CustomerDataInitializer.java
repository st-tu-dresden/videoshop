/*
q * Copyright 2017-2019 the original author or authors.
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

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Initializes default user accounts and customers. The following are created:
 * <ul>
 * <li>An admin user named "boss".</li>
 * <li>The customers "hans", "dextermorgan", "earlhickey", "mclovinfogell" backed by user accounts with the same
 * name.</li>
 * </ul>
 *
 * @author Oliver Gierke
 */
@Component
@Order(10)
class CustomerDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerDataInitializer.class);

	private final UserAccountManager userAccountManager;
	private final CustomerManagement customerManagement;

	/**
	 * Creates a new {@link CustomerDataInitializer} with the given {@link UserAccountManager} and
	 * {@link CustomerRepository}.
	 *
	 * @param userAccountManager must not be {@literal null}.
	 * @param customerManagement must not be {@literal null}.
	 */
	CustomerDataInitializer(UserAccountManager userAccountManager, CustomerManagement customerManagement) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(customerManagement, "CustomerRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.customerManagement = customerManagement;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// Skip creation if database was already populated
		if (userAccountManager.findByUsername("boss").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers.");

		userAccountManager.create("boss", UnencryptedPassword.of("123"), Role.of("BOSS"));

		var password = "123";

		List.of(//
				new RegistrationForm("hans", password, "wurst"),
				new RegistrationForm("dextermorgan", password, "Miami-Dade County"),
				new RegistrationForm("earlhickey", password, "Camden County - Motel"),
				new RegistrationForm("mclovinfogell", password, "Los Angeles")//
		).forEach(customerManagement::createCustomer);
	}
}
