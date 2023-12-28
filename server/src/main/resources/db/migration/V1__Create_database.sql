CREATE TABLE IF NOT EXISTS session
(
    id TEXT NOT NULL PRIMARY KEY,
    date DATETIME NOT NULL,
    startAmount TEXT,
    endAmount TEXT,
    gameType TEXT NOT NULL,
    userId INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS user
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);
