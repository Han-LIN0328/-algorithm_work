public class MyStack {
    private int[] arr;
    private int top;
    private int capacity;

    public MyStack(int size) {
        this.capacity = size;
        this.arr = new int[capacity];
        this.top = -1;
    }

    // Push 操作：將元素放入 Stack 頂端
    public void push(int x) {
        if (top == capacity - 1) {
            resize(); // 陣列滿了就擴充兩倍
        }
        arr[++top] = x;
    }

    // Pop 操作：取出並移除 Stack 頂端的元素
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return arr[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    // 擴充陣列容量
    private void resize() {
        capacity *= 2;
        int[] newArr = new int[capacity];
        System.arraycopy(arr, 0, newArr, 0, top + 1);
        arr = newArr;
    }
}