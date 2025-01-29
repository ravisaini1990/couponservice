package com.ravi.couponservice.security.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class WebSecurityConfig {

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationManager = new DaoAuthenticationProvider();
        authenticationManager.setUserDetailsService(userDetailsService);
        authenticationManager.setPasswordEncoder(getPasswordEncoder());
        return new ProviderManager(authenticationManager);
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository());
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //httpSecurity.formLogin(Customizer.withDefaults()); //since we are now handling auth by ourself

        httpSecurity.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers(HttpMethod.GET, "/coupon_api/coupons/**").hasAnyRole("ADMIN", "USER")
                        //.requestMatchers(HttpMethod.GET,  "/").hasAnyRole("ADMIN", "USER") //since now used below and permitted to all
                        .requestMatchers(HttpMethod.GET,  "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/saveCoupon").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/showGetCoupon", "/getCoupon").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST,  "/getCoupon").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/","/login", "/registerUser", "/showReg").permitAll()
                        .requestMatchers(HttpMethod.POST, "/coupon_api/coupons").hasRole("ADMIN"));

        //For logout Redirected , spring handle logout self too
        httpSecurity.logout(logout -> logout.logoutSuccessUrl("/")
                .deleteCookies() //to delete cookies
                .invalidateHttpSession(true) //give false it means session will be active even on logout
        );
        httpSecurity.securityContext(httpSecuritySecurityContextConfigurer ->
                httpSecuritySecurityContextConfigurer.requireExplicitSave(true));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

   /* @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.authorizeHttpRequests(requestMatcherRegistry -> {
            requestMatcherRegistry
                    .requestMatchers(HttpMethod.GET, "/coupon_api/coupons/**").hasAnyRole("USER", "ADMIN")
//                    .requestMatchers(HttpMethod.GET, "/").hasAnyRole("USER", "ADMIN") // In order to show login form on base url
                    .requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN") // Only Admins open create a coupon page// Anyone can see coupon using a coupon code
                    .requestMatchers(HttpMethod.POST, "/saveCoupon").hasRole("ADMIN") // Only Admins open create a coupon page// Anyone can see coupon using a coupon code
                    .requestMatchers(HttpMethod.GET, "/getCoupon", "/showGetCoupon").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/getCoupon").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/","/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/coupon_api/coupons").hasRole("ADMIN");
        });
        httpSecurity.securityContext(httpSecuritySecurityContextConfigurer -> httpSecuritySecurityContextConfigurer.requireExplicitSave(true));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
*/






}
