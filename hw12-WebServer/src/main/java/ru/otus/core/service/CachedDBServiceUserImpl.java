package ru.otus.core.service;

import ru.otus.cache.HwCache;
import ru.otus.core.model.User;

import java.util.Optional;

public class CachedDBServiceUserImpl implements DBServiceUser {

    private final DBServiceUser dbServiceUser;
    private final HwCache<String, User> hwCache;

    public CachedDBServiceUserImpl(DBServiceUser dbServiceUser, HwCache<String, User> hwCache) {
        this.dbServiceUser = dbServiceUser;
        this.hwCache = hwCache;
    }

    @Override
    public long saveUser(User user) {
        long id = dbServiceUser.saveUser(user);
        putUserIntoCache(user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(hwCache.get(String.valueOf(id)))
                .or(() ->
                    dbServiceUser.getUser(id).map(user -> {putUserIntoCache(user); return user;})
                );
    }

    private void putUserIntoCache(User user) {
        hwCache.put(String.valueOf(user.getId()), user);
    }
}
