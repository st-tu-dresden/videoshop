/*
 * Copyright 2013-2018 the original author or authors.
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
package videoshop.order;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.javamoney.moneta.Money;
import videoshop.catalog.Disc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for security setup.
 * 
 * @author Oliver Gierke
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityIntegrationTests {

	@Autowired MockMvc mvc;

	/**
	 * Trying to access a secured resource should result in a redirect to the login page.
	 * 
	 * @see #19
	 */
	@Test
	void redirectsToLoginPageForSecuredResource() throws Exception {

		mvc.perform(get("/orders")) //
				.andExpect(status().isFound()) //
				.andExpect(header().string("Location", endsWith("/login")));
	}

	/**
	 * Trying to access the orders as boss should result in the page being rendered.
	 * 
	 * @see #35
	 */
	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	void returnsModelAndViewForSecuredUriAfterAuthentication() throws Exception {

		mvc.perform(get("/orders")) //
				.andExpect(status().isOk()) //
				.andExpect(view().name("orders")) //
				.andExpect(model().attributeExists("ordersCompleted"));
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void clearsCartAndRedirectsToCart() throws Exception {

		var cart = new Cart();
		cart.addOrUpdateItem(new Disc("Test", "test.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1));

		mvc.perform(post("/cart/clear").sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/cart"));

		org.assertj.core.api.Assertions.assertThat(cart).isEmpty();
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void clearingEmptyCartSucceeds() throws Exception {

		var cart = new Cart();

		mvc.perform(post("/cart/clear").sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/cart"));

		org.assertj.core.api.Assertions.assertThat(cart).isEmpty();
	}

	@Test
	void unauthenticatedRequestRedirectsToLoginWithoutClearingCart() throws Exception {

		var cart = new Cart();
		cart.addOrUpdateItem(new Disc("Test", "test.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1));

		mvc.perform(post("/cart/clear").sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", endsWith("/login")));

		org.assertj.core.api.Assertions.assertThat(cart).isNotEmpty();
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void removesSingleCartItemAndRedirectsToCart() throws Exception {

		var cart = new Cart();
		var item = cart.addOrUpdateItem(new Disc("Test", "test.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1)).orElseThrow();

		mvc.perform(post("/cart/remove").param("id", item.getId()).sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/cart"));

		org.assertj.core.api.Assertions.assertThat(cart).isEmpty();
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void removingUnknownCartItemSucceedsWithoutChangingCart() throws Exception {

		var cart = new Cart();
		cart.addOrUpdateItem(new Disc("Test", "test.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1));

		mvc.perform(post("/cart/remove").param("id", "unknown").sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/cart"));

		org.assertj.core.api.Assertions.assertThat(cart).hasSize(1);
	}

	@Test
	void unauthenticatedRequestCannotRemoveCartItem() throws Exception {

		var cart = new Cart();
		var item = cart.addOrUpdateItem(new Disc("Test", "test.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1)).orElseThrow();

		mvc.perform(post("/cart/remove").param("id", item.getId()).sessionAttr("cart", cart))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", endsWith("/login")));

		org.assertj.core.api.Assertions.assertThat(cart).hasSize(1);
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void cartOperationsOnlyChangeTheCurrentSessionCart() throws Exception {

		var firstCart = new Cart();
		var firstItem = firstCart.addOrUpdateItem(
				new Disc("First", "first.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD), Quantity.of(1))
				.orElseThrow();
		var secondCart = new Cart();
		secondCart.addOrUpdateItem(new Disc("Second", "second.jpg", Money.of(1, "EUR"), "Test", Disc.DiscType.DVD),
				Quantity.of(1));

		mvc.perform(post("/cart/remove").param("id", firstItem.getId()).sessionAttr("cart", firstCart))
				.andExpect(status().isFound());

		org.assertj.core.api.Assertions.assertThat(firstCart).isEmpty();
		org.assertj.core.api.Assertions.assertThat(secondCart).hasSize(1);
	}
}
