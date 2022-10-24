package uz.pdp.bot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.bot.model.Product;
import uz.pdp.bot.model.dao.OrderProductDao;
import uz.pdp.bot.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceBot {

    private final static ProductService productService = new ProductService();

    public static InlineKeyboardMarkup getProduct(String categoryId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Product> productList = productService.getList(categoryId);

        List<List<InlineKeyboardButton>> list = CategoryServiceBot.getProductKeyboard(productList);
        inlineKeyboardMarkup.setKeyboard(list);

        if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
            List<InlineKeyboardButton> rowButtons2 = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton("Back to Category ↩");
            button.setCallbackData("back2");

            rowButtons2.add(button);
            list.add(rowButtons2);
        }

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCaption(int count) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(" ➕ ");
        button1.setCallbackData("pluus" + count);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(String.valueOf(count));
        button2.setCallbackData("count");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(" ➖ ");
        button3.setCallbackData("minus" + count);

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Purchase \uD83D\uDCB5");
        button5.setCallbackData("buyyy");

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(button3);
        buttonList1.add(button2);
        buttonList1.add(button1);

        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        buttonList2.add(button5);

        List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Back ↩");
        button.setCallbackData("back1");
        rowButtons3.add(button);

        list.add(buttonList1);
        list.add(buttonList2);
        list.add(rowButtons3);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCaption() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Add to cart \uD83D\uDED2");
        button5.setCallbackData("buyyy");

        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        buttonList2.add(button5);

        List<InlineKeyboardButton> rowButtons3 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Back ↩");
        button.setCallbackData("back1");
        rowButtons3.add(button);

        list.add(buttonList2);
        list.add(rowButtons3);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getCart(List<OrderProductDao> orderProducts) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (OrderProductDao order : orderProducts) {
            InlineKeyboardButton button5 = new InlineKeyboardButton();
            button5.setText(" ✖️" + order.getProductEntity().getName());
            button5.setCallbackData("delet" + order.getId());

            InlineKeyboardButton button1 = new InlineKeyboardButton();
            button1.setText(" ➕ ");
            button1.setCallbackData("Oplus" + order.getId());

            InlineKeyboardButton button2 = new InlineKeyboardButton();
            button2.setText(String.valueOf(order.getCount()));
            button2.setCallbackData("Ocont");

            InlineKeyboardButton button3 = new InlineKeyboardButton();
            button3.setText(" ➖ ");
            button3.setCallbackData("Omins" + order.getId());

            List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
            buttonList2.add(button3);
            buttonList2.add(button2);
            buttonList2.add(button1);

            List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
            buttonList1.add(button5);

            rowList.add(buttonList1);
            rowList.add(buttonList2);
            inlineKeyboardMarkup.setKeyboard(rowList);
        }

        if (orderProducts.isEmpty()) {
            InlineKeyboardButton button5 = new InlineKeyboardButton("Back to category ↩");
            button5.setCallbackData("back1");

            List<InlineKeyboardButton> buttonList3 = new ArrayList<>();
            buttonList3.add(button5);
            rowList.add(buttonList3);
            inlineKeyboardMarkup.setKeyboard(rowList);
        }

        return inlineKeyboardMarkup;
    }

}
