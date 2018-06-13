DROP TABLE added_in;
DROP TABLE offers;
DROP TABLE lives_at;
DROP TABLE works_for;
DROP TABLE food;
DROP TABLE delivery_delivers;
DROP TABLE pick_up;
DROP TABLE orders;
DROP TABLE restaurant;
DROP TABLE restaurant_managers;
DROP TABLE courier;
DROP TABLE customer;
DROP TABLE users;
DROP TABLE points;
DROP TABLE vip_level;
DROP TABLE addresses;
DROP TABLE address_detail;

CREATE TABLE users(
     userID VARCHAR(20) NOT NULL,
     userPass VARCHAR(20) NOT NULL,
     phone# INTEGER,
     userName VARCHAR(20) NOT NULL,
     PRIMARY KEY(userID)
);

CREATE TABLE restaurant_managers(
    res_userID VARCHAR(20) NOT NULL,
    PRIMARY KEY(res_userID),
    FOREIGN KEY(res_userID) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE courier(
    cor_userID VARCHAR(20) NOT NULL,
    PRIMARY KEY(cor_userID),
    FOREIGN KEY(cor_userID) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE vip_level(
    vip_points SMALLINT,
    vip_level SMALLINT,
    PRIMARY KEY(vip_points)
);

CREATE TABLE points(
    spending DECIMAL(10,2) PRIMARY KEY,
    points SMALLINT,
    FOREIGN KEY(points) REFERENCES vip_level(vip_points) ON DELETE set NULL
);

CREATE TABLE customer(
    cus_userID VARCHAR(20) NOT NULL,
    cus_spending DECIMAL(10,2),
    PRIMARY KEY(cus_userID),
    FOREIGN KEY(cus_userID) REFERENCES users on DELETE CASCADE,
    FOREIGN KEY(cus_spending) REFERENCES points ON DELETE set NULL
);

CREATE TABLE addresses(
    postal_code CHAR(6),
    street VARCHAR(20),
    house# INTEGER,
    PRIMARY KEY(postal_code,street,house#)
);

CREATE TABLE address_detail(
    province CHAR(2),
    city VARCHAR(15)
);

CREATE TABLE restaurant(
    resID INTEGER PRIMARY KEY,
    res_name VARCHAR(25),
    res_open_time CHAR(5),
    res_close_time CHAR(5),
    res_rating DECIMAL(2,1),
    res_type VARCHAR(20),
    res_delivery_option INTEGER,
    res_managerID VARCHAR(20),
    res_postal_code CHAR(6),
    res_street VARCHAR(20),
    res_house# SMALLINT,
    FOREIGN KEY(res_managerID) REFERENCES restaurant_managers on DELETE SET NULL,
    FOREIGN KEY(res_postal_code,res_street,res_house#) REFERENCES addresses ON DELETE SET NULL
);

CREATE TABLE orders(
    orderID INTEGER PRIMARY KEY,
    order_date DATE,
    order_time CHAR(5),
    order_amount DECIMAL(10,2),
    order_status VARCHAR(20),
    order_customerID VARCHAR(20),
    order_restaurantID INTEGER,
    FOREIGN KEY(order_customerID) REFERENCES customer ON DELETE SET NULL,
    FOREIGN KEY(order_restaurantID) REFERENCES restaurant ON DELETE CASCADE
);

CREATE TABLE pick_up(
    orderID INTEGER PRIMARY KEY,
    estimated_ready_time CHAR(5),
    FOREIGN KEY(orderID) REFERENCES orders ON DELETE CASCADE
);

CREATE TABLE delivery_delivers(
    orderID INTEGER PRIMARY KEY,
    estimated_arrival_time CHAR(5),
    delivery_fee DECIMAL(10,2),
    courierID VARCHAR(20),
    postal_code CHAR(6),
    street VARCHAR(20),
    house# INTEGER,
    FOREIGN KEY(orderID) REFERENCES orders on DELETE CASCADE,
    FOREIGN KEY(courierID) REFERENCES courier ON DELETE SET NULL,
    FOREIGN KEY(postal_code,street,house#) REFERENCES addresses on DELETE SET NULL
);

CREATE TABLE food(
    food_name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE works_for(
    courierID VARCHAR(20),
    restaurantID INTEGER,
    PRIMARY KEY(courierID,restaurantID),
    FOREIGN KEY(courierID) REFERENCES courier ON DELETE CASCADE,
    FOREIGN KEY(restaurantID) REFERENCES restaurant ON DELETE CASCADE
);

CREATE TABLE lives_at(
    postal_code CHAR(6),
    street VARCHAR(20),
    house# INTEGER,
    customerID VARCHAR(20),
    PRIMARY KEY(postal_code,street,house#,customerID),
    FOREIGN KEY(postal_code,street,house#)REFERENCES addresses on DELETE SET NULL,
    FOREIGN KEY(customerID) REFERENCES customer ON DELETE CASCADE
);

CREATE TABLE offers(
    food_name VARCHAR(20),
    restaurantID INTEGER,
    price DECIMAL(10,2),
    PRIMARY KEY(food_name,restaurantID),
    FOREIGN KEY(food_name) REFERENCES food ON DELETE CASCADE,
    FOREIGN KEY(restaurantID) REFERENCES restaurant ON DELETE CASCADE
);

CREATE TABLE added_in(
    food_name VARCHAR(20),
    orderID INTEGER,
    quantity INTEGER,
    PRIMARY KEY(food_name,orderID),
    FOREIGN KEY(food_name) REFERENCES food ON DELETE CASCADE,
    FOREIGN KEY(orderID) REFERENCES orders ON DELETE CASCADE
);

INSERT INTO address_detail VALUES('BC','Vancouver');
INSERT INTO addresses VALUES ('V6T1Z2','West Mall',2075);
INSERT INTO addresses VALUES('V6S0B1','Wesbrook Mall',3381);
INSERT INTO vip_level VALUES(10,1);
INSERT INTO points VALUES(10.03,10);
INSERT INTO users VALUES('854','aqb39q',8795074061,'Kobe Bryant');
INSERT INTO users VALUES('480','1a6vov',1762594815,'James Gong');
INSERT INTO users VALUES('186','hnqemy',8954713309,'Harry Ralap');
INSERT INTO customer VALUES('854',10.03);
INSERT INTO courier VALUES('480');
INSERT INTO restaurant_managers VALUES('186');
INSERT INTO restaurant VALUES(1,'Blenz Coffee','06:30','22:00',4.2,'Coffee Shop',0,'186','V6S0B1','Wesbrook Mall',3381);
INSERT INTO orders VALUES(024,'2018-05-31','12:00',10,'Submitted','854',1);
INSERT INTO orders VALUES(015,'2018-05-31','17:00',6,'Ready','854',1);
INSERT INTO pick_up VALUES(024,'00:15');
INSERT INTO delivery_delivers VALUES(015,'00:40',9.85,'480','V6T1Z2','West Mall',2075);
INSERT INTO food VALUES('latte');
INSERT INTO works_for VALUES('480',1);
INSERT INTO lives_at VALUES('V6T1Z2','West Mall',2075,'854');
INSERT INTO offers VALUES('latte',1,6.49);
INSERT INTO added_in VALUES('latte',015,1);


