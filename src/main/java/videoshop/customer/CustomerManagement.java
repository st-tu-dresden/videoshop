/*
 * Copyright 2017 the original author or authors.
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

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Oliver Gierke
 */
@Service
@Transactional
public class CustomerManagement {

	private final CustomerRepository customers;
	private final UserAccountManager userAccounts;

	/**
	 * @param customers must not be {@literal null}.
	 * @param userAccounts must not be {@literal null}.
	 */
	CustomerManagement(CustomerRepository customers, UserAccountManager userAccounts) {

		Assert.notNull(customers, "CustomerRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManager must not be null!");

		this.customers = customers;
		this.userAccounts = userAccounts;
	}

	/**
	 * Creates a new {@link Customer} using the information given in the {@link RegistrationForm}.
	 * 
	 * @param form must not be {@literal null}.
	 * @return
	 */
	public Customer createCustomer(RegistrationForm form) {

		Assert.notNull(form, "Registration form must not be null!");

		UserAccount userAccount = userAccounts.create(form.getName(), form.getPassword(), Role.of("ROLE_CUSTOMER"));

		return customers.save(new Customer(userAccount, form.getAddress()));
	}

	/**
	 * Returns all {@link Customer}s currently available in the system.
	 * 
	 * @return
	 */
	public Streamable<Customer> findAll() {
		return Streamable.of(customers.findAll());
	}
}
