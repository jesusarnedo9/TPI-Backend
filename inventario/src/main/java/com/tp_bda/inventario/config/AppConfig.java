package com.tp_bda.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importado
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Imports CRUCIALES (copiados de tu ejemplo)
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Arrays; // Importado
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Este bean te servirá si este microservicio necesita llamar a otros
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Habilitar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Deshabilitar CSRF
                .csrf(csrf -> csrf.disable())

                // 3. Reglas de autorización específicas para INVENTARIO
                .authorizeHttpRequests(authorize -> authorize

                        // REGLAS PARA CAMIONES: Solo ADMIN y OPERADOR
                        .requestMatchers(HttpMethod.GET, "/camiones", "/camiones/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.POST, "/camiones").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/camiones/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/camiones/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // REGLAS PARA CONTENEDORES: Solo ADMIN y OPERADOR
                        .requestMatchers(HttpMethod.GET, "/contenedores", "/contenedores/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.POST, "/contenedores").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/contenedores/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/contenedores/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // REGLAS PARA DEPOSITOS: Solo ADMIN y OPERADOR
                        .requestMatchers(HttpMethod.GET, "/depositos", "/depositos/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.POST, "/depositos").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/depositos/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/depositos/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // REGLA GENERAL: Cualquier otra solicitud debe estar autenticada
                        .anyRequest().authenticated()
                )

                // 4. Configurar como Servidor de Recursos OAuth2 (JWT)
                //    usando nuestro conversor de roles personalizado
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir solicitudes desde cualquier origen (para desarrollo)
        configuration.setAllowedOrigins(Arrays.asList("*"));

        // Permitir los métodos HTTP más comunes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Permitir todas las cabeceras
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a todas las rutas de tu microservicio
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Este bean es el conversor 100% personalizado (copiado de tu ejemplo).
     * Define la lógica exacta para leer los roles desde "realm_access.roles"
     * y los convierte en SimpleGrantedAuthority (sin prefijo "ROLE_").
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        // 1. Creamos nuestro converter con la lógica de extracción de roles
        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> {

            // 2. Obtenemos el claim "realm_access" como un Map.
            final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

            // Si no existe, devolvemos lista vacía
            if (realmAccess == null || realmAccess.isEmpty()) {
                return new ArrayList<>();
            }

            // 3. De dentro de "realm_access", obtenemos la lista "roles"
            @SuppressWarnings("unchecked")
            final Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            // Si no hay roles, lista vacía
            if (roles == null || roles.isEmpty()) {
                return new ArrayList<>();
            }

            // 4. Mapeamos cada string de rol (ej: "ADMIN")
            //    a un objeto SimpleGrantedAuthority("ADMIN").
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        };

        // 5. Creamos el conversor principal de Spring
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // 6. Le asignamos nuestro conversor personalizado
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}