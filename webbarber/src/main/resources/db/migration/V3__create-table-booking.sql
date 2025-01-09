CREATE TABLE booking (
    id TEXT UNIQUE PRIMARY KEY,
    client_id TEXT NOT NULL,
    service_id TEXT NOT NULL,
    "date" DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);