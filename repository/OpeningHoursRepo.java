package repository;

import java.util.HashMap;
import java.util.Map;

import pojo.OpeningHours;

public class OpeningHoursRepo {
    //Map the opening hours using day number as key (1-7)(Sunday-Saturday)
    private Map<Integer, OpeningHours> datastore = new HashMap<>();

    public void createOpeningHours(OpeningHours hours) {
        this.datastore.put(hours.getDayNumber(), hours.clone());
    }

    public OpeningHours retrieveOpeningHours(int id) {
        return this.datastore.get(id).clone();
    }
}
