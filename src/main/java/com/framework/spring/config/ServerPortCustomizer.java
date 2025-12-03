package com.framework.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Makes Spring Boot resilient to a busy default port (8080) by probing a list of candidates and
 * falling back to an ephemeral port when none are available. This lets `mvn spring-boot:run`,
 * `./gradlew bootRun`, and IDE launches start even if another process already occupies 8080.
 */
@Component
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    private static final Logger log = LoggerFactory.getLogger(ServerPortCustomizer.class);

    private final int configuredPort;
    private final List<Integer> fallbackPorts;

    public ServerPortCustomizer(@Value("${server.port:8080}") int configuredPort,
                                @Value("${app.server.additional-ports:8081,0}") String fallbackPortList) {
        this.configuredPort = configuredPort;
        this.fallbackPorts = parsePorts(fallbackPortList);
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        int selected = pickPort(configuredPort, fallbackPorts);
        if (selected == configuredPort) {
            log.info("Starting Spring Boot server on configured port {}", selected);
        } else if (selected == 0) {
            log.warn("No preferred ports were free. Falling back to an ephemeral port.");
        } else {
            log.warn("Configured port {} is busy. Switching to fallback port {}", configuredPort, selected);
        }
        factory.setPort(selected);
    }

    static int pickPort(int primary, List<Integer> fallbacks) {
        List<Integer> candidates = new ArrayList<>();
        candidates.add(primary);
        if (fallbacks != null) {
            for (Integer fallback : fallbacks) {
                if (fallback == null || candidates.contains(fallback)) {
                    continue;
                }
                candidates.add(fallback);
            }
        }
        for (int port : candidates) {
            if (port <= 0) {
                return 0; // 0 instructs the server to pick any free port
            }
            if (isPortFree(port)) {
                return port;
            }
        }
        return 0;
    }

    static List<Integer> parsePorts(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        Set<Integer> unique = new LinkedHashSet<>();
        for (String token : raw.split(",")) {
            String trimmed = token.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                unique.add(Integer.parseInt(trimmed));
            } catch (NumberFormatException ex) {
                log.warn("Ignoring non-numeric port entry '{}' in app.server.additional-ports", trimmed);
            }
        }
        return List.copyOf(unique);
    }

    private static boolean isPortFree(int port) {
        try (ServerSocket socket = new ServerSocket()) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(port));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
