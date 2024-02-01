# java-explore-with-me

Explore with me – сервис, который для позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них. Реализовал backend часть: микросервисную архитектуру (основной сервис и сервис статистики), 3 части основного сервиса (публичная будет доступна без регистрации любому пользователю сети, закрытая будет доступна только авторизованным пользователям, административная — для администраторов сервиса). Так же для развёртывания решения использовал контейнеры Docker.

В рамках самостоятельного изучения дополнительного материала, использовал Spring Security для разделения доступа к разным частям основного сервиса с помощью JWT токенов.
[https://github.com/omon4412/java-explore-with-me/tree/secure_jwt](https://github.com/omon4412/java-explore-with-me/tree/secure_jwt)

Отдельно сделал авторизацию на основе web сессий. [https://github.com/omon4412/java-explore-with-me/tree/secure_session](https://github.com/omon4412/java-explore-with-me/tree/secure_session)

Используемый стек технологий: Java 11, Spring Boot, Hibernate, PostgreSQL, Spring Security
