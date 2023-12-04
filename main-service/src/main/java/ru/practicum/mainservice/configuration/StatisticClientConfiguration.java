package ru.practicum.mainservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statiticservice.client.StatisticClient;

/**
 * Класс конфигурации для настройки бина StatisticClient.
 */
@Configuration
public class StatisticClientConfiguration {
    /**
     * Базовый URL для сервиса статистики.
     */
    @Value("${main-service.statistic-url}")
    private String statisticUrl;

    /**
     * Конфигурирует и создает бин StatisticClient.
     *
     * @return Экземпляр {@link StatisticClient}.
     */
    @Bean
    public StatisticClient config() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatisticClient(statisticUrl, builder);
    }
}
