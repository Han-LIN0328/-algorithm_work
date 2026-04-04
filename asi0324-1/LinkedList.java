// 節點結構定義
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

// 鏈結串列主體
public class LinkedList {
    Node head;

    // 在頭部插入節點 (Insert at Head)
    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    // 在尾部插入節點 (Insert at Tail)
    public void insertAtTail(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    // 刪除特定數值的節點 (Delete)
    public void delete(int key) {
        if (head == null) return;
        
        // 若要刪除的是頭節點
        if (head.data == key) {
            head = head.next;
            return;
        }

        Node temp = head;
        // 尋找要刪除的節點的前一個節點
        while (temp.next != null && temp.next.data != key) {
            temp = temp.next;
        }

        // 若找到該節點，則跳過它以進行刪除
        if (temp.next != null) {
            temp.next = temp.next.next;
        }
    }

    // 搜尋節點 (Search)
    public boolean search(int key) {
        Node temp = head;
        while (temp != null) {
            if (temp.data == key) return true;
            temp = temp.next;
        }
        return false;
    }
}