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
        Scanner scan = new Scanner(System.in);

        try {
            loadProducts();
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        Product test = promptForProduct(scan);

        System.out.println(test.getName());

        scan.close();
    }

    /**
     * Check if scanned line has input
     * @param input String
     * @return boolean
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    /**
     * Check if scanned int is within repository bounds
     * @param index int
     * @return boolean
     */
    public static boolean invalidIndex(int index) {
        return index < 0 || index > 11;
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

    /**
     * CLI prompt to retrieve a product from the repository by Id or Name
     * @param scan Scanner
     * @return Product
     */
    public static Product promptForProduct(Scanner scan) {
        while (true) {
            System.out.print("\nPlease enter the name or id of a product: ");
            //Check if the input is an integer
            if(scan.hasNextInt()) {
                int index = scan.nextInt();

                //Validate if the input is within repository bounds
                if (!invalidIndex(index)) {
                    return service.retrieveProductById(index);
                } else {
                    System.out.println("Id does not exist, please select a valid ID.");
                    scan.nextLine();
                    continue;
                }

            //If the input was a String
            } else {
                String input = scan.nextLine();
                Product temp; //Null Product for validation

                //Validate if the input is null or blank
                if (!isNullOrBlank(input)) {
                    temp = service.retrieveProductByName(input);
                } else {
                    System.out.println("Query is blank.");
                    continue;
                }

                //If the return from service was null, means that the input wasn't found in the product names
                if (temp == null) {
                    System.out.println("There is no product by that name.");
                } else {
                    //Return the product object once validated
                    return temp;
                }
            }
        }
    }
}
