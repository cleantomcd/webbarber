CREATE TABLE services (
    id BIGINT UNIQUE PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    estimated_time INTEGER NOT NULL
);