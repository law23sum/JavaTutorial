package com.apps.demo;

import com.apps.launcher.DemoDescriptor;
import com.apps.launcher.DemoScanner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Adapts the reflective demo discovery (all public static void main methods) to the
 * data model consumed by the web UI.
 */
@Component
public class DemoRegistry {

    private volatile List<DemoExample> demos = List.of();
    private volatile Map<String, DemoExample> demoIndex = Map.of();

    public DemoRegistry() {
        refresh();
    }

    public synchronized void refresh() {
        List<DemoExample> discovered = DemoScanner.discover().stream()
                .map(DemoRegistry::toExample)
                .collect(Collectors.toUnmodifiableList());
        Map<String, DemoExample> index = discovered.stream()
                .collect(Collectors.toUnmodifiableMap(DemoExample::id, Function.identity()));
        this.demos = Collections.unmodifiableList(discovered);
        this.demoIndex = index;
    }

    public List<DemoExample> demos() {
        return demos;
    }

    public synchronized String run(String id) {
        return getRequired(id).runDemo();
    }

    private DemoExample getRequired(String id) {
        DemoExample example = demoIndex.get(id);
        if (example == null) {
            throw new NoSuchElementException("Unknown demo: " + id);
        }
        return example;
    }

    private static DemoExample toExample(DemoDescriptor descriptor) {
        String className = descriptor.className();
        String displayName = buildDisplayName(descriptor);
        String pseudo = describe(descriptor);
        return new DemoExample(className, displayName, pseudo, () -> descriptor.run(new String[0]));
    }

    private static String buildDisplayName(DemoDescriptor descriptor) {
        String simple = descriptor.simpleName();
        String qualified = descriptor.className();
        return simple.equals(qualified) ? qualified : simple + " (" + qualified + ")";
    }

    private static String describe(DemoDescriptor descriptor) {
        String sourceHint = descriptor.className().replace('.', '/') + ".java";
        return """
                Fully qualified class: %s
                Package: %s
                Source hint: %s
                Action: Executes the main(String[]) method through reflection.
                """.formatted(descriptor.className(), descriptor.packageName(), sourceHint).strip();
    }
}
