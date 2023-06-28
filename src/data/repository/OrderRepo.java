package src.data.repository;

import src.data.pojo.Order;

import java.io.IOException;

import static src.data.reader.JSONReader.readOrder;
import static src.data.reader.JSONReader.saveOrder;

public class OrderRepo {
    private Order order;

    public void update(String file) {

    }
    public void retrieve(String file) {
        try {
            order = readOrder(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
