package com.tp_bda.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.OAuth2Error;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class GWConfig {

    @Bean
    public RouteLocator configureRoutes(RouteLocatorBuilder builder,
                                        @Value("${APIGW_USUARIOS_URL}") String uriUsuarios,
                                        @Value("${APIGW_SOLICITUDES_URL}") String uriSolicitudes,
                                        @Value("${APIGW_INVENTARIO_URL}") String uriInventario,
                                        @Value("${APIGW_VIAJES_URL}") String uriViajes,
                                        @Value("${APIGW_TARIFAS_URL}") String uriTarifas) {

        System.out.println("Cargando ruta para Usuarios: " + uriUsuarios);
        System.out.println("Cargando ruta para Solicitudes: " + uriSolicitudes);

        return builder.routes()
                .route(p -> p.path("/api/usuarios/**").uri(uriUsuarios))
                .route(p -> p.path("/api/solicitudes/**").uri(uriSolicitudes))
                .route(p -> p.path("/api/inventario/**").uri(uriInventario))
                .route(p -> p.path("/api/viajes/**").uri(uriViajes))
                .route(p -> p.path("/api/tarifas/**").uri(uriTarifas))
                .build();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/tarifas/param/**").hasAnyAuthority("ADMIN", "OPERADOR")
                        .pathMatchers("/api/viajes/tramos/*/inicio", "/api/viajes/tramos/*/fin").hasAnyAuthority("TRANSPORTISTA")
                        .pathMatchers("/api/usuarios/clientes/**").hasAnyAuthority("CLIENTE", "ADMIN", "OPERADOR")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Reactive JwtDecoder that fetches JWKs from the internal keycloak host
     * and accepts tokens whose issuer is either the internal keycloak hostname
     * or the localhost host-mapped URL (useful for local testing).
     */
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(
            @Value("${OAUTH_JWK_SET_URI:http://keycloak:8080/realms/backend-tps/protocol/openid-connect/certs}") String jwkSetUri) {
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator(Duration.ofSeconds(60));

        OAuth2TokenValidator<Jwt> issuerValidator = jwt -> {
            String iss = jwt.getIssuer() != null ? jwt.getIssuer().toString() : "";
            List<String> allowed = Arrays.asList(
                    "http://keycloak:8080/realms/backend-tps",
                    "http://localhost:9090/realms/backend-tps"
            );
            ;
            if (allowed.contains(iss)) {
                return OAuth2TokenValidatorResult.success();
            }
            OAuth2Error err = new OAuth2Error("invalid_token", "The iss claim is not valid", null);
            return OAuth2TokenValidatorResult.failure(err);
        };

        DelegatingOAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(timestampValidator, issuerValidator);
        decoder.setJwtValidator(validator);

        return decoder;
    }
}