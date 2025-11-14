package com.tp_bda.Viajes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
        // Construye la URI del JWKS a partir del issuer del realm. Ej: http://keycloak:8080/realms/backend-tps/protocol/openid-connect/certs
        String jwkSetUri = issuer.endsWith("/") ? issuer + "protocol/openid-connect/certs" : issuer + "/protocol/openid-connect/certs";

        var jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Validadores: dejamos la validación de timestamps (exp/nbf) pero no forzamos la comprobación estricta del issuer.
        // Esto es útil en entornos de desarrollo cuando el realm puede no existir al primer arranque.
        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();
        jwtDecoder.setJwtValidator(timestampValidator);

        return jwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/viajes/tramos/*/inicio", "/api/viajes/tramos/*/fin")
                        .hasAnyAuthority("TRANSPORTISTA")
                        .requestMatchers("/api/viajes/rutas/**", "/api/viajes/tramos/**")
                        .hasAnyAuthority("ADMIN", "OPERADOR")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}