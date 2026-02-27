
INSERT INTO Users (username, password) VALUES 
('TestUser', 'Password123'),
('Me', 'MySecurePass!'),
('Alice', 'alice_pass_789');

INSERT INTO Channel (name, Created_at) VALUES 
('General', CURRENT_TIMESTAMP),
('Random', CURRENT_TIMESTAMP),
('Dev', CURRENT_TIMESTAMP);

INSERT INTO UsersInChannel (username, channel) VALUES 
('TestUser', 'General'),
('TestUser', 'Random'),
('Me', 'General'),
('Me', 'Dev'),
('Alice', 'Dev');

INSERT INTO UserInActiveChannel (username, channel) VALUES 
('TestUser', 'General'),
('Me', 'General'),
('Alice', 'Dev');

INSERT INTO Message (username, time, channel, type, content) VALUES 
('TestUser', CURRENT_TIMESTAMP - INTERVAL '10 minutes', 'General', 'text', 'Hey everyone, is this working?'),
('Me', CURRENT_TIMESTAMP - INTERVAL '9 minutes', 'General', 'text', 'Yep! I can see your message.'),
('Alice', CURRENT_TIMESTAMP - INTERVAL '5 minutes', 'Dev', 'text', 'Just uploaded the new database schema!'),
('TestUser', CURRENT_TIMESTAMP - INTERVAL '2 minutes', 'Random', 'text', 'Does anyone want to grab coffee later?');