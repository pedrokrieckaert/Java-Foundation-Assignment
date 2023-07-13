package src.service;

import src.pojo.OpeningHours;
import src.data.repository.OpeningHoursRepo;

import java.util.ArrayList;
import java.util.List;

public class OpeningHoursService {
    static OpeningHoursRepo openingHoursRepo = new OpeningHoursRepo();

    public OpeningHoursService(){
        openingHoursRepo.load();
    }

    public void createOpeningHours(OpeningHours hours) {
        this.openingHoursRepo.create(hours);
    }

    public OpeningHours retrievOpeningHours(int dayNumber) {
        return this.openingHoursRepo.retrieve(dayNumber);
    }

    /**
     * Returns all the OpeningHours objects in the repository
     * @return List of OpeningHours
     */
    public List<OpeningHours> retrieveOpeningHoursList() {
        List<OpeningHours> timeTable = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            timeTable.add(retrievOpeningHours(i + 1));
        }

        return timeTable;
    }
}
