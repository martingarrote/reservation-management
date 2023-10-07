package com.martingarrote.reservationmanagement.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class ReservationDTO implements Serializable {

    private static final int MIN_DURATION = 1;

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "The code field cannot be blank")
    private String code;

    @NotNull(message = "The customer field cannot be null")
    private CustomerDTO customer;

    @NotNull(message = "The reservedRoom field cannot be null")
    private RoomDTO reservedRoom;

    @NotNull(message = "The reservationPrice field cannot be null")
    private Double reservationPrice;

    @NotBlank(message = "The description field cannot be blank")
    private String description;

    @Min(value = MIN_DURATION, message = "The minimal value to duration field is one")
    @NotNull(message = "The duration field cannot be null")
    private Integer duration;

    @NotNull(message = "The startDate field cannot be null")
    private LocalDate startDate;

    @NotNull(message = "The endDate field cannot be null")
    private LocalDate endDate;

    @NotNull(message = "The active field cannot be null")
    private Boolean active;

    public ReservationDTO() {

    }

    public ReservationDTO(Long id,
                          @NotBlank(message = "The code field cannot be blank") String code,
                          @NotNull(message = "The customer field cannot be null") CustomerDTO customer,
                          @NotNull(message = "The reservedRoom field cannot be null") RoomDTO reservedRoom,
                          @NotNull(message = "The reservationPrice field cannot be null") Double reservationPrice,
                          @NotBlank(message = "The description field cannot be blank") String description,
                          @NotNull(message = "The duration field cannot be null") Integer duration,
                          @NotNull(message = "The startDate field cannot be null") LocalDate startDate,
                          @NotNull(message = "The endDate field cannot be null") LocalDate endDate,
                          @NotNull(message = "The active field cannot be null") Boolean active) {
        this.id = id;
        this.code = code;
        this.customer = customer;
        this.reservedRoom = reservedRoom;
        this.reservationPrice = reservationPrice;
        this.description = description;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public RoomDTO getReservedRoom() {
        return reservedRoom;
    }

    public void setReservedRoom(RoomDTO reservedRoom) {
        this.reservedRoom = reservedRoom;
    }

    public Double getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(Double reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", customer=" + customer +
                ", reservedRoom=" + reservedRoom +
                ", reservationPrice=" + reservationPrice +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", active=" + active +
                '}';
    }
}
