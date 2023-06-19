import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import pojo.Product;
import repository.ProductRepo;
import service.ProductService;

public class Main {
    static ProductRepo repo = new ProductRepo();
    static ProductService service = new ProductService(repo);

    static Path[] paths = new Path[] {Paths.get("database/PhotoShop_OpeningHours.csv"), Paths.get("database/PhotoShop_PriceList.csv")};

    public static void main(String[] args) {
        try {
            loadProducts();
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        Scanner scan = new Scanner(System.in);

        System.out.println("id: ");
        int tempIndex = scan.nextInt();
        scan.nextLine();
        System.out.println(service.retrieveProductById(tempIndex).getName());

        System.out.println("name: ");
        String tempName = scan.nextLine();
        System.out.println(service.retrieveProductByName(tempName).getName());

        scan.close();
    }

    /**
     * Fetches product details from the database and stores it in the product repositroy
     * @throws IOException
     */
    public static void loadProducts() throws IOException {
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
                
                //Adds product object to the product repository through the product service
                service.createProduct(product);
            });
    }
}
