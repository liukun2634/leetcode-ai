# 05 · 自定义节点 · `ListNode` & `TreeNode`

> LeetCode 默认给的链表 / 二叉树节点定义都长这样。本文档汇总：
> 1. 标准定义
> 2. 常用辅助函数（构造、打印、反转、找中点……）
> 3. 易错点

---

## 一、`ListNode` —— 单链表

### 标准定义

```java
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```

### 万能模板：dummy 头

```java
ListNode dummy = new ListNode(0);
ListNode cur = dummy;
// ... cur.next = ...; cur = cur.next;
return dummy.next;
```

> **任何"返回头节点"的题都先建 dummy**，避免头节点特判。

### 高频辅助函数

```java
// 1) 数组 → 链表
public static ListNode fromArray(int[] a) {
    ListNode dummy = new ListNode(0), cur = dummy;
    for (int x : a) { cur.next = new ListNode(x); cur = cur.next; }
    return dummy.next;
}

// 2) 链表 → 打印
public static void print(ListNode head) {
    StringBuilder sb = new StringBuilder("[");
    while (head != null) {
        sb.append(head.val);
        if (head.next != null) sb.append(", ");
        head = head.next;
    }
    sb.append("]");
    System.out.println(sb);
}

// 3) 反转
public static ListNode reverse(ListNode head) {
    ListNode prev = null, cur = head;
    while (cur != null) {
        ListNode nxt = cur.next;
        cur.next = prev;
        prev = cur;
        cur = nxt;
    }
    return prev;
}

// 4) 找中点（快慢指针，偶数返回左中）
public static ListNode middle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast.next != null && fast.next.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    return slow;
}

// 5) 检测环（Floyd）
public static boolean hasCycle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) return true;
    }
    return false;
}
```

### 易错点

- **判空顺序**：`while (fast != null && fast.next != null)`，反过来 NPE。
- **断链**：拼接前要先把 `next` 存下来，再改 `next`。
- **dummy 没保存**：用 `cur` 遍历，**不能动 `dummy`**，否则丢头。

---

## 二、`TreeNode` —— 二叉树

### 标准定义

```java
public class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val; this.left = left; this.right = right;
    }
}
```

### 高频辅助函数

```java
// 1) 层序数组 → 二叉树（LeetCode 输入格式 [1,2,3,null,4]）
public static TreeNode fromLevelOrder(Integer[] arr) {
    if (arr.length == 0 || arr[0] == null) return null;
    TreeNode root = new TreeNode(arr[0]);
    Deque<TreeNode> q = new ArrayDeque<>();
    q.offer(root);
    int i = 1;
    while (!q.isEmpty() && i < arr.length) {
        TreeNode node = q.poll();
        if (i < arr.length && arr[i] != null) {
            node.left = new TreeNode(arr[i]);
            q.offer(node.left);
        }
        i++;
        if (i < arr.length && arr[i] != null) {
            node.right = new TreeNode(arr[i]);
            q.offer(node.right);
        }
        i++;
    }
    return root;
}

// 2) 层序打印
public static void printLevelOrder(TreeNode root) {
    if (root == null) { System.out.println("[]"); return; }
    List<String> res = new ArrayList<>();
    Deque<TreeNode> q = new ArrayDeque<>();
    q.offer(root);
    while (!q.isEmpty()) {
        TreeNode n = q.poll();
        if (n == null) { res.add("null"); continue; }
        res.add(String.valueOf(n.val));
        q.offer(n.left); q.offer(n.right);
    }
    while (!res.isEmpty() && res.get(res.size()-1).equals("null"))
        res.remove(res.size()-1);
    System.out.println(res);
}
```

### 三种遍历（递归）

```java
void preorder (TreeNode r) { if (r==null) return; visit(r); preorder(r.left); preorder(r.right); }
void inorder  (TreeNode r) { if (r==null) return; inorder(r.left);  visit(r); inorder(r.right);  }
void postorder(TreeNode r) { if (r==null) return; postorder(r.left); postorder(r.right); visit(r); }
```

### 三种遍历（迭代 · 用 `Deque` 当栈）

```java
// 前序：root → left → right
List<Integer> preorderIter(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    if (root == null) return res;
    Deque<TreeNode> st = new ArrayDeque<>();
    st.push(root);
    while (!st.isEmpty()) {
        TreeNode n = st.pop();
        res.add(n.val);
        if (n.right != null) st.push(n.right);   // 先压右，后压左
        if (n.left  != null) st.push(n.left);
    }
    return res;
}

// 中序：left → root → right
List<Integer> inorderIter(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    Deque<TreeNode> st = new ArrayDeque<>();
    TreeNode cur = root;
    while (cur != null || !st.isEmpty()) {
        while (cur != null) { st.push(cur); cur = cur.left; }
        cur = st.pop();
        res.add(cur.val);
        cur = cur.right;
    }
    return res;
}
```

### BFS / 层序遍历

```java
List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) return res;
    Deque<TreeNode> q = new ArrayDeque<>();
    q.offer(root);
    while (!q.isEmpty()) {
        int size = q.size();         // 关键：快照当前层大小
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode n = q.poll();
            level.add(n.val);
            if (n.left  != null) q.offer(n.left);
            if (n.right != null) q.offer(n.right);
        }
        res.add(level);
    }
    return res;
}
```

> **BFS 分层口诀**：「**外循环看队列空，内循环看当前 size**」。

---

## 三、用 `int[]` 当 `Pair` —— 比新建类更快

Java 没有内建的 `Pair`，LeetCode 上用 `int[]` 就够了：

```java
// 把 (x, y) 当一对存进堆 / 队列
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
pq.offer(new int[]{cost, node});

while (!pq.isEmpty()) {
    int[] cur = pq.poll();
    int cost = cur[0], node = cur[1];
}
```

> 比 `new Pair<>(...)` / `Map.Entry` 更轻。

如果非要类，简洁写法：

```java
record Pair(int a, int b) {}      // Java 14+ record
Pair p = new Pair(1, 2);
p.a(); p.b();
```

---

## 四、回顾自测

1. 写一个空头链表用 dummy 节点的 3 行模板。
2. `while (fast != null && fast.next != null)` 和 `while (fast.next != null && fast != null)` 区别？
3. 二叉树前序的迭代版，为什么压栈时要"先压右后压左"？
4. BFS 分层时，记录"当前层大小"的代码写在哪一步？
5. LeetCode 上 `(cost, node)` 这种二元组应该用什么存？

<details>
<summary>答案</summary>

1.
```java
ListNode dummy = new ListNode(0), cur = dummy;
// ... cur.next = new ListNode(x); cur = cur.next;
return dummy.next;
```
2. 后者当 `fast == null` 时先访问 `fast.next` → NPE。`&&` 短路要先判空。
3. 栈是 LIFO，先压右后压左 → 左先弹出 → 实现"先访问左"。
4. `while (!q.isEmpty())` 进入后**第一行** `int size = q.size();`，固化该层节点数。
5. `int[]`（最快、最省内存）。

</details>
