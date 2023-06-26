import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import pojo.CartItem;
import pojo.OpeningHours;
import pojo.Product;
import repository.OpeningHoursRepo;
import repository.ProductRepo;
import service.OpeningHoursService;
import service.ProductService;

class Main {
    static ProductRepo productRepo = new ProductRepo();
    static ProductService productService = new ProductService(productRepo);
    static OpeningHoursRepo openingHoursRepo = new OpeningHoursRepo();
    static OpeningHoursService openingHoursService = new OpeningHoursService(openingHoursRepo);

    static Path[] paths = new Path[] {Paths.get("database/PhotoShop_OpeningHours.csv"), Paths.get("database/PhotoShop_PriceList.csv")};
    static String shoppingCart = "database/shoppingCart.json";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            loadProducts();
            loadOpeningHours();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        Product test = promptForProduct(scan);

        int productQuantity = 4;

        CartItem item = new CartItem(test, productQuantity);

        System.out.println((item));

        try {
            List<CartItem> cart = readOrder();
            cart.add(item);
            saveOrder(cart);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

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
     * Fetches product details from the database and stores it in the product repository
     * @throws IOException File Not Found
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
                Product product = new Product(Integer.parseInt(productData[0]),
                    productData[1],
                    new BigDecimal(productData[2]), 
                    Integer.parseInt(productData[3].substring(0, 2)) //Substring to take only first two characters of "00:00"
                );
                
                //Adds product object to the product repository through the product service
                productService.createProduct(product);
            });
    }

    /**
     * Fetches store opening hours from the database and stores it in the opening hours repository
     * @throws IOException File not found
     */
    public static void loadOpeningHours() throws IOException {
        Files.lines(paths[0])
            .forEach(line -> {
                String[] openingHoursData = line.split(";");

                //Validate that the first row contains valid data
                try {
                    Integer.parseInt(openingHoursData[0]);
                } catch (NumberFormatException nfe) {
                    return; //skip loop
                }

                OpeningHours openingHour = new OpeningHours(Integer.parseInt(openingHoursData[0]), 
                    openingHoursData[1], 
                    Integer.parseInt(openingHoursData[2].substring(0, 2)),
                    Integer.parseInt(openingHoursData[3].substring(0, 2))
                    );

                openingHoursService.createOpeningHours(openingHour);
            });
    }

    /**
     * CLI prompt to retrieve a product from the repository by id or Name
     * @param scan Scanner
     * @return Product
     */
    public static Product promptForProduct(Scanner scan) {
        System.out.print("\nPlease enter the name or id of a product: ");
        while (true) {
            //Check if the input is an integer
            if(scan.hasNextInt()) {
                int index = scan.nextInt();

                //Validate if the input is within repository bounds
                if (!invalidIndex(index)) {
                    return productService.retrieveProductById(index);
                } else {
                    System.out.println("Id does not exist, please select a valid ID.");
                    scan.nextLine();
                }

            //If the input was a String
            } else {
                String input = scan.nextLine();
                //Validate if the input is null or blank
                if (isNullOrBlank(input)) {
                    scan.skip("");
                    continue;
                }

                Product temp = productService.retrieveProductByName(input);
                
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

    /**
     * Checks if the JSON exists or is empty
     * @return boolean
     */
    public static boolean checkOrder() {
        File tempDir = new File(shoppingCart);

        if (!tempDir.exists()) return false;

        return tempDir.length() != 0;
    }

    /**
     * Reads a JSON file and stores the existing information into a List
     * @return List of Product or an empty List
     * @throws IOException File not found
     */
    public static List<CartItem> readOrder() throws IOException {
        if (checkOrder()) { //If the file exists and is not empty
            JsonReader reader = new JsonReader(new FileReader(shoppingCart));
            Type listOfProductObject = new TypeToken<List<CartItem>>() {}.getType();

            Gson gson = new Gson();

            return gson.fromJson(reader, listOfProductObject);
        } else { //If the file does not exist or is empty
            return new ArrayList<>();
        }
    }

    /**
     * Overwrites a list of products to a JSON file
     * @param cart List Product
     * @throws IOException File not found
     */
    public static void saveOrder(List<CartItem> cart) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Writer writer = new FileWriter(shoppingCart);

        gson.toJson(cart, writer); //Overwrites existing content

        writer.flush();
        writer.close();
    }
}
