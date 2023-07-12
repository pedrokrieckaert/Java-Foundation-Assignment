package src.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private String orderDate;
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("EEEE dd-MM-yyyy HH:mm");
    private String pickUpDate;
    private int totalPrice;
    private int totalHours;
    private String pickUpTime;
    private String pickUpDay;
    private List<CartItem> cart;

    public Order(int totalPrice, int totalHours, List<CartItem> cart) {
        this.setTotalPrice(totalPrice);
        this.setTotalHours(totalHours);
        this.setCart(cart);
        this.setOrderDate();
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    /**
     * Checks to see if the cart is already populated and returns the cart variable if it is.
     * Otherwise, this returns a new cart to be populated.
     * @return A new ArrayList or an existing List of CartItem
     */
    public List<CartItem> getCart() {
        return Objects.requireNonNullElseGet(this.cart, ArrayList::new);
    }

    /**
     * Deep copies the parameter cart and sets this cart to the copy cart.
     * <p><i>How many more times can I write cart.</i></p>
     * @param source List of CartItem
     */
    public void setCart(List<CartItem> source) {
        List<CartItem> cart = new ArrayList<>();

        for (CartItem cartItem : source) {
            cart.add(cartItem.clone());
        }
        this.cart = cart;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getPickUpDay() {
        return pickUpDay;
    }

    public void setPickUpDay(String pickUpDay) {
        this.pickUpDay = pickUpDay;
    }

    public String getOrderDate() {
        return orderDate;
    }

    /**
     * Gets the current system time and date.
     * Formats it to the appropriate DateTimeFormat and sets this variable.
     */
    public void setOrderDate() {
        LocalDateTime now = LocalDateTime.now();
        this.orderDate = DTF.format(now);
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    /**
     * Custom toString method to only format relevant data to the String.
     * @return String of pickup day, date, and pick up time.
     */
    public String pickUpDataToString() {
        return this.pickUpDay + " " + this.pickUpDate + " " + this.pickUpTime;
    }
}
