package org.springframework.samples.petclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}pass").roles("USER")
            .and()
            .withUser("admin").password("{noop}pass").roles("ADMIN");

        //User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
            .authorizeRequests()
            .antMatchers("/","/login","/login-error","/css/**", "/js/**", "/images/**", "/webjars/**")
            .permitAll()
            .anyRequest()
            .fullyAuthenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureUrl("/login-error")
            .defaultSuccessUrl("/")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            ;
        httpSecurity.csrf().disable();


        /**httpSecurity
            .authorizeRequests()
            .antMatchers("/welcome").hasRole("USER")
            .anyRequest()
            .fullyAuthenticated()
            .and().httpBasic();
        httpSecurity.csrf().disable();*/
    }


}
