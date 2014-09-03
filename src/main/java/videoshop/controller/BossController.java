package videoshop.controller;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import videoshop.model.Customer;
import videoshop.model.CustomerRepository;

// (｡◕‿◕｡)
// Straight forward?

@Controller
@PreAuthorize("hasRole('ROLE_BOSS')")
class BossController {

	private final OrderManager<Order> orderManager;
	private final Inventory<InventoryItem> inventory;
	private final CustomerRepository customerRepository;

	@Autowired
	public BossController(OrderManager<Order> orderManager, Inventory<InventoryItem> inventory,
			CustomerRepository customerRepository) {

		this.orderManager = orderManager;
		this.inventory = inventory;
		this.customerRepository = customerRepository;
	}

	@RequestMapping("/customers")
	public String customers(ModelMap modelMap) {

		Iterable<Customer> customerList = customerRepository.findAll();
		modelMap.addAttribute("customerList", customerList);

		return "customers";
	}

	@RequestMapping("/orders")
	public String orders(ModelMap modelMap) {

		Iterable<Order> completedOrders = orderManager.find(OrderStatus.COMPLETED);
		modelMap.addAttribute("ordersCompleted", completedOrders);

		return "orders";
	}

	@RequestMapping("/stock")
	public String stock(ModelMap modelMap) {

		Iterable<InventoryItem> stock = inventory.findAll();
		modelMap.addAttribute("stock", stock);

		return "stock";
	}
}
