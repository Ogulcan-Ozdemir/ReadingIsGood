package tr.com.readingisgood.app.model.user;

import tr.com.readingisgood.app.model.order.BookOrder;
import tr.com.readingisgood.app.model.security.GrantedAuthRoles;
import tr.com.readingisgood.app.model.security.Jwt;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
//    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    List<GrantedAuthRoles> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderedUser")
    private List<BookOrder> bookOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Jwt> jwts;

    public User() {}

    public User(String email, String password, List<GrantedAuthRoles> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<GrantedAuthRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<GrantedAuthRoles> roles) {
        this.roles = roles;
    }

    public List<BookOrder> getOrders() {
        return bookOrders;
    }

    public void setOrders(List<BookOrder> bookOrders) {
        this.bookOrders = bookOrders;
    }

    public List<BookOrder> getBookOrders() {
        return bookOrders;
    }

    public void setBookOrders(List<BookOrder> bookOrders) {
        this.bookOrders = bookOrders;
    }

    public List<Jwt> getJwts() {
        return jwts;
    }

    public void setJwts(List<Jwt> jwts) {
        this.jwts = jwts;
    }

}
