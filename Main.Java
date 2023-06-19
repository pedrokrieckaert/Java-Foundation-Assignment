import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import pojo.Product;
import repository.ProductRepo;

public class Main {

    static Path[] paths = new Path[] {Paths.get("database/PhotoShop_OpeningHours.csv"), Paths.get("database/PhotoShop_PriceList.csv")};

    public static void main(String[] args) {
        ProductRepo repo = new ProductRepo();
        try {
            Files.lines(paths[1])
                .forEach(line -> {
                    /* productData[]
                     * [1] - Product Name
                     * [2] - Product Price
                     * [3] - Product Hours
                     */
                    String[] productData = line.split(";");
                    
                    //Create product object
                    Product product = new Product(productData[1], new BigDecimal(productData[2]), Integer.parseInt(productData[3].substring(0, 2)));
                    
                    //Adds product object to the product repository
                    repo.createProduct(product);

                });
        } catch(FileNotFoundException e) {
            System.out.println(e);
        } catch(IOException e) {
            System.out.println(e);
        }

        System.out.println(repo.retrieveProduct(0).getName());
    }
}
