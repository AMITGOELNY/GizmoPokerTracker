CREATE TABLE IF NOT EXISTS session
(
    id TEXT NOT NULL PRIMARY KEY,
    date DATETIME NOT NULL,
    startAmount TEXT,
    endAmount TEXT,
    gameType TEXT NOT NULL,
    venue TEXT NOT NULL,
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

CREATE TABLE IF NOT EXISTS feed
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    link TEXT NOT NULL,
    image TEXT NOT NULL,
    title TEXT NOT NULL,
    pub_date DATE NOT NULL,
    category TEXT NOT NULL,
    site TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);
