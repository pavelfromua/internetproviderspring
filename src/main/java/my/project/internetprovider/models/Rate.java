package my.project.internetprovider.models;

import my.project.internetprovider.repositories.ProviderServiceRepository;
import my.project.internetprovider.repositories.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @SequenceGenerator( name = "jpaRateSequence", sequenceName = "JPA_RATES_SEQUENCE", allocationSize = 1, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "jpaRateSequence")
    private Long id;

    @NotEmpty(message = "new.product.rate.name.notEmpty")
    @Size(min = 2, max = 120, message = "new.product.rate.name.size")
    private String name;

    @NotNull(message = "new.product.rate.price.notEmpty")
    private Double price;

    @ManyToOne
    private ProviderService service;

    @ManyToMany
    private Set<Account> accounts;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProviderService getService() {
        return service;
    }

    public void setService(ProviderService service) {
        this.service = service;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
