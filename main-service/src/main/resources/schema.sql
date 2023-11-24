DROP TABLE IF EXISTS events CASCADE;
--
-- DROP TABLE IF EXISTS users CASCADE;
--
-- DROP TABLE IF EXISTS categories CASCADE;


CREATE TABLE IF NOT EXISTS categories
(
    category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name        VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name    VARCHAR(256) NOT NULL,
    email   VARCHAR(256) NOT NULL UNIQUE
);

create table if not exists events
(
    event_id           serial
        primary key
        unique,
    category_id        int                   not null
        constraint events_categories_category_id_fk
            references categories,
    annotation         varchar(2000)         not null,
    created_on         varchar(50)           not null,
    description        varchar(7000)         not null,
    event_date         varchar(30)           not null,
    initiator_id       int                   not null
        constraint events_users_user_id_fk
            references users,
    location_latitude  DOUBLE PRECISION      not null,
    location_longitude DOUBLE PRECISION      not null,
    paid               boolean default false not null,
    participant_limit  int     default 0     not null,
    published_on       varchar(30),
    request_moderation boolean default true  not null,
    state              varchar(50)           not null,
    title              varchar(128)          not null
);

