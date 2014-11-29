/*
 * Copyright 2013-2014 the original author or authors.
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
package videoshop.controller;

import java.util.Optional;

import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.Units;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import videoshop.model.Comment;
import videoshop.model.Disc;
import videoshop.model.Disc.DiscType;
import videoshop.model.VideoCatalog;

@Controller
class CatalogController {

	private final VideoCatalog videoCatalog;
	private final Inventory<InventoryItem> inventory;
	private final BusinessTime businessTime;

	// (｡◕‿◕｡)
	// Da wir nur ein Catalog.html-Template nutzen, aber dennoch den richtigen Titel auf der Seite haben wollen,
	// nutzen wir den MessageSourceAccessor um an die messsages.properties Werte zu kommen
	private final MessageSourceAccessor messageSourceAccessor;

	@Autowired
	public CatalogController(VideoCatalog videoCatalog, Inventory<InventoryItem> inventory, BusinessTime businessTime,
			MessageSource messageSource) {

		this.videoCatalog = videoCatalog;
		this.inventory = inventory;
		this.businessTime = businessTime;
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	@RequestMapping("/dvdCatalog")
	public String dvdCatalog(ModelMap modelMap) {

		modelMap.addAttribute("catalog", videoCatalog.findByType(DiscType.DVD));
		modelMap.addAttribute("title", messageSourceAccessor.getMessage("catalog.dvd.title"));

		return "discCatalog";
	}

	@RequestMapping("/blurayCatalog")
	public String blurayCatalog(Model model) {

		model.addAttribute("catalog", videoCatalog.findByType(DiscType.BLURAY));
		model.addAttribute("title", messageSourceAccessor.getMessage("catalog.bluray.title"));

		return "discCatalog";
	}

	// (｡◕‿◕｡)
	// Befindet sich die angesurfte Url in der Form /foo/5 statt /foo?bar=5 so muss man @PathVariable benutzen
	// Lektüre: http://spring.io/blog/2009/03/08/rest-in-spring-3-mvc/
	@RequestMapping("/detail/{pid}")
	public String detail(@PathVariable("pid") Disc disc, Model model) {

		Optional<InventoryItem> item = inventory.findByProductIdentifier(disc.getIdentifier());
		Quantity quantity = item.map(InventoryItem::getQuantity).orElse(Units.ZERO);

		model.addAttribute("disc", disc);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(Units.ZERO));

		return "detail";
	}

	// (｡◕‿◕｡)
	// Der Katalog bzw die Datenbank "weiß" nicht, dass die Disc mit einem Kommentar versehen wurde,
	// deswegen wird die update-Methode aufgerufen
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String comment(@RequestParam("pid") Disc disc, @RequestParam("comment") String comment,
			@RequestParam("rating") int rating) {

		disc.addComment(new Comment(comment, rating, businessTime.getTime()));
		videoCatalog.save(disc);

		return "redirect:detail/" + disc.getIdentifier();
	}
}
