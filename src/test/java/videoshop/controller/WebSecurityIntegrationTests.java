package videoshop.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.util.NestedServletException;

import videoshop.AbstractWebIntegrationTests;

/**
 * Integratio tests for security setup.
 * 
 * @author Oliver Gierke
 */
public class WebSecurityIntegrationTests extends AbstractWebIntegrationTests {

	public @Rule ExpectedException exception = ExpectedException.none();

	/**
	 * @see #19
	 */
	@Test
	public void rejectsAccessToSecuredResource() throws Exception {

		exception.expect(NestedServletException.class);
		exception.expectCause(is(instanceOf(AuthenticationException.class)));

		mvc.perform(get("/orders"));
	}
}
