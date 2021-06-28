package ru.otus;

import ru.otus.asserts.MyAssertions;
import java.util.*;

import java.util.*;
import static ru.otus.asserts.MyAssertions.*;

@DisplayName("Class should")
class DIYArrayListTest {

    private final List<Double> reference20list = Arrays.asList(
            1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0,
            11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);

    @Before
    public void beforeEach() {
    }

    @After
    public void afterEach() {
    }

    @Test
    @DisplayName("Test size for empty DIYArray.")
    public void testEmptyDIYArraySize() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        // MyAssertions.assertThat(diyArrayList.size()).isEqualTo(0);
        assertThat(diyArrayList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка создания новый DIYArrayList на базе другой коллекции")
    public void testConstructorWithCollection() {
        List<Double> diyArrayList = new DIYArrayList<>(reference20list);
        // MyAssertions.assertEquals(reference20list, diyArrayList);
        assertEquals(reference20list, diyArrayList);
    }

    @Test
    @DisplayName("Проверка exception создания DIYArrayList с отрицательной вместимостью")
    public void testConstructorWithNegativeCapacity() {
        // MyAssertions.assertThrows(IllegalArgumentException.class, () -> new DIYArrayList<Double>(-1));
        assertThrows(IllegalArgumentException.class, () -> new DIYArrayList<Double>(-1));
    }

    @Test
    @DisplayName("Проверка расширения внутреннего массива при добавлении множества элементов")
    void addElements() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        double d = 1;
        for (int i = 0; i < 32; i++) {
            diyArrayList.add(d);
            d *= 2;
        }
        // MyAssertions.assertThat(diyArrayList.size()).isEqualTo(32);
        // MyAssertions.assertThat(diyArrayList.get(31)).isEqualTo(2147483648.0);
        assertThat(diyArrayList.size()).isEqualTo(32);
        assertThat(diyArrayList.get(31)).isEqualTo(2147483648.0);
    }

    @Test
    @DisplayName("Тестирование iterator на пустой коллекции")
    void testIteratorOnEmptyDIYList() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        Iterator<Double> iterator = diyArrayList.iterator();

        // test hasNext on an empty collection (returns false)
        // MyAssertions.assertThat(iterator.hasNext()).isFalse();

