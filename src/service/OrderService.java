package src.service;

import src.data.pojo.CartItem;
import src.data.pojo.OpeningHours;
import src.data.pojo.Order;
import src.data.repository.OrderRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private int incrementIndex(int i, int min, int max) {
        return i++ >= max ? min : i++;
    }

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

    private String displacedDate(String curDate, int diff) {
        LocalDate date = LocalDate.parse(curDate, Order.dtf);

        date = date.plusDays(diff);

        DateTimeFormatter resultFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return date.format(resultFormat);
    }

    private int displaceStartTime(String curDate, OpeningHours day) {
        int timeOfOrder = Integer.parseInt(curDate.substring(curDate.lastIndexOf(" ") + 1, curDate.lastIndexOf(" ") + 3));

        return (timeOfOrder + 1) - day.getOpenHourInt(); //Order starts in the next hour
    }
}
