/*
 * Copyright 2013-2019 the original author or authors.
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
package videoshop.inventory;

import videoshop.catalog.VideoCatalog;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
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
@Order(20)
class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final VideoCatalog videoCatalog;

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, VideoCatalog videoCatalog) {

		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(videoCatalog, "VideoCatalog must not be null!");

		this.inventory = inventory;
		this.videoCatalog = videoCatalog;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// (｡◕‿◕｡)
		// Über alle Discs iterieren und jeweils ein InventoryItem mit der Quantity 10 setzen
		// Das heißt: Von jeder Disc sind 10 Stück im Inventar.

		videoCatalog.findAll().forEach(disc -> {

			// Try to find an InventoryItem for the project and create a default one with 10 items if none available
			inventory.findByProduct(disc) //
					.orElseGet(() -> inventory.save(new UniqueInventoryItem(disc, Quantity.of(10))));
		});
	}
}
