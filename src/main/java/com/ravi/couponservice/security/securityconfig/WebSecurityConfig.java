package com.ravi.couponservice.security.securityconfig;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

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
                        .requestMatchers(HttpMethod.GET, "/coupon_api/coupons/**")
                        .hasAnyRole("ADMIN", "USER")
                        //.permitAll()

                        //.requestMatchers(HttpMethod.GET,  "/").hasAnyRole("ADMIN", "USER") //since now used below and permitted to all
                        .requestMatchers(HttpMethod.GET,  "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/saveCoupon").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/showGetCoupon", "/getCoupon").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST,  "/getCoupon").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/","/login", "/registerUser", "/showReg").permitAll()
                        .requestMatchers(HttpMethod.POST, "/coupon_api/coupons").hasRole("ADMIN"));

        //For logout Redirected , spring handle logout self too , logout button need to be added since defualt logout won't work
        httpSecurity.logout(logout -> logout.logoutSuccessUrl("/")
                .deleteCookies() //to delete cookies
                .invalidateHttpSession(true) //give false it means session will be active even on logout
        );
        httpSecurity.securityContext(httpSecuritySecurityContextConfigurer ->
                httpSecuritySecurityContextConfigurer.requireExplicitSave(true));

        //Enable by defualt Csrf - to prevent unwanted access to cookies of current session by cross origin
        //httpSecurity.csrf(AbstractHttpConfigurer::disable);

        //or can ignore certain request macher in csrf
        httpSecurity.csrf(csrfConfigurer -> {
            //csrfConfigurer.ignoringRequestMatchers("/coupon_api/coupons/**");

            //or

            //RequestMatcher ignoreAnyGetRequest = new RegexRequestMatcher("/getCoupon", HttpMethod.GET.name());
           // csrfConfigurer.ignoringRequestMatchers(ignoreAnyGetRequest);

            //or
            RequestMatcher requestMatcher = new RegexRequestMatcher("/getCoupon", HttpMethod.POST.name());
            requestMatcher = new MvcRequestMatcher(new HandlerMappingIntrospector(), "/getCoupon");
            csrfConfigurer.ignoringRequestMatchers(requestMatcher);
        });

        //allow additional port - used flutter app with given port
        httpSecurity.cors(cors -> {
            CorsConfigurationSource corsSource = request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:49705"));
                corsConfiguration.setAllowedMethods(List.of("GET"));
                return corsConfiguration;
            };
            cors.configurationSource(corsSource);
        });
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
