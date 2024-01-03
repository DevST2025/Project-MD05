package com.cardealer.security.config;

import com.cardealer.security.jwt.JwtAuthTokenFilter;
import com.cardealer.security.jwt.JwtEntryPoint;
import com.cardealer.security.jwt.JwtProvider;
import com.cardealer.security.principle.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @Bean
    public JwtEntryPoint jwtEntryPoint() {
        return new JwtEntryPoint();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    // Config Author
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // xử lí lỗi liên quan đến xác thực
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtEntryPoint()))
                // ko lưu trạng thái
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api.cardealer.com/v1/auth/**").permitAll()
                                .requestMatchers("/api.cardealer.com/v1/test/**").permitAll()
                                .requestMatchers("/api.cardealer.com/v1/categories/**").permitAll()
                                .requestMatchers("/api.cardealer.com/v1/products/**").permitAll()

                                .requestMatchers("/api.cardealer.com/v1/shopping-cart/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api.cardealer.com/v1/account/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api.cardealer.com/v1/user/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api.cardealer.com/v1/wish-list/**").hasAuthority("ROLE_USER")

                                .requestMatchers("/api.cardealer.com/v1/admin/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
