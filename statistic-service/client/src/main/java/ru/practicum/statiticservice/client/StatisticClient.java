package ru.practicum.statiticservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statisticservice.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Клиент для взаимодействия с сервером статистики.
 */
@Service
public class StatisticClient extends BaseClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Конструктор класса StatisticClient.
     *
     * @param rest Объект RestTemplate для взаимодействия с сервером.
     */
    public StatisticClient(RestTemplate rest) {
        super(rest);
    }

    /**
     * Конструктор класса StatisticClient.
     *
     * @param serverUrl Адрес сервера статистики.
     * @param builder   Объект RestTemplateBuilder для настройки RestTemplate.
     */
    @Autowired
    public StatisticClient(@Value("${statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * Отправляет данные о хите на сервер статистики.
     *
     * @param endpointHitDto DTO объект с данными об обращении к endpoint`у.
     * @return Объект ResponseEntity с результатом операции.
     */
    public ResponseEntity<Object> addStatistic(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    /**
     * Получает статистику за определенный период времени и для указанных URIs.
     *
     * @param start  Начальная дата и время.
     * @param end    Конечная дата и время.
     * @param uris   Список URIs, для которых запрашивается статистика.
     * @param unique Флаг, указывающий, нужно ли возвращать обращения по уникальным ip адресам.
     * @return Объект ResponseEntity с результатом операции.
     */
    public ResponseEntity<Object> getStatistic(LocalDateTime start, LocalDateTime end,
                                               List<String> uris,
                                               Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