        // test next() on an empty collection (throws exception)
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, iterator::next);
        assertThat(iterator.hasNext()).isFalse();

        // test next() on an empty collection (throws exception)
        assertThrows(IndexOutOfBoundsException.class, iterator::next);
    }

    @Test
    @DisplayName("Тестирование iterator на коллекции с одним элементом")
    void testIteratorOnDIYListWithOneElement() {

        DIYArrayList<Double> diyArrayList = new DIYArrayList<>(Arrays.asList(1.0));
        Iterator<Double> iterator = diyArrayList.iterator();

        // test hasNext on a collection with one item (returns true)
        // MyAssertions.assertTrue(iterator.hasNext());

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        // MyAssertions.assertThat(iterator.next()).isEqualTo(1.0);
        // MyAssertions.assertThat(iterator.hasNext()).isFalse();
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, iterator::next);
        assertTrue(iterator.hasNext());

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        assertThat(iterator.next()).isEqualTo(1.0);
        assertThat(iterator.hasNext()).isFalse();
        assertThrows(IndexOutOfBoundsException.class, iterator::next);
    }

    @Test
    @DisplayName("Тестирование iterator на коллекции с несколькими элементами")
    void testIteratorOnDIYArrayListWithSeveralElements() {

        DIYArrayList<Double> diyArrayList = new DIYArrayList<>(reference20list);
        Iterator<Double> iterator = diyArrayList.iterator();

        // test with a collection with several items, make sure the iterator goes through each item,
        // in the correct order (if there is one)
        List<Double> actualList = Lists.newArrayList(iterator);
        // MyAssertions.assertEquals(reference20list, actualList);
        assertEquals(reference20list, actualList);
    }

    @Test
    @DisplayName("Проверка ListIterator на пустой коллекции")
    void testListIteratorOnEmptyDIYList() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        ListIterator<Double> listIterator = diyArrayList.listIterator();

        // test hasNext on an empty collection (returns false)
        // MyAssertions.assertThat(listIterator.hasNext()).isFalse();

        // test next() on an empty collection (throws exception)
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, listIterator::next);

        // test hasPrevious on an empty collection (returns false)
        // MyAssertions.assertThat(listIterator.hasPrevious()).isFalse();

        // test previous() on an empty collection (throws exception)
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, listIterator::previous);
        assertThat(listIterator.hasNext()).isFalse();

        // test next() on an empty collection (throws exception)
        assertThrows(IndexOutOfBoundsException.class, listIterator::next);

        // test hasPrevious on an empty collection (returns false)
        assertThat(listIterator.hasPrevious()).isFalse();

        // test previous() on an empty collection (throws exception)
        assertThrows(IndexOutOfBoundsException.class, listIterator::previous);
    }

    @Test
    @DisplayName("Проверка ListIterator на коллекции с одним элементом")
    void testListIteratorOnDIYListWithOneElement() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        diyArrayList.add(1.0);
        ListIterator<Double> listIterator = diyArrayList.listIterator();

        // test hasNext on a collection with one item (returns true)
        // MyAssertions.assertThat(listIterator.hasNext()).isTrue();

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        // MyAssertions.assertThat(listIterator.next()).isEqualTo(1.0);
        // MyAssertions.assertThat(listIterator.hasNext()).isFalse();
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, listIterator::next);

        // test hasPrevious on a collection with one element (returns false)
        // MyAssertions.assertThat(listIterator.hasPrevious()).isTrue();
        // MyAssertions.assertThat(listIterator.previous()).isEqualTo(1.0);
        // in second time hasPrevious returns false, twice previous exception
        // MyAssertions.assertThrows(IndexOutOfBoundsException.class, listIterator::previous);
        assertThat(listIterator.hasNext()).isTrue();

        // test hasNext/next on a collection with one item: hasNext returns true,
        // next returns the item, hasNext returns false, twice
        assertThat(listIterator.next()).isEqualTo(1.0);
        assertThat(listIterator.hasNext()).isFalse();
        assertThrows(IndexOutOfBoundsException.class, listIterator::next);

        // test hasPrevious on a collection with one element (returns false)
        assertThat(listIterator.hasPrevious()).isTrue();
        assertThat(listIterator.previous()).isEqualTo(1.0);
        // in second time hasPrevious returns false, twice previous exception
        assertThrows(IndexOutOfBoundsException.class, listIterator::previous);
    }

    @Test
    @DisplayName("Тестирование ListIterator на прямой перебор коллекции с несколькими элементами")
    void testListIteratorOnDIYArrayListWithSeveralElements() {

        DIYArrayList<Double> diyArrayList = new DIYArrayList<>(reference20list);
        ListIterator<Double> iterator = diyArrayList.listIterator();

        // test with a collection with several items, make sure the ListIterator goes through each item,
        // in the correct order (if there is one)
        List<Double> actualList = Lists.newArrayList(iterator);
        // MyAssertions.assertEquals(reference20list, actualList);
        assertEquals(reference20list, actualList);
    }

    @Test
    @DisplayName("Тестирование ListIterator на обратный перебор коллекции с несколькими элементами")
    void testBackwardListIteratorOnDIYArrayListWithSeveralElements() {

        DIYArrayList<Double> diyArrayList = new DIYArrayList<>(reference20list);
        ListIterator<Double> iterator = diyArrayList.listIterator(diyArrayList.size());
        List<Double> actualList = new ArrayList<>(diyArrayList.size());

        // backward goes, test with a collection with several items,
        // make sure the ListIterator goes backward through each item,
        // in the correct order
        while (iterator.hasPrevious()) {
            actualList.add(iterator.previous());
        }
        List<Double> reverseReferenceCollection = new ArrayList<>(reference20list);
        Collections.reverse(reverseReferenceCollection);
        // MyAssertions.assertEquals(reverseReferenceCollection, actualList);
        assertEquals(reverseReferenceCollection, actualList);
    }

    @Test
    @DisplayName("Test Collections.addAll(Collection<? super T> c, T... elements)")
    void testCollectionsAddAll() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();
        Collections.addAll(diyArrayList, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0,
                11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);
        // MyAssertions.assertEquals(reference20list, diyArrayList);
        assertEquals(reference20list, diyArrayList);
    }

    @Test
    @DisplayName("Test Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)")
    void testCollectionCopy() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>();

        for (double d = 1; d < 21; d++) {
            diyArrayList.add(0.0);
        }
        Collections.copy(diyArrayList, reference20list);
        // MyAssertions.assertEquals(reference20list, diyArrayList);
        assertEquals(reference20list, diyArrayList);
    }

    @Test
    @DisplayName("Test Collections.static <T> void sort(List<T> list, Comparator<? super T> c)")
    void testCollectionSort() {
        DIYArrayList<Double> diyArrayList = new DIYArrayList<>(reference20list);
        // Test reverse order sorting
        Collections.sort(diyArrayList, Collections.reverseOrder());

        List<Double> reverseReferenceCollection = new ArrayList<>(reference20list);
        Collections.reverse(reverseReferenceCollection);
        // MyAssertions.assertEquals(reverseReferenceCollection, diyArrayList);
        assertEquals(reverseReferenceCollection, diyArrayList);
    }
}