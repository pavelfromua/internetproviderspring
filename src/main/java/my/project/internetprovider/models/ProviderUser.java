package my.project.internetprovider.models;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class ProviderUser {
    @Id
    @SequenceGenerator( name = "jpaUserSequence", sequenceName = "JPA_USERS_SEQUENCE", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "jpaUserSequence")
    private Long id;

    @NotEmpty(message = "new.user.login.notEmpty")
    @Size(min = 2, max = 30, message = "new.user.login.size")
    @Column(unique = true)
    private String login;

    @NotEmpty(message = "new.user.name.notEmpty")
    @Size(min = 2, max = 120, message = "new.user.name.size")
    private String name;

    @NotEmpty(message = "new.user.email.notEmpty")
    @Email(message = "new.user.email.beValid")
    private String email;

    @NotEmpty(message = "new.user.password.notEmpty")
    private String password;

    private boolean active;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id")
    private Account account;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private ProviderUser() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public static Builder newBuilder() {
        return new ProviderUser().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            ProviderUser.this.id = id;

            return this;
        }

        public Builder setLogin(String login) {
            ProviderUser.this.login = login;

            return this;
        }

        public Builder setName(String name) {
            ProviderUser.this.name = name;

            return this;
        }

        public Builder setEmail(String email) {
            ProviderUser.this.email = email;

            return this;
        }

        public Builder setPassword(String password) {
            ProviderUser.this.password = password;

            return this;
        }

        public Builder setActive(boolean active) {
            ProviderUser.this.active = active;

            return this;
        }

        public Builder setRoles(Set<Role> roles) {
            ProviderUser.this.roles = roles;

            return this;
        }

        public Builder setAccount(Account account) {
            ProviderUser.this.account = account;

            return this;
        }

        public ProviderUser build() {
            return ProviderUser.this;
        }
    }
}
