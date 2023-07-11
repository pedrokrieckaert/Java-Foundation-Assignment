package src.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.pojo.Product;
import src.data.reader.CSVReader;

public class ProductRepo {
    private Map<Integer, Product> datastore = new HashMap<>();
    private List<String> ord = new ArrayList<>() {
        {
            add("id");
            add("name");
            add("price");
            add("hours");
        }
    };
    private String file = "database/PhotoShop_PriceList.csv";

    public void create(Product product) {
        this.datastore.put(product.getId(), product.clone());
    }

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

    public List<Product> retrieveAll() {
        List<Product> buffer = new ArrayList<Product>();

        for (Product product : this.datastore.values()) {
            buffer.add(product.clone());
        }

        return buffer;
    }

    public void load() {
        CSVReader<Product> reader = new CSVReader<Product>(Product.class, file, false, ";")
                .setOrder(ord)
                .read();

        for (Product msg : reader.getData()) {
            create(msg);
        }
    }
}
