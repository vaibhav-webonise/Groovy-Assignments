create table UserData (
    id int primary key AUTO_INCREMENT,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    new_password varchar(255) NOT NULL
);
