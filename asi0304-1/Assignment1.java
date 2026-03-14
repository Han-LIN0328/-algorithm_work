public class Assignment1 {
    // 自製亂數種子，避免使用內建套件
    static long seed = 12345;

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 50000, 100000}; 
        
        System.out.println("資料量(n)\tLinear O(n)\tInsertion O(n^2)\tMerge O(n log n) (單位: 奈秒)");
        
        for (int n : sizes) {
            int[] original = generateRandomArray(n);
            
            // 1. Linear Scan (Array Sum)
            long start = System.nanoTime();
            linearScan(original);
            long timeLinear = System.nanoTime() - start;
            
            // 2. Insertion Sort
            int[] arr1 = copyArray(original);
            start = System.nanoTime();
            insertionSort(arr1);
            long timeInsertion = System.nanoTime() - start;
            
            // 3. Merge Sort
            int[] arr2 = copyArray(original);
            start = System.nanoTime();
            mergeSort(arr2, 0, arr2.length - 1);
            long timeMerge = System.nanoTime() - start;
            
            System.out.printf("%d\t\t%d\t\t%d\t\t\t%d\n", 
                n, timeLinear, timeInsertion, timeMerge);
        }
    }

    // --- Linear Scan ---
    public static long linearScan(int[] arr) {
        long sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
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

    // --- Merge Sort ---
    public static void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
    }

    private static void merge(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; ++i) L[i] = a[left + i];
        for (int j = 0; j < n2; ++j) R[j] = a[mid + 1 + j];
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) { a[k] = L[i]; i++; } 
            else { a[k] = R[j]; j++; }
            k++;
        }
        while (i < n1) { a[k] = L[i]; i++; k++; }
        while (j < n2) { a[k] = R[j]; j++; k++; }
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