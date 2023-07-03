package src.service;

import src.data.pojo.CartItem;
import src.data.pojo.OpeningHours;
import src.data.pojo.Order;
import src.data.repository.OrderRepo;

import java.math.BigDecimal;
import java.time.DayOfWeek;
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
        int hoursRemaining = orderBuffer.getTotalHours();
        int pickUpTime = 0;

        while (hoursRemaining > 0) {
            for (dayIndex = 0; dayIndex < days; dayIndex++) {
                hoursRemaining -= timeTable.get(dayIndex).getHoursOpen();

                if (hoursRemaining <= 0) break;
            }
        }

        //Does not open on Sunday (0) so skips to Monday (1)
        if (dayIndex == 0) {
            dayIndex++;
        }

        /*
        If there are no hours remaining: take the opening hour of the next day
        If there are negative hours remaining: deduct from the closing hours of that day
         */
        if (hoursRemaining == 0) {
            dayIndex++;
            pickUpTime = timeTable.get(dayIndex).getOpenHourInt();
        } else {
            pickUpTime = timeTable.get(dayIndex).getCloseHourInt() + hoursRemaining;
        }

        return timeTable.get(dayIndex).getDay() + " " + pickUpTime;
    }
}
