CREATE TABLE scheduling (
    id BIGINT UNIQUE PRIMARY KEY,
    client_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    start TIME NOT NULL,
    estimated_end TIME NOT NULL,
    status TEXT NOT NULL
);