/*
 * Copyright 2013-2015 the original author or authors.
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

import static org.salespointframework.core.Currencies.*;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;
import videoshop.model.Disc;
import videoshop.model.Disc.DiscType;
import videoshop.model.VideoCatalog;

import java.util.Arrays;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the application on application startup.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 * @see DataInitializer
 */
@Component
public class VideoShopDataInitializer implements DataInitializer {

	private final Inventory<InventoryItem> inventory;
	private final VideoCatalog videoCatalog;
	private final UserAccountManager userAccountManager;
	private final CustomerRepository customerRepository;

	@Autowired
	public VideoShopDataInitializer(CustomerRepository customerRepository, Inventory<InventoryItem> inventory,
			UserAccountManager userAccountManager, VideoCatalog videoCatalog) {

		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(videoCatalog, "VideoCatalog must not be null!");

		this.customerRepository = customerRepository;
		this.inventory = inventory;
		this.userAccountManager = userAccountManager;
		this.videoCatalog = videoCatalog;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		initializeUsers(userAccountManager, customerRepository);
		initializeCatalog(videoCatalog, inventory);
	}

	private void initializeCatalog(VideoCatalog videoCatalog, Inventory<InventoryItem> inventory) {

		if (videoCatalog.findAll().iterator().hasNext()) {
			return;
		}

		videoCatalog.save(new Disc("Last Action Hero", "lac", Money.of(9.99, EURO), "Äktschn/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Back to the Future", "bttf", Money.of(9.99, EURO), "Sci-Fi", DiscType.DVD));
		videoCatalog.save(new Disc("Fido", "fido", Money.of(9.99, EURO), "Comedy/Drama/Horror", DiscType.DVD));
		videoCatalog.save(new Disc("Super Fuzz", "sf", Money.of(9.99, EURO), "Action/Sci-Fi/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Armour of God II: Operation Condor", "aog2oc", Money.of(14.99, EURO),
				"Action/Adventure/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Persepolis", "pers", Money.of(14.99, EURO), "Animation/Biography/Drama", DiscType.DVD));
		videoCatalog
				.save(new Disc("Hot Shots! Part Deux", "hspd", Money.of(9999.0, EURO), "Action/Comedy/War", DiscType.DVD));
		videoCatalog.save(new Disc("Avatar: The Last Airbender", "tla", Money.of(19.99, EURO), "Animation/Action/Adventure",
				DiscType.DVD));

		videoCatalog.save(new Disc("Secretary", "secretary", Money.of(6.99, EURO), "Political Drama", DiscType.BLURAY));
		videoCatalog.save(new Disc("The Godfather", "tg", Money.of(19.99, EURO), "Crime/Drama", DiscType.BLURAY));
		videoCatalog
				.save(new Disc("No Retreat, No Surrender", "nrns", Money.of(29.99, EURO), "Martial Arts", DiscType.BLURAY));
		videoCatalog
				.save(new Disc("The Princess Bride", "tpb", Money.of(39.99, EURO), "Adventure/Comedy/Family", DiscType.BLURAY));
		videoCatalog.save(new Disc("Top Secret!", "ts", Money.of(39.99, EURO), "Comedy", DiscType.BLURAY));
		videoCatalog
				.save(new Disc("The Iron Giant", "tig", Money.of(34.99, EURO), "Animation/Action/Adventure", DiscType.BLURAY));
		videoCatalog.save(new Disc("Battle Royale", "br", Money.of(19.99, EURO), "Action/Drama/Thriller", DiscType.BLURAY));
		videoCatalog.save(new Disc("Oldboy", "old", Money.of(24.99, EURO), "Action/Drama/Thriller", DiscType.BLURAY));
		videoCatalog.save(new Disc("Bill & Ted's Excellent Adventure", "bt", Money.of(29.99, EURO),
				"Adventure/Comedy/Family", DiscType.BLURAY));

		// (｡◕‿◕｡)
		// Über alle eben hinzugefügten Discs iterieren und jeweils ein InventoryItem mit der Quantity 10 setzen
		// Das heißt: Von jeder Disc sind 10 Stück im Inventar.

		for (Disc disc : videoCatalog.findAll()) {
			InventoryItem inventoryItem = new InventoryItem(disc, Quantity.of(10));
			inventory.save(inventoryItem);
		}
	}

	private void initializeUsers(UserAccountManager userAccountManager, CustomerRepository customerRepository) {

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

		UserAccount bossAccount = userAccountManager.create("boss", "123", new Role("ROLE_BOSS"));
		userAccountManager.save(bossAccount);

		final Role customerRole = new Role("ROLE_CUSTOMER");

		UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
		userAccountManager.save(ua1);
		UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
		userAccountManager.save(ua2);
		UserAccount ua3 = userAccountManager.create("earlhickey", "123", customerRole);
		userAccountManager.save(ua3);
		UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", customerRole);
		userAccountManager.save(ua4);

		Customer c1 = new Customer(ua1, "wurst");
		Customer c2 = new Customer(ua2, "Miami-Dade County");
		Customer c3 = new Customer(ua3, "Camden County - Motel");
		Customer c4 = new Customer(ua4, "Los Angeles");

		// (｡◕‿◕｡)
		// Zu faul um save 4x am Stück aufzurufen :)
		customerRepository.save(Arrays.asList(c1, c2, c3, c4));
	}
}
