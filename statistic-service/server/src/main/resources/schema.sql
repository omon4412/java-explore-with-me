DROP TABLE IF EXISTS STATISTICS CASCADE;

CREATE TABLE IF NOT EXISTS STATISTICS
(
    id
            INT
        GENERATED
            ALWAYS AS
            IDENTITY
        PRIMARY
            KEY
        UNIQUE,
    app
            VARCHAR(255) NOT NULL,
    uri     VARCHAR(255) NOT NULL,
    ip      VARCHAR(45)  NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE
);