package src.data.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import src.data.pojo.Product;

public class ProductRepo {
    //Store products in array list, the product id is intrinsic to the index
    private ArrayList<Product> datastore = new ArrayList<>();

    public void createProduct(Product product) {
        this.datastore.add(product.clone());
    }

    public Product retrieveProductById(int index) {
        return this.datastore.get(index).clone();
    }

    public Product retrieveProductByName(String name) {
        for (Product product : this.datastore) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product.clone();
            }
        }
        return null;
    }

    /**
     * Fetches product details from the database and stores it in the product src.data.repository
     * @throws IOException File Not Found
     */
    public static void loadProducts(Path path) throws IOException {
        Files.lines(path)
                .forEach(line -> {
                    /* productData[]
                     * [1] - Product Name
                     * [2] - Product Price
                     * [3] - Product Hours
                     */
                    String[] productData = line.split(";");

                    //Create product object
                    Product product = new Product(Integer.parseInt(productData[0]),
                            productData[1],
                            new BigDecimal(productData[2]),
                            Integer.parseInt(productData[3].substring(0, 2)) //Substring to take only first two characters of "00:00"
                    );

                    //Adds product object to the product src.data.repository through the product src.service
                    //productService.createProduct(product);
                });
    }
}
