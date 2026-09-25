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

package videoshop.customer.frontend;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import selenidePageObjects.*;
import videoshop.AT;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A test class for acceptance tests of the login functionality of the application. The tests are the related acceptance tests
 * you can find in the <strong>Pflichtenheft.adoc</strong>
 *
 * @author Oliver Geisel
 * @see LoginPage
 */
@Tag("UI")
@DisplayNameGeneration(videoshop.AT_DisplayNameGenerator.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Reset the context after each test method to ensure independent tests
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LoginAcceptanceTests {


	@LocalServerPort
	int     port; // Inject the random port number

	@Value("${test.withDelay:true}")
	boolean withDelay;
	@Value("${test.withCursor:true}")
	boolean withCursor;

	private LoginPage page;

	@AfterAll
	static void tearDownAll() {
		// Close the browser after all tests
		closeWebDriver();
	}

	@BeforeEach
	void setup() {
		if (withDelay) {
			WebDriverRunner.addListener(new SlowMotionListener());
		}
		page = open("http://localhost:" + port + "/login", LoginPage.class);
		if (withCursor) {
			enableCursor();
		}
	}

	private void enableCursor() {
		executeJavaScript("""
				const cursor = document.createElement('div');
				cursor.id = 'selenide-cursor';
				cursor.style.cssText = `
				position: fixed;
				width: 12px;
				height: 12px;
				border: 2px solid red;
				border-radius: 50%;
				pointer-events: none;
				z-index: 99;
				transform: translate(-50%, -50%);
				`;
				document.body.appendChild(cursor);
				
				document.addEventListener('mousemove', e => {
					cursor.style.left = e.clientX + 'px';
					cursor.style.top = e.clientY + 'px';
				});
				""");
	}

	@AfterEach
	void tearDown() {
		// Delete all cookies after each test to ensure a clean state for the next test
		cookies().clear();
	}

	@Test
	@AT("AT0010")
	void loginWithNormalUser() {
		var userName = "hans";
		var password = "123";

		var result = page.login(userName, password);

		assertEquals("/", result.getPagePath(),
				"Login with normal user should redirect to home page");
	}

	@Test
	@AT("AT0011")
	void logoutUser() {
		var userName = "hans";
		var password = "123";
		var result = page.login(userName, password);

		assertEquals("/", result.getPagePath(),
				"Login with normal user should redirect to home page");

		try {
			result.logout();
		} catch (Exception e) {
			fail("There was an exception during logout: " + e.getMessage());
		}
	}

	@Test
	@AT("AT0012")
	void loginWithWrongPassword() {
		var userName = "hans";
		var password = "wrongpassword";

		var result = page.login(userName, password);

		assertEquals("/login?error", result.getPagePath(),
				"Login with wrong password should redirect to login page with error");
	}

	@Test
	@AT("AT0020")
	void registerNewUser() {
		var userName = "TestCustomer";
		var password = "1234";
		var address = "Nöthnitzer Straße 46";

		// assure user does not exist yet
		Request result = page.login(userName, password);
		assertEquals("/login?error", result.getPagePath(), "User should not exist yet");

		var registerPage = open("http://localhost:" + port + "/register", RegisterPage.class);
		result = registerPage.register(userName, password, address);

		page = open("http://localhost:" + port + "/login", LoginPage.class); // Go back to login page

		page.login(userName, password);

		assertEquals("/", result.getPagePath(), "Login with new user should redirect to home page");
	}


	@AT("AT0021")
	@Test
	void registerExistingUser() {
		var userName = "hans";
		var password = "123";
		var address = "Nöthnitzer Straße 46";

		var registerPage = open("http://localhost:" + port + "/register", RegisterPage.class);
		Request result = registerPage.register(userName, password, address);

		assertEquals("/register", result.getPagePath(),
				"Registering an existing user should redirect to register page with whitelabel error message");

		$("h1").shouldHave(text("Whitelabel Error Page"));

	}
}
