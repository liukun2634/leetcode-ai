# LeetCode 322. 零钱兑换 (Coin Change)

> 难度：Medium　|　标签：动态规划、完全背包　|　**DP 必刷 ⭐⭐⭐**

---

## 一、题目

给你一个整数数组 `coins`（表示不同面额的硬币）和一个整数 `amount`（表示总金额）。

计算并返回可以凑成总金额所需的 **最少的硬币个数**。如果没有任何一种硬币组合能组成总金额，返回 `-1`。

你可以认为每种硬币的数量是 **无限** 的。

**约束**

- `1 <= coins.length <= 12`，`1 <= coins[i] <= 2^31 - 1`
- `0 <= amount <= 10^4`

**示例**

| 输入 | 输出 | 解释 |
|---|---|---|
| `coins=[1,2,5], amount=11` | `3` | 11 = 5+5+1 |
| `coins=[2], amount=3` | `-1` | 无法凑出 |
| `coins=[1], amount=0` | `0` | 不需要硬币 |

---

## 二、解题思路（学习重点）

### 1. 为什么不能贪心？

第一反应可能是"每次选最大的硬币"，但这对 `coins=[1,3,4], amount=6` 失败：
- 贪心：`4 + 1 + 1 = 3` 枚
- 最优：`3 + 3 = 2` 枚

> **学习点 ①**：**硬币面额不能整除时贪心失效**，必须 DP。

### 2. DP 状态定义

`dp[i]` = **凑出金额 `i` 所需的最少硬币数**；不可凑 = `+∞`。

边界：`dp[0] = 0`（金额 0 不需硬币）。

转移：对每个金额 `i`，枚举每种硬币 `c`：
$$dp[i] = \min(dp[i],\; dp[i - c] + 1) \quad \text{若 } i \geq c$$

含义：从 `dp[i-c]` 加一枚 `c` 转移过来。

### 3. 这是 **完全背包 + 求最值** 的模板

- 背包容量：amount
- 物品：每种硬币（无限个）
- 求：恰好装满的最少物品数

外层 **金额**、内层 **硬币** 的循环（也可反过来），求最少所以两层 `for` 都是正向。

### 4. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 用 `Integer.MAX_VALUE` 初始 `dp` → `+1` 溢出 | 用 `amount + 1` 当"不可达"哨兵 |
| 最后忘判"仍是不可达" → 错返 `amount+1` | `return dp[amount] > amount ? -1 : dp[amount];` |
| 用记忆化 DFS 没缓存 | TLE，必须缓存 |

---

## 三、Java 题解

### 解法 A：自底向上 DP（推荐）

```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);   // 哨兵：不可达
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int c : coins) {
                if (c <= i) dp[i] = Math.min(dp[i], dp[i - c] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

**记忆口诀**：
> **"金额从小到大，每个金额试一遍硬币，取最小 + 1。"**

### 解法 B：记忆化 DFS（自顶向下）

```java
class Solution {
    private int[] memo;
    public int coinChange(int[] coins, int amount) {
        memo = new int[amount + 1];
        Arrays.fill(memo, -2);          // -2: 未计算; -1: 不可达
        return dfs(coins, amount);
    }
    private int dfs(int[] coins, int rem) {
        if (rem < 0) return -1;
        if (rem == 0) return 0;
        if (memo[rem] != -2) return memo[rem];
        int best = Integer.MAX_VALUE;
        for (int c : coins) {
            int sub = dfs(coins, rem - c);
            if (sub >= 0) best = Math.min(best, sub + 1);
        }
        return memo[rem] = (best == Integer.MAX_VALUE) ? -1 : best;
    }
}
```

---

## 四、复杂度

| 项 | 复杂度 |
|---|---|
| 时间 | **O(amount · |coins|)** |
| 空间 | O(amount) |

---

## 五、示例验证

`coins = [1,2,5], amount = 11`

| i | 试 1 (dp[i-1]+1) | 试 2 (dp[i-2]+1) | 试 5 (dp[i-5]+1) | dp[i] |
|---|---|---|---|---|
| 0 | — | — | — | 0 |
| 1 | dp[0]+1=1 | — | — | 1 |
| 2 | dp[1]+1=2 | dp[0]+1=1 | — | 1 |
| 3 | dp[2]+1=2 | dp[1]+1=2 | — | 2 |
| 4 | dp[3]+1=3 | dp[2]+1=2 | — | 2 |
| 5 | dp[4]+1=3 | dp[3]+1=3 | dp[0]+1=1 | 1 |
| 6 | 2 | 2 | dp[1]+1=2 | 2 |
| 7 | 2 | 3 | dp[2]+1=2 | 2 |
| 8 | 3 | 3 | dp[3]+1=3 | 3 |
| 9 | 3 | 4 | dp[4]+1=3 | 3 |
| 10 | 4 | 4 | dp[5]+1=2 | 2 |
| 11 | 3 | 3 | dp[6]+1=3 | **3** ✅ |

---

## 六、复盘与延伸

### 一句话总结
> **完全背包求最少件数：`dp[i] = min(dp[i-c] + 1)` 遍历所有硬币。**

### 自我提问
1. 为什么不用贪心？→ 面额不"互整除"时贪心错（如 [1,3,4],amount=6）。
2. 为何金额 i 是从小到大遍历？→ 求"件数最少"的完全背包，正向遍历允许同一硬币多次使用。
3. 怎么改成 **求方案数**（LC 518）？→ 外层 **硬币**、内层 **金额**（避免重复计同一组合）；`dp[i] += dp[i-c]`。
4. 如果硬币面额可以是负数？→ 题目不会出现；若出现要 BFS（最短路） + 防环。

### 同类型推荐（**背包家族**）
**完全背包**：
- LC 322. Coin Change（求最少件数，本题）
- LC 518. Coin Change II（求方案数）
- LC 279. 完全平方数（拆数 = 完全背包）
- LC 139. 单词拆分（字符串完全背包）

**0/1 背包**：
- LC 416. 分割等和子集
- LC 494. 目标和
- LC 474. 一和零

**经典序列 DP**（顺路看）：
- LC 70. 爬楼梯
- LC 198. 打家劫舍
- LC 213. 打家劫舍 II
- LC 337. 打家劫舍 III（树形 DP）
