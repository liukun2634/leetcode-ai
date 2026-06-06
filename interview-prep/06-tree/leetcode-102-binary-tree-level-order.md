# LeetCode 102. 二叉树的层序遍历 (Binary Tree Level Order Traversal)

> 难度：Medium　|　标签：树、BFS、队列　|　**BFS 模板题 ⭐⭐⭐**

---

## 一、题目

给你二叉树的根节点 `root`，返回其节点值的 **层序遍历**（即逐层地，从左到右访问所有节点）。

**约束**

- 节点数目范围 `[0, 2000]`
- `-1000 <= Node.val <= 1000`

**示例**

```
输入：    3
        / \
       9  20
          / \
         15  7

输出：[[3], [9,20], [15,7]]
```

---

## 二、解题思路（学习重点）

### 1. BFS 模板：队列 + 当前层 size 快照

直觉做法：用队列层层扩展。但需要"知道当前这一层有几个节点"才能分层 → **每轮开始前记录 `size = queue.size()`**，然后只 `poll size 次`，这一组就是一层。

```text
queue.offer(root)
while !queue.empty():
    size = queue.size()      // 当前层节点数
    for k = 0..size-1:
        node = queue.poll()
        收集 node.val 到本层列表
        把 node.left/right 非空地入队
    把本层列表加入答案
```

> **学习点 ①**：**"size 快照分层"** 是树/图 BFS 的通用技巧，必须熟到肌肉记忆。

### 2. 也可以 DFS（递归 + 层数 depth）

```java
void dfs(TreeNode node, int depth, List<List<Integer>> ans) {
    if (node == null) return;
    if (ans.size() == depth) ans.add(new ArrayList<>()); // 新建一层
    ans.get(depth).add(node.val);
    dfs(node.left, depth + 1, ans);
    dfs(node.right, depth + 1, ans);
}
```

> **学习点 ②**：DFS 也能层序。当题目允许递归且想用更短代码时可以选。但 **BFS 是"层"的更自然表达**。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 在循环中调用 `queue.size()`（变化中） | **必须** 在循环前快照 `size` |
| 没判 `root == null` | 直接返回空 `ans` |
| 用 `LinkedList` 当队列慢一点 | 用 `ArrayDeque` 更快 |

---

## 三、Java 题解

### 解法 A：BFS + size 快照（推荐）

```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) return ans;
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>(size);
            for (int k = 0; k < size; k++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null)  queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            ans.add(level);
        }
        return ans;
    }
}
```

**记忆口诀**：
> **"先 size 快照、再 for 一层、左右入队、收集成层。"**

### 解法 B：DFS 按 depth 落格子

```java
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        dfs(root, 0, ans);
        return ans;
    }
    private void dfs(TreeNode node, int depth, List<List<Integer>> ans) {
        if (node == null) return;
        if (ans.size() == depth) ans.add(new ArrayList<>());
        ans.get(depth).add(node.val);
        dfs(node.left, depth + 1, ans);
        dfs(node.right, depth + 1, ans);
    }
}
```

---

## 四、复杂度

| 项 | 复杂度 |
|---|---|
| 时间 | **O(n)** |
| 空间 | O(n)（队列 / 递归栈最坏） |

---

## 五、示例验证

```
       3
      / \
     9  20
        / \
       15  7
```

| 轮 | size | poll 顺序 | 入队 | level | ans 增量 |
|---|---|---|---|---|---|
| 1 | 1 | 3 | 9, 20 | [3] | [[3]] |
| 2 | 2 | 9 → 20 | 15, 7 | [9,20] | [...,[9,20]] |
| 3 | 2 | 15 → 7 | — | [15,7] | [...,[15,7]] |

输出 `[[3],[9,20],[15,7]]` ✅

---

## 六、复盘与延伸

### 一句话总结
> **BFS 分层 = 队列 + size 快照。每轮处理 size 个就是一层。**

### 自我提问
1. 不快照 size 会怎样？→ 当下一层节点入队后 `queue.size()` 会变大，分层失败。
2. 为什么用 `ArrayDeque` 不用 `LinkedList`？→ `ArrayDeque` 数组实现，缓存友好，操作常数更小。
3. 怎么改成 **锯齿形层序**（LC 103）？→ 用一个 `boolean reverse`，每层取反；或用 `Deque` 双端插入。
4. 怎么自底向上输出（LC 107）？→ 最后 `Collections.reverse(ans)` 即可。

### 同类型推荐（**树 BFS / 层序家族**）
- LC 107. 二叉树的层序遍历 II（自底向上）
- LC 103. 锯齿形层序遍历
- LC 199. 二叉树的右视图（每层取最后一个）
- LC 515. 在每个树行中找最大值
- LC 116/117. 填充每个节点的下一个右侧节点指针
- LC 994. 腐烂的橘子（BFS 多源最短路）
- LC 1162. 地图分析（BFS 距海最远）
