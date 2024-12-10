CREATE TABLE booking (
    id TEXT UNIQUE PRIMARY KEY,
    client_id TEXT NOT NULL,
    service_id TEXT NOT NULL,
    start TIME NOT NULL,
    estimated_end TIME NOT NULL,
    status TEXT NOT NULL
);