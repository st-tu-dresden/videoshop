package videoshop.controller;

import java.util.Optional;


import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import videoshop.model.Disc;

/**
 * A Spring MVC controller to manage the {@link Cart}. {@link Cart} instances are held in the session as they're
 * specific to a certain user. That's also why the entire controller is secured by a {@code hasRole(…)} clause.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Controller
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
@SessionAttributes("cart")
class CartController {

	private final OrderManager<Order> orderManager;

	/**
	 * Creates a new {@link CartController} with the given {@link OrderManager}.
	 * 
	 * @param orderManager must not be {@literal null}.
	 */
	@Autowired
	public CartController(OrderManager<Order> orderManager) {

		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
	}

	/**
	 * Adds a {@link Disc} to the {@link Cart}. Note how the type of the parameter taking the request parameter
	 * {@code pid} is {@link Disc}. For all domain types extening {@link AbstractEntity} (directly or indirectly) a tiny
	 * Salespoint extension will directly load the object instance from the database. If the identifier provided is
	 * invalid (invalid format or no {@link Product} with the id found), {@literal null} will be handed into the method.
	 */
	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	public String addDisc(@RequestParam("pid") Disc disc, @RequestParam("number") int number, @ModelAttribute Cart cart) {

		// (｡◕‿◕｡)
		// Das Inputfeld im View ist eigentlich begrenz, allerdings sollte man immer Clientseitig validieren
		if (number <= 0 || number > 5) {
			number = 1;
		}
		Quantity quantity = Units.of(number);

		cart.add(disc, quantity);

		// (｡◕‿◕｡)
		// Je nachdem ob disc eine Dvd oder eine Bluray ist, leiten wir auf die richtige Seite weiter

		switch (disc.getType()) {
			case DVD:
				return "redirect:dvdCatalog";
			case BLURAY:
			default:
				return "redirect:blurayCatalog";
		}
	}

	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String basket() {
		return "cart";
	}

	/**
	 * Checks out the current state of the {@link Cart}. Using a method parameter of type {@code Optional<UserAccount>}
	 * annotated with {@link LoggedIn} you can access the {@link UserAccount} of the currently logged in user.
	 * 
	 * @param userAccount must not be {@literal null}.
	 * @return
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {

		return userAccount.map(account -> {

			// (｡◕‿◕｡)
			// Mit commit wird der Warenkorb in die Order überführt, diese wird dann bezahlt und abgeschlossen.
			// Orders können nur abgeschlossen werden, wenn diese vorher bezahlt wurden.
				Order order = new Order(account, Cash.CASH);

				cart.toOrder(order);

				orderManager.payOrder(order);
				orderManager.completeOrder(order);
				orderManager.add(order);

				cart.clear();

				return "redirect:/";
			}).orElse("redirect:/cart");
	}

	/**
	 * Puts a {@link Cart} object under the key {@code cart} into the model for every request.
	 * You will get the same instance, as long as the session is not invalidated. 
	 * 
	 * @return
	 */
	@ModelAttribute("cart")
	private Cart createCart() {
		return new Cart();
	}
}
