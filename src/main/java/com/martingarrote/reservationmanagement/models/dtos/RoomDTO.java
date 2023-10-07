package com.martingarrote.reservationmanagement.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class RoomDTO implements Serializable {

    private static final int MIN_SIZE = 10;

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "The number field cannot be null")
    private Integer number;

    @NotBlank(message = "The description field cannot be blank")
    private String description;

    @Min(value = MIN_SIZE, message = "The minimal value for the size field is ten square meters")
    @NotNull(message = "The size field cannot be null")
    private Double size;

    @NotNull(message = "The pricePerMonth field cannot be null")
    private Double pricePerMonth;

    @NotNull(message = "The busy field cannot be null")
    private Boolean busy;

    @NotNull(message = "The active field cannot be null")
    private Boolean active;

    public RoomDTO() {
    }

    public RoomDTO(Long id,
                   @NotNull(message = "The number field cannot be null") Integer number,
                   @NotBlank(message = "The description field cannot be blank") String description,
                   @NotNull(message = "The size field cannot be null") Double size,
                   @NotNull(message = "The pricePerMonth field cannot be null") Double pricePerMonth,
                   @NotNull(message = "The busy field cannot be null") Boolean busy,
                   @NotNull(message = "The active field cannot be null") Boolean active) {
        this.id = id;
        this.number = number;
        this.description = description;
        this.size = size;
        this.pricePerMonth = pricePerMonth;
        this.busy = busy;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(Double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "id=" + id +
                ", number=" + number +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", pricePerMonth=" + pricePerMonth +
                ", busy=" + busy +
                ", active=" + active +
                '}';
    }
}
