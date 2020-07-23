
-- USE SEED SOME DATA

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INTEGER PRIMARY KEY auto_increment,
    email TEXT NOT NULL,
    password TEXT NOT NULL
);

INSERT into users (email, password) VALUES ("john@example.com", "password1");
INSERT into users (email, password) VALUES ("eliza@example.com", "password2");
