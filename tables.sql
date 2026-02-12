CREATE TABLE Users(
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);
CREATE TABLE Channel(
    name TEXT PRIMARY KEY,
    Created_at DATETIME NOT NULL
);
CREATE TABLE Message(
    username TEXT References Users(username),
    time DATETIME NOT NULL,
    channel  TEXT References Channel(name), --tvek om denna behövs, då om vi ändå sparar en 
                                            --messagelist i Channel objektet, så kommer alla  
                                            --messages var sorterade där
    type TEXT CHECK (type in ('text', 'image')),
    content TEXT NOT NULL,
    PRIMARY KEY (username, time, channel)
);
CREATE TABLE UsersInChannel(
    User TEXT References User(username),
    Channel TEXT References Channel(name),
    PRIMARY KEY(User, Channel)
);
CREATE TABLE AdminInChannel(
    User TEXT References UsersInChannel(User),
    Channel TEXT References UsersInChannel(Channel),
    PRIMARY KEY(User, Channel)
);
