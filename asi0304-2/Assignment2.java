public class Assignment2 {
    static long seed = 54321; // 使用不同的種子

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 50000, 100000}; 
        
        System.out.println("資料量(n)\tInsertion O(n^2)\tBubble O(n^2)\tQuick O(n log n) (單位: 奈秒)");
        
        for (int n : sizes) {
            int[] original = generateRandomArray(n);
            
            // 1. Insertion Sort
            int[] arr1 = copyArray(original);
            long start = System.nanoTime();
            insertionSort(arr1);
            long timeInsertion = System.nanoTime() - start;
            
            // 2. Bubble Sort
            int[] arr2 = copyArray(original);
            start = System.nanoTime();
            bubbleSort(arr2);
            long timeBubble = System.nanoTime() - start;
            
            // 3. Quick Sort
            int[] arr3 = copyArray(original);
            start = System.nanoTime();
            quickSort(arr3, 0, arr3.length - 1);
            long timeQuick = System.nanoTime() - start;
            
            System.out.printf("%d\t\t%d\t\t\t%d\t\t%d\n", 
                n, timeInsertion, timeBubble, timeQuick);
        }
    }

    // --- Insertion Sort ---
    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    // --- Bubble Sort ---
    public static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                }
            }
        }
    }

    // --- Quick Sort ---
    public static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);
            quickSort(a, low, pi - 1);
            quickSort(a, pi + 1, high);
        }
    }

    private static int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (a[j] < pivot) {
                i++;
                int temp = a[i]; a[i] = a[j]; a[j] = temp;
            }
        }
        int temp = a[i + 1]; a[i + 1] = a[high]; a[high] = temp;
        return i + 1;
    }

    // --- Helper Methods ---
    private static int customRandom() {
        seed = (seed * 1103515245 + 12345) & 0x7fffffff;
        return (int)(seed % 10000);
    }

    private static int[] generateRandomArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = customRandom();
        return arr;
    }

    private static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) copy[i] = arr[i];
        return copy;
    }
}