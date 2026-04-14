// Assignment 2: Rooted Tree, Binary Tree
class TreeNode {
    String key;
    TreeNode left, right;

    public TreeNode(String item) {
        key = item;
        left = right = null;
    }
}

public class Assignment2_Tree {
    TreeNode root;

    public Assignment2_Tree() {
        root = null;
    }

    public static void main(String[] args) {
        Assignment2_Tree tree = new Assignment2_Tree();
        
        // 依照講義建立二元樹結構：A 為 root [cite: 183, 188]
        tree.root = new TreeNode("A");
        
        // A 的子節點為 B, C [cite: 195, 226]
        tree.root.left = new TreeNode("B");
        tree.root.right = new TreeNode("C");
        
        // B 的子節點為 D, E [cite: 229, 238]
        tree.root.left.left = new TreeNode("D");
        tree.root.left.right = new TreeNode("E");
        
        // C 的子節點為 F, G [cite: 238]
        tree.root.right.left = new TreeNode("F");
        tree.root.right.right = new TreeNode("G");

        System.out.println("成功建立二元樹結構！(Root is " + tree.root.key + ")");
    }
}