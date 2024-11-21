CREATE TABLE services (
    id TEXT UNIQUE PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100),
    estimated_time INTEGER NOT NULL
);