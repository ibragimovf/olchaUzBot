package uz.pdp.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import uz.pdp.bot.bot.CategoryServiceBot;
import uz.pdp.bot.bot.ExecuteBot;
import uz.pdp.bot.bot.ProductServiceBot;
import uz.pdp.bot.common.Constants;
import uz.pdp.bot.model.OrderProduct;
import uz.pdp.bot.model.Product;
import uz.pdp.bot.model.Status;
import uz.pdp.bot.model.dao.OrderProductDao;
import uz.pdp.bot.model.dto.UserLoginDto;
import uz.pdp.bot.model.dto.UserRegisterDto;
import uz.pdp.bot.service.OrderProductService;
import uz.pdp.bot.service.ProductService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MyBot extends TelegramLongPollingBot {
    private static final HashMap<String, Product> currentProduct = new HashMap<>();
    private static final HashMap<Long, Status> status = new HashMap<>();
    private static final HashMap<Long, String> contact = new HashMap<>();
    private static final HashMap<Long, UserRegisterDto> register = new HashMap<>();
    private static final HashMap<Long, String> passwordMap = new HashMap<>();
    private static final HashMap<Integer, OrderProduct> currentOrder = new HashMap<>();
    private static final HashMap<String, ArrayList<Integer>> deleteListList = new HashMap<>();

    @Override
    public String getBotUsername() {
        return Constants.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_SECRET_KEY;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        int count = 1;

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (!deleteListList.isEmpty() && deleteListList.containsKey(message.getChatId().toString())) {
                deletCallback(message.getChatId().toString());
            }

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            System.out.println(message.getText());

            if (message.hasText()) {
                if (message.getText().equals("/start")) {
                    execute(ExecuteBot.execute(message, "Send username"));
                    status.put(message.getChatId(), Status.USERNAME);

                } else if (status.get(message.getChatId()) != null
                        && status.get(message.getChatId()).equals(Status.PASSWORD)
                        || status.get(message.getChatId()).equals(Status.REGISTER_PASSWORD)) {

                    InlineKeyboardMarkup category;
                    if (status.get(message.getChatId()).equals(Status.PASSWORD)) {

                        status.put(message.getChatId(), Status.CATEGORY);
                        passwordMap.put(message.getChatId(), message.getText());
                        category = CategoryServiceBot.getCategory(
                                new UserLoginDto(message.getText(), contact.get(message.getChatId())), null);

                        if (!category.getKeyboard().isEmpty()) {

                            Integer messageIds = execute(ExecuteBot.execute(
                                    message.getChatId(),
                                    "Select Category", category)).getMessageId();
                            addToDelete(messageIds, message.getChatId().toString());

                        } else {

                            status.put(message.getChatId(), Status.REGISTER);
                            execute(ExecuteBot.execute(message, "Send full name"));

                        }
                    }

                    if (status.get(message.getChatId()).equals(Status.REGISTER_PASSWORD)) {

                        UserRegisterDto userRegisterDto = register.get(message.getChatId());
                        register.put(message.getChatId(), userRegisterDto);
                        userRegisterDto.setPassword(message.getText());
                        category = CategoryServiceBot.getCategory(userRegisterDto);

                        if (!category.getKeyboard().isEmpty()) {

                            Integer messageIds = execute(ExecuteBot.execute(
                                    message.getChatId(),
                                    "Select Category", category)).getMessageId();
                            addToDelete(messageIds, message.getChatId().toString());

                        } else {

                            status.put(message.getChatId(), Status.REGISTER);
                            execute(ExecuteBot.execute(
                                    message, "There is already a user with this username"));

                        }
                    }

                } else if (status.get(message.getChatId()) != null
                        && status.get(message.getChatId()).equals(Status.USERNAME)) {

                    SendMessage sendMessage = ExecuteBot.execute(message, "Send password");
                    execute(sendMessage);
                    status.put(message.getChatId(), Status.PASSWORD);
                    contact.put(message.getChatId(), message.getText());

                } else if (status.get(message.getChatId()) != null
                        && status.get(message.getChatId()).equals(Status.REGISTER)) {

                    SendMessage sendMessage = ExecuteBot.execute(message, "Send username");
                    execute(sendMessage);

                    status.put(message.getChatId(), Status.REGISTER_USERNAME);
                    register.put(message.getChatId(), register(message.getText()));

                } else if (status.get(message.getChatId()) != null
                        && status.get(message.getChatId()).equals(Status.REGISTER_USERNAME)) {

                    SendMessage sendMessage = ExecuteBot.execute(message, "Send password");
                    execute(sendMessage);

                    status.put(message.getChatId(), Status.REGISTER_PASSWORD);
                    UserRegisterDto userRegisterDto = register.get(message.getChatId());
                    userRegisterDto.setUsername(message.getText());
                    register.put(message.getChatId(), userRegisterDto);

                }
            }

        } else if (update.hasCallbackQuery()) {

            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            String chatId = callbackQuery.getMessage().getChatId().toString();
            System.out.println(callbackQuery.getFrom().getFirstName() + " = " + data);
            addToDelete(messageId, chatId);

            Product product = null;
            if (data.startsWith("produ")) {
                product = new ProductService().getProduct(data);
                currentProduct.put(chatId, product);
            } else if (currentProduct.get(chatId) != null) {
                product = currentProduct.get(chatId);
            }
            InlineKeyboardMarkup inlineKeyboardMarkup;

            switch (data.substring(0, 5)) {

                case "categ":
                    execute(ExecuteBot.execute(
                            callbackQuery,
                            "Select Category",
                            CategoryServiceBot.getCategory(
                                    new UserLoginDto(passwordMap.get(callbackQuery.getMessage().getChatId()),
                                            contact.get(callbackQuery.getMessage().getChatId())),
                                    data.substring(5))));
                    break;

                case "produ":
                    product = currentProduct.get(chatId);
                    if (product != null) {
                        inlineKeyboardMarkup = ProductServiceBot.getCaption();
                        Integer messageIds2 = execute(SendPhoto.builder().photo(new InputFile().setMedia(
                                        new File("C:\\Java\\exam\\olchaUz\\src\\main\\resources\\static" +
                                                product.getImgUrl()))).caption(" \n" + product.getName() + "\n\n" +
                                        count + " unit costs " + product.getPrice() + "$ \n" + "\nThe total cost is " +
                                        (product.getPrice() * count) + "$").chatId(chatId).
                                replyMarkup(inlineKeyboardMarkup).build()).getMessageId();

                        if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                            deletCallback(chatId);
                        }

                        addToDelete(messageIds2, chatId);
                    }
                    break;

                case "back1":
                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }
                    Integer messageIds2 = execute(ExecuteBot.execute(
                            callbackQuery.getMessage().getChatId(), "Select Category",
                            CategoryServiceBot.getCategory(
                                    new UserLoginDto(passwordMap.get(callbackQuery.getMessage().getChatId()),
                                            contact.get(callbackQuery.getMessage().getChatId())),
                                    null))).getMessageId();
                    addToDelete(messageIds2, chatId);
                    break;

                case "back2":
                    execute(ExecuteBot.execute(
                            callbackQuery,
                            "Select Category",
                            CategoryServiceBot.getCategory(
                                    new UserLoginDto(passwordMap.get(callbackQuery.getMessage().getChatId()),
                                            contact.get(callbackQuery.getMessage().getChatId())), null)));
                    break;

                case "pluus":
                    count = (Integer.parseInt(data.substring(5)) + 1);
                    currentOrder.put(messageId,
                            new OrderProduct(product, callbackQuery.getMessage().getChatId(), count));

                    execute(ExecuteBot.execute(ProductServiceBot.getCaption(count),
                            callbackQuery, " \n" + product.getName() + "\n\n" + count +
                                    " unit costs " + product.getPrice() + "$ \n" +
                                    "\nThe total cost is " + (product.getPrice() * count) + "$"));
                    break;

                case "count":
                    break;

                case "Ocont":
                    break;

                case "minus":
                    count = (Integer.parseInt(data.substring(5)) - 1);
                    if (count < 1) {
                        return;
                    }
                    currentOrder.put(messageId, new OrderProduct(product,
                            callbackQuery.getMessage().getChatId(), count));

                    execute(ExecuteBot.execute(ProductServiceBot.getCaption(count),
                            callbackQuery, " \n" + product.getName() + "\n\n" + count + " unit costs " +
                                    product.getPrice() + "$ \n" +
                                    "\nThe total cost is " + (product.getPrice() * count) + "$"));
                    break;

                case "buyyy":
                    if (currentOrder.get(messageId) == null) {
                        currentOrder.put(messageId, new OrderProduct(
                                callbackQuery.getMessage().getChatId(),
                                product, contact.get(Long.parseLong(chatId)), 1));
                    }

                    execute(ExecuteBot.execute(callbackQuery.getMessage(),
                            "Item added to cart:\n" + product.getName() + " x " +
                                    currentOrder.get(messageId).getCount() + " pcs = " +
                                    (product.getPrice() * currentOrder.get(messageId).getCount())));

                    execute(ExecuteBot.execute(callbackQuery.getMessage().getChatId(),
                            "Select Category", CategoryServiceBot.getCategory(
                                    new OrderProductService().add(currentOrder.get(messageId)))));

                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }

                    break;

                case "caart":
                    List<OrderProductDao> list = new OrderProductService().getBasket(
                            contact.get(callbackQuery.getMessage().getChatId()));
                    String str = new OrderProductService().getStr(list);

                    execute(ExecuteBot.execute(callbackQuery.getMessage().getChatId(),
                            str, ProductServiceBot.getCart(list)));

                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }
                    break;

                case "delet":
                    list = new OrderProductService().deleteBasket(data.substring(5),
                            contact.get(callbackQuery.getMessage().getChatId()));
                    str = new OrderProductService().getStr(list);

                    execute(ExecuteBot.execute(callbackQuery.getMessage().getChatId(),
                            str, ProductServiceBot.getCart(list)));

                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }
                    break;

                case "Oplus":
                    list = new OrderProductService().plusBasket(data.substring(5),
                            contact.get(callbackQuery.getMessage().getChatId()));
                    str = new OrderProductService().getStr(list);
                    execute(ExecuteBot.execute(callbackQuery.getMessage().getChatId(),
                            str, ProductServiceBot.getCart(list)));

                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }
                    break;

                case "Omins":
                    list = new OrderProductService().minusBasket(data.substring(5),
                            contact.get(callbackQuery.getMessage().getChatId()));
                    str = new OrderProductService().getStr(list);
                    execute(ExecuteBot.execute(callbackQuery.getMessage().getChatId(),
                            str, ProductServiceBot.getCart(list)));

                    if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
                        deletCallback(chatId);
                    }
                    break;

                default:
                    inlineKeyboardMarkup = CategoryServiceBot.getCategory(
                            new UserLoginDto(passwordMap.get(callbackQuery.getMessage().getChatId()),
                                    contact.get(callbackQuery.getMessage().getChatId())), data);

                    if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
                        execute(ExecuteBot.execute(callbackQuery, "Select Category", inlineKeyboardMarkup));

                    } else {
                        inlineKeyboardMarkup = ProductServiceBot.getProduct(data);

                        if (!inlineKeyboardMarkup.getKeyboard().isEmpty()) {
                            execute(ExecuteBot.execute(callbackQuery, "Choose a product", inlineKeyboardMarkup));
                        }
                    }
                    break;
            }

        }
    }


    private void addToDelete(Integer messageId, String chatId) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(messageId);
        if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
            integers.addAll(deleteListList.get(chatId));
        }
        deleteListList.put(chatId, integers);
    }

    @SneakyThrows
    private void deletCallback(String chatId) {
        ArrayList<Integer> integers = deleteListList.get(chatId);
        if (integers.size() > 0) {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, integers.get(0));
            execute(deleteMessage);

            for (int i = 1; i < deleteListList.size(); i++) {
                if (!Objects.equals(integers.get(i - 1), integers.get(i))) {
                    deleteMessage = new DeleteMessage(chatId, integers.get(i));
                    execute(deleteMessage);
                }
            }
            deleteListList.remove(chatId);
        }
    }

    public static UserRegisterDto register(String fullName) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setFullName(fullName);
        return userRegisterDto;
    }

}
