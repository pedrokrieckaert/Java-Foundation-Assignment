package src.data.repository;

import src.pojo.Order;

import static src.data.reader.JSONReader.readOrder;
import static src.data.reader.JSONReader.saveOrder;

public class OrderRepo {
    private Order order;

    public void create(Order order, String file) {
        saveOrder(order, file);
    }

    public Order retrieve() {
        return this.order;
    }

    public void update(String file) {

    }
}
