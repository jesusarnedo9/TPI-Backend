package com.tp_bda.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
// CORRECCIÓN: Importamos las clases de seguridad Reactivas
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
// CORRECCIÓN: Usamos la anotación de seguridad Reactiva
@EnableWebFluxSecurity
public class GWConfig {

    // --- 1. EL "MAPA" (Enrutamiento) ---
    // Lee las variables de entorno del docker-compose
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

    // --- 2. EL "GUARDIA DE SEGURIDAD" (Seguridad Reactiva) ---
    // CORRECCIÓN: Usamos ServerHttpSecurity (reactivo) en lugar de HttpSecurity (MVC)
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilitamos CSRF para APIs REST
                .authorizeExchange(exchange -> exchange
                        // Rutas de tarifas para Operador/Admin
                        .pathMatchers("/api/tarifas/param/**")
                        .hasAnyAuthority("ADMIN", "OPERADOR")

                        // Rutas de inicio/fin de tramo solo para Transportista
                        .pathMatchers("/api/viajes/tramos/*/inicio", "/api/viajes/tramos/*/fin")
                        .hasAnyAuthority("TRANSPORTISTA")

                        // Rutas de clientes para Cliente (auto-registro) u Operador/Admin
                        .pathMatchers("/api/usuarios/clientes/**")
                        .hasAnyAuthority("CLIENTE", "ADMIN", "OPERADOR")

                        // Cualquier otra petición debe estar autenticada
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}