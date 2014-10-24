package videoshop.controller;

import javax.servlet.http.HttpSession;

import org.salespointframework.order.Basket;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Disc;

// (｡◕‿◕｡)
// Alle Aktionen/Methoden im Controller sollen nur durch einen eingeloggten Kunden ausgeführt werden können,
// von daher "schützen" wir den ganzen Controller mit @PreAuthorize. 
// Dadurch wird auch verhindet, dass Angreifer durch URL Guessing auf die Adminseite oder ähnliches zugreifen können.
// @PreAuthorize kann auch nur auf einzelnen Controllermethoden angewendet werden.
// Via hasAnyRole() kann man auch kommasepariert mehrere Rollen angeben.
// Lektüre http://docs.spring.io/spring-security/site/docs/current/reference/el-access.html
@Controller
@PreAuthorize("hasRole('ROLE_CUSTOMER')")
class BasketController {

	private final OrderManager<Order> orderManager;

	@Autowired
	public BasketController(OrderManager<Order> orderManager) {
		this.orderManager = orderManager;
	}

	// (｡◕‿◕｡)
	// Wie zu sehen ist, funktioniert @RequestParam auch mit eigenen Klassen, in dem Fall Disc
	// Es funktioniert mit allen Salespoint Entity Klassen beziehungsweise Ableitungen davon, Disc erbt von Product
	@RequestMapping(value = "/addDisc", method = RequestMethod.POST)
	public String addDisc(@RequestParam("pid") Disc disc, @RequestParam("number") int number, HttpSession session,
			ModelMap modelMap) {

		// (｡◕‿◕｡)
		// Das Inputfeld im View ist eigentlich begrenz, allerdings sollte man immer Clientseitig validieren
		if (number <= 0 || number > 5) {
			number = 1;
		}

		Basket basket = this.getBasket(session);

		// (｡◕‿◕｡)
		// Eine OrderLine besteht aus einem Produkt und einer Quantity, diese kann auch direkt in eine Order eingefügt
		// werden

		Quantity quantity = Units.of(number);
		OrderLine orderLine = new OrderLine(disc, quantity);
		basket.add(orderLine);

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

	@RequestMapping("/shoppingBasket")
	public String basket() {
		return "basket";
	}

	// (｡◕‿◕｡)
	// Über @LoggedIn können wir uns den gerade eingeloggten UserAccount geben lassen
	//string debug this shit !!!
	@RequestMapping(value="/buy", method=RequestMethod.POST)
	public String buy(HttpSession session, @LoggedIn UserAccount userAccount) {

		Basket basket = this.getBasket(session);

		// (｡◕‿◕｡)
		// Mit commit wird der Warenkorb in die Order überführt, diese wird dann bezahlt und abgeschlossen.
		// Orders können nur abgeschlossen werden, wenn diese vorher bezahlt wurden.
		Order order = new Order(userAccount, Cash.CASH);
		basket.commit(order);

		orderManager.payOrder(order);
		orderManager.completeOrder(order);
		orderManager.add(order);

		basket.clear();

		return "index";
	}

	// (｡◕‿◕｡)
	// Warenkorb (Basket) aus der Session holen, existiert keiner so legen wir einen an und packen den in die Session
	// Außerdem wird der Basket bei jedem Aufruf des Controllers durch das @ModelAttribute in die ModelMap abgelegt
	@ModelAttribute("shoppingBasket")
	private Basket getBasket(HttpSession session) {
		Basket basket = (Basket) session.getAttribute("shoppingBasket");
		if (basket == null) {
			basket = new Basket();
			session.setAttribute("shoppingBasket", basket);
		}
		return basket;

	}
}
