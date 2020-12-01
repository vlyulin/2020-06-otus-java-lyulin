package ru.otus.frontend.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Addresses")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER) // TODO: Delete
    @JoinColumn(name = "user_id", referencedColumnName="id", insertable=false, updatable=false)
    private User user;

    @Column(name = "street", nullable = false)
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(User user, String street) {
        this.user = user;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet that = (AddressDataSet) o;
        return street.equals(that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street);
    }
}
