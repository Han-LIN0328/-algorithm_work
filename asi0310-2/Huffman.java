public class Huffman {
    
    // 定義 Huffman 樹節點
    static class Node {
        char ch;
        int freq;
        Node left, right;
        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
    }

    // 自製 Min-Heap (取代 PriorityQueue)
    static class MinHeap {
        Node[] heap;
        int size;
        public MinHeap(int capacity) {
            heap = new Node[capacity];
            size = 0;
        }
        public void insert(Node node) {
            heap[size] = node;
            int current = size++;
            while (current > 0 && heap[current].freq < heap[(current - 1) / 2].freq) {
                Node temp = heap[current];
                heap[current] = heap[(current - 1) / 2];
                heap[(current - 1) / 2] = temp;
                current = (current - 1) / 2;
            }
        }
        public Node extractMin() {
            Node min = heap[0];
            heap[0] = heap[--size];
            heap[size] = null;
            minHeapify(0);
            return min;
        }
        private void minHeapify(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < size && heap[left].freq < heap[smallest].freq) smallest = left;
            if (right < size && heap[right].freq < heap[smallest].freq) smallest = right;
            if (smallest != i) {
                Node temp = heap[i];
                heap[i] = heap[smallest];
                heap[smallest] = temp;
                minHeapify(smallest);
            }
        }
    }

    public static void main(String[] args) {
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F'};
        int[] freqs = {5, 9, 12, 13, 16, 45};
        
        long startTime = System.nanoTime();

        MinHeap minHeap = new MinHeap(chars.length);
        for (int i = 0; i < chars.length; i++) {
            minHeap.insert(new Node(chars[i], freqs[i], null, null));
        }

        while (minHeap.size > 1) {
            Node left = minHeap.extractMin();
            Node right = minHeap.extractMin();
            Node sumNode = new Node('-', left.freq + right.freq, left, right);
            minHeap.insert(sumNode);
        }

        Node root = minHeap.extractMin();
        
        long endTime = System.nanoTime();

        System.out.println("Huffman Codes:");
        printCodes(root, "");
        System.out.println("\nTime Complexity: O(n log n)");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");
    }

    // 走訪樹印出編碼 (左=0, 右=1)
    private static void printCodes(Node root, String code) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            System.out.println(root.ch + ": " + code);
            return;
        }
        printCodes(root.left, code + "0");
        printCodes(root.right, code + "1");
    }
}