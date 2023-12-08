CREATE TABLE IF NOT EXISTS session
(
    id TEXT NOT NULL PRIMARY KEY,
    date DATETIME NOT NULL,
    startAmount TEXT,
    endAmount TEXT
);
