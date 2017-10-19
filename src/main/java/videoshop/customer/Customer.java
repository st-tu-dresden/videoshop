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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

// (｡◕‿◕｡)
// Salespoint bietet nur eine UserAccount Verwaltung, für weitere Attribute sollte eine extra Klasse geschriebenw werden 
// Unser Kunde hat noch eine Adresse, das bietet der UserAccount nicht
// Um den Customer in die Datenbank zu bekommen, schreiben wir ein CustomerRepository 

@Entity
public class Customer {

	private @Id @GeneratedValue long id;

	private String address;

	// (｡◕‿◕｡)
	// Jedem Customer ist genau ein UserAccount zugeordnet, um später über den UserAccount an den Customer zu kommen,
	// speichern wir den hier
	@OneToOne //
	private UserAccount userAccount;

	@SuppressWarnings("unused")
	private Customer() {}

	public Customer(UserAccount userAccount, String address) {
		this.userAccount = userAccount;
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
}
