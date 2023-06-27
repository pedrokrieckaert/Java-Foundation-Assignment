package src.data.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.data.pojo.OpeningHours;
import src.data.reader.CSVReader;

public class OpeningHoursRepo {
    //Map the opening hours using day number as the key (1-7)(Sunday-Saturday)
    private Map<Integer, OpeningHours> datastore = new HashMap<>();
    private List<String> ord = new ArrayList<String>() {
        {
            add("dayNumber");
            add("day");
            add("openHour");
            add("closeHour");
        }
    };
    private String file = "database/PhotoShop_OpeningHours.csv";

    public void create(OpeningHours hours) {
        this.datastore.put(hours.getDayNumber(), hours.clone());
    }

    public OpeningHours retrieve(int id) {
        return this.datastore.get(id).clone();
    }

    public void load() {
        CSVReader<OpeningHours> reader = new CSVReader<OpeningHours>(OpeningHours.class, file, true, ";")
                .setOrder(ord)
                .read();
        for (OpeningHours msg : reader.getData()) {
            create(msg);
        }
    }
    /**
     * Fetches store opening hours from the database and stores it in the opening hours src.data.repository
     * @throws IOException File not found
     */
//    public static void loadOpeningHours(Path path) throws IOException {
//        Files.lines(path)
//                .forEach(line -> {
//                    String[] openingHoursData = line.split(";");
//
//                    //Validate that the first row contains valid data
//                    try {
//                        Integer.parseInt(openingHoursData[0]);
//                    } catch (NumberFormatException nfe) {
//                        return; //skip loop
//                    }
//
//                    OpeningHours openingHour = new OpeningHours(Integer.parseInt(openingHoursData[0]),
//                            openingHoursData[1],
//                            Integer.parseInt(openingHoursData[2].substring(0, 2)),
//                            Integer.parseInt(openingHoursData[3].substring(0, 2))
//                    );
//
//                    openingHoursService.createOpeningHours(openingHour);
//                });
//    }
}
