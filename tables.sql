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
    channel  TEXT References Channel(name), --tvek om denna behövs, då om vi ändå sparar en 
                                            --messagelist i Channel objektet, så kommer alla  
                                            --messages var sorterade där
    type TEXT CHECK (type in ('text', 'image')),
    content TEXT NOT NULL,
    PRIMARY KEY (username, time, channel)
);
CREATE TABLE UsersInChannel(
    name TEXT,
    Channel TEXT,
    PRIMARY KEY(name, Channel),
    FOREIGN KEY (name) REFERENCES Users(username),
    FOREIGN KEY (Channel) REFERENCES Channel(name)
);
/*CREATE TABLE AdminInChannel(
    User TEXT References UsersInChannel(User),
    Channel TEXT References UsersInChannel(Channel),
    PRIMARY KEY(User, Channel)
);
*/