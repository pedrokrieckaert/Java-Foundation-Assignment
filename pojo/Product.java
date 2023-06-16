package pojo;

import java.math.BigDecimal;

public class Product {
    
    //Product id is implicit to the index in the storage array
    private String name;
    private BigDecimal price;
    private int hours;

    public Product(String name, BigDecimal price, int hours) {
        setName(name);
        setPrice(price);
        setHours(hours);
    }

    public Product(Product source) {
        setName(source.name);
        setPrice(source.price);
        setHours(source.hours);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be null or blank.");
        else this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price cannot be less than 0.");
        else this.price = price;
    }

    public int getHours() {
        return this.hours;
    }

    public void setHours(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be less than 0.");
        else this.hours = hours;
    }
}
