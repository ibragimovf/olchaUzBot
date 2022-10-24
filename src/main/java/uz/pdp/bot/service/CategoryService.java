package uz.pdp.bot.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.bot.connection.UrlConnect;
import uz.pdp.bot.model.Category;
import uz.pdp.bot.model.dto.UserLoginDto;
import uz.pdp.bot.model.dto.UserRegisterDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryService {
    public List<Category> getCategory(UserLoginDto userLoginDto, String parentId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Category> categories = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>(Arrays.asList(gson.fromJson(
                UrlConnect.loginUser(userLoginDto).toString(), Category[].class)));

        for (Category category : categoryList) {
            if (category.getParentId() == null && parentId == null) {
                categories.add(category);
            } else if (category.getParentId() != null && category.getParentId().toString().equals(parentId)) {
                categories.add(category);
            }
        }

        return categories;
    }

    public List<Category> getCategory(UserRegisterDto userRegisterDto) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Category> categories = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>(Arrays.asList(gson.fromJson(
                UrlConnect.registerUser(userRegisterDto).toString(), Category[].class)));

        for (Category category : categoryList) {
            if (category.getParentId() == null) {
                categories.add(category);
            }
        }

        return categories;
    }
}
