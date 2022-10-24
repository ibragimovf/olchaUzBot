package uz.pdp.bot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.bot.model.Category;
import uz.pdp.bot.model.Product;
import uz.pdp.bot.model.dto.UserLoginDto;
import uz.pdp.bot.model.dto.UserRegisterDto;
import uz.pdp.bot.service.CategoryService;
import uz.pdp.bot.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceBot {
    private final static CategoryService categoryService = new CategoryService();
    private final static ProductService productService = new ProductService();

    public static InlineKeyboardMarkup getCategory(UserLoginDto userLoginDto, String parentId) {

        List<Category> categoryList = categoryService.getCategory(userLoginDto, parentId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        if (parentId != null) {
            if (categoryList.isEmpty()) {

                List<Product> productList = productService.getList(parentId);
                List<List<InlineKeyboardButton>> list = getProductKeyboard(productList);
                inlineKeyboardMarkup.setKeyboard(list);

            } else {

                List<List<InlineKeyboardButton>> list = getCategoryKeyboard(categoryList);
                inlineKeyboardMarkup.setKeyboard(list);

            }

        } else {

            List<List<InlineKeyboardButton>> list = getCategoryKeyboard(categoryList);
            inlineKeyboardMarkup.setKeyboard(list);

        }

        if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
            inlineKeyboardMarkup.getKeyboard().add(getCartButton());
        }

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getCategory(UserRegisterDto userRegisterDto) throws IOException {

        List<Category> categoryList = categoryService.getCategory(userRegisterDto);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getCategoryKeyboard(categoryList);
        inlineKeyboardMarkup.setKeyboard(list);

        if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
            inlineKeyboardMarkup.getKeyboard().add(getCartButton());
        }

        return inlineKeyboardMarkup;

    }

    public static List<InlineKeyboardButton> getCartButton() {

        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Cart \uD83D\uDED2");
        button.setCallbackData("caart");
        rowButtons.add(button);

        return rowButtons;
    }

    public static List<List<InlineKeyboardButton>> getCategoryKeyboard(List<Category> categoryList) {

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();

        for (Category category : categoryList) {

            InlineKeyboardButton button = new InlineKeyboardButton(category.getName());
            button.setCallbackData("categ" + category.getId());
            rowButtons.add(button);

            if (rowButtons.size() % 3 == 0) {
                list.add(rowButtons);
                rowButtons = new ArrayList<>();
            }

        }

        if (!rowButtons.isEmpty()) {
            list.add(rowButtons);
        }

        return list;
    }

    public static List<List<InlineKeyboardButton>> getProductKeyboard(List<Product> productList) {

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();

        for (Product product : productList) {

            InlineKeyboardButton button = new InlineKeyboardButton(product.getName());
            button.setCallbackData("produ" + product.getId());
            rowButtons.add(button);

            if (rowButtons.size() % 3 == 0) {
                list.add(rowButtons);
                rowButtons = new ArrayList<>();
            }

        }

        if (!rowButtons.isEmpty()) {
            list.add(rowButtons);
        }

        return list;
    }

    public static InlineKeyboardMarkup getCategory(List<Category> categoryList) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getCategoryKeyboard(categoryList);
        inlineKeyboardMarkup.setKeyboard(list);

        if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
            inlineKeyboardMarkup.getKeyboard().add(getCartButton());
        }

        return inlineKeyboardMarkup;
    }


}
