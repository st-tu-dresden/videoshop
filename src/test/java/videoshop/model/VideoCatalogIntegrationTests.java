package videoshop.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import videoshop.AbstractIntegrationTests;
import videoshop.model.Disc.DiscType;

/**
 * Integration tests for {@link VideoCatalog}.
 * 
 * @author Oliver Gierke
 */
public class VideoCatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired VideoCatalog catalog;

	@Test
	public void findsAllBluRays() {

		Iterable<Disc> result = catalog.findByType(DiscType.BLURAY);
		assertThat(result, is(iterableWithSize(9)));
	}
}
