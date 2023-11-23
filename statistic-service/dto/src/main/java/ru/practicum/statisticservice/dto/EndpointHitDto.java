package ru.practicum.statisticservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Объект, представляющий данные об обращении на конечную точку.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    private Long id;
    private String app;
    private String uri;

    /**
     * IP-адрес, совершившего обращение.
     */
    @Size(max = 15)
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$")
    private String ip;

    /**
     * Временная метка обращения.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
