# LeetCode 121. 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)

> 难度：Easy　|　标签：数组、动态规划、贪心　|	|	**贪心入门 ⭐⭐⭐**

---

## 一、题目

给定一个数组 `prices`，它的第 `i` 个元素 `prices[i]` 表示一支给定股票第 `i` 天的价格。

你只能选择 **某一天** 买入这只股票，并选择在 **未来的某一个不同的日子** 卖出该股票。设计一个算法来计算你所能获取的最大利润。

返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 `0`。

**约束**

- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

**示例**

| 输入 | 输出 | 说明 |
|---|---|---|
| `[7,1,5,3,6,4]` | `5` | 买 1 卖 6 |
| `[7,6,4,3,1]` | `0` | 一直跌，不交易 |

---

## 二、解题思路（学习重点）

### 1. 题意翻译

求：`max(prices[j] - prices[i])`，其中 `i < j`。

### 2. 一遍扫描：**维护历史最低买入价**

边走边问"**今天卖出能赚多少？**"：
- 当前价 `prices[i]`
- 历史最低买入价 `minPrice`
- 今日利润 = `prices[i] - minPrice`

全程取最大。同时更新 `minPrice = min(minPrice, prices[i])`。

> **学习点 ①**：**"求最大差 / 双下标关系"** 的经典模板 = **边扫边维护"对面端"的最优值**。
> 同模板：LC 122（多次交易，差分贪心）、LC 309（含冷冻期 DP）、LC 188（最多 k 次交易）。

### 3. DP 视角

`dp[i]` = 前 i 天的最大利润：
$$dp[i] = \max(dp[i-1],\; prices[i] - \min(prices[0..i]))$$

由于只用 `min` 和 `dp[i-1]` → 两个变量足矣。

### 4. 容易踩的坑

| 坑 | 处理 |
|---|---|
| `minPrice` 初始化 `Integer.MAX_VALUE` 后忘了更新顺序 | 必须 **先算利润、再更新 min**（其实顺序无所谓，因为同一天不能买卖；但建议先算利润思路更清晰） |
| 误以为是"区间最大值 − 区间最小值" | 必须满足 `min` 在 `max` 之前出现 |

---

## 三、详细解题步骤

**步骤 1**：初始化
```java
int minPrice = Integer.MAX_VALUE;
int maxProfit = 0;
```

**步骤 2**：遍历每一天 `prices[i]`：
  1. **更新历史最低买入价**：`minPrice = Math.min(minPrice, prices[i]);`
  2. **更新最大利润**：`maxProfit = Math.max(maxProfit, prices[i] - minPrice);`

**步骤 3**：返回 `maxProfit`。

> 注：步骤 2 的两行可以互换。若先算利润再更新 min，今天的利润 = 今天卖 − 之前最低，逻辑也对（同一天买卖利润为 0，不会改变 max）。

---

## 四、Java 题解

### 解法 A：一遍扫描（推荐）

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int p : prices) {
            minPrice = Math.min(minPrice, p);
            maxProfit = Math.max(maxProfit, p - minPrice);
        }
        return maxProfit;
    }
}
```

**记忆口诀**：
> **"扫一遍：刷新历史最低买，今天卖了能赚多少。"**

### 解法 B：DP 状态机（为后续股票系列铺路）

定义 `hold[i]` = 第 i 天持有股票时的最大现金，`cash[i]` = 第 i 天不持有时的最大现金。

```java
class Solution {
    public int maxProfit(int[] prices) {
        int hold = -prices[0], cash = 0;
        for (int i = 1; i < prices.length; i++) {
            hold = Math.max(hold, -prices[i]);        // 只能买一次 → 持有最小成本
            cash = Math.max(cash, hold + prices[i]);  // 卖出
        }
        return cash;
    }
}
```

> 状态机版是 **LC 122 / 309 / 188** 的通用模板。先把本题理解透。

---

## 五、复杂度

| 项 | 复杂度 |
|---|---|
| 时间 | **O(n)** |
| 空间 | **O(1)** |

---

## 六、示例验证

`prices = [7,1,5,3,6,4]`

| i | price | minPrice | maxProfit |
|---|---|---|---|
| 0 | 7 | 7 | 0 |
| 1 | 1 | 1 | 0 |
| 2 | 5 | 1 | 4 |
| 3 | 3 | 1 | 4 |
| 4 | 6 | 1 | **5** |
| 5 | 4 | 1 | 5 |

输出 `5` ✅

---

## 七、复盘与延伸

### 一句话总结
> **维护历史最低买入价，每天问"今天卖能赚多少"，取全程最大。**

### 自我提问
1. 一定要 `i < j` 吗？→ 是；本算法天然保证（minPrice 是当天及之前的）。
2. 与"区间最大 − 区间最小"区别？→ 后者不保证最大在最小之后，可能给出非法方案。
3. 多次交易（LC 122）？→ 贪心：所有正向相邻差都吃下；或状态机 DP。
4. 冷冻期 / 含手续费 / 最多 k 次？→ 用状态机 DP 通用模板。

### 同类型推荐（**股票系列**）
- LC 122. 买卖股票最佳时机 II（多次交易）
- LC 123. 买卖股票最佳时机 III（最多 2 次）
- LC 188. 买卖股票最佳时机 IV（最多 k 次）
- LC 309. 含冷冻期
- LC 714. 含手续费

**"扫描维护对面端最优"模板**：
- LC 11. 盛最多水的容器
- LC 42. 接雨水（双指针 + 前后缀最大）
- LC 53. 最大子数组和（Kadane）
- LC 152. 乘积最大子数组
