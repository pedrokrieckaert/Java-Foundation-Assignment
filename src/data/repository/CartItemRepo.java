package src.data.repository;

import src.data.pojo.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemRepo {
    private Map<Integer, CartItem> datastore = new HashMap<>();

    public List<Integer> getKeys() {
        return new ArrayList<Integer>(datastore.keySet());
    }

    public void create(CartItem item) {
        this.datastore.put(item.getId(), item.clone());
    }

    public CartItem retrieve(int id) {
        return this.datastore.get(id).clone();
    }
    //currently only adds
    public void update(int id, CartItem item) {
        CartItem temp = datastore.get(id).clone();

        temp.setAmount(temp.getAmount() + item.getAmount());

        datastore.put(temp.getId(), temp.clone());
    }
}
