import java.util.Arrays;

public class ParallelMergeSort {

    private static final int THREAD_THRESHOLD = 10000; // Minimum size to use threads

    public static void parallelMergeSort(int[] arr) {
        parallelMergeSort(arr, 0, arr.length - 1);
    }

    private static void parallelMergeSort(int[] arr, int left, int right) {
        if (right - left < THREAD_THRESHOLD) {
            // Small part → use normal merge sort
            mergeSort(arr, left, right);
            return;
        }

        int mid = (left + right) / 2;

        // Create threads to sort left and right parts
        Thread leftThread = new Thread(() -> parallelMergeSort(arr, left, mid));
        Thread rightThread = new Thread(() -> parallelMergeSort(arr, mid + 1, right));

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(arr, left, mid, right);
    }

    // Normal Sequential Merge Sort (used for small chunks)
    private static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    // Merge operation
    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = Arrays.copyOfRange(arr, left, right + 1);

        int i = 0, j = mid - left + 1;
        int k = left;

        while (i <= mid - left && j <= right - left) {
            if (temp[i] <= temp[j]) arr[k++] = temp[i++];
            else arr[k++] = temp[j++];
        }

        while (i <= mid - left) arr[k++] = temp[i++];
        while (j <= right - left) arr[k++] = temp[j++];
    }

    // Test Code
    public static void main(String[] args) {
        int size = 20000000;
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) arr[i] = (int) (Math.random() * size);

        long start = System.currentTimeMillis();
        parallelMergeSort(arr);
        long end = System.currentTimeMillis();

        System.out.println("Array Sorted: " + isSorted(arr));
        System.out.println("Time Taken (Parallel): " + (end - start) + " ms");
    }

    // Utility check
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < arr[i - 1]) return false;
        return true;
    }
}