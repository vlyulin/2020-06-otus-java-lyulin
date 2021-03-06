package ru.otus.dbserver.core.service;

import ro.otus.common.core.model.User;
import ru.otus.dbserver.cache.HwCache;

import java.util.Optional;

import static java.util.Optional.ofNullable;

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
        Optional<User> optUser = ofNullable(hwCache.get(String.valueOf(id)));
        if(!optUser.isPresent()) {
            optUser = dbServiceUser.getUser(id);
            if(optUser.isPresent()) {
                putUserIntoCache(optUser.get());
            }
        }
        return optUser;
//        return ofNullable(hwCache.get(String.valueOf(id)))
//                .or(() ->
//                    dbServiceUser.getUser(id).map(user -> {putUserIntoCache(user); return user;})
//                );
    }

    private void putUserIntoCache(User user) {
        hwCache.put(String.valueOf(user.getId()), user);
    }
}
