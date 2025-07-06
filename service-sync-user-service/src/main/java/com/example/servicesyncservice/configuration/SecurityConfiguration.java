package com.example.servicesyncservice.configuration;

import com.example.servicesyncservice.security.UserDetailsServiceImpl;
import com.example.servicesyncservice.security.jwt.JwtAuthenticationEntryPoint;
import com.example.servicesyncservice.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
        
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) ->
                        auth.requestMatchers("/service-sync/user/register").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/service-sync/user/signin").permitAll()
                                .requestMatchers("/service-sync/user/create/admin").permitAll()
                                .requestMatchers("/service-sync/user/refresh-token").permitAll()
                                .requestMatchers("/service-sync/user/logout").authenticated()
                                .requestMatchers("/service-sync/user/profile").authenticated()
                                .requestMatchers("/service-sync/user/get/all").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/user/get/role").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/user/get/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/service-sync/user/update/**").authenticated()
                                .requestMatchers("/service-sync/user/delete/**").authenticated()
                                .requestMatchers("/service-sync/user/role/update/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
