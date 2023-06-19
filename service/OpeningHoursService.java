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

    public OpeningHours retrievOpeningHours(int id) {
        return this.openingHoursRepo.retrieveOpeningHours(id);
    }
}
