import java.util.*;
import java.io.*;

class DLList<String> {

    // (Internal) DLL_Node Class
    private class DLL_Node<String> {
        String data;
        DLL_Node<String> previous;
        DLL_Node<String> next;

        // default constructor
        public DLL_Node() {
            data = null;
            previous = null;
            next = null;
        }

        // overloaded constructor
        public DLL_Node(String value) {
            data = value;
            previous = null;
            next = null;
        }

        // copy constructor
        public DLL_Node(DLL_Node<String> node) {
            data = node.getData();
            previous = node.previous;
            next = node.next;
        }

        public String getData() {
            return data;
        }

    }

    // Data members
    private DLL_Node<String> front;
    private DLL_Node<String> back;
    private DLL_Node<String> current;
    private int size;
    private int index;

    // Data functions (Methods)

    // Default constructor
    public DLList() {
        clear();
    }

    // copy constructor
    public DLList(DLList<String> other) {
        front = other.front;
        back = other.back;
        current = other.current;
        size = other.size;
        index = other.index;
    }

    // clear list method
    public void clear() {
        front = new DLL_Node();
        back = new DLL_Node();
        current = new DLL_Node();
        size = 0;
        index = -1;
    }

    // get size method
    public int getSize() {
        return size;
    }

    // get index method
    public int getIndex() {
        return index;
    }

    // is emty method
    public boolean isEmpty() {
        return size == 0;
    }

    // is at first node method
    public boolean atFirst() {
        return index == 0;
    }

    // is at last node method
    public boolean atLast() {
        return index == size - 1;
    }

    // get data at current method
    public String getData() {
        if (!isEmpty())
            return current.data;
        else
            return null;
    }

    // set data at current method
    public String setData(String x) {
        if (!isEmpty()) {
            current.data = x;
            return x;
        }
        return null;
    }

    // seek to the first node method
    public boolean first() {
        return (seek(0));
    }

    // seek to the next node method
    public boolean next() {
        return (seek(index + 1));
    }

    // seek to the previous node method
    public boolean previous() {
        return (seek(index - 1));
    }

    // seek to the last node method
    public boolean last() {
        return (seek(size - 1));
    }

    // seek method
    public boolean seek(int loc) {
        // local variables
        boolean retval = false;

        // is the list empty
        if (isEmpty())
            retval = false;

        // is the loc in range
        if (loc < 0 || loc >= getSize())
            retval = false;

        // is the loc==0
        if (loc == 0) {
            current = front;
            index = 0;
            retval = true;
        }

        // is the loc==last index;
        if (loc == size - 1) {
            current = back;
            index = size - 1;
            retval = true;
        }

        // is the loc<current index;
        if (loc < index) {
            while (loc < index) {
                current = current.previous;
                index--;
            }
            retval = true;
        }

        // is the loc>current index;
        if (loc > index) {
            while (loc > index) {
                current = current.next;
                index++;
            }
            retval = true;
        }
        // else ...loc is at the current index..
        return true;
    }

    // insert first method
    public boolean insertFirst(String item) {
        first();
        return (insertBeforeAt(item));
    }

    // insert a node before current
    public boolean insertBeforeAt(String item) {
        DLL_Node nn = new DLL_Node(item);

        // if the list is empty
        if (isEmpty()) {
            front = nn;
            back = nn;
            current = nn;
            size = 1;
            index = 0;
            return true;
        }

        // add the first node
        if (atFirst()) {
            nn.next = current;
            current.previous = nn;
            current = nn;
            front = current;
            size = size + 1;
            return true;
        }

        //
        DLL_Node prev = current.previous;
        nn.previous = prev;
        prev.next = nn;
        nn.next = current;
        current.previous = nn;
        current = current.previous;
        size = size + 1;

        return true;
    }

    // insert a node after current
    public boolean insertAfterAt(String item) {
        DLL_Node nn = new DLL_Node(item);

        // if the list is empty
        if (isEmpty()) {
            front = nn;
            back = nn;
            current = nn;
            size = 1;
            index = 0;
            return true;
        }

        // add the last node
        if (atLast()) {
            nn.previous = back;
            back.next = nn;
            back = nn;
            current = back;
            size = size + 1;
            index = size - 1;
            return true;
        }

        //
        DLL_Node nextNode = current.next;
        nn.next = nextNode;
        nextNode.previous = nn;
        nn.previous = current;
        current.next = nn;
        current = current.next;
        size = size + 1;
        index++;
        return true;
    }

    // insert last method
    public boolean insertLast(String item) {
        last();
        return insertAfterAt(item);
    }

    // delete first method
    public boolean deleteFirst() {
        first();
        return (deleteAt());
    }

    // delete at method
    public boolean deleteAt() {
        if (isEmpty())
            return false;
        if (size == 1) {
            clear();
            return true;
        }
        if (atFirst()) {
            current = current.next;
            front = current;
            current.previous = null;
            size--;
            return true;
        }

        if (atLast()) {
            current = current.previous;
            back = current;
            current.next = null;
            size--;
            index--;
            return true;
        }

        DLL_Node prev = current.previous;
        DLL_Node Next = current.next;
        prev.next = Next;
        Next.previous = prev;
        current = Next;
        size--;

        return true;
    }

    // delete last method
    public boolean deleteLast() {
        last();
        return (deleteAt());
    }

    // //print all the list
    public void printall() {
        if (isEmpty()) {
            System.out.println("Empty list");
            return;
        }
        int i = 0;
        DLL_Node nn = new DLL_Node(front);
        while (i < size) {
            System.out.println(nn.data);
            nn = nn.next;
            i++;
        }
    }

}