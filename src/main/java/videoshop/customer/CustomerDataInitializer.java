/*
q * Copyright 2017 the original author or authors.
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

import java.util.Arrays;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Initalizes {@link Customer}s.
 * 
 * @author Oliver Gierke
 */
@Component
@Order(10)
class CustomerDataInitializer implements DataInitializer {

	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;

	/**
	 * @param userAccountManager
	 * @param customerRepository
	 */
	CustomerDataInitializer(UserAccountManager userAccountManager, CustomerRepository customerRepository) {

		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");

		this.userAccountManager = userAccountManager;
		this.customerRepository = customerRepository;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// (｡◕‿◕｡)
		// UserAccounts bestehen aus einem Identifier und eine Password, diese werden auch für ein Login gebraucht
		// Zusätzlich kann ein UserAccount noch Rollen bekommen, diese können in den Controllern und im View dazu genutzt
		// werden
		// um bestimmte Bereiche nicht zugänglich zu machen, das "ROLE_"-Prefix ist eine Konvention welche für Spring
		// Security nötig ist.

		// Skip creation if database was already populated
		if (userAccountManager.findByUsername("boss").isPresent()) {
			return;
		}

		UserAccount bossAccount = userAccountManager.create("boss", "123", Role.of("ROLE_BOSS"));
		userAccountManager.save(bossAccount);

		Role customerRole = Role.of("ROLE_CUSTOMER");

		UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
		UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
		UserAccount ua3 = userAccountManager.create("earlhickey", "123", customerRole);
		UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", customerRole);

		Customer c1 = new Customer(ua1, "wurst");
		Customer c2 = new Customer(ua2, "Miami-Dade County");
		Customer c3 = new Customer(ua3, "Camden County - Motel");
		Customer c4 = new Customer(ua4, "Los Angeles");

		customerRepository.saveAll(Arrays.asList(c1, c2, c3, c4));
	}
}
