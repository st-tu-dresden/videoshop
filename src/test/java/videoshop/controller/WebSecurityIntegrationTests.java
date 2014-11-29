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
