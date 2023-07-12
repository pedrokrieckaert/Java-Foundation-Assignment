package src.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.pojo.OpeningHours;
import src.data.reader.CSVReader;

public class OpeningHoursRepo {
    //Map the opening hours using day number as the key (1-7)(Sunday-Saturday)
    private Map<Integer, OpeningHours> datastore = new HashMap<>();
    private static final List<String> ORD = new ArrayList<String>() {
        {
            add("dayNumber");
            add("day");
            add("openHour");
            add("closeHour");
        }
    };
    private static final String FILE = "database/PhotoShop_OpeningHours.csv";

    public void create(OpeningHours hours) {
        this.datastore.put(hours.getDayNumber(), hours.clone());
    }

    public OpeningHours retrieve(int id) {
        return this.datastore.get(id).clone();
    }

    /**
     * Uses the CSVReader to read the csv file for opening hours and creates
     * a map of OpeningHours objects with the day number as keys.
     */
    public void load() {
        CSVReader<OpeningHours> reader = new CSVReader<OpeningHours>(OpeningHours.class, FILE, true, ";")
                .setOrder(ORD)
                .read();
        for (OpeningHours msg : reader.getData()) {
            create(msg);
        }
    }
}
