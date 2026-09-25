package videoshop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Custom annotation to mark test methods with AT numbers (Acceptance Test).
 *
 *
 *
 * @author Oliver Geisel
 * @see AT_DisplayNameGenerator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AT {
	/**
	 * The AT number associated with the test method. This value is used to generate the display name for the test method in the format "ATXXXX - Method Name".
	 * @return the AT number as a string
	 */
	String value() default "";

	/**
	 * The AT number associated with the test method. This will override the value of the {@link #value()} if set.
	 * @return the AT number as an integer
	 */
	int number() default 0;

	/**
	 * Alternative name for the test method. Will override the default display name generated from the method name if set.
	 * @return the alternative name as a string
	 */
	String name() default "";
}