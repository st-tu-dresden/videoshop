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
 * Page Object representing the registration page of the application. (/register)
 */
public class RegisterPage {


	public Request register(String username, String password, String address) {
		$("#name").setValue(username);
		$("#password").setValue(password);
		$("#address").setValue(address);
		$("button[type='submit']").click();
		return page(RegisterRequest.class);
	}

	public static class RegisterRequest extends Request {
	}

}
