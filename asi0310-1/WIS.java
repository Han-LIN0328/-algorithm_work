public class WIS {
    // 定義活動物件
    static class Activity {
        int start, finish, value;
        public Activity(int start, int finish, int value) {
            this.start = start;
            this.finish = finish;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Activity[] arr = {
            new Activity(1, 4, 5), new Activity(3, 5, 1),
            new Activity(0, 6, 8), new Activity(4, 7, 4),
            new Activity(3, 8, 6), new Activity(5, 9, 3),
            new Activity(6, 10, 2), new Activity(8, 11, 4)
        };

        long startTime = System.nanoTime();
        
        mergeSort(arr, 0, arr.length - 1);
        
        int n = arr.length;
        int[] opt = new int[n + 1];
        opt[0] = 0; 

        for (int j = 1; j <= n; j++) {
            int currentVal = arr[j - 1].value;
            int p = binarySearch(arr, j - 1); 
            
            int include = currentVal + (p != -1 ? opt[p + 1] : 0);
            int exclude = opt[j - 1];
            opt[j] = include > exclude ? include : exclude;
        }

        long endTime = System.nanoTime();

        System.out.println("Maximum total value: " + opt[n]);
        System.out.println("Time Complexity: O(n log n)");
        System.out.println("Execution Time: " + (endTime - startTime) + " ns");
    }

    private static int binarySearch(Activity[] arr, int index) {
        int low = 0, high = index - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid].finish <= arr[index].start) {
                if (mid + 1 <= high && arr[mid + 1].finish <= arr[index].start) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    private static void mergeSort(Activity[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private static void merge(Activity[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        Activity[] L = new Activity[n1];
        Activity[] R = new Activity[n2];
        for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i].finish <= R[j].finish) { arr[k] = L[i]; i++; } 
            else { arr[k] = R[j]; j++; }
            k++;
        }
        while (i < n1) { arr[k] = L[i]; i++; k++; }
        while (j < n2) { arr[k] = R[j]; j++; k++; }
    }
}