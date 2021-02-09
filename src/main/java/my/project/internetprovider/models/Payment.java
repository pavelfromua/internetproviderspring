package my.project.internetprovider.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @SequenceGenerator( name = "jpaPaymentSequence", sequenceName = "JPA_PAYMENTS_SEQUENCE", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "jpaPaymentSequence")
    private Long id;
    private String name;
    private Double amount;
    private LocalDateTime date;
    private FlowDirection fd;

    @ManyToOne
    Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FlowDirection getFd() {
        return fd;
    }

    public void setFd(FlowDirection fd) {
        this.fd = fd;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static Builder newBuilder() {
        return new Payment().new Builder();
    }

    public class Builder {
        private Builder() {}

        public Builder setId(Long id) {
            Payment.this.id = id;

            return this;
        }

        public Builder setName(String name) {
            Payment.this.name = name;

            return this;
        }

        public Builder setAmount(Double amount) {
            Payment.this.amount = amount;

            return this;
        }

        public Builder setDate(LocalDateTime date) {
            Payment.this.date = date;

            return this;
        }

        public Builder setFd(FlowDirection fd) {
            Payment.this.fd = fd;

            return this;
        }

        public Builder setAccount(Account account) {
            Payment.this.account = account;

            return this;
        }

        public Payment build() {
            return Payment.this;
        }
    }
}
