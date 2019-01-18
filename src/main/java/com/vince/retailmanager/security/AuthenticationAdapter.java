package com.vince.retailmanager.security;


import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthenticationAdapter extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource dataSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/", "/login", "/mobile/login", "/api/auth/**", "/reservations/**").permitAll()
        .anyRequest().authenticated()
        .anyRequest().hasRole("ADMIN")
        .and()
        .httpBasic()
        .and()
        .csrf().disable()
    ;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .jdbcAuthentication()
        .dataSource(dataSource)
        .passwordEncoder(this.passwordEncoder())
        .usersByUsernameQuery(
            "select username,password,enabled from users where username=?")
        .authoritiesByUsernameQuery("select username,role from roles where username=?");
  }


  @Bean
  public StrictHttpFirewall httpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowSemicolon(true);
    return firewall;
  }

/*  used for testing
  let's us add authorization with plaintext passwords
  */
//  @SuppressWarnings("deprecation")
//  @Bean
//  public static NoOpPasswordEncoder passwordEncoder() {
//    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//
}

