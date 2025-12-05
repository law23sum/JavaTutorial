package com.tutorial.algorithms;

import java.util.*;

public class Algorithms {

    // =========================
    // PLAIN (NO-COMMENT) VERSIONS
    // =========================
    /**
     * Given an array nums of length n containing distinct integers from 0..n
     * with exactly one number missing, return the missing number.
     *
     * Example:
     *   nums = [3, 0, 1] -> 2
     *
     * Approach:
     *  - Use XOR to avoid integer overflow from the sum formula.
     *  - XOR all numbers from 0 to n.
     *  - XOR all values in the array.
     *  - Every number that appears twice cancels out (a ^ a = 0),
     *    leaving only the missing number.
     *
     * Time:  O(n)
     * Space: O(1)
     */
    public static int findMissingNumber(int[] nums) {
        int n = nums.length;
        int xor = 0;
        for (int i = 0; i <= n; i++) {
            xor ^= i;
        }
        for (int num : nums) {
            xor ^= num;
        }
        return xor;
    }

    /**
     * Given an array of integers, move all zeros to the end while maintaining
     * the relative order of non-zero elements. The operation should be done in-place.
     *
     * Example:
     *   [0, 1, 0, 3, 12] -> [1, 3, 12, 0, 0]
     *
     * Approach:
     *  - Use a write pointer insertPos that tracks where the next non-zero
     *    element should be placed.
     *  - First pass: scan the array and write each non-zero element to nums[insertPos],
     *    incrementing insertPos each time.
     *  - Second pass: fill the remaining positions from insertPos to end with zeros.
     *
     * Time:  O(n)
     * Space: O(1)
     */
    public static void moveZerosToEnd(int[] nums) {
        int insertPos = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[insertPos++] = num;
            }
        }
        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }

    /**
     * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
     *
     * Example:
     *   s = "listen", t = "silent" -> true
     *
     * Approach:
     *  - If lengths differ, they cannot be anagrams.
     *  - Convert both strings to character arrays.
     *  - Sort both arrays.
     *  - If the sorted arrays are identical, the two strings are anagrams.
     *
     * Time:  O(n log n), where n is the length of the strings (sorting dominates).
     * Space: O(n) for the character arrays (or O(1) extra if you treat them as views).
     */
    public static boolean areAnagrams(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    /**
     * Given an array of strings, return the longest common prefix among them.
     * If there is no common prefix, return an empty string "".
     *
     * Example:
     *   ["flower", "flow", "flight"] -> "fl"
     *
     * Approach:
     *  - Edge case: if the array is null or empty, return "".
     *  - Sort the array of strings lexicographically.
     *  - The longest common prefix of the whole set must be a prefix of both:
     *      - the first string, and
     *      - the last string (after sorting).
     *  - Compare the first and last strings character-by-character until a mismatch.
     *  - Return the common prefix segment.
     *
     * Time:  O(n log n * L), where n is the number of strings and L is the
     *        length of the common prefix comparison (sorting dominates).
     * Space: O(1) extra (ignoring the internal sorting stack).
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        Arrays.sort(strs);
        String first = strs[0];
        String last  = strs[strs.length - 1];
        int i = 0;
        while (i < first.length() &&
                i < last.length()  &&
                first.charAt(i) == last.charAt(i)) {
            i++;
        }
        return first.substring(0, i);
    }

    /**
     * Given an integer array nums, return the length of the longest strictly
     * increasing subsequence (LIS).
     *
     * Example:
     *   [10, 9, 2, 5, 3, 7, 101, 18] -> 4 (one LIS is [2, 3, 7, 101])
     *
     * Approach (O(n log n) patience sorting style):
     *  - Maintain an array tails[], where tails[k] is the smallest possible tail
     *    value of an increasing subsequence of length (k + 1).
     *  - For each number x in nums:
     *      - Use binary search over tails[0..size) to find the first index i
     *        such that tails[i] >= x.
     *      - Set tails[i] = x (this either extends a subsequence or makes a better tail).
     *      - If i == size, we extended the longest subsequence; increment size.
     *  - The final value of size is the length of the LIS.
     *
     * Time:  O(n log n)
     * Space: O(n)
     */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int low = 0, high = size;
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < x) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            tails[low] = x;
            if (low == size) {
                size++;
            }
        }
        return size;
    }

    /**
     * Simple data holder class representing a single stock trade with:
     *  - buyDay: index of the day we buy
     *  - sellDay: index of the day we sell
     *  - profit: profit obtained (prices[sellDay] - prices[buyDay])
     */
    public static class StockTrade {
        public int buyDay;
        public int sellDay;
        public int profit;

        public StockTrade(int buyDay, int sellDay, int profit) {
            this.buyDay = buyDay;
            this.sellDay = sellDay;
            this.profit = profit;
        }

        @Override
        public String toString() {
            return "StockTrade{buyDay=" + buyDay +
                    ", sellDay=" + sellDay +
                    ", profit=" + profit + "}";
        }
    }

    /**
     * Given an array prices where prices[i] is the stock price on day i,
     * find the best day to buy and the best day to sell for maximum profit.
     *
     * We return a StockTrade object containing:
     *  - buyDay
     *  - sellDay
     *  - profit
     *
     * Approach:
     *  - Track the minimum price seen so far and its index.
     *  - At each day i, compute profit = prices[i] - minPrice.
     *  - If profit > maxProfit, update best buy/sell days.
     *  - Also update minPrice when we see a new lower price.
     *
     * Time:  O(n)
     * Space: O(1)
     */
    public static StockTrade bestStockTrade(int[] prices) {
        if (prices == null || prices.length < 2) {
            return new StockTrade(-1, -1, 0);
        }
        int minPrice = prices[0];
        int minIndex = 0;
        int maxProfit = 0;
        int bestBuyDay = -1;
        int bestSellDay = -1;
        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            int profit = price - minPrice;
            if (profit > maxProfit) {
                maxProfit = profit;
                bestBuyDay = minIndex;
                bestSellDay = i;
            }
            if (price < minPrice) {
                minPrice = price;
                minIndex = i;
            }
        }
        return new StockTrade(bestBuyDay, bestSellDay, maxProfit);
    }

    /**
     * Edge representation used in Dijkstra's algorithm.
     * Each edge has:
     *  - to: destination node index
     *  - weight: non-negative cost/weight of the edge
     */
    public static class Edge {
        int to;
        int weight;
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
        @Override
        public String toString() {
            return "(" + to + ", w=" + weight + ")";
        }
    }

    /**
     * Dijkstra's algorithm.
     *
     * Given:
     *  - n nodes labeled 0..n-1
     *  - graph: adjacency list where graph[u] is a list of edges (u -> v, weight w)
     *  - src: source node
     *
     * Return:
     *  - dist[] where dist[v] is the shortest distance from src to v.
     *    If unreachable, dist[v] stays Integer.MAX_VALUE.
     *
     * Approach:
     *  - Initialize dist[src] = 0, all other distances = INF.
     *  - Use a min-heap (priority queue) storing (distance, node).
     *  - While the queue is not empty:
     *      - Pop the node u with the smallest distance d.
     *      - If u is already visited, skip it.
     *      - For each edge (u -> v, w), relax the edge:
     *          if d + w < dist[v], update dist[v] and push (dist[v], v) into the queue.
     *
     * Time:  O((V + E) log V) using adjacency list and a binary heap priority queue.
     * Space: O(V) for dist, visited, and PQ; O(V + E) for the graph representation.
     */
    public static int[] dijkstra(int n, List<List<Edge>> graph, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});
        boolean[] visited = new boolean[n];
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0];
            int u = curr[1];
            if (visited[u]) continue;
            visited[u] = true;
            for (Edge e : graph.get(u)) {
                int v = e.to;
                int w = e.weight;
                if (!visited[v] && d + w < dist[v]) {
                    dist[v] = d + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }


    // =========================
    // EXPLAINED / COMMENTED VERSIONS
    // =========================

    public static int findMissingNumberExplained(int[] nums) {
        System.out.println("=== findMissingNumberExplained ===");
        System.out.println("Input array: " + Arrays.toString(nums));

        int n = nums.length;
        int xor = 0;

        System.out.println("Step 1: XOR all numbers from 0 to n (n = " + n + ")");
        for (int i = 0; i <= n; i++) {
            xor ^= i;
            System.out.println("  XOR with " + i + " -> current xor = " + xor);
        }

        System.out.println("Step 2: XOR with all elements of the array");
        for (int num : nums) {
            System.out.println("  XOR with element " + num + " (before xor = " + xor + ")");
            xor ^= num;
            System.out.println("  after xor = " + xor);
        }

        System.out.println("Result: Missing number = " + xor);
        System.out.println();
        return xor;
    }

    public static void moveZerosToEndExplained(int[] nums) {
        System.out.println("=== moveZerosToEndExplained ===");
        System.out.println("Original array: " + Arrays.toString(nums));

        int insertPos = 0;

        System.out.println("First pass: move all non-zero elements to the front");
        for (int i = 0; i < nums.length; i++) {
            System.out.println("  i = " + i + ", nums[i] = " + nums[i]);
            if (nums[i] != 0) {
                System.out.println("    Non-zero found, placing at insertPos = " + insertPos);
                nums[insertPos] = nums[i];
                insertPos++;
                System.out.println("    Array now: " + Arrays.toString(nums));
            } else {
                System.out.println("    Zero found, skip for now");
            }
        }

        System.out.println("Second pass: fill remaining positions with zeros from insertPos = " + insertPos);
        while (insertPos < nums.length) {
            nums[insertPos] = 0;
            System.out.println("  Setting nums[" + insertPos + "] = 0");
            insertPos++;
        }

        System.out.println("Final array: " + Arrays.toString(nums));
        System.out.println();
    }

    public static boolean areAnagramsExplained(String s, String t) {
        System.out.println("=== areAnagramsExplained ===");
        System.out.println("String s = \"" + s + "\"");
        System.out.println("String t = \"" + t + "\"");

        if (s.length() != t.length()) {
            System.out.println("Different lengths -> cannot be anagrams.");
            System.out.println();
            return false;
        }

        System.out.println("Same length. Converting to char arrays and sorting...");
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();

        Arrays.sort(a);
        Arrays.sort(b);

        System.out.println("Sorted s: " + Arrays.toString(a));
        System.out.println("Sorted t: " + Arrays.toString(b));

        boolean result = Arrays.equals(a, b);
        System.out.println("Are they equal? " + result);
        System.out.println();
        return result;
    }

    public static String longestCommonPrefixExplained(String[] strs) {
        System.out.println("=== longestCommonPrefixExplained ===");
        System.out.println("Original strings: " + Arrays.toString(strs));

        if (strs == null || strs.length == 0) {
            System.out.println("Empty input -> prefix = \"\"");
            System.out.println();
            return "";
        }

        System.out.println("Sorting the array to compare first and last strings...");
        Arrays.sort(strs);
        System.out.println("Sorted strings: " + Arrays.toString(strs));

        String first = strs[0];
        String last = strs[strs.length - 1];
        System.out.println("First string: \"" + first + "\"");
        System.out.println("Last string:  \"" + last + "\"");

        int i = 0;
        while (i < first.length()
                && i < last.length()
                && first.charAt(i) == last.charAt(i)) {
            System.out.println("  Matching character at index " + i + ": '" + first.charAt(i) + "'");
            i++;
        }

        String prefix = first.substring(0, i);
        System.out.println("Longest common prefix: \"" + prefix + "\"");
        System.out.println();
        return prefix;
    }

    public static int lengthOfLISExplained(int[] nums) {
        System.out.println("=== lengthOfLISExplained ===");
        System.out.println("Input: " + Arrays.toString(nums));

        if (nums == null || nums.length == 0) {
            System.out.println("Empty array -> LIS length = 0");
            System.out.println();
            return 0;
        }

        int[] tails = new int[nums.length];
        int size = 0;

        System.out.println("We maintain an array tails[], where tails[k] is the smallest tail");
        System.out.println("of an increasing subsequence of length (k+1).");
        System.out.println();

        for (int x : nums) {
            System.out.println("Processing x = " + x);
            int low = 0, high = size;
            while (low < high) {
                int mid = low + (high - low) / 2;
                System.out.println("  Binary search: low=" + low + ", high=" + high + ", mid=" + mid + ", tails[mid]=" + tails[mid]);
                if (tails[mid] < x) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = x;
            System.out.println("  Place x at tails[" + low + "] -> " + Arrays.toString(Arrays.copyOf(tails, size + 1)));

            if (low == size) {
                size++;
                System.out.println("  Extended size of LIS to " + size);
            }
            System.out.println();
        }

        System.out.println("Final tails array (effective part): " +
                Arrays.toString(Arrays.copyOf(tails, size)));
        System.out.println("Length of LIS = " + size);
        System.out.println();
        return size;
    }

    public static StockTrade bestStockTradeExplained(int[] prices) {
        System.out.println("=== bestStockTradeExplained ===");
        System.out.println("Prices: " + Arrays.toString(prices));

        if (prices == null || prices.length < 2) {
            System.out.println("Not enough data to trade. Returning profit=0.");
            System.out.println();
            return new StockTrade(-1, -1, 0);
        }

        int minPrice = prices[0];
        int minIndex = 0;

        int maxProfit = 0;
        int bestBuyDay = -1;
        int bestSellDay = -1;

        System.out.println("Initial minPrice = " + minPrice + " at day 0");
        System.out.println();

        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            System.out.println("Day " + i + ": price = " + price);

            int profitIfSoldToday = price - minPrice;
            System.out.println("  Potential profit if bought at day " + minIndex +
                    " (" + minPrice + ") and sold today: " + profitIfSoldToday);

            if (profitIfSoldToday > maxProfit) {
                maxProfit = profitIfSoldToday;
                bestBuyDay = minIndex;
                bestSellDay = i;
                System.out.println("  New best trade found: buyDay=" + bestBuyDay +
                        ", sellDay=" + bestSellDay + ", profit=" + maxProfit);
            }

            if (price < minPrice) {
                minPrice = price;
                minIndex = i;
                System.out.println("  New minimum price found: " + minPrice + " at day " + minIndex);
            }

            System.out.println();
        }

        System.out.println("Best trade result: " + new StockTrade(bestBuyDay, bestSellDay, maxProfit));
        System.out.println();
        return new StockTrade(bestBuyDay, bestSellDay, maxProfit);
    }

    public static int[] dijkstraExplained(int n, List<List<Edge>> graph, int src) {
        System.out.println("=== dijkstraExplained ===");
        System.out.println("Number of nodes: " + n);
        System.out.println("Source node: " + src);
        System.out.println("Graph adjacency list:");
        for (int i = 0; i < graph.size(); i++) {
            System.out.println("  " + i + " -> " + graph.get(i));
        }
        System.out.println();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        boolean[] visited = new boolean[n];

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        System.out.println("Initial dist: " + Arrays.toString(dist));
        System.out.println("Starting Dijkstra's main loop...");
        System.out.println();

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0];
            int u = curr[1];

            if (visited[u]) {
                System.out.println("Node " + u + " already visited, skipping.");
                continue;
            }

            System.out.println("Visiting node " + u + " with current distance " + d);
            visited[u] = true;

            for (Edge e : graph.get(u)) {
                int v = e.to;
                int w = e.weight;

                if (!visited[v] && d + w < dist[v]) {
                    System.out.println("  Relaxing edge " + u + " -> " + v + " (w = " + w + ")");
                    System.out.println("    Old dist[" + v + "] = " + dist[v]);
                    dist[v] = d + w;
                    System.out.println("    New dist[" + v + "] = " + dist[v]);
                    pq.offer(new int[]{dist[v], v});
                }
            }

            System.out.println("dist after processing node " + u + ": " + Arrays.toString(dist));
            System.out.println();
        }

        System.out.println("Final shortest distances from source " + src + ":");
        System.out.println("dist = " + Arrays.toString(dist));
        System.out.println();
        return dist;
    }

    public static void main(String[] args) {
        int[] missingExample = {3, 0, 1};
        findMissingNumberExplained(missingExample);

        int[] zerosExample = {0, 1, 0, 3, 12};
        moveZerosToEndExplained(zerosExample);

        areAnagramsExplained("listen", "silent");
        areAnagramsExplained("hello", "bello");

        String[] lcpExample = {"flower", "flow", "flight"};
        longestCommonPrefixExplained(lcpExample);

        int[] lisExample = {10, 9, 2, 5, 3, 7, 101, 18};
        lengthOfLISExplained(lisExample);

        int[] stockPrices = {7, 1, 5, 3, 6, 4};
        bestStockTradeExplained(stockPrices);

        int n = 5;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        graph.get(0).add(new Edge(1, 2));
        graph.get(0).add(new Edge(2, 4));
        graph.get(1).add(new Edge(2, 1));
        graph.get(1).add(new Edge(3, 7));
        graph.get(2).add(new Edge(4, 3));
        graph.get(3).add(new Edge(4, 1));

        dijkstraExplained(n, graph, 0);
    }
}
