package com.tutorial.algorithms;

import java.util.*;

// Definition for singly-linked list.
class ListNode {
    int val;
    ListNode next;

    ListNode() {}

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

// assuming standard TreeNode:
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class BasicAlgorithms {

    int[] twoSumExplanation(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        System.out.println("Input array: " + java.util.Arrays.toString(nums));
        System.out.println("Target: " + target);
        System.out.println("======================================");

        int step = 1;

        for (int i = 0; i < nums.length; i++) {
            int current = nums[i];
            int need = target - current;

            System.out.println("Step " + step++ + ":");
            System.out.println("  i = " + i);
            System.out.println("  current = nums[" + i + "] = " + current);
            System.out.println("  need = target - current = " + target + " - " + current + " = " + need);
            System.out.println("  Map before checking: " + map);

            if (map.containsKey(need)) {
                System.out.println("  -> Found a match!");
                System.out.println("     map contains key 'need' = " + need);

                int indexOfNeed = map.get(need);
                int indexOfCurrent = i;

                System.out.println("     indexOfNeed (value " + need + ") = " + indexOfNeed);
                System.out.println("     indexOfCurrent (value " + current + ") = " + indexOfCurrent);

                int[] result = new int[2];
                result[0] = indexOfNeed;
                result[1] = indexOfCurrent;

                System.out.println("  -> Returning result: [" + result[0] + ", " + result[1] + "]");
                System.out.println("======================================");
                return result;
            }

            System.out.println("  -> No match found for " + need + " yet.");
            System.out.println("  -> Storing current number in map: value " + current + " at index " + i);
            map.put(current, i);
            System.out.println("  Map after put: " + map);
            System.out.println("--------------------------------------");
        }

        System.out.println("No two-sum solution found.");
        System.out.println("======================================");
        return null; // or throw an exception depending on the LeetCode version
    }


    int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int current = nums[i];
            int need = target - current;

            if (map.containsKey(need)) {
                // ✅ We found a pair: (need, current)

                int indexOfNeed = map.get(need);
                int indexOfCurrent = i;

                // Make the result array in the long, explicit way
                int[] result = new int[2];  // array of length 2
                result[0] = indexOfNeed;
                result[1] = indexOfCurrent;

                return result;
            }

            // store current value -> its index
            map.put(current, i);
        }

        // If no pair found, return [-1, -1] in the same explicit way
        int[] notFound = new int[2];
        notFound[0] = -1;
        notFound[1] = -1;

