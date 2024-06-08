DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS event_compilations CASCADE;

CREATE TABLE IF NOT EXISTS users (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     email VARCHAR(256) NOT NULL,
     name VARCHAR(256) NOT NULL,
     CONSTRAINT pk_users PRIMARY KEY (id),
     CONSTRAINT unique_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50),
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT unique_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat DECIMAL NOT NULL,
    lon DECIMAL NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_requests BIGINT NOT NULL DEFAULT 0,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INT NOT NULL DEFAULT 0,
    published_on TIMESTAMP,
    request_moderation BOOLEAN DEFAULT TRUE,
    state VARCHAR(32) NOT NULL,
    title VARCHAR(120) NOT NULL,
    views BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_categories FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_events_users FOREIGN KEY (initiator_id) REFERENCES users(id),
    CONSTRAINT fk_events_locations FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_users FOREIGN KEY (requester_id) REFERENCES users(id),
    CONSTRAINT fk_requests_events FOREIGN KEY (event_id) REFERENCES events(id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title VARCHAR(64),
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_compilations (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT fk_compilation_EC FOREIGN KEY (compilation_id) REFERENCES compilations(id),
    CONSTRAINT fk_events_EC FOREIGN KEY (event_id) REFERENCES events(id)
);