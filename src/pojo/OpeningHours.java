package src.pojo;

public class OpeningHours {
    private int dayNumber;
    private String day;
    private String openHour;
    private String closeHour;
    private int hoursOpen;


    public OpeningHours(int dayNumber, String day, String openHour, String closeHour) {
        setDayNumber(dayNumber);
        setDay(day);
        setOpenHour(openHour);
        setCloseHour(closeHour);
        setHoursOpen();
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

    public int getOpenHourInt() {
        return Integer.parseInt(this.openHour.substring(0,2));
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return this.closeHour;
    }
    public int getCloseHourInt() {
        return Integer.parseInt(this.closeHour.substring(0,2));
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }
    public int getHoursOpen() {
        return hoursOpen;
    }
    public void setHoursOpen() {
        this.hoursOpen = getCloseHourInt() - getOpenHourInt();
    }
}
