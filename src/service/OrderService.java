package src.service;

import src.pojo.CartItem;
import src.pojo.OpeningHours;
import src.pojo.Order;
import src.data.repository.OrderRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderService {
    static OrderRepo orderRepo = new OrderRepo();

    private static Order orderBuffer;

    public OrderService () {

    }

    public void createNewOrder(BigDecimal totalPrice, int totalHours, List<CartItem> cart) {
        orderBuffer = new Order(totalPrice.intValue(), totalHours, cart);
    }

    public Order retrieveBufferOrder() {
        return orderBuffer;
    }

    /**
     * Calls to write this Order to a JSON file.
     * @param writeTarget String file path to write to
     */
    public void writeOrder(String writeTarget) {
        orderRepo.create(orderBuffer, writeTarget);
    }

    /**
     * @return int index of the day this order is made
     */
    public int getOrderDay() {
        String curDate = orderBuffer.getOrderDate();
        String dotw = curDate.substring(0, curDate.indexOf(" ")).toUpperCase();
        int dayIndex = 0;

        for (DayOfTheWeekEnum day : DayOfTheWeekEnum.values()) {
            if (dotw.equals(day.name())) {
                dayIndex = day.dayIndex;
            }
        }

        return dayIndex;
    }

    /**
     * Calculates the time and date of pickup for the order.
     * @param timeTable List of OpeningHours
     * @return String Date and Time of pickup
     */
    public String calcPickUpWindow(List<OpeningHours> timeTable) {
        String curDate = orderBuffer.getOrderDate();

        int days = timeTable.size();
        int dayIndex = getOrderDay();
        int hoursRemaining = orderBuffer.getTotalHours();
        int pickUpTime = 0;
        int dayDisplaced = 0;

        ShopStatus shopStatus = checkShopOpen(curDate ,timeTable.get(dayIndex));

        switch (shopStatus) {
            case OPEN-> hoursRemaining += displaceStartTime(curDate ,timeTable.get(dayIndex));

            case CLOSED -> {
                //Do nothing
            }

            case NEXT -> {
                int tempIndex = dayIndex;
                dayIndex = incrementIndex(dayIndex, 1, days);

                /*
                Check if the dayIndex is less than before
                If it is, this means the index was reset to Monday (1)
                Sunday (0) will be skipped meaning two days would've passed
                They dayDisplaced needs to be incremented by 2 instead of 1
                 */
                dayDisplaced = tempIndex > dayIndex ? dayDisplaced + 2 : dayDisplaced + 1;
            }
            default -> throw new IllegalStateException("Unexpected value: " + shopStatus);
        }

        while (hoursRemaining > 0) {
            for (; dayIndex < days; dayIndex++) {
                hoursRemaining -= timeTable.get(dayIndex).getHoursOpen();

                if (hoursRemaining <= 0) break;

                dayDisplaced++;
            }

            if (hoursRemaining > 0) {
                dayIndex = 1;
                dayDisplaced++;
            }
        }

        //Does not open on Sunday (0) so skips to Monday (1)
        //Probably redundant with the new increment method, but I'll worry about it another time
        if (dayIndex == 0) {
            dayIndex = incrementIndex(dayIndex, 1, days);
            dayDisplaced++;
        }

        /*
        If there are no hours remaining: take the opening hour of the next day
        If there are negative hours remaining: deduct from the closing hours of that day
         */
        if (hoursRemaining == 0) {
            int tempIndex = dayIndex;
            dayIndex = incrementIndex(dayIndex, 1, days);

            dayDisplaced = tempIndex > dayIndex ? dayDisplaced + 2 : dayDisplaced + 1;

            pickUpTime = timeTable.get(dayIndex).getOpenHourInt();
        } else {
            pickUpTime = timeTable.get(dayIndex).getCloseHourInt() + hoursRemaining;
        }

        orderBuffer.setPickUpDay(timeTable.get(dayIndex).getDay());
        orderBuffer.setPickUpTime(pickUpTime + ":00");
        orderBuffer.setPickUpDate(displacedDate(curDate ,dayDisplaced));

        return orderBuffer.pickUpDataToString();
    }

    /**
     * Increments an index up to a maximum value or resets it to a minimum value.
     * @param i int Index to increment
     * @param min int minimum possible value of Index
     * @param max int maximum possible value of Index
     * @return int Index
     */
    private int incrementIndex(int i, int min, int max) {
        return i++ >= max ? min : i++;
    }

    /**
     * Checks if the time of order is within the shop's current day openings time.
     * @param curDate String date of order
     * @param day OpeningHours object of the order day
     * @return ShopStatus Enum state of the shop
     */
    private ShopStatus checkShopOpen(String curDate, OpeningHours day) {
        int timeOfOrder = Integer.parseInt(curDate.substring(curDate.lastIndexOf(" ") + 1, curDate.lastIndexOf(" ") + 3));

        if (timeOfOrder < day.getCloseHourInt()) {
            return ShopStatus.OPEN;
        } else if (timeOfOrder >= day.getOpenHourInt()) {
            return ShopStatus.CLOSED;
        } else {
            return ShopStatus.NEXT;
        }
    }

    /**
     * Adds the days displaced to complete the order to the date of the order.
     * @param curDate String date of order
     * @param diff int tallied days displaced to complete production
     * @return String date of pickup
     */
    private String displacedDate(String curDate, int diff) {
        LocalDate date = LocalDate.parse(curDate, Order.DTF);

        date = date.plusDays(diff);

        DateTimeFormatter resultFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return date.format(resultFormat);
    }

    /**
     * Calculates a time displacement to factor any remaining time in the day to work.
     * @param curDate String date of order
     * @param day OpeningHours object of the order day
     * @return int time remaining in the work day
     */
    private int displaceStartTime(String curDate, OpeningHours day) {
        int timeOfOrder = Integer.parseInt(curDate.substring(curDate.lastIndexOf(" ") + 1, curDate.lastIndexOf(" ") + 3));

        return (timeOfOrder + 1) - day.getOpenHourInt(); //Order starts in the next hour
    }
}
