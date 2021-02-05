package my.project.internetprovider.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @SequenceGenerator( name = "jpaAccountSequence", sequenceName = "JPA_ACCOUNTS_SEQUENCE", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "jpaAccountSequence")
    Long id;

    @OneToOne(mappedBy = "account")
    ProviderUser user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments;

    @ManyToMany
    Set<Rate> rates;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProviderUser getUser() {
        return user;
    }

    public void setUser(ProviderUser user) {
        this.user = user;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Builder newBuilder() {
        return new Account().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Account.this.id = id;

            return this;
        }

        public Builder setUser(ProviderUser user) {
            Account.this.user = user;

            return this;
        }

        public Builder setPayments(Set<Payment> payments) {
            Account.this.payments = payments;

            return this;
        }

        public Builder setRates(Set<Rate> rates) {
            Account.this.rates = rates;

            return this;
        }

        public Builder setActive(boolean active) {
            Account.this.active = active;

            return this;
        }

        public Account build() {
            return Account.this;
        }
    }
}
