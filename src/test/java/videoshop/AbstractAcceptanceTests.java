package videoshop;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import selenidePageObjects.SlowMotionListener;

import static com.codeborne.selenide.Selenide.closeWebDriver;

/**
 * Base class for acceptance tests bootstrapping.
 *
 * @author Oliver Geisel
 */
@Tag("UI")
@DisplayNameGeneration(videoshop.AT_DisplayNameGenerator.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Reset the context after each test method to ensure independent tests
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractAcceptanceTests {

	@LocalServerPort
	protected int port;

	@Value("${test.withDelay:true}")
	protected boolean withDelay;
	@Value("${test.withCursor:true}")
	protected boolean withCursor;


	@BeforeEach
	public void setupConfiguration() {
		// Override the base URL for Selenide
		Configuration.baseUrl = "http://localhost:" + port + "/";

		if (withDelay) {
			WebDriverRunner.addListener(new SlowMotionListener());
		}
	}

	@AfterAll
	static void tearDownAll() {
		// Close the browser after all tests
		closeWebDriver();
	}
	
}
