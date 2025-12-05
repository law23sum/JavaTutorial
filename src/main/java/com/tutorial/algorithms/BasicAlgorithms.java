package com.tutorial.algorithms;

import java.util.HashMap;
import java.util.Map;

public class BasicAlgorithms {

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
        Map<Character,Integer> lastIndex = new HashMap<>();
        int left = 0;
        int right = 0;
        int maxLength = 0;

        System.out.println("Character array aka String: " + S);

        for(right = 0; right < S.length(); right ++){
            char letter = S.charAt(right);		//charAt input arguments accepts index value, returns char at that index

            if(lastIndex.containsKey(letter)){
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

    public static void main(String[] args){
        BasicAlgorithms demo = new BasicAlgorithms();
        demo.twoSum(new int[]{0, 3, 5, 7, 9}, 8);
        demo.lengthOfLongestSubstring("zyxzyxwzyxwu");
        demo.lengthOfLongestSubStringShorten("ababcabcdabcde");
    }
}
