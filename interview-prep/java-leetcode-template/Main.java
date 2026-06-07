import java.util.*;

/**
 * 默认演示：LeetCode 1. Two Sum。
 * 输入（两行）：
 *   第 1 行：数组，如 [2,7,11,15]
 *   第 2 行：target，如 9
 * 输出：[0, 1]
 *
 * 换题时：
 *   1) 改 Solution.java 的方法签名 + 实现；
 *   2) 改本文件 main() 里的解析 + 调用即可。
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] nums = LeetCodeIO.parseIntArray(sc.nextLine());
        int target = Integer.parseInt(sc.nextLine().trim());
        int[] ans = new Solution().twoSum(nums, target);
        System.out.println(Arrays.toString(ans));
    }
}
