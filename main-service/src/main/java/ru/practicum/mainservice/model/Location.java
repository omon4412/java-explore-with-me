package ru.practicum.mainservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Встраиваемый класс, представляющий координаты местоположения.
 */
@Embeddable
@Data
public class Location {
    /**
     * Широта местоположения.
     */
    @Column(name = "location_latitude")
    private double lat;

    /**
     * Долгота местоположения.
     */
    @Column(name = "location_longitude")
    private double lon;
}
