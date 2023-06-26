package src.data.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import src.data.pojo.CartItem;
import src.data.pojo.Order;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    /**
     * Checks if the JSON exists or is empty
     * @param target String - JSON file to check
     * @return boolean
     */
    public static boolean checkOrder(String target) {
        File tempDir = new File(target);

        if (!tempDir.exists()) return false;

        return tempDir.length() != 0;
    }

    /**
     * Reads a JSON file and stores the existing information into a List
     * @param source String - JSON file to read
     * @return List of Product or an empty List
     * @throws IOException File not found
     */
    public static List<CartItem> readOrder(String source) throws IOException {
        if (checkOrder(source)) { //If the file exists and is not empty
            JsonReader reader = new JsonReader(new FileReader(source));
            Type listOfProductObject = new TypeToken<Order>() {}.getType();

            Gson gson = new Gson();

            return gson.fromJson(reader, listOfProductObject);
        } else { //If the file does not exist or is empty
            return new ArrayList<>();
        }
    }

    /**
     * Overwrites a list of products to a JSON file
     * @param cart List Product
     * @param target String - JSON file to write to.
     * @throws IOException File not found
     */
    public static void saveOrder(JsonObject cart, String target) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Writer writer = new FileWriter(target);

        gson.toJson(cart, writer); //Overwrites existing content

        writer.flush();
        writer.close();
    }
}
