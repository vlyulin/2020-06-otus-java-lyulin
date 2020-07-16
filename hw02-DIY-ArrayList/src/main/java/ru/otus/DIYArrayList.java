package ru.otus;

import java.util.*;

public class DIYArrayList<E> implements List<E> {

    private final int INIT_SIZE = 16;
    private Object[] array;
    private int pointer = 0;

    class DIYListIterator<E> implements ListIterator {

        private int currentIndex = 0;

        public DIYListIterator () {
        }

        public DIYListIterator (int index) {
            if (index < 0 || index > pointer) throw new IllegalArgumentException();
            this.currentIndex = index;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size();
        }

        @Override
        public Object next() {
            if (pointer == 0 || currentIndex >= pointer) throw new IndexOutOfBoundsException();
            return array[currentIndex++];
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > 0;
        }

        @Override
        public Object previous() {
            if (pointer == 0 || currentIndex == 0) throw new IndexOutOfBoundsException();
            return array[--currentIndex];
        }

        @Override
        public int nextIndex() {
            return currentIndex + 1;
        }

        @Override
        public int previousIndex() {
            return currentIndex - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Object o) {
            array[currentIndex - 1] = o;
        }

        @Override
        public void add(Object o) {
            throw new UnsupportedOperationException();
        }
    }

    public DIYArrayList() {
        resize(INIT_SIZE);
    }

    public DIYArrayList(Collection<? extends E> c) {
        resize(c.size());
        System.arraycopy(c.toArray(), 0, array, pointer, c.size());
        pointer = c.size();
    }

    public DIYArrayList(int initialCapacity) {
        resize(initialCapacity);
    }

    @Override
    public int size() {
        return pointer;
    }

    @Override
    public boolean isEmpty() {
        return pointer == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {

        return new Iterator<E>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size(); // && arrayList[currentIndex] != null;
            }

            @Override
            public E next() {
                if (pointer == 0 || currentIndex >= pointer) throw new IndexOutOfBoundsException();
                // TODO: Warning:(190, 16) Unchecked cast: 'java.lang.Object' to 'E'
                // Как тут делать правильно?
                return (E) array[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size());
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) Arrays.copyOf(array, size(), a.getClass());
    }

    @Override
    public boolean add(E e) {
        if (needResize()) {
            // TODO: Тут Warning:(139, 24) Dereference of 'array' may produce 'NullPointerException'
            // Надо ли проверять на null и как реагировать, если надо?
            int size = array.length;
            size = (size == 0) ? INIT_SIZE : size + (size >> 1);
            resize(size);
        }
        array[pointer++] = e;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (array.length < (size() + c.size())) {
            resize((size() + c.size()) - array.length);
        }
        System.arraycopy(c.toArray(), 0, array, pointer, c.size());
        pointer += c.size();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        if (index >= pointer) throw new IndexOutOfBoundsException();
        return (E) array[index];
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return (ListIterator<E>) new DIYListIterator<E>();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return (ListIterator<E>) new DIYListIterator<E>(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private boolean needResize() {
        return array == null || pointer == array.length;
    }

    private void resize(int newLength) {

        if (newLength < 0 || pointer > newLength) throw new IllegalArgumentException();

        if (array == null) {
            array = new Object[newLength];
            return;
        }

        Object[] newArray = new Object[newLength];
        System.arraycopy(array, 0, newArray, 0, pointer);
        array = newArray;
    }
}
