package uz.pdp.bot.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.bot.connection.UrlConnect;
import uz.pdp.bot.model.Category;
import uz.pdp.bot.model.OrderProduct;
import uz.pdp.bot.model.dao.OrderProductDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderProductService {

    public List<Category> add(OrderProduct orderProduct) {
        System.out.println("orderProduct = " + orderProduct);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Category> categories = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>(Arrays.asList(gson.fromJson
                (UrlConnect.addOrderToCart(orderProduct, "/addOrderToCart").toString(), Category[].class)));
        for (Category category : categoryList) {
            if (category.getParentId() == null) {
                categories.add(category);
            }
        }
        return categories;
    }

    public List<OrderProductDao> getBasket(String username) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new ArrayList<>(Arrays.asList(gson.fromJson
                (UrlConnect.findByAllOrElseNull(username, "/getCarts").toString(), OrderProductDao[].class)));
    }

    public List<OrderProductDao> deleteBasket(String deleteOrderId, String username) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new ArrayList<>(Arrays.asList(gson.fromJson
                (UrlConnect.findByAllOrElseNull(deleteOrderId, username, "/deleteBasket").toString(), OrderProductDao[].class)));
    }

    public List<OrderProductDao> plusBasket(String plusOrderId, String username) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new ArrayList<>(Arrays.asList(gson.fromJson
                (UrlConnect.findByAllOrElseNull(plusOrderId, username, "/plusBasket").toString(), OrderProductDao[].class)));
    }

    public List<OrderProductDao> minusBasket(String plusOrderId, String username) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return new ArrayList<>(Arrays.asList(gson.fromJson
                (UrlConnect.findByAllOrElseNull(plusOrderId, username, "/minusBasket").toString(), OrderProductDao[].class)));
    }


    public String getStr(List<OrderProductDao> list) {

        if (list.isEmpty()) {
            return "Your cart is empty";
        }

        StringBuilder str = new StringBuilder();
        double total = 0;
        for (OrderProductDao orderProduct : list) {
            double sum = orderProduct.getProductEntity().getPrice() * orderProduct.getCount();
            total += sum;
            str.append(" \n").append(orderProduct.getProductEntity().getName()).append(" ✖️ ").append(orderProduct.getCount()).append(" \uD83D\uDFF0 ").append(sum).append("\uD83D\uDCB2 \n");
        }
        str.append("\nTotal price = ").append(total).append("\uD83D\uDCB2 \n");
        return str.toString();
    }
}
