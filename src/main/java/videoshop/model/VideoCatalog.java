package videoshop.model;

import org.salespointframework.catalog.Catalog;

import videoshop.model.Disc.DiscType;

/**
 * @author Oliver Gierke
 */
public interface VideoCatalog extends Catalog<Disc> {

	Iterable<Disc> findByType(DiscType type);
}
