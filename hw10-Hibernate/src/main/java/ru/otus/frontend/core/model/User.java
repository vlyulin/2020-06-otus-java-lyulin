package ru.otus.frontend.core.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author sergey
 * created on 03.02.19.
 */
@NamedEntityGraph(
        name = "user-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("phones"),
                @NamedAttributeNode("addresses"),
        }
)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", insertable=false, updatable=false)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = PhoneDataSet.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Set<PhoneDataSet> phones;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = AddressDataSet.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Set<AddressDataSet> addresses;

    public User() {
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String newName) {
        name = newName;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    public void setPhones(Set<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public Set<AddressDataSet> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressDataSet> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
