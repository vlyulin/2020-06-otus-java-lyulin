package ru.otus.frontend.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
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
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", insertable=false, updatable=false)
    private long id;
    @Column(name = "name")
    @NotEmpty(message = "First name is required")
    private String name;
    @Column(name = "age")
    @Min(1)
    @Max(150)
    private int age;
    @Column(name = "login", nullable = false, unique = true)
    @NotEmpty(message = "Login name is required")
    private String login;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password name is required")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = PhoneDataSet.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonManagedReference // Это чтобы infinitive loop в toJson не было
    private Set<PhoneDataSet> phones;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = AddressDataSet.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonManagedReference
    private Set<AddressDataSet> addresses;

    public User() {
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(long id, String name, int age, String login, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", phones=" + phones +
                ", addresses=" + addresses +
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
