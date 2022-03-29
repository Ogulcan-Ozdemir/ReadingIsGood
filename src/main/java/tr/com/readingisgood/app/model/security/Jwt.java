package tr.com.readingisgood.app.model.security;

import tr.com.readingisgood.app.model.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Jwt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(unique = true, nullable = false)
    private Timestamp issuedAt;

    @Column(unique = true, nullable = false)
    private Timestamp expiration;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public Jwt() {}

    public Jwt(User user, String token, Timestamp issuedAt, Timestamp expiration) {
        this.user = user;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiration = expiration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public User getCustomer() {
        return user;
    }

    public void setCustomer(User user) {
        this.user = user;
    }
}
