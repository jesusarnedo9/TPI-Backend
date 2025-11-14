# Dockerfile para extender la imagen oficial de Keycloak e instalar curl
FROM quay.io/keycloak/keycloak:latest

# Cambiar a root para instalar paquetes
USER root

# Instala curl usando el gestor disponible (microdnf, yum, dnf, apk o zypper)
RUN set -eux; \
    if command -v microdnf >/dev/null 2>&1; then \
        microdnf install -y curl && microdnf clean all; \
    elif command -v yum >/dev/null 2>&1; then \
        yum install -y curl && yum clean all; \
    elif command -v dnf >/dev/null 2>&1; then \
        dnf install -y curl && dnf clean all; \
    elif command -v apk >/dev/null 2>&1; then \
        apk add --no-cache curl; \
    elif command -v zypper >/dev/null 2>&1; then \
        zypper --non-interactive install -y curl; \
    else \
        echo "Ningún gestor de paquetes encontrado. Imposible instalar curl."; \
        exit 0; \
    fi

# Volver al usuario por defecto de la imagen (si aplica)
USER 1000

# No se sobrescribe ENTRYPOINT/CMD: se usa el comportamiento por defecto de la imagen
