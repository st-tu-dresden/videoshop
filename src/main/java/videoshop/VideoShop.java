package videoshop;

import org.salespointframework.Salespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = { Salespoint.class, VideoShop.class })
@EnableJpaRepositories(basePackageClasses = { Salespoint.class, VideoShop.class })
@ComponentScan
public class VideoShop {

	public static void main(String[] args) {
		SpringApplication.run(VideoShop.class, args);
	}

	@Configuration
	static class VideoShopWebConfiguration extends SalespointWebConfiguration {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/login").setViewName("login");
		}
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().//
					formLogin().loginPage("/login").loginProcessingUrl("/login").and(). //
					logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
