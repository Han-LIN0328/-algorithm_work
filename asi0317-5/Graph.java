public class Graph {
    private final int V;
    private final java.util.LinkedList<Integer>[] adj;

    public Graph(int v) {
        if (v <= 0) {
            throw new IllegalArgumentException("Vertex count must be greater than 0");
        }
        V = v;
        @SuppressWarnings("unchecked")
        java.util.LinkedList<Integer>[] temp = (java.util.LinkedList<Integer>[]) new java.util.LinkedList<?>[v];
        adj = temp;
        for (int i = 0; i < v; ++i) {
            adj[i] = new java.util.LinkedList<>();
        }
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
    }

    // BFS 實作 (使用 Queue)
    public void BFS(int s) {
        validateVertex(s);
        boolean visited[] = new boolean[V];
        java.util.LinkedList<Integer> queue = new java.util.LinkedList<Integer>();

        visited[s] = true;
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            System.out.print(s + " ");

            java.util.Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }

    // DFS 實作 (使用遞迴/Call Stack)
    public void DFSUtil(int v, boolean visited[]) {
        visited[v] = true;
        System.out.print(v + " ");

        java.util.Iterator<Integer> i = adj[v].listIterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n])
                DFSUtil(n, visited);
        }
    }

    public void DFS(int v) {
        validateVertex(v);
        boolean visited[] = new boolean[V];
        DFSUtil(v, visited);
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        System.out.print("BFS from 2: ");
        g.BFS(2);
        System.out.println();

        System.out.print("DFS from 2: ");
        g.DFS(2);
        System.out.println();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IndexOutOfBoundsException("Vertex " + v + " is out of range 0.." + (V - 1));
        }
    }
}