package ru.otus;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DIYArrayListTest {

    @Test
    @DisplayName("Test size for empty DIYArray.")
    public void testEmptyDIYArraySize() {
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        assertEquals(0, diyArrayList.size());
    }

    @Test
    @DisplayName("Test adding element to DIYArray.")
    void addElements() {
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        double d = 1;
        for (int i = 0; i < 32; i++) {
            diyArrayList.add(d);
            d *= 2;
        }
        assertEquals(32, diyArrayList.size());
        assertEquals(2147483648.0, diyArrayList.get(31));
    }

    @Test
    @DisplayName("Test iterator")
    void testIterator() {
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        Iterator<Double> iterator = diyArrayList.iterator();

        // test hasNext on an empty collection (returns false)
        assertFalse(iterator.hasNext());

        // test next() on an empty collection (throws exception)
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
           Double d = iterator.next();
        });

        // test hasNext on a collection with one item (returns true)
        diyArrayList.add(1.0);
        assertTrue(iterator.hasNext());

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        assertEquals(1.0, iterator.next());
        assertFalse(iterator.hasNext());
        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Double d = iterator.next();
        });

        // final test with a collection with several items, make sure the iterator goes through each item,
        // in the correct order (if there is one)
        diyArrayList.add(2.0);
        diyArrayList.add(3.0);
        Iterator<Double> iterator2 = diyArrayList.iterator();
        List<Double> actualList = Lists.newArrayList(iterator2);
        List<Double> referenceList = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(referenceList, actualList);
    }

    @Test
    @DisplayName("Test ListIterator")
    void testListIterator() {
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        ListIterator<Double> listIterator = diyArrayList.listIterator();

        // test hasNext on an empty collection (returns false)
        assertFalse(listIterator.hasNext());

        // test next() on an empty collection (throws exception)
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Double d = listIterator.next();
        });

        // test hasPrevious on an empty collection (returns false)
        assertFalse(listIterator.hasPrevious());

        // test previous() on an empty collection (throws exception)
        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Double d = listIterator.previous();
        });

        // test hasNext on a collection with one item (returns true)
        diyArrayList.add(1.0);
        assertTrue(listIterator.hasNext());

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        assertEquals(1.0, listIterator.next());
        assertFalse(listIterator.hasNext());
        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Double d = listIterator.next();
        });

        // test hasPrevious on a collection with one element (returns false)
        assertTrue(listIterator.hasPrevious());
        assertEquals(1.0,listIterator.previous());
        // in second time hasPrevious returns false, twice previous exception
        exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            Double d = listIterator.previous();
        });

        // test with a collection with several items, make sure the ListIterator goes through each item,
        // in the correct order (if there is one)
        diyArrayList.add(2.0);
        diyArrayList.add(3.0);
        ListIterator<Double> iterator2 = diyArrayList.listIterator();
        List<Double> actualList = Lists.newArrayList(iterator2);
        List<Double> referenceList = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(referenceList, actualList);

        // backward goes, test with a collection with several items, make sure the ListIterator goes backward through each item,
        // in the correct order
        while(iterator2.hasPrevious()) {
            actualList.add(iterator2.previous());
        }
        referenceList = Arrays.asList(1.0, 2.0, 3.0, 3.0, 2.0, 1.0);
        assertEquals(referenceList, actualList);
    }

    @Test
    @DisplayName("Test Collections.addAll(Collection<? super T> c, T... elements)")
    void testCollectionsAddAll() {
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        Collections.addAll(diyArrayList, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0,
                11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);

        List<Double> referenceList = new ArrayList<>(20);
        for(int d = 1; d < 21; d++) {
            referenceList.add(Double.valueOf(d));
        }
        assertEquals(referenceList, diyArrayList);
    }

    @Test
    @DisplayName("Test Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)")
    void testCollectionCopy() {
        List<Double> referenceList = new ArrayList<>();
        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();

        for(double d = 1; d < 21; d++) {
            referenceList.add(d);
            diyArrayList.add(0.0);
        }
        Collections.copy(diyArrayList, referenceList);
        assertEquals(referenceList, diyArrayList);
    }

    @Test
    @DisplayName("Test Collections.static <T> void sort(List<T> list, Comparator<? super T> c)")
    void testCollectionSort() {

        ru.otus.DIYArrayList<Double> diyArrayList = new DIYArrayList();
        for(int d = 1; d < 21; d++) {
            diyArrayList.add(Double.valueOf(d));
        }
        Collections.sort(diyArrayList, Collections.reverseOrder());

        // Test reverse order sorting
        List<Double> referenceList = new ArrayList<>(20);
        for(int d = 20; d > 0; d--) {
            referenceList.add(Double.valueOf(d));
        }
        assertEquals(referenceList, diyArrayList);
    }
}