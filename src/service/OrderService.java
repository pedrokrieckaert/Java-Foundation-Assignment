package src.service;

import src.data.pojo.CartItem;
import src.data.pojo.Order;
import src.data.repository.OrderRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    static OrderRepo orderRepo = new OrderRepo();

    public OrderService () {orderRepo.retrieve("database/shoppingCart.json");}

    public Order create (BigDecimal totalPrice, int totalHours, List<CartItem> cart) {
        return new Order(totalPrice.intValue(), totalHours, cart);
    }

    public void addItem(CartItem item) {

    }
}
