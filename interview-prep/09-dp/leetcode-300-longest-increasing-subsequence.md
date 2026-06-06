# LeetCode 300. 最长递增子序列 (Longest Increasing Subsequence)

> 难度：Medium　|　标签：动态规划、二分查找、贪心　|　**LIS 模板 ⭐⭐⭐**

---

## 一、题目

给你一个整数数组 `nums`，找到其中最长 **严格递增子序列** 的长度。

**子序列**：不要求连续，只需保持原相对顺序。

**约束**

- `1 <= nums.length <= 2500`
- `-10^4 <= nums[i] <= 10^4`

**进阶**：能将算法的时间复杂度降低到 **O(n log n)** 吗？

**示例**

| 输入 | 输出 | 一个 LIS |
|---|---|---|
| `[10,9,2,5,3,7,101,18]` | `4` | `[2,3,7,101]` |
| `[0,1,0,3,2,3]` | `4` | `[0,1,2,3]` |
| `[7,7,7,7,7,7,7]` | `1` | `[7]` |

---

## 二、解题思路（学习重点）

### 1. 解法一：O(n²) 经典 DP

`dp[i]` = **以 `nums[i]` 结尾的 LIS 长度**。

转移：对每个 `i`，向前看所有 `j < i`：
$$dp[i] = \max\{dp[j] + 1\;\big|\; j < i,\ nums[j] < nums[i]\} \cup \{1\}$$

答案 = `max(dp)`。

> **学习点 ①**：序列 DP 的状态几乎都是"以 i 结尾"，**这是为了保证子序列连续依赖关系**。

### 2. 解法二：O(n log n) 贪心 + 二分（必背）

维护一个数组 `tails`，**`tails[k]` 表示长度为 `k+1` 的所有递增子序列的"末尾元素的最小值"**。

遍历 `nums`，对每个 `x`：
- 用 **二分查找** 在 `tails` 里找到 **第一个 >= x** 的位置 `pos`：
  - 若 `pos == tails.size()` → `x` 比所有 tails 都大 → **追加** 到 tails 末尾，LIS 长度 +1
  - 否则 → **替换** `tails[pos] = x`（让长度为 pos+1 的子序列末尾更小，给后续元素留出更大空间）

`tails` 的长度即 LIS 长度。

> **学习点 ②**：`tails` 数组在过程中是 **严格递增** 的（这是它本身的不变量），所以可以二分。**注意 `tails` 本身不一定是真正的 LIS**，只是长度对。
>
> **学习点 ③**："找第一个 >= x" 用 `Arrays.binarySearch` 的返回值 `-(insertion_point) - 1` 处理；或自己写 `lowerBound`。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 把 "严格递增" 看成 "非递减" | "严格" 用 `lowerBound`（>= x 替换）；"非递减" 用 `upperBound`（> x 替换） |
| 误以为 tails 就是真正的 LIS | 它只是 **长度** 正确，并非具体序列；要还原 LIS 需要额外记录前驱 |
| O(n²) 内层 j 写成 `for j=i+1`（方向反了） | 应该 `for j in [0, i)` |

---

## 三、Java 题解

### 解法 A：O(n²) DP（入门必背）

```java
class Solution {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length, best = 1;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }
}
```

### 解法 B：O(n log n) 贪心 + 二分（**面试必背**）

```java
class Solution {
    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int pos = lowerBound(tails, 0, size, x);
            tails[pos] = x;
            if (pos == size) size++;
        }
        return size;
    }
    // 返回 [l, r) 内第一个 >= target 的下标
    private int lowerBound(int[] a, int l, int r, int target) {
        while (l < r) {
            int m = (l + r) >>> 1;
            if (a[m] < target) l = m + 1;
            else r = m;
        }
        return l;
    }
}
```

**记忆口诀**：
> **"tails 保最小末尾，二分找 lowerBound，能加就加不能就替。"**

---

## 四、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| DP | O(n²) | O(n) |
| 贪心 + 二分 | **O(n log n)** | O(n) |

---

## 五、示例验证

`nums = [10, 9, 2, 5, 3, 7, 101, 18]`，贪心 + 二分过程：

| 当前 x | tails 操作 | tails |
|---|---|---|
| 10 | 追加 | [10] |
| 9 | 替换 tails[0] | [9] |
| 2 | 替换 tails[0] | [2] |
| 5 | 追加 | [2,5] |
| 3 | 替换 tails[1] | [2,3] |
| 7 | 追加 | [2,3,7] |
| 101 | 追加 | [2,3,7,101] |
| 18 | 替换 tails[3] | [2,3,7,18] |

`size = 4` ✅

---

## 六、复盘与延伸

### 一句话总结
> **维护"长度为 k 的递增子序列的最小末尾"，二分插入更新；最终长度即 LIS。**

### 自我提问
1. tails 是真正的 LIS 吗？→ **不是**，只是长度正确（如示例最后变成 `[2,3,7,18]` 但真实 LIS 含 101）。
2. 为什么严格递增用 `lowerBound`？→ 因为遇到等值时也要替换（不能让相同值并列存在）。
3. 怎么 **还原**一条具体的 LIS？→ O(n²) DP 中记录每个 `i` 的前驱下标；或在 O(n log n) 中记录每个 `x` 加入时所处的"层数"，再从右往左还原。
4. 二维 LIS（信封套娃 LC 354）？→ 一维按宽升序，宽相同时高降序排，再对高跑 LIS。

### 同类型推荐（**LIS 家族**）
- LC 354. 俄罗斯套娃信封问题（二维 LIS）
- LC 673. 最长递增子序列的个数（同时维护 cnt）
- LC 1671. 得到山形数组的最少删除次数（双向 LIS）
- LC 1218. 最长定差子序列（特化为哈希）
- LC 1143. 最长公共子序列（DP 入门姊妹题）
- LC 583. 两个字符串的删除操作（基于 LCS）
- LC 72. 编辑距离（LCS 进阶）
