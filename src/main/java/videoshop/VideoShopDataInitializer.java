package videoshop;

import static org.joda.money.CurrencyUnit.*;

import java.util.Arrays;

import org.joda.money.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;
import videoshop.model.Disc;
import videoshop.model.Disc.DiscType;
import videoshop.model.VideoCatalog;

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

		videoCatalog.save(new Disc("Last Action Hero", "lac", Money.of(EUR, 9.99), "Äktschn/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Back to the Future", "bttf", Money.of(EUR, 9.99), "Sci-Fi", DiscType.DVD));
		videoCatalog.save(new Disc("Fido", "fido", Money.of(EUR, 9.99), "Comedy/Drama/Horror", DiscType.DVD));
		videoCatalog.save(new Disc("Super Fuzz", "sf", Money.of(EUR, 9.99), "Action/Sci-Fi/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Armour of God II: Operation Condor", "aog2oc", Money.of(EUR, 14.99),
				"Action/Adventure/Comedy", DiscType.DVD));
		videoCatalog.save(new Disc("Persepolis", "pers", Money.of(EUR, 14.99), "Animation/Biography/Drama", DiscType.DVD));
		videoCatalog
				.save(new Disc("Hot Shots! Part Deux", "hspd", Money.of(EUR, 9999.0), "Action/Comedy/War", DiscType.DVD));
		videoCatalog.save(new Disc("Avatar: The Last Airbender", "tla", Money.of(EUR, 19.99), "Animation/Action/Adventure",
				DiscType.DVD));

		videoCatalog.save(new Disc("Secretary", "secretary", Money.of(EUR, 6.99), "Political Drama", DiscType.BLURAY));
		videoCatalog.save(new Disc("The Godfather", "tg", Money.of(EUR, 19.99), "Crime/Drama", DiscType.BLURAY));
		videoCatalog.save(new Disc("No Retreat, No Surrender", "nrns", Money.of(EUR, 29.99), "Martial Arts",
				DiscType.BLURAY));
		videoCatalog.save(new Disc("The Princess Bride", "tpb", Money.of(EUR, 39.99), "Adventure/Comedy/Family",
				DiscType.BLURAY));
		videoCatalog.save(new Disc("Top Secret!", "ts", Money.of(EUR, 39.99), "Comedy", DiscType.BLURAY));
		videoCatalog.save(new Disc("The Iron Giant", "tig", Money.of(EUR, 34.99), "Animation/Action/Adventure",
				DiscType.BLURAY));
		videoCatalog.save(new Disc("Battle Royale", "br", Money.of(EUR, 19.99), "Action/Drama/Thriller", DiscType.BLURAY));
		videoCatalog.save(new Disc("Oldboy", "old", Money.of(EUR, 24.99), "Action/Drama/Thriller", DiscType.BLURAY));
		videoCatalog.save(new Disc("Bill & Ted's Excellent Adventure", "bt", Money.of(EUR, 29.99),
				"Adventure/Comedy/Family", DiscType.BLURAY));

		// (｡◕‿◕｡)
		// Über alle eben hinzugefügten Discs iterieren und jeweils ein InventoryItem mit der Quantity 10 setzen
		// Das heißt: Von jeder Disc sind 10 Stück im Inventar.

		for (Disc disc : videoCatalog.findAll()) {
			InventoryItem inventoryItem = new InventoryItem(disc, Units.TEN);
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
		if (userAccountManager.get(new UserAccountIdentifier("boss")).isPresent()) {
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
