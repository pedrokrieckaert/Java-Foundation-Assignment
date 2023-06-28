package src.service;

import src.data.pojo.OpeningHours;
import src.data.repository.OpeningHoursRepo;

public class OpeningHoursService {
    OpeningHoursRepo openingHoursRepo = new OpeningHoursRepo();

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
}
