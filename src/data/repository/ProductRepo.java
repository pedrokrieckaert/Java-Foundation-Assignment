package src.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.pojo.Product;
import src.data.reader.CSVReader;

public class ProductRepo {
    private Map<Integer, Product> datastore = new HashMap<>();
    private static final List<String> ORD = new ArrayList<>() {
        {
            add("id");
            add("name");
            add("price");
            add("hours");
        }
    };
    private static final String FILE = "database/PhotoShop_PriceList.csv";

    public void create(Product product) {
        this.datastore.put(product.getId(), product.clone());
    }

    /**
     * Gets a product from this datastore with an input of either String or int.
     * Integer value should be validated prior to inputting.
     * @param input T
     * @return Product or null if a product isn't found.
     */
    public <T> Product retrieve(T input) {
        if (input instanceof Integer) {
            return this.datastore.get(Integer.parseInt(String.valueOf(input)));

        } else if (input instanceof String) {
            for (Product product : this.datastore.values()) {
                if (product.getName().equalsIgnoreCase(input.toString())) {
                    return product.clone();
                }
            }

        }

        return null;
    }

    /**
     * Gets a deep copy list of this datastore.
     * @return List of Product
     */
    public List<Product> retrieveAll() {
        List<Product> buffer = new ArrayList<Product>();

        for (Product product : this.datastore.values()) {
            buffer.add(product.clone());
        }

        return buffer;
    }

    /**
     * Uses the CSVReader to read the csv file for products and creates
     * a map of Product objects with the product id as keys.
     */
    public void load() {
        CSVReader<Product> reader = new CSVReader<Product>(Product.class, FILE, false, ";")
                .setOrder(ORD)
                .read();

        for (Product msg : reader.getData()) {
            create(msg);
        }
    }
}
