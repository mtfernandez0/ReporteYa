package com.cons.reporteya.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] RESOURCES = {"/css/**", "/img/**", "/favicon/**", "/webjars/**"};
    @Value("${rememberMe}")
    private String rememberMePrivateKey;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors().and()
//                .csrf().disable()
//                .authorizeHttpRequests()
//                    .requestMatchers(resources).permitAll()
//                    .requestMatchers("/login", "/register", "/", "").permitAll()
//                    .anyRequest().authenticated()
//                    .and()
//                .formLogin()
//                    .loginPage("/login").permitAll()
//                    .failureHandler(new CustomAuthFailureHandler())
//                    .defaultSuccessUrl("/")
//                    .and()
//                .rememberMe()
//                    .rememberMeCookieName("remember-me-cookie")
//                    .tokenValiditySeconds(60 * 60 * 24 * 7) //a week
//                    .and()
//                .logout()
//                    .logoutUrl("/logout").permitAll()
//                    .logoutSuccessUrl("/")
//                    .and()
//                .sessionManagement(session -> session.maximumSessions(1))
//                .httpBasic();
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RESOURCES).permitAll()
                        .requestMatchers("/login**", "/register**", "/home", "", "/", "/map").permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .usernameParameter("email")
                        .permitAll()
                        .failureHandler(new CustomAuthFailureHandler()))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeCookieName("remember-me-cookie")
                        .tokenValiditySeconds(60 * 60 * 24 * 7))
                .logout(LogoutConfigurer::permitAll)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(passwordEncoder());
        dao.setUserDetailsService(userDetailsService);
        return dao;
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        return new TokenBasedRememberMeServices(rememberMePrivateKey, userDetailsService);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOriginPatterns(List.of(
//                "http://localhost:",
//                "https://localhost:"));
//        config.setAllowedMethods(List.of(
//                HttpMethod.GET.name(),
//                HttpMethod.POST.name(),
//                HttpMethod.OPTIONS.name(),
//                HttpMethod.DELETE.name(),
//                HttpMethod.PUT.name(),
//                HttpMethod.PATCH.name()));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
