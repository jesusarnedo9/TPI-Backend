package com.tp_bda.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

// Imports CRUCIALES para la solución
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize

                        // REGLAS PARA CLIENTES (Usando hasAuthority)
                        .requestMatchers(HttpMethod.GET, "/clientes/mi-perfil").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/clientes").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/clientes", "/clientes/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // REGLAS PARA TRANSPORTISTAS (Usando hasAuthority)
                        .requestMatchers(HttpMethod.GET, "/transportistas/mi-perfil").hasAuthority("TRANSPORTISTA")
                        .requestMatchers(HttpMethod.POST, "/transportistas").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/transportistas", "/transportistas/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/transportistas/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // REGLA GENERAL
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        // Le decimos a Spring que use nuestro conversor de roles personalizado
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));

        return http.build();
    }

    /**
     * Este bean es el conversor 100% personalizado.
     * Define la lógica exacta para leer los roles desde "realm_access.roles"
     * y los convierte en SimpleGrantedAuthority (sin prefijo).
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

            // 4. Mapeamos cada string de rol (ej: "TRANSPORTISTA")
            //    a un objeto SimpleGrantedAuthority("TRANSPORTISTA").
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