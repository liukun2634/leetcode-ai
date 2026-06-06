# LeetCode 53. 最大子数组和 (Maximum Subarray)

> 难度：Medium　|　标签：数组、动态规划、贪心、分治　|　**DP 入门必刷 ⭐**

---

## 一、题目

给你一个整数数组 `nums`，请找出一个具有 **最大和** 的 **连续子数组**（子数组最少包含一个元素），返回其最大和。

**约束**

- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

**示例**

| 输入 | 输出 | 子数组 |
|---|---|---|
| `[-2,1,-3,4,-1,2,1,-5,4]` | `6` | `[4,-1,2,1]` |
| `[1]` | `1` | `[1]` |
| `[5,4,-1,7,8]` | `23` | 全数组 |

---

## 二、解题思路（学习重点）

### 1. **Kadane 算法**：DP 的最经典入门

定义 `f(i) = 以 nums[i] 结尾 的最大子数组和`。

转移：
$$f(i) = \max(f(i-1) + nums[i],\; nums[i])$$

含义：要么 **接着前面的扩展**，要么 **从 i 重新开始**（前面的负贡献就丢掉）。

最终答案：`max(f(0), f(1), ..., f(n-1))`。

> **学习点 ①**：DP 状态定义要"**以 i 结尾**"而非"前 i 个的最大"，**才能保证连续性**。

### 2. 空间优化到 O(1)

`f(i)` 只依赖 `f(i-1)`，用一个变量滚动即可。

### 3. 贪心理解（同一段代码的另一种说法）

维护当前累加和 `cur`：若 `cur < 0`，对未来一定是负贡献，**清零重启**。

### 4. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 全负数组（如 `[-3,-1,-2]`）误返回 0 | 答案初始化为 `Integer.MIN_VALUE` 或 `nums[0]`，**不能** 是 0 |
| 用滑动窗口正负判断 | 元素含负数时窗口不单调，**滑动窗口失效**，必须 DP |

---

## 三、Java 题解

### 解法 A：Kadane O(1) 空间（推荐）

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int cur = nums[0], best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(cur + nums[i], nums[i]); // 接 or 重启
            best = Math.max(best, cur);
        }
        return best;
    }
}
```

**记忆口诀**：
> **"接着走，还是重开？取大者；全程最大记 best。"**

### 解法 B：分治（O(n log n)，面试加分）

把数组对半分，最大子数组要么在左、要么在右、要么 **横跨中点**。横跨情况从中点向两边各做一次贪心扩展。

```java
class Solution {
    public int maxSubArray(int[] nums) {
        return divide(nums, 0, nums.length - 1);
    }
    private int divide(int[] a, int l, int r) {
        if (l == r) return a[l];
        int m = (l + r) >>> 1;
        int leftMax = divide(a, l, m);
        int rightMax = divide(a, m + 1, r);

        // 横跨中点：从 m 向左、从 m+1 向右各取最大前缀
        int sum = 0, lMax = Integer.MIN_VALUE;
        for (int i = m; i >= l; i--) { sum += a[i]; lMax = Math.max(lMax, sum); }
        sum = 0; int rMax = Integer.MIN_VALUE;
        for (int i = m + 1; i <= r; i++) { sum += a[i]; rMax = Math.max(rMax, sum); }

        return Math.max(Math.max(leftMax, rightMax), lMax + rMax);
    }
}
```

> 分治版是 **「线段树」** 解决区间最大子段和的雏形（支持单点修改 + 区间查询）。

---

## 四、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| Kadane | **O(n)** | **O(1)** |
| 分治 | O(n log n) | O(log n) 递归栈 |

---

## 五、示例验证

`nums = [-2,1,-3,4,-1,2,1,-5,4]`

| i | nums[i] | cur (接 vs 重开) | best |
|---|---|---|---|
| 0 | -2 | -2 | -2 |
| 1 | 1  | max(-2+1, 1)=1 | 1 |
| 2 | -3 | max(1-3, -3)=-2 | 1 |
| 3 | 4  | max(-2+4, 4)=4 | 4 |
| 4 | -1 | max(4-1, -1)=3 | 4 |
| 5 | 2  | max(3+2, 2)=5 | 5 |
| 6 | 1  | max(5+1, 1)=6 | **6** |
| 7 | -5 | max(6-5, -5)=1 | 6 |
| 8 | 4  | max(1+4, 4)=5 | 6 |

输出 `6` ✅

---

## 六、复盘与延伸

### 一句话总结
> **以 i 结尾的最大子数组：要么把 i 接上前面，要么从 i 重新开始。**

### 自我提问
1. 为什么状态要"以 i 结尾"？→ 这样保证子数组连续；如果定义"前 i 个的最大"，无法表达"不取 i" vs "取 i" 的连续性。
2. 全负数组怎么办？→ 初始化 `best = nums[0]`（不能是 0），算法天然正确。
3. 数据流场景？→ Kadane 天然支持，每来一个 `x`，`cur = max(cur+x, x); best = max(best, cur);`。
4. 如何返回最大子数组的 **下标区间** ？→ 重启时记录 `start = i`，更新 `best` 时记录 `[start, i]`。

### 同类型推荐（最大子段和家族）
- LC 152. 乘积最大子数组（同时维护 max/min）
- LC 918. 环形子数组的最大和（断环：`max子段和` vs `total - min子段和`）
- LC 1186. 删除一次得到子数组最大和（双状态 DP）
- LC 363. 矩形区域不超过 K 的最大数值和（前缀和 + 有序集合）
- LC 209. 长度最小的子数组（同思路但目标改为和 ≥ s）
