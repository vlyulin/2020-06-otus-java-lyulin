package ru.otus.frontend.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.frontend.cache.HwCache;
import ru.otus.frontend.cache.MyCache;
import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CachedDBServiceUserImplTest {

    public static final int USER_ID = 1;

    @Test
    void saveUser() {
    }

    @Test
    @DisplayName("Проверка получения закэшированного User")
    void getCachedUser() {

        HwCache<String, User> hwCache = new MyCache<>();
        HwCache<String,User> spyHwCache = spy(hwCache);
        User user = getDummyUser(USER_ID, "Дурилов Картон Испытанович", 54);
        spyHwCache.put(String.valueOf(user.getId()),user);

        DBServiceUser mockDbServiceUser = mock(DbServiceUserImpl.class);
        when(mockDbServiceUser.getUser(USER_ID)).thenReturn(Optional.of(user));

        DBServiceUser cachedDbServiceUser = new CachedDBServiceUserImpl(mockDbServiceUser, spyHwCache);
        Optional<User> receivedUser = cachedDbServiceUser.getUser(USER_ID);

        verify(spyHwCache, times(1)).get(String.valueOf(USER_ID));
        assertThat(receivedUser.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Проверка получения НЕзакэшированного User")
    void getNonCachedUser() {

        HwCache<String,User> hwCache = new MyCache<>();
        HwCache<String,User> spyHwCache = spy(hwCache);
        User user = getDummyUser(USER_ID, "Дурилов Картон Испытанович, или как родные зовут ласково Дурилка Картонная", 54);

        DBServiceUser mockDbServiceUser = mock(DbServiceUserImpl.class);
        when(mockDbServiceUser.getUser(USER_ID)).thenReturn(Optional.of(user));

        DBServiceUser cachedDbServiceUser = new CachedDBServiceUserImpl(mockDbServiceUser, spyHwCache);
        Optional<User> receivedUser = cachedDbServiceUser.getUser(USER_ID);

        verify(spyHwCache, times(1)).get(String.valueOf(USER_ID));
        verify(spyHwCache, times(1)).put(String.valueOf(USER_ID), user);
        assertThat(receivedUser.get()).isEqualTo(user);
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