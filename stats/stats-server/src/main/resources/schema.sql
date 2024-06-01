DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(512) NOT NULL,
    uri VARCHAR(1024) NOT NULL,
    ip VARCHAR(1024) NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);