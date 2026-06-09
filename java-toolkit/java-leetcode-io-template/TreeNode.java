import java.util.*;

/**
 * LeetCode 标准二叉树节点 + 层序构造/打印工具。
 * 输入格式：[1,2,3,null,4,5,null]，与 LeetCode 一致（按层序，null 占位但不展开其子节点）。
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}
    public TreeNode(int val) { this.val = val; }
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val; this.left = left; this.right = right;
    }

    /** 从形如 [1,2,3,null,4] 的字符串构造（与 LeetCode 输入一致）。 */
    public static TreeNode fromLevelOrder(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.startsWith("[")) s = s.substring(1);
        if (s.endsWith("]")) s = s.substring(0, s.length() - 1);
        s = s.trim();
        if (s.isEmpty()) return null;

        String[] tokens = s.split(",");
        for (int i = 0; i < tokens.length; i++) tokens[i] = tokens[i].trim();
        if (tokens[0].equals("null")) return null;

        TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < tokens.length) {
            TreeNode cur = q.poll();
            // 左
            if (i < tokens.length) {
                String t = tokens[i++];
                if (!t.equals("null")) {
                    cur.left = new TreeNode(Integer.parseInt(t));
                    q.offer(cur.left);
                }
            }
            // 右
            if (i < tokens.length) {
                String t = tokens[i++];
                if (!t.equals("null")) {
                    cur.right = new TreeNode(Integer.parseInt(t));
                    q.offer(cur.right);
                }
            }
        }
        return root;
    }

    /** 打印为 [1,2,3,null,4] 层序形式。 */
    public static void printLevelOrder(TreeNode root) {
        if (root == null) { System.out.println("[]"); return; }
        List<String> out = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (cur == null) { out.add("null"); continue; }
            out.add(String.valueOf(cur.val));
            q.offer(cur.left);
            q.offer(cur.right);
        }
        // 去掉末尾多余的 null
        int end = out.size();
        while (end > 0 && out.get(end - 1).equals("null")) end--;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < end; i++) {
            if (i > 0) sb.append(",");
            sb.append(out.get(i));
        }
        sb.append("]");
        System.out.println(sb);
    }
}
