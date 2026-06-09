# LeetCode 面试备考路线图 · Step 2（进阶刷题）

> **承接 [`interview-prep/`](../interview-prep/README.md)** —— step-1 解决了 10 大分类的 **核心模板 + 高频入门题（33 题）**。
> step-2 在每个分类下增加 **更难、更进阶、更有变种考点** 的高频题，把每个分类从「会做」推到「熟练 + 能讲 follow-up」。
>
> **使用建议**：先在 step-1 里把对应分类的模板掌握扎实，再来 step-2 进阶。

---

## 一、刷题策略

| 阶段 | 建议节奏 | 验收 |
|---|---|---|
| **Step-1 巩固** | 优先把 [step-1](../interview-prep/README.md) 的 33 题刷到能 20 min bug-free | 模板背熟 |
| **Step-2 进阶** | 每天 2~3 题，先看思路再写代码 | 能口述 follow-up，能识别题型 |
| **混合模拟** | 从 step-1 + step-2 共 58 题里随机抽 5 题限时刷 | 25 min 内出解 |

---

## 二、十大分类 · 进阶题清单

### 1) 数组 & 哈希
- ✅ [11. Container With Most Water](./01-array-hash/leetcode-11-container-with-most-water.md) 🟡 —— 双指针经典姊妹题
- ✅ [41. First Missing Positive](./01-array-hash/leetcode-41-first-missing-positive.md) 🔴 —— 原地哈希

### 2) 双指针 & 滑动窗口
- ✅ [209. Minimum Size Subarray Sum](./02-sliding-window/leetcode-209-minimum-size-subarray-sum.md) 🟡 —— 变长窗求最短
- ✅ [438. Find All Anagrams in a String](./02-sliding-window/leetcode-438-find-all-anagrams.md) 🟡 —— 固定窗 + 计数

### 3) 链表
- ✅ [23. Merge K Sorted Lists](./03-linked-list/leetcode-23-merge-k-sorted-lists.md) 🔴 —— K 路合并 + 堆
- ✅ [25. Reverse Nodes in K-Group](./03-linked-list/leetcode-25-reverse-nodes-in-k-group.md) 🔴 —— 反转链表的进阶组合

### 4) 栈 & 队列
- ✅ [155. Min Stack](./04-stack-queue/leetcode-155-min-stack.md) 🟡 —— 辅助栈设计
- ✅ [739. Daily Temperatures](./04-stack-queue/leetcode-739-daily-temperatures.md) 🟡 —— 单调栈入门

### 5) 二分查找
- ✅ [153. Find Minimum in Rotated Sorted Array](./05-binary-search/leetcode-153-find-min-rotated.md) 🟡 —— 旋转数组对偶题
- ✅ [875. Koko Eating Bananas](./05-binary-search/leetcode-875-koko-eating-bananas.md) 🟡 —— 答案二分模板
- ✅ [4. Median of Two Sorted Arrays](./05-binary-search/leetcode-4-median-two-sorted-arrays.md) 🔴 —— 双数组二分（顶级难题）

### 6) 树
- ✅ [98. Validate Binary Search Tree](./06-tree/leetcode-98-validate-bst.md) 🟡 —— BST 性质 + 中序遍历
- ✅ [199. Binary Tree Right Side View](./06-tree/leetcode-199-right-side-view.md) 🟡 —— BFS / DFS 变种
- ✅ [297. Serialize and Deserialize Binary Tree](./06-tree/leetcode-297-serialize-tree.md) 🔴 —— 树的编码

### 7) 图
- ✅ [210. Course Schedule II](./07-graph/leetcode-210-course-schedule-ii.md) 🟡 —— 拓扑排序返回顺序
- ✅ [547. Number of Provinces](./07-graph/leetcode-547-number-of-provinces.md) 🟡 —— 连通分量（DFS/并查集）
- ✅ [994. Rotting Oranges](./07-graph/leetcode-994-rotting-oranges.md) 🟡 —— 多源 BFS

### 8) 回溯
- ✅ [39. Combination Sum](./08-backtracking/leetcode-39-combination-sum.md) 🟡 —— 元素可重复选
- ✅ [51. N-Queens](./08-backtracking/leetcode-51-n-queens.md) 🔴 —— 回溯剪枝天花板

### 9) 动态规划
- ✅ [55. Jump Game](./09-dp/leetcode-55-jump-game.md) 🟡 —— 贪心 / DP 两种角度
- ✅ [70. Climbing Stairs](./09-dp/leetcode-70-climbing-stairs.md) 🟢 —— DP 入门第一题
- ✅ [122. Best Time to Buy and Sell Stock II](./09-dp/leetcode-122-best-time-stock-ii.md) 🟡 —— 多次交易（贪心 + 状态机）
- ✅ [1143. Longest Common Subsequence](./09-dp/leetcode-1143-longest-common-subsequence.md) 🟡 —— LCS 模板

### 10) 堆 / 设计
- ✅ [208. Implement Trie (Prefix Tree)](./10-heap/leetcode-208-implement-trie.md) 🟡 —— 前缀树
- ✅ [347. Top K Frequent Elements](./10-heap/leetcode-347-top-k-frequent.md) 🟡 —— 堆 + 哈希 / 桶排序
- ✅ [460. LFU Cache](./10-heap/leetcode-460-lfu-cache.md) 🔴 —— LRU 升级版（双哈希 + 双向链表）

---

## 三、与 step-1 的关系速查

| 类型 | step-1（基础 / 入门）| step-2（进阶 / 变种）|
|---|---|---|
| 双指针 | 1, 15 | 11, 41 |
| 滑动窗口 | 3, 76 | 209, 438 |
| 链表反转 | 206 | 25 |
| K 路合并 | 21（两路）| 23（K 路）|
| 单调栈 | 84, 42 | 739 |
| 旋转数组二分 | 33（搜索）| 153（找最小）|
| BST | — | 98 |
| 树 BFS | 102 | 199 |
| 树编码 | 236（LCA） | 297（序列化） |
| 拓扑排序 | 207（判环）| 210（求顺序）|
| 网格 BFS | 200 | 994 |
| 并查集 | — | 547 |
| 回溯组合 | 78, 46 | 39, 51 |
| 序列 DP | 53, 198 | 70 |
| 双串 DP | 72 | 1143 |
| 股票 | 121 | 122 |
| 堆 TopK | 215 | 347 |
| 设计 | 146（LRU） | 460（LFU）, 208（Trie） |

---

## 四、还可继续补充的题（step-3 候选）

- **图论进阶**：Dijkstra（743）、Bellman-Ford（787）、Tarjan SCC（1192）、最小生成树（1135）
- **DP 进阶**：背包变种（416, 494, 474）、状态压缩 DP（1494, 698）、区间 DP（312, 1547）
- **字符串**：KMP（28）、Z 函数、字符串哈希、Manacher（5）
- **数据结构**：线段树、树状数组（307, 308）、跳表（1206）
- **数学/位运算**：快速幂（50）、矩阵快速幂、组合数取模、位 DP

如需 step-3 路线，请告诉我重点方向。
