CREATE TABLE barbers (
    id TEXT UNIQUE PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT DEFAULT 'admin'
);
