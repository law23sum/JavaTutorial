package com.framework.spring.config;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ServerPortCustomizerTest {

    @Test
    public void pickPortPrefersExplicitlyConfiguredPortWhenFree() {
        int freePort = reserveFreePortAndRelease();
        int chosen = ServerPortCustomizer.pickPort(freePort, List.of(0));
        assertEquals(chosen, freePort);
    }

    @Test
    public void pickPortFallsBackWhenConfiguredPortIsBusy() throws IOException {
        try (ServerSocket busy = new ServerSocket(0)) {
            int busyPort = busy.getLocalPort();
            int fallback = reserveFreePortAndRelease(busyPort);
            int chosen = ServerPortCustomizer.pickPort(busyPort, List.of(fallback));
            assertEquals(chosen, fallback);
        }
    }

    @Test
    public void pickPortUsesEphemeralPortWhenNothingElseIsFree() throws IOException {
        try (ServerSocket busyPrimary = new ServerSocket(0);
             ServerSocket busySecondary = new ServerSocket(0)) {
            int busyPort = busyPrimary.getLocalPort();
            int otherBusyPort = busySecondary.getLocalPort();
            int chosen = ServerPortCustomizer.pickPort(busyPort, List.of(otherBusyPort));
            assertEquals(chosen, 0);
        }
    }

    private static int reserveFreePortAndRelease(int... excluded) {
        try (ServerSocket socket = new ServerSocket(0)) {
            int candidate = socket.getLocalPort();
            if (excluded != null) {
                for (int value : excluded) {
                    if (candidate == value) {
                        return reserveFreePortAndRelease(excluded);
                    }
                }
            }
            return candidate;
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to reserve a free port", ex);
        }
    }
}
