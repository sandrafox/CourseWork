import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static class Node {
        int value = 0;
        Node next = null;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    class Tree {
        int value;
        Tree right;
        Tree left;
    }

    static void makeEqualLength(Node head1, Node head2) {
        int length1 = length(head1), length2 = length(head2);
        if (length1 < length2) {
            head1 = addZeroNodes(head1, length2 - length1);
        } else {
            head2 = addZeroNodes(head2, length1 - length2);
        }
    }

    static Node addZeroNodes(Node head, int length) {
        for (int i = 0; i < length; i++) {
            head = new Node(0, head);
        }
        return head;
    }

    static int length(Node head) {
        Node cur = head;
        int count = 0;
        while (cur != null) {
            count++;
            cur = cur.next;
        }
        return count;
    }

    static Node sumLists(Node head1, Node head2) {
        Node curHead1 = head1, curHead2 = head2;
        makeEqualLength(curHead1, curHead2);
        return sumNodes(curHead1, curHead2, 0);
    }

    static Node sumNodes(Node head1, Node head2, int s) {
        if (head1 == null) return null;
        int shift = 0;
        Node preHead = sumNodes(head1.next, head2.next, shift);
        int res = head1.value + head2.value + shift;
        s = res / 10;
        return new Node(res % 10, preHead);
    }






    public static void main(String[] args) {
        /*System.out.println(checkOneAway("pale", "bales"));*/
        Node head = sumLists(new Node(6, new Node(1, new Node(7, null))),
                new Node(2, new Node(9, new Node(5, null))));
        while (head != null) {
            System.out.println(head.value);
            head = head.next;
        }
    }

}
