package src.data.repository;

import src.pojo.CartItem;

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

    public void update(int id, CartItem item) {
        datastore.put(id, item.clone());
    }

    public void delete(int id) {
        datastore.remove(id);
    }
}
