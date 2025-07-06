package com.example.servicesyncservice.configuration;

import com.example.servicesyncservice.security.JwtAuthenticationEntryPoint;
import com.example.servicesyncservice.security.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenFilter jwtTokenFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/service-sync/order/get/all").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/order/get/by-id/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/get/by/status").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/set/status/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/mechanic/get/all").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/order/task/update/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/update/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/get/by-car-vin").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/order/create-order").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/order/delete/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/order/order-part/create").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/order-part/get/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/order-part/delete/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/add-task/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/get-tasks/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/task/delete/**").hasAnyRole("ADMIN", "MANAGER", "MECHANIC")
                                .requestMatchers("/service-sync/order/mechanic/tasks/**").hasRole("MECHANIC")
                                .requestMatchers("/service-sync/order/task/get/status/**").hasRole("MECHANIC")
                                .requestMatchers("/service-sync/order/mechanic/profile").hasRole("MECHANIC"))
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
