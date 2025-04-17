package org.springframework.samples.petclinic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "true")
public class BasicAuthenticationConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        var encoders = Map.of("noop", NoOpPasswordEncoder.getInstance());
        var passwordEncoder = new DelegatingPasswordEncoder("noop", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/api/owners").hasAnyRole("ROLE_OWNER_ADMIN")
                .requestMatchers("/api/pets").hasAnyRole("ROLE_OWNER_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/pettypes").hasAnyRole("ROLE_OWNER_ADMIN", "ROLE_VET_ADMIN")
                .requestMatchers("/api/pettypes").hasAnyRole("ROLE_VET_ADMIN")
                .requestMatchers("/api/specialties").hasAnyRole("ROLE_VET_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ROLE_ADMIN")
                .requestMatchers("/api/vets").hasAnyRole("ROLE_VET_ADMIN")
                .requestMatchers("/api/visits").hasAnyRole("ROLE_OWNER_ADMIN")
                .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());
        // @formatter:on
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth
            .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery("select username,role from roles where username=?");
        // @formatter:on
    }
}
