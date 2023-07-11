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

    /**
     * Retrieve opening hour object
     * @param dayNumber int - day of the week 1 - 7
     * @return OpeningHours
     */
    public OpeningHours retrievOpeningHours(int dayNumber) {
        return this.openingHoursRepo.retrieve(dayNumber);
    }

    public List<OpeningHours> retrieveOpeningHoursList() {
        List<OpeningHours> timeTable = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            timeTable.add(retrievOpeningHours(i + 1));
        }

        return timeTable;
    }
}
