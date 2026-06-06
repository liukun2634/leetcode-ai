# LeetCode 146. LRU 缓存 (LRU Cache)

> 难度：Medium　|　标签：设计、哈希表、双向链表　|	|	**设计题必考 ⭐⭐⭐⭐**

---

## 一、题目

请你设计并实现一个满足 **LRU（最近最少使用）缓存** 约束的数据结构 `LRUCache`。

- `LRUCache(int capacity)` 以正整数容量初始化 LRU 缓存
- `int get(int key)` 如果 key 存在则返回值，否则返回 `-1`
- `void put(int key, int value)` 若 key 已存在则更新值；否则插入。若插入导致容量超出，**逐出最久未使用** 的关键字。

**进阶**：`get` 和 `put` 都必须 **O(1)** 平均时间复杂度。

**约束**

- `1 <= capacity <= 3000`
- `0 <= key <= 10^4`
- 调用次数 `<= 2 * 10^5`

---

## 二、解题思路（学习重点）

### 1. 为什么需要哈希表 + 双向链表？

| 操作 | 单独用哈希 | 单独用链表 | 哈希 + 双向链表 |
|---|---|---|---|
| 按 key 找节点 | O(1) | O(n) | O(1) |
| 维护"最近使用"顺序 | 做不到 | O(1) 移动节点 | O(1) |
| 删除某节点 | — | O(n) 找它 | **O(1)** |

**结论**：哈希表负责"O(1) 找节点"，双向链表负责"O(1) 移到头部 / 删除尾部"。

> **学习点 ①**：**"既要按 key 找，又要按顺序逐出"** → 经典 HashMap + 双向链表组合。其他例子：LFU、`OrderedDict`、`LinkedHashMap`。

### 2. 数据结构设计

- **节点**：`Node { int key; int val; Node prev; Node next; }`
  - 为什么节点要存 key？→ 从链表尾部逐出时要顺便从哈希表里 `remove(key)`
- **双向链表**：用 **dummy head + dummy tail** 两个哨兵，让任何位置的插入/删除都不用判 null
  - 约定：**头部 = 最近使用**，**尾部 = 最久未使用**
- **HashMap**：`Map<Integer, Node>` 直接拿到节点引用

### 3. 三个核心私有操作

```text
addToHead(node): 把 node 插入 head 之后
removeNode(node): 从链表中删除 node（直接操作 prev/next）
moveToHead(node): removeNode(node) + addToHead(node)
removeTail(): 取 tail.prev 节点并删除，返回它（用于逐出）
```

### 4. get / put 的逻辑

`get(key)`：
- 不存在 → 返回 -1
- 存在 → `moveToHead(node)`，返回 `node.val`

`put(key, value)`：
- 已存在 → 更新 val 并 `moveToHead`
- 不存在 → 新建节点 → `addToHead` + map.put；若 size > capacity → `removeTail()` 并 `map.remove(tail.key)`

### 5. Java 偷懒法：直接继承 `LinkedHashMap`

`LinkedHashMap` 自带 access-order 模式（按访问顺序）+ `removeEldestEntry` 钩子，**5 行代码就能 AC**。但面试**多半要手写双向链表**，所以两种都得会。

### 6. 容易踩的坑

| 坑 | 处理 |
|---|---|
| 没用哨兵 head/tail → 边界判断爆炸 | 必须两端各放一个 dummy |
| 节点只存 val 不存 key → 逐出时哈希表删除不了 | 节点必须存 key |
| `put` 更新已有 key 时忘记 `moveToHead` | 算法错 |
| 用 `LinkedList<Integer>` + `HashMap` → remove 是 O(n) | 必须自己写双向链表 |

---

## 三、详细解题步骤

**步骤 1**：定义内部节点类
```java
class Node {
    int key, val;
    Node prev, next;
    Node(int k, int v) { key = k; val = v; }
}
```

**步骤 2**：成员变量
```java
private final int capacity;
private final Map<Integer, Node> map = new HashMap<>();
private final Node head = new Node(0, 0);   // dummy
private final Node tail = new Node(0, 0);   // dummy
```

**步骤 3**：构造器 + 初始化哨兵互连
```java
public LRUCache(int capacity) {
    this.capacity = capacity;
    head.next = tail;
    tail.prev = head;
}
```

**步骤 4**：私有操作（4 个）
```java
private void addToHead(Node node) {
    node.prev = head;
    node.next = head.next;
    head.next.prev = node;
    head.next = node;
}
private void removeNode(Node node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
}
private void moveToHead(Node node) {
    removeNode(node);
    addToHead(node);
}
private Node removeTail() {
    Node last = tail.prev;
    removeNode(last);
    return last;
}
```

