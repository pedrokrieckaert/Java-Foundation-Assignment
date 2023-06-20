package pojo;

public class OpeningHours {
    private int dayNumber;
    private String day;
    private int openHour;
    private int closeHour;


    public OpeningHours(int dayNumber, String day, int openHour, int closeHour) {
        setDayNumber(dayNumber);
        setDay(day);
        setOpenHour(openHour);
        setCloseHour(closeHour);
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

    public int getOpenHour() {
        return this.openHour;
    }

    public void setOpenHour(int openHour) {
        this.openHour = openHour;
    }

    public int getCloseHour() {
        return this.closeHour;
    }

    public void setCloseHour(int closeHour) {
        this.closeHour = closeHour;
    }
    
}
