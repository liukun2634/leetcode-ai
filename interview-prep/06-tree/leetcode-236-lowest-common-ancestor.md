# LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor)

> 难度：Medium　|　标签：树、DFS、递归　|　**树递归思维天花板 ⭐⭐⭐**

---

## 一、题目

给定一个二叉树，找到该树中两个指定节点 `p` 和 `q` 的 **最近公共祖先**（LCA）。

> 最近公共祖先：两个节点 `p`、`q` 在树中的最低（深度最大）的那个 **同时拥有它们作为后代** 的节点（一个节点也可以是它自己的祖先）。

**约束**

- 节点数 `[2, 10^5]`，节点值唯一
- `p != q`，`p`、`q` 均存在于树中

**示例**

```
        3
       / \
      5   1
     / \ / \
    6  2 0  8
      / \
     7   4
```

| p | q | 输出 | 说明 |
|---|---|---|---|
| 5 | 1 | 3 | 分别在左右子树 |
| 5 | 4 | 5 | p 本身是 q 的祖先 |

---

## 二、解题思路（学习重点）

### 1. 用 **"递归返回值携带信息"** 的思维

定义函数：
> `lca(node, p, q)` = "在以 `node` 为根的子树里寻找 p 或 q；若找到返回该节点，否则返回 null"

递归三件套：
1. **递归出口**：`node == null` → 返回 null；`node == p` 或 `node == q` → 返回 `node` 本身（**自己是祖先**）。
2. **递归子问题**：在左子树和右子树里分别 `lca`，得到 `left`、`right`。
3. **合并结果**：
   - 若 `left != null && right != null` → 当前 `node` 就是 LCA（p、q 分别在两侧）。
   - 否则返回非空那一侧（两个目标都在那一侧，或都没找到）。

> **学习点 ①**：**"递归返回 = 子树是否含目标 / 哪个目标"**，是树递归的最强模板。Max Path Sum、Validate BST、Diameter 都是同思想。

### 2. 为什么这样能保证是"最近"？

递归从叶子向上回溯，**第一个**满足"左右都非空"的节点必然是最深的公共祖先（更深的节点不可能同时拥有两侧）。

### 3. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 漏掉"node == p 或 node == q 就返回 node"的情形 | 必须写，否则"祖先关系"会漏 |
| 误以为要分别求 p、q 的路径再比较 | 那种做法对，但代码长一倍 |
| 二叉搜索树（BST）特化（LC 235）误用本模板 | BST 有更快的方法：利用值大小比较单边下沉 |

---

## 三、Java 题解

### 解法 A：递归（最常考、必背）

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left  = lowestCommonAncestor(root.left,  p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) return root;   // 分居两侧
        return left != null ? left : right;               // 都在一侧 / 都不在
    }
}
```

**记忆口诀**：
> **"左右都有 → 我是；只有一边 → 返那边。"**

### 解法 B：父指针 + 集合（辅助理解）

1. 一次 BFS/DFS 记录每个节点的父指针。
2. 从 `p` 一路往上把祖先存进 `Set`。
3. 从 `q` 往上走，第一个出现在 `Set` 里的就是 LCA。

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        parent.put(root, null);
        stack.push(root);
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            if (node.left  != null) { parent.put(node.left,  node); stack.push(node.left);  }
            if (node.right != null) { parent.put(node.right, node); stack.push(node.right); }
        }
        Set<TreeNode> ancestors = new HashSet<>();
        for (TreeNode x = p; x != null; x = parent.get(x)) ancestors.add(x);
        for (TreeNode y = q; y != null; y = parent.get(y)) if (ancestors.add(y) == false) return y;
        return null;
    }
}
```

> 解法 B 更"直觉"，但代码长。**面试默认写解法 A。**

---

## 四、复杂度

| 解法 | 时间 | 空间 |
|---|---|---|
| 递归 | **O(n)** | O(h) 递归栈，h 为高度 |
| 父指针 + 集合 | O(n) | O(n) |

---

## 五、示例验证

`p = 5, q = 4`（树见题目）

- `lca(3)`：left = `lca(5)`，right = `lca(1)`
  - `lca(5)`：**自己是 p** → 返回 5
  - `lca(1)`：左右各递归，最终找不到 4 在 1 子树？错，4 在 5 子树。结构看清楚：4 在 5 的右子树。
    - 重新算：right 分支是 1 的子树（0,8），找不到 4 也找不到 5 → 返回 null
- 在 `3`：left = 5, right = null → **返回 left = 5** ✅

`p = 5, q = 1`

- `lca(3)`：left = `lca(5)` = 5；right = `lca(1)` = 1。
- 左右都非空 → **返回 3** ✅

---

## 六、复盘与延伸

### 一句话总结
> **递归返回"我子树里找到了谁"；若我自己是 p 或 q 就立刻报告；左右都报告非空，我就是 LCA。**

### 自我提问
1. 为什么 `node == p` 时不需要继续往下找 q？→ 题目保证 p、q 都存在，若 q 也在 p 子树里，最终 LCA 也是 p（祖先关系），返回 p 自身正确。
2. 这套递归框架还能解什么？→ "子树是否平衡（LC 110）"、"二叉树直径（LC 543）"、"路径最大和（LC 124）" —— 都是 **"返回携带子树信息 + 在当前节点更新答案"**。
3. BST 怎么更快？→ 利用值大小：若 `p,q` 都比 `root` 小走左；都大走右；否则 `root` 就是 LCA。O(h) 但常数小。
4. 多次查询？→ 离线 Tarjan / 倍增 / 欧拉序 + RMQ 可做 O(1) 查询，竞赛常考；面试少见。

### 同类型推荐（**树递归家族**）
- LC 235. BST 的 LCA（利用 BST 性质）
- LC 1644. LCA II（节点可能不存在）
- LC 1650. LCA III（节点带父指针）
- LC 124. 二叉树最大路径和（**最强递归模板题**）
- LC 543. 二叉树的直径
- LC 110. 平衡二叉树
- LC 297. 二叉树的序列化与反序列化
