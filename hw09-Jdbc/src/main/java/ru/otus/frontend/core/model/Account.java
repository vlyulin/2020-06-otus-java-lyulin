package ru.otus.frontend.core.model;

import ru.otus.frontend.anotations.Id;

import java.util.Objects;

public class Account {

    @Id
    private final long no;
    private final String type;
    private final float rest;

    public Account(long no, String type, float rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public float getRest() {
        return rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return no == account.no &&
                Float.compare(account.rest, rest) == 0 &&
                type.equals(account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, type, rest);
    }
}
