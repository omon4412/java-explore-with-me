package ru.practicum.mainservice.model;

/**
 * Перечисление, представляющее возможные действия, связанные со статусами событий.
 */
public enum StateAction {
    /**
     * Отправить событие на рассмотрение.
     */
    SEND_TO_REVIEW,

    /**
     * Отменить рассмотрение события.
     */
    CANCEL_REVIEW,

    /**
     * Опубликовать событие.
     */
    PUBLISH_EVENT,

    /**
     * Отклонить событие.
     */
    REJECT_EVENT
}

