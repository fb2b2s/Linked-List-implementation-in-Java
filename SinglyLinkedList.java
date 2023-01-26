/** @author rbk
 *  Singly linked list: for instructional purposes only
 *  Ver 1.0: 2018/08/21
 *  Ver 2.0: 2018/08/28: modified to be able to extend to DoublyLinkedList
 *  Entry class has generic type associated with it, to allow inheritance.
 *  We can now have a doubly linked list class DLL that has

public class DoublyLinkedList<T> extends SinglyLinkedList<T> {
    static class Entry<E> extends SinglyLinkedList.Entry<E> {
	Entry<E> prev;
	Entry(E x, Entry<E> next, Entry<E> prev) {
	    super(x, nxt);
	    this.prev = prev;
	}
    }

 */

package idsa; 	// change to your netid
import java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class SinglyLinkedList<T> implements Iterable<T> {

    /** Class Entry holds a single node of the list */
    static class Entry<E> {
        E element;
        Entry<E> next;

        Entry(E x, Entry<E> nxt) {
            element = x;
            next = nxt;
        }	
    }

    // Dummy header is used.  tail stores reference of tail element of list
    Entry<T> head, tail;
    int size;

    public SinglyLinkedList() {
        head = new Entry<>(null, null);
        tail = head;
        size = 0;
    }

    public Iterator<T> iterator() { return new SLLIterator(); }

    protected class SLLIterator implements Iterator<T> {
		Entry<T> cursor, prev;
		boolean ready;  // is item ready to be removed?

		SLLIterator() {
			cursor = head;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next != null;
		}

		public T next() {
			prev = cursor;
			cursor = cursor.next;
			ready = true;
			return cursor.element;
		}

		// Removes the current element (retrieved by the most recent next())
		// Remove can be called only if next has been called and the element has not been removed
		public void remove() {
			if(!ready) {
				throw new NoSuchElementException();
			}
			prev.next = cursor.next;
			// Handle case when tail of a list is deleted
			if(cursor == tail) {
				tail = prev;
			}
			cursor = prev;
			ready = false;  // Calling remove again without calling next will result in exception thrown
			size--;
		}
    }  // end of class SLLIterator

    // Add new elements to the end of the list
    public void add(T x) {
	add(new Entry<>(x, null));
    }

    public void add(Entry<T> ent) {
		tail.next = ent;
		tail = tail.next;
		size++;
    }

    public void printList() {
		System.out.print(this.size + ": ");
		for(T item: this) {
			System.out.print(item + " ");
		}

		System.out.println();
    }

    // Rearrange the elements of the list by linking the elements at even index
    // followed by the elements at odd index. Implement by rearranging pointers
    // of existing elements without allocating any new elements.
    public void unzip() {
		if(size < 3) {  // Too few elements.  No change.
			return;
		}
		// write the invariant
		Entry<T> e_head = head;
		Entry<T> o_head = head.next;

		Entry<T> e, o, cursor;
		Entry<T> e_tail = e_head;
		Entry<T> o_tail = o_head;
		e_tail.next = o_tail.next = null;
		cursor = o_head.next;
		int size = 2;

		while(cursor != null){
			if(size%2 == 0) {
				e_tail.next = cursor;
				e_tail = e_tail.next;
				e_tail.next = null;
			}
			else {
				o_tail.next = cursor;
				o_tail = o_tail.next;
				o_tail.next = null;
			}
			size++;
			cursor = cursor.next;
		}
		e_tail.next = o_head;
    }

	public void addFirst(T x){
		Entry<T> ent = new Entry<>(x, null);
		ent.next = head;
		head = ent;
	}

	public void removeFirst(){
		if(head == tail){
			head = tail = null;
			return;
		}

		head = head.next;
	}

	public T remove(T x) throws NoSuchElementException{
		T val;
		if(head == tail && x == head.element){
			val = head.element;
			head = tail = null;
			return val;
		}

		Entry<T> cursor = head;
		while(cursor.next != null){
			if(cursor.next.element == x){
				if(cursor.next == tail)
					tail = cursor;
				val = cursor.next.element;
				cursor.next = cursor.next.next;
				return val;
			}
			cursor = cursor.next;
		}
		System.out.println("No such element is present.");
		return null;
	}

    public static void main(String[] args) throws NoSuchElementException {
        int n = 10;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
        for(int i=1; i<=n; i++) {
            lst.add(Integer.valueOf(i));
        }
        lst.printList();

		Iterator<Integer> it = lst.iterator();
		Scanner in = new Scanner(System.in);
		whileloop:
		while(in.hasNext()) {
			int com = in.nextInt();
			switch(com) {
				case 1:  // Move to next element and print it
					if (it.hasNext()) {
						System.out.println(it.next());
					} else {
						break whileloop;
					}
					break;
				case 2:  // Remove element
					it.remove();
					lst.printList();
					break;
				default:  // Exit loop
					break whileloop;
			}
		}
		lst.printList();
		lst.unzip();
		lst.printList();
		lst.removeFirst();
		lst.remove(9);
		lst.addFirst(2);
		lst.printList();

    }
}

/* Sample input:
   1 2 1 2 1 1 1 2 1 1 2 0
   Sample output:
10: 1 2 3 4 5 6 7 8 9 10 
1
9: 2 3 4 5 6 7 8 9 10 
2
8: 3 4 5 6 7 8 9 10 
3
4
5
7: 3 4 6 7 8 9 10 
6
7
6: 3 4 6 8 9 10 
6: 3 4 6 8 9 10 
6: 3 6 9 4 8 10
5: 2 6 4 8 10
*/
