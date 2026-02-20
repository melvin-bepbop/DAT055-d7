CREATE TABLE Users(
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);
CREATE TABLE Channel(
    name TEXT PRIMARY KEY,
    Created_at TIMESTAMP NOT NULL
);
CREATE TABLE Message(
    username TEXT References Users(username),
    time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    channel  TEXT References Channel(name), 
    type TEXT CHECK (type in ('text', 'image')),
    content TEXT NOT NULL,
    PRIMARY KEY (username, time, channel)
);
CREATE TABLE UserInActiveChannel(
    user TEXT PRIMARY KEY,
    channel TEXT,
    FOREIGN KEY (name) REFERENCES Users(username),
    FOREIGN KEY (Channel) REFERENCES Channel(name)
);
CREATE TABLE UsersInChannel(
    user TEXT,
    channel TEXT,
    PRIMARY KEY(user, channel),
    FOREIGN KEY (name) REFERENCES Users(username),
    FOREIGN KEY (Channel) REFERENCES Channel(name)
);

/*CREATE TABLE AdminInChannel(
    User TEXT References UsersInChannel(User),
    Channel TEXT References UsersInChannel(Channel),
    PRIMARY KEY(User, Channel)
);
*/