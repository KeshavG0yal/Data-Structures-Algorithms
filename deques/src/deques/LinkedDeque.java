package deques;

/**
 * A doubly linked list implementation of the Deque interface.
 *
 * @param <T> the type of items stored in the deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    Node<T> front;
    Node<T> back;

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null, null, null);
        back = new Node<>(null, front, null);
        front.next = back;
    }

    @Override
    public void addFirst(T item) {
        size += 1;
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }
        Node<T> newNode = new Node<>(item, front, front.next);
        front.next.prev = newNode;
        front.next = newNode;
    }

    @Override
    public void addLast(T item) {
        size += 1;
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }
        Node<T> newNode = new Node<>(item, back.prev, back);
        back.prev.next = newNode;
        back.prev = newNode;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> first = front.next;
        T val = first.value;
        front.next = first.next;
        first.next.prev = front;
        size -= 1;

        return val;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> last = back.prev;
        T val = last.value;
        back.prev = last.prev;
        last.prev.next = back;
        size -= 1;

        return val;
    }

    @Override
    public T get(int index) {
        if ((index < 0) || (index >= size)) {
            return null;
        }

        Node<T> current;
        if (index < (size / 2)) {
            current = front.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = back.prev;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current.value;
    }

    @Override
    public int size() {
        return size;
    }
}
