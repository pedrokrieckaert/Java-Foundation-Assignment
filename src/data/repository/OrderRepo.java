package src.data.repository;

import src.data.pojo.Order;

import java.io.IOException;

import static src.data.reader.JSONReader.readOrder;
import static src.data.reader.JSONReader.saveOrder;

public class OrderRepo {
    private Order order;

    public void create(String file) {
        try {
            order = readOrder(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Order retrieve() {
        return this.order;
    }

    public void update(String file) {

    }
}