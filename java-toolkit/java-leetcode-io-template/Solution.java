import java.util.*;

/**
 * 在这里写你要测试的题解。默认示例：LeetCode 1. Two Sum。
 * 切换题目时：把方法签名 + 实现替换掉，记得同步修改 Main.java 的调用。
 */
public class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (idx.containsKey(need)) return new int[]{idx.get(need), i};
            idx.put(nums[i], i);
        }
        return new int[0];
    }
}
