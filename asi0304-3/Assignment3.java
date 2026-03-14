public class Assignment3 {
    static long seed = 98765;

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 50000, 100000}; 
        
        System.out.println("資料量(n)\tCocktail Shaker Sort (單位: 奈秒)");
        
        for (int n : sizes) {
            int[] original = generateRandomArray(n);
            
            long start = System.nanoTime();
            cocktailShakerSort(original);
            long timeTaken = System.nanoTime() - start;
            
            System.out.printf("%d\t\t%d\n", n, timeTaken);
        }
    }

    // --- Cocktail Shaker Sort (雙向氣泡排序改良) ---
    public static void cocktailShakerSort(int[] a) {
        boolean swapped = true;
        int start = 0;
        int end = a.length - 1;

        while (swapped) {
            swapped = false;
            
            // 1. 從左到右掃描，將區間內最大值推到最右側
            for (int i = start; i < end; ++i) {
                if (a[i] > a[i + 1]) {
                    int temp = a[i]; 
                    a[i] = a[i + 1]; 
                    a[i + 1] = temp;
                    swapped = true;
                }
            }
            
            // 如果沒有發生交換，代表已經排序完成
            if (!swapped) break; 
            
            // 否則，準備進行反向掃描
            swapped = false;
            end = end - 1; // 右側最大值已歸位，縮小右邊界
            
            // 2. 從右到左掃描，將區間內最小值推到最左側
            for (int i = end - 1; i >= start; --i) {
                if (a[i] > a[i + 1]) {
                    int temp = a[i]; 
                    a[i] = a[i + 1]; 
                    a[i + 1] = temp;
                    swapped = true;
                }
            }
            start = start + 1; // 左側最小值已歸位，縮小左邊界
        }
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
}