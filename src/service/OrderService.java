package src.service;

import src.data.pojo.CartItem;
import src.data.pojo.OpeningHours;
import src.data.pojo.Order;
import src.data.repository.OrderRepo;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderService {
    static OrderRepo orderRepo = new OrderRepo();

    static Order orderBuffer;

    public OrderService () {
        orderRepo.create("database/shoppingCart.json");
    }

    public void createNewOrder(BigDecimal totalPrice, int totalHours, List<CartItem> cart) {
        orderBuffer = new Order(totalPrice.intValue(), totalHours, cart);
    }

    public Order retrieveBufferOrder() {
        return orderBuffer;
    }

    public void addItem(CartItem item) {

    }

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

    public String calcPickUpWindow(List<OpeningHours> timeTable) {
        int days = timeTable.size();
        int dayIndex = getOrderDay();
        int hoursRemaining = orderBuffer.getTotalHours() + displaceStartTime(timeTable.get(dayIndex));
        int pickUpTime = 0;
        int dayDisplaced = 0;

        while (hoursRemaining > 0) {
            for (dayIndex = 0; dayIndex < days; dayIndex++) {
                hoursRemaining -= timeTable.get(dayIndex).getHoursOpen();
                dayDisplaced++;

                if (hoursRemaining <= 0) break;
            }
        }

        //Does not open on Sunday (0) so skips to Monday (1)
        if (dayIndex == 0) {
            dayIndex++;
            dayDisplaced++;
        }

        /*
        If there are no hours remaining: take the opening hour of the next day
        If there are negative hours remaining: deduct from the closing hours of that day
         */
        if (hoursRemaining == 0) {
            dayIndex++;
            dayDisplaced++;
            pickUpTime = timeTable.get(dayIndex).getOpenHourInt();
        } else {
            pickUpTime = timeTable.get(dayIndex).getCloseHourInt() + hoursRemaining;
        }

        return timeTable.get(dayIndex).getDay() + " " + displacedDate(dayDisplaced) + " after " + pickUpTime + ":00";
    }

    private String displacedDate(int diff) {
        String curDate = orderBuffer.getOrderDate();
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("EEEE yyyy/MM/dd HH:mm");
        LocalDate date = LocalDate.parse(curDate, inputFormat);

        date = date.plusDays(diff);

        DateTimeFormatter resultFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        return date.format(resultFormat);
    }

    private int displaceStartTime(OpeningHours day) {
        String curDate = orderBuffer.getOrderDate();
        int timeOfOrder = Integer.parseInt(curDate.substring(curDate.lastIndexOf(" ") + 1, curDate.lastIndexOf(" ") + 3));

        if (timeOfOrder > day.getOpenHourInt() && timeOfOrder < day.getCloseHourInt()) {
            return day.getCloseHourInt() - timeOfOrder;
        }

        return 0;
    }
}
