package src.data.pojo;

public class OpeningHours {
    private int dayNumber;
    private String day;
    private String openHour;
    private String closeHour;


    public OpeningHours(int dayNumber, String day, String openHour, String closeHour) {
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

    public String getOpenHour() {
        return this.openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return this.closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }
    
}
