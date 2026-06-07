/**
 * LeetCode 标准链表节点定义 + 构造/打印工具。
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {}
    public ListNode(int val) { this.val = val; }
    public ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    /** [1,2,3] -> 1 -> 2 -> 3 */
    public static ListNode fromArray(int[] arr) {
        ListNode dummy = new ListNode();
        ListNode tail = dummy;
        for (int v : arr) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    /** 打印为 [1,2,3] 形式。 */
    public static void print(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        while (head != null) {
            if (!first) sb.append(",");
            sb.append(head.val);
            first = false;
            head = head.next;
        }
        sb.append("]");
        System.out.println(sb);
    }
}
