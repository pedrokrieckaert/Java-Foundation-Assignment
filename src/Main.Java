package src;

import src.data.repository.CartItemRepo;
import src.data.repository.OpeningHoursRepo;
import src.data.repository.ProductRepo;
import src.presentation.UI;
import src.service.CartItemService;
import src.service.OpeningHoursService;
import src.service.ProductService;

class Main {
    static ProductRepo productRepo = new ProductRepo();
    static ProductService productService = new ProductService(productRepo);
    static OpeningHoursRepo openingHoursRepo = new OpeningHoursRepo();
    static OpeningHoursService openingHoursService = new OpeningHoursService(openingHoursRepo);
    static CartItemRepo cartItemRepo = new CartItemRepo();
    static CartItemService cartItemService = new CartItemService(cartItemRepo);
    static String shoppingCart = "database/shoppingCart.json";
    static UI ui = new UI();

    public static void main(String[] args) {
        ui.testProcess();

    }
}
