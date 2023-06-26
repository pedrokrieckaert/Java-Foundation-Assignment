package service;

import pojo.CartItem;
import repository.CartItemRepo;

public class CartItemService {
    CartItemRepo itemRepo;

    public CartItemService (CartItemRepo repo) {this.itemRepo = repo;}

    public void addCartItem(CartItem item) {this.itemRepo.create(item);}

    public CartItem getCartItem(int id) {return this.itemRepo.retrieve(id);}
}
