package my.project.internetprovider.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @SequenceGenerator( name = "jpaUserSequence", sequenceName = "JPA_USERS_SEQUENCE", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "jpaUserSequence")
    private Long id;

    @NotEmpty(message = "new.user.login.notEmpty")
    @Size(min = 2, max = 30, message = "new.user.login.size")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "new.user.name.notEmpty")
    @Size(min = 2, max = 120, message = "new.user.name.size")
    private String name;

    @NotEmpty(message = "new.user.email.notEmpty")
    @Email(message = "new.user.email.beValid")
    private String email;

    @NotEmpty(message = "new.user.password.notEmpty")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id")
    private Account account;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public static Builder newBuilder() {
        return new User().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            User.this.id = id;

            return this;
        }

        public Builder setUsername(String username) {
            User.this.username = username;

            return this;
        }

        public Builder setName(String name) {
            User.this.name = name;

            return this;
        }

        public Builder setEmail(String email) {
            User.this.email = email;

            return this;
        }

        public Builder setPassword(String password) {
            User.this.password = password;

            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            User.this.roles = roles;

            return this;
        }

        public Builder setAccount(Account account) {
            User.this.account = account;

            return this;
        }

        public User build() {
            return User.this;
        }
    }
}
