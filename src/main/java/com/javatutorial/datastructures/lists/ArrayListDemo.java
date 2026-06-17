package com.javatutorial.datastructures.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>ArrayList</h2>
 * Backed by a dynamically-resized array. Best when you need fast random
 * access and append-heavy workloads.
 *
 * <pre>
 *   add(e)        amortized O(1)
 *   get(i)/set(i) O(1)
 *   add(i, e)     O(n - i)   (shifts elements right)
 *   remove(i)     O(n - i)   (shifts elements left)
 *   contains(e)   O(n)
 * </pre>
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        List<String> urls = new ArrayList<>(List.of(
                "https://test.login",
                "https://test.dashboard",
                "https://test.logout"));

        urls.add("https://test.reports");          // append: O(1) amortized
        urls.add(1, "https://test.signup");        // insert: shifts right
        urls.set(0, "https://test.home");          // overwrite by index: O(1)
        urls.remove(2);                            // delete: shifts left

        for (int i = 0; i < urls.size(); i++) {
            System.out.println(i + " -> " + urls.get(i));
        }
    }
}
