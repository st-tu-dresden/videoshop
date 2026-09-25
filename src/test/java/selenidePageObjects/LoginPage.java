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
package selenidePageObjects;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;


/**
 * The SiteLoginPage class represents the Login Page of the VideoShop Application.
 *
 * <p>
 * This is a small example Class for Login of the VideoShop Application. It uses the PageObject Pattern to represent
 * the Login Page. It's a more advanced technique to test the UI of a Web Application.<br>
 * You can still use the simple Selenide Selectors to find elements on the page.
 * Below you can find a small description of the PageObject Pattern and its advantages and drawbacks.
 * </p>
 *
 * <p>
 * PageObject is a design pattern that creates an object repository for web UI elements. The advantage of the Page Object pattern is that it reduces code duplication and improves test maintenance.
 * In other words: "It brings OOP to your UI". So the important ELements of a page are represented as variables in a
 * class. The methods of the class represent the possible user interactions with the page.
 * </p>
 *
 * <p>
 * <strong>Advantages:</strong>
 * <ul>
 *     <li>Reduces code duplication</li>
 *     <li>Improves test maintenance</li>
 *     <li>Brings OOP to your UI - hiding the complexity of the UI</li>
 *     <li>Changes in UI will only affect the page object, not the tests itself</li>
 * </ul>
 * <strong>Drawback:</strong>
 * <ul>
 *     <li>Can lead to a lot of boilerplate code</li>
 *     <li>Extra Work - Every page has its own class</li>
 *     <li>Know the page structure and its elements</li>
 * </ul>
 * </p>
 *
 * @author Oliver Geisel
 * @see <a href="https://martinfowler.com/bliki/PageObject.html">Martin Fowler - Page Object Pattern</a>
 * @see <a href="https://github.com/SeleniumHQ/selenium/wiki/PageFactory">Page Factory</a>
 * @see <a href="https://selenide.org/documentation/page-objects.html">Selenide Page Objects</a>
 */
// page_url = http://127.0.0.1:8080/login
public class LoginPage {


	@FindBy(how = How.ID, using = "username")
	private SelenideElement userNameInput;
	@FindBy(how = How.ID, using = "password")
	private  SelenideElement passwordInput;
	@FindBy(how = How.CSS, using = "button[type='submit']")
	private SelenideElement submitButton;


	public List<SelenideElement> getAllInputFields() {
		return List.of(userNameInput, passwordInput);
	}

	public LoginRequest login(String username, String password) {
		userNameInput.setValue(username);
		passwordInput.setValue(password);
		submitButton.click();

		return page(LoginRequest.class);
	}

	public static class LoginRequest extends Request {

	}
}