package uz.pdp.bot.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bot.model.Product;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductDao {
    private Long id;
    private UserDao userEntity;
    private Product productEntity;
    private Integer count;
    private Boolean isBuyed;
}
