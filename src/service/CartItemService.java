package src.service;

import src.data.pojo.CartItem;
import src.data.repository.CartItemRepo;

public class CartItemService {
    CartItemRepo itemRepo = new CartItemRepo();

    public CartItemService(CartItemRepo repo) {this.itemRepo = repo;}

    public CartItemService() {

    }

    public void addCartItem(CartItem item) {this.itemRepo.create(item);}

    public CartItem getCartItem(int id) {return this.itemRepo.retrieve(id);}
}
