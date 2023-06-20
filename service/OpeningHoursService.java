package service;

import pojo.OpeningHours;
import repository.OpeningHoursRepo;

public class OpeningHoursService {
    OpeningHoursRepo openingHoursRepo;

    public OpeningHoursService(OpeningHoursRepo repo) {
        this.openingHoursRepo = repo;
    }

    public void createOpeningHours(OpeningHours hours) {
        this.openingHoursRepo.createOpeningHours(hours);
    }

    /**
     * Retrieve opening hour object
     * @param dayNumber int - day of the week 1 - 7
     * @return OpeningHours
     */
    public OpeningHours retrievOpeningHours(int dayNumber) {
        return this.openingHoursRepo.retrieveOpeningHours(dayNumber);
    }
}
