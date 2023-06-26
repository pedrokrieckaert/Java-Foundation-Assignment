package repository;

import pojo.CartItem;

import java.util.HashMap;
import java.util.Map;

public class CartItemRepo {
    private Map<Integer, CartItem> datastore = new HashMap<>();

    public void create(CartItem item) {this.datastore.put(item.getId(), item.clone());}

    public CartItem retrieve(int id) {return this.datastore.get(id).clone();}
}
