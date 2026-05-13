package es.educastur.todo_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, 
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());

        // REQUISITO: API Stateless 
        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Configuración de rutas
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(
                        "/auth/register", 
                        "/error",
                        "/v3/api-docs",        
                        "/v3/api-docs/**",     
                        "/swagger-ui/**",      
                        "/swagger-ui.html"     
                ).permitAll()
                .anyRequest().authenticated() 
        );

        // Activamos Basic Auth
        http.httpBasic(Customizer.withDefaults());

        // REQUISITO: Manejo de errores 401 y 403 personalizados
        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 No autenticado
                .accessDeniedHandler(customAccessDeniedHandler)           // 403 Sin permisos
        );

        http.headers(headers -> headers.frameOptions(opts -> opts.disable()));

        return http.build();
    }
}