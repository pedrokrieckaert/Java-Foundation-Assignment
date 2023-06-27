package src.data.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    
    //Product id is implicit to the index in the storage array
    private int id;
    private String name;
    private BigDecimal price;
    private Date hours;

    public Product(int id, String name, BigDecimal price, Date hours) {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setHours(hours);
    }

    public Product() {

    }

    public Product clone() {
        return new Product(this.getId(), this.getName(), this.getPrice(), this.getHours());
    }

    public int getId() {return this.id;};

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("Id cannot be less than 0.");
        else this.id = id;
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

    public Date getHours() {
        return this.hours;
    }

    public void setHours(Date hours) {
        this.hours = hours;
    }
}
