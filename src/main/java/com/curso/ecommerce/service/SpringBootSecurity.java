package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity {

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Deshabilitar CSRF (si es necesario)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/administrador/**").hasRole("ADMIN") // Solo ADMIN puede acceder a /administrador/**
                .requestMatchers("/productos/**").hasRole("ADMIN") // Solo ADMIN puede acceder a /productos/**
                .anyRequest().permitAll() // Permitir acceso a cualquier otra ruta sin autenticación
            )
            .formLogin(form -> form
                .loginPage("/usuario/login") // Página de login personalizada
                .permitAll() // Permitir acceso a la página de login
                .defaultSuccessUrl("/usuario/acceder") // Redirigir después de un login exitoso
            );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailService) // Usar el UserDetailsService personalizado
            .passwordEncoder(passwordEncoder); // Configurar el codificador de contraseñas
    }
}