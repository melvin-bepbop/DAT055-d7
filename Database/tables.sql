CREATE TABLE Users(
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);

CREATE TABLE Channel(
    name TEXT PRIMARY KEY,
    Created_at TIMESTAMP NOT NULL
);

CREATE TABLE Message(
    username TEXT REFERENCES Users(username),
    time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    channel TEXT REFERENCES Channel(name), 
    type TEXT CHECK (type in ('text', 'image')),
    content TEXT NOT NULL,
    PRIMARY KEY (username, time, channel)
);

CREATE TABLE UserInActiveChannel(
    username TEXT PRIMARY KEY,
    channel TEXT,
    FOREIGN KEY (username) REFERENCES Users(username),
    FOREIGN KEY (channel) REFERENCES Channel(name)
);

CREATE TABLE UsersInChannel(
    username TEXT,
    channel TEXT,
    PRIMARY KEY(username, channel),
    FOREIGN KEY (username) REFERENCES Users(username),
    FOREIGN KEY (channel) REFERENCES Channel(name)
);