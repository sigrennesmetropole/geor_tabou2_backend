package rm.tabou2.facade.spring.security;

import java.util.Arrays;
import java.util.List;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	private static final String[] PERMIT_ALL_URL = {

			// URL public
			"/configuration/*",

			// -- swagger ui
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**" };

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
				// starts authorizing configurations
				.authorizeHttpRequests((authorizeHttpRequests -> authorizeHttpRequests
						// ignoring the guest's urls "
						.requestMatchers(PERMIT_ALL_URL).permitAll()
						// authenticate all remaining URLS
						.anyRequest().fullyAuthenticated()))
				.exceptionHandling(Customizer.withDefaults())
				// configuring the session on the server
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// installation des filtres
				.addFilterAfter(createPreAuthenticationFilter(), BasicAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE"));
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		// Url autorisées
		// 4200 pour les développement | 8080 pour le déploiement
		configuration.setAllowedOriginPatterns(List.of("*"));

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	RewriteFilter rewriteFilter() {
		return new RewriteFilter();
	}

	private Filter createPreAuthenticationFilter() {
		return new PreAuthenticationFilter();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}4dM1nApp!").roles("ADMIN");
		auth.authenticationProvider(createPreAuthenticationProvider());
	}

	private AuthenticationProvider createPreAuthenticationProvider() {
		return new PreAuthenticationProvider();
	}

	@Bean
	protected GrantedAuthorityDefaults grantedAuthorityDefaults() {
		// Remove the ROLE_ prefix
		return new GrantedAuthorityDefaults("");
	}

}
