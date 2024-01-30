DROP TABLE IF EXISTS events CASCADE;

DROP TABLE IF EXISTS compilations CASCADE;

DROP TABLE IF EXISTS compilations_event CASCADE;

DROP TABLE IF EXISTS users_roles CASCADE;

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS roles CASCADE;

DROP TABLE IF EXISTS categories CASCADE;

DROP TABLE IF EXISTS requests CASCADE;

DROP TABLE IF EXISTS comments CASCADE;

DROP TABLE IF EXISTS comments_likes CASCADE;

CREATE TABLE IF NOT EXISTS categories
(
    category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name        VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    username      VARCHAR(256) NOT NULL,
    email         VARCHAR(256) NOT NULL UNIQUE,
    password_hash varchar(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    role_id serial,
    name    varchar(150) not null,
    primary key (role_id)
);

CREATE TABLE users_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (user_id),
    foreign key (role_id) references roles (role_id)
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
    created_on         timestamp             not null,
    description        varchar(7000)         not null,
    event_date         timestamp             not null,
    initiator_id       int                   not null
        constraint events_users_user_id_fk
            references users,
    location_latitude  DOUBLE PRECISION      not null,
    location_longitude DOUBLE PRECISION      not null,
    paid               boolean default false not null,
    participant_limit  int     default 0     not null,
    published_on       timestamp,
    request_moderation boolean default true  not null,
    state              varchar(50)           not null,
    title              varchar(128)          not null
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id serial primary key unique,
    pinned         boolean     not null,
    title          varchar(50) not null
);

CREATE TABLE IF NOT EXISTS compilations_event
(
    id             serial primary key unique,
    event_id       int not null
        constraint compilations_events_events_event_id_fk
            references events,
    compilation_id int not null
        constraint compilations_events_compilations_compilation_id_fk
            references compilations
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    event_id     INT         NOT NULL
        constraint requests_events_event_id_fk
            references events,
    requester_id INT         NOT NULL
        constraint requests_users_user_id_fk
            references users,
    created      timestamp   not null,
    status       varchar(50) not null
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id          serial primary key,
    event_id            integer       not null
        constraint comments_events_event_id_fk references events,
    author_id           integer       not null
        constraint comments_users_user_id_fk
            references users,
    text                varchar(1000) not null,
    "parent_comment_id" integer
        constraint comments_comments_comment_id_fk
            references comments on delete cascade,
    comment_date        timestamp     not null,
    update_date         timestamp
);

CREATE TABLE IF NOT EXISTS comments_likes
(
    comment_id integer not null
        references comments on delete set null,
    user_id    integer not null
        references users on delete set null,
    primary key (comment_id, user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS SPRING_SESSION_ATTRIBUTES;
DROP TABLE IF EXISTS SPRING_SESSION;

CREATE TABLE IF NOT EXISTS SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BYTEA        NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);