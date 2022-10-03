package edunhnil.project.forum.api.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
                http
                                .cors().disable() // block strange domain
                                .csrf()
                                .disable()
                                .authorizeRequests()
                                .antMatchers("/**").permitAll().anyRequest().authenticated();
        }

}
