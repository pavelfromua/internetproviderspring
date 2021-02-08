package my.project.internetprovider.models.dto;

import my.project.internetprovider.models.Plan;
import my.project.internetprovider.models.Product;

public class AccountProductPlanDto {
    private Product product;
    private Plan plan;
    private String description;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