**步骤 5**：实现 `get`
```java
public int get(int key) {
    Node n = map.get(key);
    if (n == null) return -1;
    moveToHead(n);
    return n.val;
}
```

**步骤 6**：实现 `put`
```java
public void put(int key, int value) {
    Node n = map.get(key);
    if (n != null) {
        n.val = value;
        moveToHead(n);
        return;
    }
    Node node = new Node(key, value);
    map.put(key, node);
    addToHead(node);
    if (map.size() > capacity) {
        Node removed = removeTail();
        map.remove(removed.key);
    }
}
```

---

## 四、Java 题解

### 解法 A：手写双向链表 + HashMap（面试必背）

```java
class LRUCache {
    private class Node {
        int key, val;
        Node prev, next;
        Node(int k, int v) { key = k; val = v; }
    }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(0, 0), tail = new Node(0, 0);

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail; tail.prev = head;
    }

    public int get(int key) {
        Node n = map.get(key);
        if (n == null) return -1;
        moveToHead(n);
        return n.val;
    }

    public void put(int key, int value) {
        Node n = map.get(key);
        if (n != null) {
            n.val = value;
            moveToHead(n);
            return;
        }
        Node node = new Node(key, value);
        map.put(key, node);
        addToHead(node);
        if (map.size() > capacity) {
            Node removed = removeTail();
            map.remove(removed.key);
        }
    }

    private void addToHead(Node x) {
        x.prev = head; x.next = head.next;
        head.next.prev = x; head.next = x;
    }
    private void removeNode(Node x) {
        x.prev.next = x.next; x.next.prev = x.prev;
    }
    private void moveToHead(Node x) { removeNode(x); addToHead(x); }
    private Node removeTail() {
        Node last = tail.prev;
        removeNode(last);
        return last;
    }
}
```

**记忆口诀**：
> **"哈希定位 O(1)，双链维序 O(1)；头新尾旧，逐尾时哈希同步删。"**

### 解法 B：LinkedHashMap 偷懒版（应急 / 当亮点）

```java
class LRUCache extends LinkedHashMap<Integer, Integer> {
    private final int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);   // accessOrder = true
        this.capacity = capacity;
    }
    public int get(int key)              { return super.getOrDefault(key, -1); }
    public void put(int key, int value)  { super.put(key, value); }
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) {
        return size() > capacity;
    }
}
```

---

## 五、复杂度

| 操作 | 时间 | 空间 |
|---|---|---|
| `get`/`put` | **O(1)** 摊销 | O(capacity) |

---

## 六、示例验证

```
LRUCache cache = new LRUCache(2);
cache.put(1, 1);    // 链表: head ↔ [1] ↔ tail
cache.put(2, 2);    // 链表: head ↔ [2] ↔ [1] ↔ tail
cache.get(1);       // 返回 1；链表: head ↔ [1] ↔ [2] ↔ tail
cache.put(3, 3);    // 容量超 → 逐 [2]；链表: head ↔ [3] ↔ [1] ↔ tail
cache.get(2);       // 返回 -1（已逐出）
cache.put(4, 4);    // 容量超 → 逐 [1]；链表: head ↔ [4] ↔ [3] ↔ tail
cache.get(1);       // 返回 -1
cache.get(3);       // 返回 3
cache.get(4);       // 返回 4
```

---

## 七、复盘与延伸

### 一句话总结
> **HashMap 给 O(1) 定位，双向链表给 O(1) 移动；头节点最近、尾节点最旧。**

### 自我提问
1. 为什么不用单链表？→ 删除中间节点需要 O(n) 找前驱；双向链表 O(1)。
2. 节点为什么存 key？→ 逐出时要从 HashMap 里同步 `remove(key)`，必须知道 key。
3. 用 `TreeMap`/`OrderedDict` 行吗？→ `OrderedDict`（Python）天然 O(1)；`TreeMap` 是有序 map 不是访问顺序。
4. 怎么改成 **LFU**？→ LC 460，要再加一个"频率桶"链表；难度跃升。

### 同类型推荐（**设计题 + 双向链表家族**）
- LC 460. LFU 缓存（频率桶 + 双向链表，🔴）
- LC 432. 全 O(1) 数据结构
- LC 380. 常数时间插入、删除和获取随机元素（哈希 + 数组）
- LC 295. 数据流的中位数（双堆）
- LC 155. 最小栈（辅助栈）
- LC 706. 设计哈希映射（自己实现 HashMap）
