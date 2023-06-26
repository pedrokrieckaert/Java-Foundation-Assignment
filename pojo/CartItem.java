package pojo;

import java.math.BigDecimal;

public class CartItem extends Product{
    private int amount;

    public CartItem(int id, String name, BigDecimal price, int hours, int amount) {
        super(id, name, price, hours);
        this.setAmount(amount);
    }

    public CartItem(Product product, int amount){
        super(product.getId(), product.getName(), product.getPrice(), product.getHours());
        this.setAmount(amount);
    }

    public CartItem clone() {return new CartItem(super.getId(), super.getName(), super.getPrice(), super.getHours(), this.getAmount());}

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be less than 0.");
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + "\nPrice: " + getPrice() + "\nHours: " + getHours() + "\nAmount: " + getAmount();
    }
}
