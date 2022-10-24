package uz.pdp.bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProduct {

    private Long id;
    private Long customerId;
    private Product product;
    private String username;
    private int count;
    private Boolean isBuyed;

    public OrderProduct(Long customerId, Product product, String username, int count) {
        this.customerId = customerId;
        this.product = product;
        this.username = username;
        this.count = count;
    }

    public OrderProduct(Product product, Long customerId, int count) {
        this.product = product;
        this.customerId = customerId;
        this.count = count;
    }
}
