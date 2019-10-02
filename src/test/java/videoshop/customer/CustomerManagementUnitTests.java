/*
 * Copyright 2019 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;

/**
 * Unit tests for {@link CustomerManagement}.
 *
 * @author Oliver Drotbohm
 */
class CustomerManagementUnitTests {

	@Test // #93
	void createsUserAccountWhenCreatingACustomer() {

		// Given
		// … a CustomerRepository returning customers handed into save(…),
		CustomerRepository repository = mock(CustomerRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// … a UserAccountManager
		UserAccountManager userAccountManager = mock(UserAccountManager.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(new UserAccount());

		// … and the CustomerManagement using both of them,
		CustomerManagement customerManagement = new CustomerManagement(repository, userAccountManager);

		// When
		// … a registration form is submitted
		RegistrationForm form = new RegistrationForm("name", "password", "address");
		Customer customer = customerManagement.createCustomer(form);

		// Then
		// … a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(CustomerManagement.CUSTOMER_ROLE));

		// … the customer has a user account attached
		assertThat(customer.getUserAccount()).isNotNull();
	}
}
