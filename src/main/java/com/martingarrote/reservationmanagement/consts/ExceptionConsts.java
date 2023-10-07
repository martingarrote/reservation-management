package com.martingarrote.reservationmanagement.consts;

public class ExceptionConsts {

    public final static String INSERT_ERROR = "Unable to insert";
    public final static String UPDATE_ERROR = "Unable to update";

    public static class CustomerExceptions {
        public final static String AGE_SHOULD_BIGGER_THAN_EIGHTEEN = "The customers age should be 18 or older for them to be inserted";
    }

    public static class RoomExceptions {
        public final static String ROOM_IS_BUSY = "The room is busy, is not possible make a reservation with this room.";
    }

    public static class ReservationExceptions {
        public final static int DISCOUNT_MONTHS = 12;
        public final static int DISCOUNT_PERCENTAGE = 10;
        public final static int MAX_RESERVATION_DURATION = 36;
        public final static String DURATION_OUT_OF_ALLOWED_RANGE = "The reservation duration cannot be greater than 36 months.";
    }

}
