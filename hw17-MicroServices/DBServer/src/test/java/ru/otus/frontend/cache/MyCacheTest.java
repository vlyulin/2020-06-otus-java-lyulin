package ru.otus.frontend.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.otus.common.core.model.AddressDataSet;
import ro.otus.common.core.model.PhoneDataSet;
import ro.otus.common.core.model.User;
import ru.otus.dbserver.cache.HwCache;
import ru.otus.dbserver.cache.HwListener;
import ru.otus.dbserver.cache.MyCache;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

// Примечание: для прохождения теста cacheDischargingOnLackOfSpace
// требуется в конфигурации теста установить VM параметры как -Xms8m -Xmx8m
// Пока не умею устанавливать профили для отдельного метода
class MyCacheTest {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(MyCache.class);

    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";
    public static final String VERY_LONG_USER_NAME = "Очень длинное арабское имя пользователя с перечислением всех предков по отцовской линии";
    public static final int GcThreshold = 20000;

    @Test
    @DisplayName("Тестирование put и get")
    void putAndGet() {
        HwCache<String, String> hwCache = new MyCache<>();
        hwCache.put(KEY, VALUE);
        String value = hwCache.get(KEY);
        assertThat(value).isEqualTo(VALUE);
    }

    @Test
    @DisplayName("Тестирование remove")
    void remove() {
        HwCache<String, String> hwCache = new MyCache<>();
        hwCache.put(KEY, VALUE);
        hwCache.remove(KEY);
        String value = hwCache.get(KEY);
        assertThat(value).isNull();
    }

    private class HwListenerClass<K, V> implements HwListener {

        @Override
        public void notify(Object key, Object value, String action) {
            logger.info("key:{}, value:{}, action: {}", key, value, action);
        }
    }

    @Test
    @DisplayName("Проверка добавления и вызова listener")
    void verifyListenerCalling() {
        HwListener<String, String> listener = mock(HwListener.class);
        HwCache<String, String> hwCache = new MyCache<>();
        hwCache.addListener(listener);
        hwCache.put(KEY, VALUE);
        verify(listener, Mockito.times(1)).notify(KEY, VALUE, MyCache.PUT_ACTION);
    }

    @Test
    @DisplayName("Поверка удаления listener")
    void removeListener() {
        HwListener<String, String> listener = mock(HwListener.class);
        HwCache<String, String> hwCache = new MyCache<>();
        hwCache.addListener(listener);
        hwCache.put(KEY, VALUE);
        verify(listener, Mockito.times(1)).notify(KEY, VALUE, MyCache.PUT_ACTION);
        hwCache.removeListener(listener);
        hwCache.put(KEY, VALUE);
        // Вызов только один, который был до удаления listener
        verify(listener, Mockito.times(1)).notify(KEY, VALUE, MyCache.PUT_ACTION);
    }

    private User getDummyUser(long user_id, String user_name, int age) {
        User user = new User(user_id, user_name, age);
        // Телефоны
        PhoneDataSet phone1 = new PhoneDataSet(user, "+7 (916) 123-12-12");
        PhoneDataSet phone2 = new PhoneDataSet(user, "+7 (916) 444-33-12");
        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));
        // Адреса
        AddressDataSet address1 = new AddressDataSet(user, "Siktivkar, d.1");
        AddressDataSet address2 = new AddressDataSet(user, "Stalinks, Lenina str. 70");
        user.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));
        return user;
    }
}