package src.data.pojo;

import java.util.Date;

public class OpeningHours {
    private int dayNumber;
    private String day;
    private Date openHour;
    private Date closeHour;


    public OpeningHours(int dayNumber, String day, Date openHour, Date closeHour) {
        setDayNumber(dayNumber);
        setDay(day);
        setOpenHour(openHour);
        setCloseHour(closeHour);
    }

    public OpeningHours() {

    }

    public OpeningHours clone() {
        return new OpeningHours(this.getDayNumber(), this.getDay(), this.getOpenHour(), this.getCloseHour());
    }

    public int getDayNumber() {
        return this.dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getOpenHour() {
        return this.openHour;
    }

    public void setOpenHour(Date openHour) {
        this.openHour = openHour;
    }

    public Date getCloseHour() {
        return this.closeHour;
    }

    public void setCloseHour(Date closeHour) {
        this.closeHour = closeHour;
    }
    
}
