public class DivideAndConquer {
    
    // Binary Search 實作
    // Recurrence: T(n) = T(n/2) + 1
    public int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // Merge Sort 實作
    // Recurrence: T(n) = 2T(n/2) + n
    public void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSort(arr, left, mid);      // 分割左半邊
            mergeSort(arr, mid + 1, right); // 分割右半邊
            merge(arr, left, mid, right);   // 合併
        }
    }

    private void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    public static void main(String[] args) {
        DivideAndConquer dc = new DivideAndConquer();

        int[] sorted = {1, 3, 5, 7, 9, 11};
        int index = dc.binarySearch(sorted, 7);
        System.out.println("binarySearch(7) = " + index);

        int[] data = {38, 27, 43, 3, 9, 82, 10};
        dc.mergeSort(data, 0, data.length - 1);
        System.out.println("mergeSort = " + java.util.Arrays.toString(data));
    }
}