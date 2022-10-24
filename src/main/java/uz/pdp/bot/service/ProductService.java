package uz.pdp.bot.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.bot.connection.UrlConnect;
import uz.pdp.bot.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {
    public List<Product> getList(String categoryId) {

        List<Product> products = new ArrayList<>();

        for (Product product : getProductList(categoryId)) {
            if (product.getCategory().getId() == Long.parseLong(categoryId)) {
                products.add(product);
            }
        }

        return products;
    }

    public Product getProduct(String id) {
        return new Gson().fromJson(UrlConnect.findByAllOrElseNull(id.substring(5), "/product").toString(), Product.class);
    }

    public List<Product> getProductList(String categoryId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return new ArrayList<>(Arrays.asList(gson.fromJson(
                UrlConnect.findByAllOrElseNull(categoryId, "/productList").toString(), Product[].class)));
    }
}
