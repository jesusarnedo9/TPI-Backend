package com.tp_bda.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value; // Estaba mal importada (era lombok.Value)
import org.springframework.cloud.gateway.route.RouteLocator; // ¡FALTA!
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder; // ¡FALTA!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // ¡FALTA!
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // ¡FALTA!
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GWConfig {
    // ... inyecciones de dependencias, etc.

    // Define tus variables del application.yml
    @Value("${apigateway.url-microservice-usuarios}") String uriUsuarios;
    @Value("${apigateway.url-microservice-solicitudes}") String uriSolicitudes;
    // ... y las demás

    @Bean
    public RouteLocator configureRoutes(RouteLocatorBuilder builder,
                                        @Value("${apigateway.url-microservice-usuarios}") String uriUsuarios,
                                        @Value("${apigateway.url-microservice-solicitudes}") String uriSolicitudes,
                                        @Value("${apigateway.url-microservice-inventario}") String uriInventario,
                                        @Value("${apigateway.url-microservice-viajes}") String uriViajes,
                                        @Value("${apigateway.url-microservice-tarifas}") String uriTarifas) {

        return builder.routes()
                // RUTA a USUARIOS (Puerto 8001)
                .route(p -> p.path("/api/usuarios/**").uri(uriUsuarios))

                // RUTA a SOLICITUDES (Puerto 8002)
                .route(p -> p.path("/api/solicitudes/**").uri(uriSolicitudes))

                // RUTA a INVENTARIO (Puerto 8003)
                .route(p -> p.path("/api/inventario/**").uri(uriInventario))

                // RUTA a VIAJES (Puerto 8004)
                .route(p -> p.path("/api/viajes/**").uri(uriViajes))

                // RUTA a TARIFAS (Puerto 8005)
                .route(p -> p.path("/api/tarifas/**").uri(uriTarifas))

                .build();
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        // ... Lógica de seguridad para restringir endpoints por rol
        // Por ejemplo, solo el rol ADMIN puede acceder a reportes (ejemplo del código guía, adaptado a tu solución)

        http.authorizeHttpRequests(authorize -> authorize
                // Por ejemplo, los endpoints de tarifas/parámetros solo para Operador/Admin
                .requestMatchers("/api/tarifas/param/**")
                .hasAnyAuthority("ADMIN", "OPERADOR") // Asumiendo que usarán ADMIN y OPERADOR como roles de gestión

                // Rutas de inicio/fin de tramo solo para Transportista
                .requestMatchers("/api/viajes/tramos/*/inicio", "/api/viajes/tramos/*/fin")
                .hasAnyAuthority("TRANSPORTISTA")

                // Rutas de clientes solo para Cliente (auto-registro) u Operador/Admin
                .requestMatchers("/api/usuarios/clientes/**")
                .hasAnyAuthority("CLIENTE", "ADMIN", "OPERADOR")

                // Cualquier otra petición debe estar autenticada (con token JWT)
                .anyRequest()
                .authenticated()

        ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    // ... el resto de la configuración (JwtDecoder y JwtAuthenticationConverter)
}