        return notFound;
    }

    int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0;
        int maxLen = 0;
        int step = 1;

        System.out.println("Input string: \"" + s + "\"");
        System.out.println("----------------------------------------");

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            System.out.println("Step " + step++ + ":");
            System.out.println("  right = " + right + ", char = '" + c + "'");
            System.out.println("  Current window before processing: [" + left + ", " + right + "]");
            System.out.println("  Current substring before processing: \"" + s.substring(left, right + 1) + "\"");

            if (lastIndex.containsKey(c)) {
                int prevIndex = lastIndex.get(c);
                System.out.println("  -> '" + c + "' was seen before at index " + prevIndex);

                int newLeft = Math.max(left, prevIndex + 1);
                System.out.println("  -> Updating left: max(" + left + ", " + (prevIndex + 1) + ") = " + newLeft);
                left = newLeft;
            } else {
                System.out.println("  -> '" + c + "' has not been seen in the current window.");
            }

            // Update last seen index of c
            lastIndex.put(c, right);
            System.out.println("  -> lastIndex map after update: " + lastIndex);

            int windowLen = right - left + 1;
            System.out.println("  -> New window: [" + left + ", " + right + "]");
            System.out.println("  -> New substring: \"" + s.substring(left, right + 1) + "\"");
            System.out.println("  -> Window length = " + windowLen);

            int newMaxLen = Math.max(maxLen, windowLen);
            if (newMaxLen != maxLen) {
                System.out.println("  -> maxLen updated: max(" + maxLen + ", " + windowLen + ") = " + newMaxLen);
            } else {
                System.out.println("  -> maxLen stays the same: " + maxLen);
            }
            maxLen = newMaxLen;

            System.out.println("----------------------------------------");
        }

        System.out.println("Final maxLen = " + maxLen);
        return maxLen;
    }

    int lengthOfLongestSubStringShorten(String S) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0;
        int right = 0;
        int maxLength = 0;

        System.out.println("Character array aka String: " + S);

        for (right = 0; right < S.length(); right++) {
            char letter = S.charAt(right);        //charAt input arguments accepts index value, returns char at that index

            if (lastIndex.containsKey(letter)) {
                int previousIndex = lastIndex.get(letter);
                int newLeft = Math.max(left, previousIndex + 1);
                left = newLeft;
            } else {
                System.out.println("new unique letter to concat to substring unique characters");
            }

            lastIndex.put(letter, right);
            int windowLength = right - left + 1; // Since index is at zero and length is one minimum
            int newMaxLength = Math.max(maxLength, windowLength);
            maxLength = newMaxLength;

            String subString = S.substring(left, right + 1);
            System.out.println("Max Substring Length: " + subString);
        }
        return maxLength;
    }

    int maxSubArray(int[] nums) {
        int maxSoFar = nums[0];
        int curr = nums[0];

        for (int i = 1; i < nums.length; i++) {
            curr = Math.max(nums[i], curr + nums[i]);
            maxSoFar = Math.max(maxSoFar, curr);
        }
        return maxSoFar;
    }


    int maxSubArrayExplained(int[] nums) {
        System.out.println("Input: " + java.util.Arrays.toString(nums));
        System.out.println("======================================");

        // 1) Brute-force: list ALL contiguous subarrays and their sums
        System.out.println("All contiguous subarrays:");
        for (int start = 0; start < nums.length; start++) {
            int sum = 0;
            StringBuilder sb = new StringBuilder("[");
            for (int end = start; end < nums.length; end++) {
                if (end > start) sb.append(", ");
                sb.append(nums[end]);
                sum += nums[end];

                System.out.println("  " + sb.toString() + "] = " + sum);
            }
        }
        System.out.println("======================================");
        System.out.println("Now running Kadane's algorithm step-by-step:");
        System.out.println("--------------------------------------");

        // 2) Kadane's algorithm with debug prints
        int maxSoFar = nums[0];
        int curr = nums[0];

        // start index of the current subarray that curr represents
        int currStart = 0;

        System.out.println("Initial state:");
        System.out.println("  i = 0");
        System.out.println("  curr subarray = [" + nums[0] + "]");
        System.out.println("  curr = nums[0] = " + curr);
        System.out.println("  maxSoFar = " + maxSoFar);
        System.out.println("--------------------------------------");

        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];

            System.out.println("Step " + i + ":");
            System.out.println("  i = " + i);
            System.out.println("  num = nums[" + i + "] = " + num);
            System.out.println("  curr (previous sum) = " + curr);

            // ----- Build Option 1 subarray: [num] -----
            String option1Sub = "[" + num + "]";
            int option1Sum = num;

            // ----- Build Option 2 subarray: extend from currStart..i -----
            StringBuilder sbOption2 = new StringBuilder("[");
            int option2Sum = 0;
            for (int k = currStart; k <= i; k++) {
                if (k > currStart) sbOption2.append(", ");
                sbOption2.append(nums[k]);
                option2Sum += nums[k];
            }
            sbOption2.append("]");
            String option2Sub = sbOption2.toString();

            System.out.println("  Option 1: start new subarray at i");
            System.out.println("    " + option1Sub + " = " + option1Sum);

            System.out.println("  Option 2: extend previous subarray");
            System.out.println("    " + option2Sub + " = " + option2Sum);

            // ----- Apply Kadane decision -----
            int prevCurr = curr;
            int startNew = option1Sum;       // = num
            int extend   = prevCurr + num;   // should match option2Sum

            curr = Math.max(startNew, extend);

            if (curr == startNew) {
                // we restarted at i
                currStart = i;
                System.out.println("  -> Chose Option 1 (restart): " + option1Sub + " (sum = " + curr + ")");
            } else {
                // we extended previous
                System.out.println("  -> Chose Option 2 (extend):  " + option2Sub + " (sum = " + curr + ")");
            }

            int oldMax = maxSoFar;
            maxSoFar = Math.max(maxSoFar, curr);
            if (maxSoFar != oldMax) {
                System.out.println("  -> maxSoFar updated: max(" + oldMax + ", " + curr + ") = " + maxSoFar);
            } else {
                System.out.println("  -> maxSoFar stays the same: " + maxSoFar);
            }

            System.out.println("--------------------------------------");
        }

        System.out.println("Final answer (maxSubArray sum) = " + maxSoFar);
        System.out.println("======================================");
        return maxSoFar;
    }



    public ListNode reverseList(ListNode head) {
        System.out.print("Original list: ");
        printListInline(head);
        System.out.println("======================================");

        ListNode prev = null;      // The new tail (moves forward)
        ListNode curr = head;      // The node we're currently processing
        int step = 1;

        while (curr != null) {
            System.out.println("Step " + step++ + ":");
            System.out.println("  Before reversing:");
            System.out.println("    prev = " + (prev == null ? "null" : prev.val));
            System.out.println("    curr = " + (curr == null ? "null" : curr.val));
            System.out.println("    curr.next = " + (curr.next == null ? "null" : String.valueOf(curr.next.val)));

            // Save next before breaking the link
            ListNode next = curr.next;
            System.out.println("  Saved next = " + (next == null ? "null" : next.val));

            // Reverse the pointer
            curr.next = prev;
            System.out.println("  After reversing pointer: curr.next -> " + (curr.next == null ? "null" : curr.next.val));

            // Slide prev and curr forward
            prev = curr;
            curr = next;

            System.out.println("  Move prev to curr node: prev = " + (prev == null ? "null" : prev.val));
            System.out.println("  Move curr to next node: curr = " + (curr == null ? "null" : curr.val));

            System.out.print("  Partial reversed list (prev): ");
            printListInline(prev);
            System.out.print("  Remaining original list (curr): ");
            printListInline(curr);
            System.out.println("--------------------------------------");
        }

        System.out.println("Finished. New head is prev = " + (prev == null ? "null" : prev.val));
        System.out.print("Reversed list: ");
        printListInline(prev);
        System.out.println("======================================");

        // When we're done, prev is the new head
        return prev;
    }

    // Helper: print list on one line
    private void printListInline(ListNode head) {
        ListNode temp = head;
        System.out.print("[");
        while (temp != null) {
            System.out.print(temp.val);
            temp = temp.next;
            if (temp != null) System.out.print(" -> ");
        }
        System.out.println("]");
    }

    List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            System.out.println("Tree is empty. Returning [].");
            return res;
        }

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        System.out.println("Starting level-order traversal (BFS).");
        System.out.println("Initial queue: [" + root.val + "]");
        System.out.println("======================================");

        int levelNum = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();

            System.out.println("Level " + levelNum + ":");
            System.out.println("  Queue size at start of level = " + size);
            System.out.print("  Queue contents at start: [");
            {
                boolean first = true;
                for (TreeNode n : q) {
                    if (!first) System.out.print(", ");
                    System.out.print(n.val);
                    first = false;
                }
            }
            System.out.println("]");

            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                System.out.println("    Processing node " + node.val + " (position " + i + " in this level)");

                // add current node value to this level
                level.add(node.val);
                System.out.println("      -> Added " + node.val + " to current level list");

                // add children to queue
                if (node.left != null) {
                    q.offer(node.left);
                    System.out.println("      -> Enqueued left child: " + node.left.val);
                }
                if (node.right != null) {
                    q.offer(node.right);
                    System.out.println("      -> Enqueued right child: " + node.right.val);
                }
            }

            System.out.println("  Finished Level " + levelNum + " values: " + level);
            res.add(level);
            System.out.println("  Result list so far: " + res);

            System.out.print("  Queue contents after level " + levelNum + ": [");
            {
                boolean first = true;
                for (TreeNode n : q) {
                    if (!first) System.out.print(", ");
                    System.out.print(n.val);
                    first = false;
                }
            }
            System.out.println("]");
            System.out.println("--------------------------------------");

            levelNum++;
        }

        System.out.println("Traversal complete.");
        System.out.println("Final level-order result: " + res);
        System.out.println("======================================");

        return res;
    }

    public static void main(String[] args) {
        BasicAlgorithms demo = new BasicAlgorithms();
        demo.twoSum(new int[]{0, 3, 5, 7, 9}, 8);
        demo.twoSumExplanation(new int[]{0, 3, 5, 7, 9}, 16);
        demo.lengthOfLongestSubstring("zyxzyxwzyxwu");
        demo.lengthOfLongestSubStringShorten("ababcabcdabcde");
        demo.maxSubArrayExplained(new int[]{1, 2, 3, 5, 7, 11, 13});
        demo.maxSubArrayExplained(new int[]{1, -2, 3, -5, 7, -11, 13, 17});
        ListNode n3 = new ListNode(3);
        ListNode n2 = new ListNode(2, n3);
        ListNode n1 = new ListNode(1, n2);
        demo.reverseList(n1);
        TreeNode root =
                new TreeNode(1,
                        new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                        new TreeNode(3)
                );

        demo.levelOrder(root);

    }
}
