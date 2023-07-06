package src.data.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import src.data.pojo.Product;
import src.data.reader.CSVReader;

public class ProductRepo {
    //Store products in array list, the product id is intrinsic to the index
    private ArrayList<Product> datastore = new ArrayList<>();
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
        this.datastore.add(product.clone());
    }

    public Product retrieveById(int index) {
        return this.datastore.get(index).clone();
    }

    public Product retrieveByName(String name) {
        for (Product product : this.datastore) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product.clone();
            }
        }
        return null;
    }

    public <T> Product retrieve(T input) {
        if (input instanceof Integer) {
            return this.datastore.get(Integer.parseInt(String.valueOf(input)));

        } else if (input instanceof String) {
            for (Product product : this.datastore) {
                if (product.getName().equalsIgnoreCase(input.toString())) {
                    return product.clone();
                }
            }

        }

        return null;
    }

    public List<Product> retrieveAll() {
        List<Product> buffer = new ArrayList<Product>();

        for (Product product : this.datastore) {
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
