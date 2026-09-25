package videoshop;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Special {@link DisplayNameGenerator} that generates display names for test methods based on the {@link AT} annotation.
 * If the annotation is present, it uses the value of the annotation to generate a display name in the format "ATXXXX - Method Name". If the annotation is not present or the value is not a valid number, it defaults to "ATXXXX - Method Name".
 * @author Oliver Geisel
 */
public class AT_DisplayNameGenerator implements DisplayNameGenerator {

	@Override
	public @NonNull String generateDisplayNameForClass(Class<?> testClass) {

		return testClass.getSimpleName();
	}

	@Override
	public @NonNull String generateDisplayNameForNestedClass(@NonNull List<Class<?>> enclosingInstanceTypes,
			Class<?> nestedClass) {
		return nestedClass.getSimpleName();
	}

	@Override
	public @NonNull String generateDisplayNameForMethod(@NonNull List<Class<?>> enclosingInstanceTypes,
			@NonNull Class<?> testClass, Method testMethod) {

		AT displayName = testMethod.getAnnotation(AT.class);
		String methodName = displayName != null && !displayName.name().isEmpty()
				? displayName.name()
				: camelCaseToWords(testMethod.getName());

		Integer number = null;

		if (displayName != null) {
			if (displayName.number() != 0) {
				number = displayName.number();
			} else {
				var value = displayName.value();
				int offset = 0;
				if (value.startsWith("AT")) {
					offset = 2;
				}
				try {
					number = Integer.parseInt(value.substring(offset));
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		if (number != null) {
			return "AT" + String.format("%04d", number) + " - " + methodName;
		}
		return "ATXXXX - " + methodName;
	}

	@Override
	public @NonNull String generateDisplayNameForMethod(
			@NonNull Class<?> testClass, @NonNull Method testMethod) {
		return generateDisplayNameForMethod(new java.util.ArrayList<>(), testClass, testMethod);
	}

	private String camelCaseToWords(String name) {
		String result = name
				.replaceAll("([a-z])([A-Z])", "$1 $2")
				.replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2")
				.replace('_', ' ');

		return Character.toUpperCase(result.charAt(0)) + result.substring(1);
	}
}