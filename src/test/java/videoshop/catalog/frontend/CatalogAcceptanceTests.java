/*
 * Copyright 2026-2026 the original author or authors.
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
package videoshop.catalog.frontend;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import selenidePageObjects.LoginPage;
import videoshop.AT;
import videoshop.AbstractAcceptanceTests;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


/**
 * This test class is the basic example for acceptance test or end-user tests.
 *
 * @author Oliver Geisel
 */
@Tag("Catalog")
class CatalogAcceptanceTests extends AbstractAcceptanceTests {

	@AT("AT0100")
	@Test
	void goToDvdCatalog() {
		open("");

		var dvdLink = $$(".item").find(text("DVD Katalog"));
		dvdLink.shouldBe(visible);

		dvdLink.click();

		var cards = $$(".card");
		cards.shouldHave(size(8));
	}


	@AT("AT0101")
	@Test
	void goToBulRayCatalog() {
		open("");

		var burayLink = $$(".item").find(text("BluRay Katalog"));
		burayLink.shouldBe(visible);

		burayLink.click();

		var cards = $$(".card");
		cards.shouldHave(size(9));
	}

	@AT("AT0110")
	@Test
	void goToDvdCatalogAndCheckFirstItem() {
		open("");

		var dvdLink = $$(".item").find(text("DVD Katalog"));
		dvdLink.shouldBe(visible);
		dvdLink.click();

		var cards = $$(".card");
		var firstCard = cards.first();
		var title = firstCard.$(".header").text();

		firstCard.click();

		$("header h1").shouldHave(text(title));
	}


	@AT("AT0200")
	@Test
	void placeNewOrder() {
		// this is a advanced test since it use 2 browsers to simulate 2 users.
		// The first user is the boss and calls the orders
		var bossUser = "boss";
		var bossPassword = "123";
		var customerUser = "hans";
		var customerPassword = "123";

		var bossPage = open("login", LoginPage.class);
		bossPage.login(bossUser, bossPassword);
		open("orders"); // we could also use the navigation. is better for showing.
		// save the current window handle to switch back to it later

		var tableBody = $("tbody");
		tableBody.shouldBe(empty);


		// second user in new browser
		SelenideDriver browser2 = new SelenideDriver(
				new SelenideConfig().browser("firefox") // only to show that we can use a different browser.
									.baseUrl("http://localhost:" + port + "/")
		);
		try { // try to ensure that the browser is closed even if the test fails
			browser2.open("login", LoginPage.class)
					.login(customerUser, customerPassword);
			browser2.$("#content").$("p").should(exist);

			// open one Element
			browser2.open("dvds");
			browser2.$$(".ui.card").first().click();
			// add to basket
			browser2.$("div .content").
					$("button[type='submit']").click();
			// open cart
			browser2.open("cart");
			// place order
			browser2.$("#content").$("form").$("button[type='submit']").click();


			// switch back to boss window
			// reload page
			refresh();
			tableBody.$$("tr").shouldHave(size(1));

		} finally {
			browser2.close();
		}
	}
}
