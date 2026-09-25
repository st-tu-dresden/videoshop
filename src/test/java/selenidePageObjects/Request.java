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

import static com.codeborne.selenide.Selenide.*;

/**
 * Base page object to handle general requests and actions that are not specific to a particular page.
 *
 * @author Oliver Geisel
 */
public abstract class Request {

	/**
	 * General logout method for all pages. Only a hacky solution, because the logout button is not on every page.
	 *
	 * @return GeneralRequest page object after logout
	 *
	 * @throws Exception if the logout button is not found on the page. The Browser will close.
	 */
	public Request logout() throws Exception{
		$("button[type='submit']").click();
		return page(GeneralRequest.class);
	}

//region setter/getter
	/**
	 * Get the current page URL. Example: <a href="http://localhost:8080/login">http://localhost:8080/login</a>
	 *
	 * @return The current page URL as a String
	 */
	public String getPageUrl() {
		return webdriver().object().getCurrentUrl();
	}

	/**
	 * Get the current page path. Example: <a href="/login">/login</a>
	 *
	 * @return The current page path as a String
	 */
	public String getPagePath() {
		return getPageUrl().replaceAll("http://localhost:\\d+", "");
	}


//endregion

	/**
	 * Normal Instance of Request class, used for general requests that are not specific to a page.
	 */
	public static class GeneralRequest extends Request {

	}
}

