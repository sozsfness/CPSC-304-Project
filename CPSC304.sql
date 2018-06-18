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
     userID CHAR(5),
     userPass VARCHAR(18) NOT NULL CHECK(length(userPass) >= 6),
     phone# INTEGER,
     userName VARCHAR(255) NOT NULL,
     PRIMARY KEY(userID)
);

CREATE TABLE restaurant_managers(
    res_userID CHAR(5),
    PRIMARY KEY(res_userID),
    FOREIGN KEY(res_userID) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE courier(
    cor_userID CHAR(5),
    PRIMARY KEY(cor_userID),
    FOREIGN KEY(cor_userID) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE vip_level(
    vip_points SMALLINT CHECK (vip_points > 0),
    vip_level SMALLINT,
    PRIMARY KEY(vip_points)
);

CREATE TABLE points(
    spending DECIMAL(10,2) PRIMARY KEY,
    points SMALLINT,
    FOREIGN KEY(points) REFERENCES vip_level(vip_points) ON DELETE set NULL
);

CREATE TABLE customer(
    cus_userID CHAR(5),
    cus_spending DECIMAL(10,2),
    PRIMARY KEY(cus_userID),
    FOREIGN KEY(cus_userID) REFERENCES users on DELETE CASCADE,
    FOREIGN KEY(cus_spending) REFERENCES points ON DELETE SET NULL
);

CREATE TABLE address_detail(
    postal_code CHAR(7) PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    province CHAR(2) NOT NULL CHECK (province = 'BC')
);

CREATE TABLE addresses(
    postal_code CHAR(7),
    street VARCHAR(255),
    house# INTEGER,
    PRIMARY KEY(postal_code,street,house#),
    FOREIGN KEY(postal_code) REFERENCES address_detail ON DELETE CASCADE
);

CREATE TABLE restaurant(
    resID INTEGER PRIMARY KEY,
    res_name VARCHAR(255),
    res_open_time CHAR(5),
    res_close_time CHAR(5),
    res_rating DECIMAL(2,1),
    res_type VARCHAR(20) CHECK(res_type = 'Fast Food'
                                OR res_type = 'Asian Food'
                                OR res_type = 'European Food'
                                OR res_type = 'Coffee Shop'),
    res_delivery_option INTEGER,
    res_managerID CHAR(5) NOT NULL,
    res_postal_code CHAR(7) NOT NULL,
    res_street VARCHAR(20) NOT NULL,
    res_house# SMALLINT NOT NULL,
    FOREIGN KEY(res_managerID) REFERENCES restaurant_managers ON DELETE CASCADE,
    FOREIGN KEY(res_postal_code,res_street,res_house#) REFERENCES addresses ON DELETE CASCADE
);



CREATE TABLE orders(
    orderID INTEGER PRIMARY KEY,
    order_date DATE,
    order_time CHAR(5),
    order_amount DECIMAL(10,2),
    order_status VARCHAR(20) CHECK(order_status = 'SUBMITTED'
                                    OR order_status = 'READY'
                                    OR order_status = 'COMPLETE'
                                    OR order_status = 'CANCELLED'
									OR order_status = 'DELIVERED'
									OR order_status = 'DELIVERING'),
    order_customerID CHAR(5) NOT NULL,
    order_restaurantID INTEGER NOT NULL,
    FOREIGN KEY(order_customerID) REFERENCES customer ON DELETE SET NULL,
    FOREIGN KEY(order_restaurantID) REFERENCES restaurant ON DELETE CASCADE
);

CREATE TABLE pick_up(
    orderID INTEGER PRIMARY KEY,
    estimated_ready_time CHAR(5),
    FOREIGN KEY(orderID) REFERENCES orders ON DELETE CASCADE
);

CREATE TABLE delivery_delivers(
    orderID INTEGER PRIMARY KEY NOT NULL,
    estimated_arrival_time CHAR(5),
    delivery_fee DECIMAL(10,2),
    courierID CHAR(5) NOT NULL,
    postal_code CHAR(7) NOT NULL,
    street VARCHAR(20) NOT NULL,
    house# INTEGER NOT NULL,
    FOREIGN KEY(orderID) REFERENCES orders on DELETE CASCADE,
    FOREIGN KEY(courierID) REFERENCES courier ON DELETE SET NULL,
    FOREIGN KEY(postal_code,street,house#) REFERENCES addresses on DELETE SET NULL
);

CREATE TABLE food(
    food_name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE works_for(
    courierID CHAR(5),
    restaurantID INTEGER,
    PRIMARY KEY(courierID,restaurantID),
    FOREIGN KEY(courierID) REFERENCES courier ON DELETE CASCADE,
    FOREIGN KEY(restaurantID) REFERENCES restaurant ON DELETE CASCADE
);

CREATE TABLE lives_at(
    postal_code CHAR(7),
    street VARCHAR(20),
    house# INTEGER,
    customerID CHAR(5),
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

CREATE OR REPLACE TRIGGER spendingUpdate
AFTER UPDATE OF order_status ON orders
FOR EACH ROW
WHEN (new.order_status = 'COMPLETE')
DECLARE newSpending DECIMAL(10,2);
BEGIN
SELECT cus_spending + :new.order_amount
INTO newSpending
FROM customer
WHERE cus_userID =:new.order_customerID;
INSERT INTO vip_level
SELECT newSpending, newSpending/100
FROM DUAL
WHERE NOT EXISTS (SELECT * FROM vip_level WHERE vip_points = newSpending);
INSERT INTO points
SELECT newSpending, newSpending
FROM DUAL
WHERE NOT EXISTS (SELECT * FROM points WHERE points = newSpending);
UPDATE customer
SET cus_spending = newSpending
WHERE cus_userID =:new.order_customerID;
END;
/



INSERT INTO address_detail VALUES('M5N 6G7', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('L6G 4H1', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('L8H 0W0', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('T4C 2L4', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('L2R 3W3', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('H2L 5G9', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('K2N 2A4', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('J7W 4X2', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('N7Y 6X7', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('S1N 0B0', 'Vancouver', 'BC');
INSERT INTO address_detail VALUES('A9H 4N5', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('M9H 1X6', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('K9B 7X8', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('G4W 0G5', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('M3W 9L5', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('J4S 2K9', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('P0Z 8M3', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('P5V 8X0', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('J4R 4K9', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('T2N 2E8', 'Richmond', 'BC');
INSERT INTO address_detail VALUES('V0B 8K8', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('M3J 4X7', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('J1A 1X6', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('S3L 5R0', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('S2V 8B4', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('G2S 2T1', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('Y1N 6E2', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('V0K 1E8', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('J5K 6W9', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('R0A 3E3', 'Burnaby', 'BC');
INSERT INTO address_detail VALUES('N3J 6Y7', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('R5C 6W3', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('T9N 0Z3', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('H7X 7N4', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('S3B 5C6', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('H4R 3R7', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('H1R 6G6', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('L2H 5T4', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('V3M 6K4', 'North Vancouver', 'BC');
INSERT INTO address_detail VALUES('V5X 7B6', 'North Vancouver', 'BC');
INSERT INTO addresses VALUES('M5N 6G7', 'Ipsum Av', 8066);
INSERT INTO addresses VALUES('L6G 4H1', 'Auctor St', 7892);
INSERT INTO addresses VALUES('L8H 0W0', 'Nulla Ave', 3790);
INSERT INTO addresses VALUES('T4C 2L4', 'Luctus Av', 9233);
INSERT INTO addresses VALUES('L2R 3W3', 'Nunc St', 3010);
INSERT INTO addresses VALUES('H2L 5G9', 'Libero Street', 3924);
INSERT INTO addresses VALUES('K2N 2A4', 'Vel Rd', 4929);
INSERT INTO addresses VALUES('J7W 4X2', 'Vel Street', 9571);
INSERT INTO addresses VALUES('N7Y 6X7', 'Orci Av', 2001);
INSERT INTO addresses VALUES('S1N 0B0', 'Nulla Av', 4646);
INSERT INTO addresses VALUES('A9H 4N5', 'Bibendum Avenue', 272);
INSERT INTO addresses VALUES('M9H 1X6', 'A Street', 5690);
INSERT INTO addresses VALUES('K9B 7X8', 'In St', 6512);
INSERT INTO addresses VALUES('G4W 0G5', 'Lacus St', 8990);
INSERT INTO addresses VALUES('M3W 9L5', 'Quis Rd', 6306);
INSERT INTO addresses VALUES('J4S 2K9', 'Tristique Road', 208);
INSERT INTO addresses VALUES('P0Z 8M3', 'Mauris Av', 3023);
INSERT INTO addresses VALUES('P5V 8X0', 'Posuere Rd', 2571);
INSERT INTO addresses VALUES('J4R 4K9', 'Curabitur Road', 2786);
INSERT INTO addresses VALUES('T2N 2E8', 'Tincidunt Rd', 954);
INSERT INTO addresses VALUES('V0B 8K8', 'Mauris St', 9070);
INSERT INTO addresses VALUES('M3J 4X7', 'Odio Road', 4187);
INSERT INTO addresses VALUES('J1A 1X6', 'Eget Road', 5866);
INSERT INTO addresses VALUES('S3L 5R0', 'Dolor Avenue', 9546);
INSERT INTO addresses VALUES('S2V 8B4', 'Nec St', 1394);
INSERT INTO addresses VALUES('G2S 2T1', 'Augue Av', 3374);
INSERT INTO addresses VALUES('Y1N 6E2', 'Auctor Avenue', 7469);
INSERT INTO addresses VALUES('V0K 1E8', 'Lacus Road', 7295);
INSERT INTO addresses VALUES('J5K 6W9', 'Nulla Ave', 5688);
INSERT INTO addresses VALUES('R0A 3E3', 'Nullam St', 3539);
INSERT INTO addresses VALUES('N3J 6Y7', 'Elit Rd', 6303);
INSERT INTO addresses VALUES('R5C 6W3', 'Pharetra Street', 7850);
INSERT INTO addresses VALUES('T9N 0Z3', 'Sed Ave', 6851);
INSERT INTO addresses VALUES('H7X 7N4', 'Sem Av', 4374);
INSERT INTO addresses VALUES('S3B 5C6', 'Ac St', 7024);
INSERT INTO addresses VALUES('H4R 3R7', 'Bibendum Road', 4885);
INSERT INTO addresses VALUES('H1R 6G6', 'Nisl Rd', 5214);
INSERT INTO addresses VALUES('L2H 5T4', 'Magnis Rd', 7271);
INSERT INTO addresses VALUES('V3M 6K4', 'Quam Street', 2923);
INSERT INTO addresses VALUES('V5X 7B6', 'Odio Road', 5779);
INSERT INTO vip_level VALUES(349, 3);
INSERT INTO vip_level VALUES(85, 1);
INSERT INTO vip_level VALUES(249, 3);
INSERT INTO vip_level VALUES(420, 4);
INSERT INTO vip_level VALUES(447, 4);
INSERT INTO vip_level VALUES(696, 6);
INSERT INTO vip_level VALUES(628, 6);
INSERT INTO vip_level VALUES(320, 3);
INSERT INTO vip_level VALUES(512, 5);
INSERT INTO vip_level VALUES(760, 7);
INSERT INTO vip_level VALUES(83, 1);
INSERT INTO vip_level VALUES(812, 8);
INSERT INTO vip_level VALUES(685, 6);
INSERT INTO vip_level VALUES(18, 1);
INSERT INTO vip_level VALUES(300, 3);
INSERT INTO vip_level VALUES(142, 2);
INSERT INTO vip_level VALUES(666, 6);
INSERT INTO vip_level VALUES(590, 5);
INSERT INTO vip_level VALUES(732, 7);
INSERT INTO vip_level VALUES(968, 9);
INSERT INTO points VALUES (349.05, 349);
INSERT INTO points VALUES (85.05, 85);
INSERT INTO points VALUES (249.05, 249);
INSERT INTO points VALUES (420.05, 420);
INSERT INTO points VALUES (447.05, 447);
INSERT INTO points VALUES (696.5, 696);
INSERT INTO points VALUES (628.5, 628);
INSERT INTO points VALUES (320.5, 320);
INSERT INTO points VALUES (512.5, 512);
INSERT INTO points VALUES (760.5, 760);
INSERT INTO points VALUES (83.25, 83);
INSERT INTO points VALUES (812.25, 812);
INSERT INTO points VALUES (685.25, 685);
INSERT INTO points VALUES (18.25, 18);
INSERT INTO points VALUES (300.25, 300);
INSERT INTO points VALUES (142.75, 142);
INSERT INTO points VALUES (666.75, 666);
INSERT INTO points VALUES (590.75, 590);
INSERT INTO points VALUES (732.75, 732);
INSERT INTO points VALUES (968.75, 968);
INSERT INTO users VALUES('b9q3u', 'Wyw3026',4964411825,'Samson Mason');
INSERT INTO users VALUES('a3a8d', 'Wjq0842',8775293274,'Knox Decker');
INSERT INTO users VALUES('b2x1i', 'Lcj7285',2206724159,'Hu Gay');
INSERT INTO users VALUES('p4m3z', 'Oqs3287',4528440553,'Plato Nieves');
INSERT INTO users VALUES('v6v1s', 'Riw2510',2506801882,'Plato Giles');
INSERT INTO users VALUES('o5o4z', 'Kgk0257',6022190508,'Prescott House');
INSERT INTO users VALUES('s0k9j', 'Zop2526',8602461700,'Erasmus Robles');
INSERT INTO users VALUES('u0q9t', 'Fic1422',9136367286,'Wayne Hatfield');
INSERT INTO users VALUES('i6v6n', 'Flc8914',8854482297,'Amela Love');
INSERT INTO users VALUES('j3w7g', 'Edl9349',5425876513,'Brendan Prince');
INSERT INTO users VALUES('f7i2j', 'Dvf4134',9295059958,'Vaughan Bauer');
INSERT INTO users VALUES('q9e2t', 'Ugd5730',7464327235,'Zephania Brewer');
INSERT INTO users VALUES('s8l7a', 'Opp6000',6358791011,'Kelly Weeks');
INSERT INTO users VALUES('i4a9z', 'Wsh4839',3778063150,'Wylie Sears');
INSERT INTO users VALUES('y0v3p', 'Aha2278',9607529877,'Stephanie Harding');
INSERT INTO users VALUES('r0h8e', 'Eem9589',3931686741,'Christian Berger');
INSERT INTO users VALUES('e3r0h', 'Xfc6281',3353000864,'Forrest Montgomery');
INSERT INTO users VALUES('h7e1f', 'Cny7878',5668105961,'Diana Christian');
INSERT INTO users VALUES('o2y1m', 'Uso4231',6183777326,'Odysseus Brady');
INSERT INTO users VALUES('i1b6b', 'Mkf1480',5114423451,'Signe Jordan');
INSERT INTO users VALUES('j2g5z', 'Mtb0525',2202300893,'Rebekah Webb');
INSERT INTO users VALUES('u8c2s', 'Wck5898',8504480517,'Kim Levine');
INSERT INTO users VALUES('f8i4u', 'Bvw9898',9291499372,'Maxine Langley');
INSERT INTO users VALUES('j8e9d', 'Kmz9952',2972358928,'Tiger Austin');
INSERT INTO users VALUES('g1w9r', 'Ztg6437',3103261280,'Charlotte Leonard');
INSERT INTO users VALUES('a4r1i', 'Tnp9362',1297715931,'Hayley Burgess');
INSERT INTO users VALUES('n6d1j', 'Zte8655',1149139019,'Lee Bentley');
INSERT INTO users VALUES('j9w5o', 'Zuy8298',1957412794,'Kareem Larson');
INSERT INTO users VALUES('r5n0d', 'Xus2502',7463391725,'Myles Pearson');
INSERT INTO users VALUES('q9o7h', 'Dyr6662',1619745167,'Hunter Franco');
INSERT INTO users VALUES('a2l5m', 'Yqz8967',1854071398,'Cameron Marquez');
INSERT INTO users VALUES('z3n2a', 'Vud5706',5705644370,'Madonna Dennis');
INSERT INTO users VALUES('k8r8b', 'Vnk5821',4437111141,'Salvador Cervantes');
INSERT INTO users VALUES('x4n8x', 'Xzo7877',1339033040,'Gloria Tyson');
INSERT INTO users VALUES('j7m4z', 'Jxm4570',1309196296,'Keelie Simmons');
INSERT INTO users VALUES('b8r6b', 'Lde7574',2656423555,'Lamar Wheeler');
INSERT INTO users VALUES('v3l9z', 'Lep8113',6039437202,'Kylee Stuart');
INSERT INTO users VALUES('i4o2c', 'Khq3466',5655625064,'Lucius Evans');
INSERT INTO users VALUES('f4g2h', 'Mmq5534',9534828708,'Celeste Mason');
INSERT INTO users VALUES('y3s1r', 'Eum9239',4831062847,'Allen Wiley');
INSERT INTO users VALUES('o0n6k', 'Ofh6770',6972258503,'Dominic Flores');
INSERT INTO users VALUES('q8v8q', 'Jpe6939',3025371278,'Anne Eaton');
INSERT INTO users VALUES('v9j0n', 'Ltl2901',4505830312,'Keiko Henson');
INSERT INTO users VALUES('r6i7m', 'Gtc2632',2334349887,'Brynn Estes');
INSERT INTO users VALUES('n1c4g', 'Biy9777',7531146805,'Kimberly Davis');
INSERT INTO users VALUES('q6c4u', 'Eiv7251',9842366081,'Amos Baker');
INSERT INTO users VALUES('h7x4c', 'Qhs1724',5037784287,'Nasim Alvarado');
INSERT INTO users VALUES('p3o0j', 'Wht8888',1732646648,'Rahim Estrada');
INSERT INTO users VALUES('e5s7g', 'Uba5946',7055499406,'Jenna Ballard');
INSERT INTO users VALUES('q4y6g', 'Xgg7334',6932003463,'Willa Marsh');
INSERT INTO users VALUES('p1m9u', 'Qbp0987',9552328291,'Macon Franks');
INSERT INTO users VALUES('y4l1d', 'Ktz1354',2354425063,'Theodore Cochran');
INSERT INTO users VALUES('g8d0u', 'Fow7020',1727084269,'Paul Santana');
INSERT INTO users VALUES('u0c0z', 'Zos2053',6334013894,'Channing Foley');
INSERT INTO users VALUES('l9z4g', 'Pki1311',5319766374,'Chantale Nixon');
INSERT INTO users VALUES('j5z8q', 'Ylb8276',3189947630,'Chiquita Pacheco');
INSERT INTO users VALUES('u9f2w', 'Maq0802',4007840140,'Audra Foley');
INSERT INTO users VALUES('n5o8e', 'Yyy4062',7823640834,'Dorothy Robinson');
INSERT INTO users VALUES('d5m8a', 'Pyh3308',5992158136,'Kathleen Mckenzie');
INSERT INTO users VALUES('h5c2k', 'Uft5964',5805243098,'Rafael Nixon');
INSERT INTO users VALUES('a0a0a', '123456',1546654165,'James Gong');
INSERT INTO customer VALUES('b9q3u',349.05);
INSERT INTO customer VALUES('a3a8d',85.05);
INSERT INTO customer VALUES('b2x1i',249.05);
INSERT INTO customer VALUES('p4m3z',420.05);
INSERT INTO customer VALUES('v6v1s',447.05);
INSERT INTO customer VALUES('o5o4z',696.5);
INSERT INTO customer VALUES('s0k9j',628.5);
INSERT INTO customer VALUES('u0q9t',320.5);
INSERT INTO customer VALUES('i6v6n',512.5);
INSERT INTO customer VALUES('j3w7g',760.5);
INSERT INTO customer VALUES('f7i2j',83.25);
INSERT INTO customer VALUES('q9e2t',812.25);
INSERT INTO customer VALUES('s8l7a',685.25);
INSERT INTO customer VALUES('i4a9z',18.25);
INSERT INTO customer VALUES('y0v3p',300.25);
INSERT INTO customer VALUES('r0h8e',142.75);
INSERT INTO customer VALUES('e3r0h',666.75);
INSERT INTO customer VALUES('h7e1f',590.75);
INSERT INTO customer VALUES('o2y1m',732.75);
INSERT INTO customer VALUES('i1b6b',968.75);
INSERT INTO courier VALUES('j2g5z');
INSERT INTO courier VALUES('u8c2s');
INSERT INTO courier VALUES('f8i4u');
INSERT INTO courier VALUES('j8e9d');
INSERT INTO courier VALUES('g1w9r');
INSERT INTO courier VALUES('a4r1i');
INSERT INTO courier VALUES('n6d1j');
INSERT INTO courier VALUES('j9w5o');
INSERT INTO courier VALUES('r5n0d');
INSERT INTO courier VALUES('q9o7h');
INSERT INTO courier VALUES('a2l5m');
INSERT INTO courier VALUES('z3n2a');
INSERT INTO courier VALUES('k8r8b');
INSERT INTO courier VALUES('x4n8x');
INSERT INTO courier VALUES('j7m4z');
INSERT INTO courier VALUES('b8r6b');
INSERT INTO courier VALUES('v3l9z');
INSERT INTO courier VALUES('i4o2c');
INSERT INTO courier VALUES('f4g2h');
INSERT INTO courier VALUES('y3s1r');
INSERT INTO courier VALUES('a0a0a');
INSERT INTO restaurant_managers VALUES('o0n6k');
INSERT INTO restaurant_managers VALUES('q8v8q');
INSERT INTO restaurant_managers VALUES('v9j0n');
INSERT INTO restaurant_managers VALUES('r6i7m');
INSERT INTO restaurant_managers VALUES('n1c4g');
INSERT INTO restaurant_managers VALUES('q6c4u');
INSERT INTO restaurant_managers VALUES('h7x4c');
INSERT INTO restaurant_managers VALUES('p3o0j');
INSERT INTO restaurant_managers VALUES('e5s7g');
INSERT INTO restaurant_managers VALUES('q4y6g');
INSERT INTO restaurant_managers VALUES('p1m9u');
INSERT INTO restaurant_managers VALUES('y4l1d');
INSERT INTO restaurant_managers VALUES('g8d0u');
INSERT INTO restaurant_managers VALUES('u0c0z');
INSERT INTO restaurant_managers VALUES('l9z4g');
INSERT INTO restaurant_managers VALUES('j5z8q');
INSERT INTO restaurant_managers VALUES('u9f2w');
INSERT INTO restaurant_managers VALUES('n5o8e');
INSERT INTO restaurant_managers VALUES('d5m8a');
INSERT INTO restaurant_managers VALUES('h5c2k');
INSERT INTO restaurant VALUES (744,'Bishop restaurant','09:00','22:00',2.2,'Coffee Shop',0,'o0n6k','M5N 6G7','Ipsum Av',8066);
INSERT INTO restaurant VALUES (907,'Notch 8 restaurant','09:00','22:00',4.5,'Coffee Shop',0,'q8v8q','L6G 4H1','Auctor St',7892);
INSERT INTO restaurant VALUES (712,'Strike','09:00','22:00',1.8,'Coffee Shop',1,'v9j0n','L8H 0W0','Nulla Ave',3790);
INSERT INTO restaurant VALUES (504,'The holy crab Canada ','09:00','22:00',0.8,'Coffee Shop',1,'r6i7m','T4C 2L4','Luctus Av',9233);
INSERT INTO restaurant VALUES (973,'Tony oyster ','09:00','22:00',4.1,'Coffee Shop',0,'n1c4g','L2R 3W3','Nunc St',3010);
INSERT INTO restaurant VALUES (271,'Lobster man','12:00','22:00',7.6,'Fast Food',0,'q6c4u','H2L 5G9','Libero Street',3924);
INSERT INTO restaurant VALUES (650,'Cambie Street ','12:00','22:00',1.3,'Fast Food',1,'h7x4c','K2N 2A4','Vel Rd',4929);
INSERT INTO restaurant VALUES (373,'Market by Jean-Georges ','12:00','22:00',7.9,'Fast Food',0,'p3o0j','J7W 4X2','Vel Street',9571);
INSERT INTO restaurant VALUES (300,'Zen','12:00','22:00',1.0,'Fast Food',1,'e5s7g','N7Y 6X7','Orci Av',2001);
INSERT INTO restaurant VALUES (149,'Kingyo ','12:00','22:00',3.4,'Fast Food',0,'q4y6g','S1N 0B0','Nulla Av',4646);
INSERT INTO restaurant VALUES (224,'Bella Gelateria','15:00','00:00',4.8,'Asian Food',1,'p1m9u','A9H 4N5','Bibendum Avenue',272);
INSERT INTO restaurant VALUES (938,'Hapa lzakaya','15:00','00:00',4.1,'Asian Food',1,'y4l1d','M9H 1X6','A Street',5690);
INSERT INTO restaurant VALUES (771,'Masayoshi','15:00','00:00',3.2,'Asian Food',0,'g8d0u','K9B 7X8','In St',6512);
INSERT INTO restaurant VALUES (444,'Ancora','15:00','00:00',4.8,'Asian Food',0,'u0c0z','G4W 0G5','Lacus St',8990);
INSERT INTO restaurant VALUES (225,'Banana Leaf ','15:00','00:00',2.6,'Asian Food',0,'l9z4g','M3W 9L5','Quis Rd',6306);
INSERT INTO restaurant VALUES (970,'The Acorn','18:00','00:00',2.5,'European Food',0,'j5z8q','J4S 2K9','Tristique Road',208);
INSERT INTO restaurant VALUES (672,'Ancora waterfront dining and patio ','18:00','00:00',1.6,'European Food',0,'u9f2w','P0Z 8M3','Mauris Av',3023);
INSERT INTO restaurant VALUES (683,'The Keg','18:00','00:00',3.3,'European Food',0,'n5o8e','P5V 8X0','Posuere Rd',2571);
INSERT INTO restaurant VALUES (589,'Steveston pizza company','18:00','00:00',4.7,'European Food',1,'d5m8a','J4R 4K9','Curabitur Road',2786);
INSERT INTO restaurant VALUES (336,'Jam cafe','18:00','00:00',1.9,'European Food',1,'h5c2k','T2N 2E8','Tincidunt Rd',954);
INSERT INTO orders VALUES (20309,'2017-10-13','21:31',16,'SUBMITTED','b9q3u',744);
INSERT INTO orders VALUES (95288,'2018-03-29','06:52',11,'SUBMITTED','a3a8d',907);
INSERT INTO orders VALUES (46351,'2018-02-24','00:52',8,'SUBMITTED','b2x1i',712);
INSERT INTO orders VALUES (19027,'2017-07-18','08:53',2,'READY','p4m3z',504);
INSERT INTO orders VALUES (12799,'2018-04-26','15:56',15,'READY','v6v1s',973);
INSERT INTO orders VALUES (53212,'2018-02-16','02:46',14,'READY','o5o4z',271);
INSERT INTO orders VALUES (22511,'2018-04-02','10:50',4,'READY','s0k9j',650);
INSERT INTO orders VALUES (86078,'2018-03-06','17:48',5,'READY','u0q9t',373);
INSERT INTO orders VALUES (49960,'2018-04-11','17:03',7,'READY','i6v6n',300);
INSERT INTO orders VALUES (90166,'2017-07-15','09:23',7,'READY','j3w7g',149);
INSERT INTO orders VALUES (26402,'2018-01-21','18:59',3,'READY','f7i2j',224);
INSERT INTO orders VALUES (22533,'2018-01-14','15:26',9,'COMPLETE','q9e2t',938);
INSERT INTO orders VALUES (10792,'2017-10-11','10:02',2,'COMPLETE','s8l7a',771);
INSERT INTO orders VALUES (20827,'2017-07-25','16:59',5,'COMPLETE','i4a9z',444);
INSERT INTO orders VALUES (42779,'2018-03-26','15:20',3,'COMPLETE','y0v3p',225);
INSERT INTO orders VALUES (54582,'2017-09-02','11:51',4,'COMPLETE','r0h8e',970);
INSERT INTO orders VALUES (17858,'2018-03-09','08:06',2,'COMPLETE','e3r0h',672);
INSERT INTO orders VALUES (44877,'2017-11-24','21:09',7,'COMPLETE','h7e1f',683);
INSERT INTO orders VALUES (66193,'2018-01-24','22:09',4,'COMPLETE','o2y1m',589);
INSERT INTO orders VALUES (61425,'2017-10-22','14:13',8,'CANCELLED','i1b6b',336);
INSERT INTO orders VALUES (47545,'2018-01-10','03:13',5,'READY','b9q3u',744);
INSERT INTO orders VALUES (33190,'2017-10-30','07:03',2,'READY','a3a8d',907);
INSERT INTO orders VALUES (65510,'2017-09-10','08:33',4,'COMPLETE','b2x1i',712);
INSERT INTO orders VALUES (70542,'2017-09-13','09:53',7,'COMPLETE','p4m3z',504);
INSERT INTO orders VALUES (59256,'2017-10-13','19:49',3,'COMPLETE','v6v1s',973);
INSERT INTO orders VALUES (75166,'2017-09-26','04:00',3,'COMPLETE','o5o4z',271);
INSERT INTO orders VALUES (11276,'2017-12-05','14:27',5,'COMPLETE','s0k9j',650);
INSERT INTO orders VALUES (63876,'2017-08-18','06:08',4,'SUBMITTED','u0q9t',373);
INSERT INTO orders VALUES (33550,'2017-06-24','20:54',5,'SUBMITTED','i6v6n',300);
INSERT INTO orders VALUES (13959,'2018-04-24','21:02',3,'CANCELLED','j3w7g',149);
INSERT INTO orders VALUES (18692,'2018-03-09','04:29',4,'SUBMITTED','f7i2j',224);
INSERT INTO orders VALUES (91827,'2018-03-16','09:17',2,'SUBMITTED','q9e2t',938);
INSERT INTO orders VALUES (80243,'2018-02-16','09:58',1,'SUBMITTED','s8l7a',771);
INSERT INTO orders VALUES (67630,'2017-11-23','12:48',1,'COMPLETE','i4a9z',444);
INSERT INTO orders VALUES (40112,'2017-09-10','23:35',7,'COMPLETE','y0v3p',225);
INSERT INTO orders VALUES (83144,'2018-06-06','03:59',8,'COMPLETE','r0h8e',970);
INSERT INTO orders VALUES (91486,'2017-11-29','09:14',1,'COMPLETE','e3r0h',672);
INSERT INTO orders VALUES (11938,'2018-06-02','11:52',4,'COMPLETE','h7e1f',683);
INSERT INTO orders VALUES (37775,'2017-12-09','07:00',2,'READY','o2y1m',589);
INSERT INTO orders VALUES (55191,'2017-07-11','02:53',3,'CANCELLED','i1b6b',336);
INSERT INTO orders VALUES (39425,'2017-10-05','10:14',1,'COMPLETE','b9q3u',744);
INSERT INTO orders VALUES (76386,'2017-10-06','07:51',3,'COMPLETE','a3a8d',907);
INSERT INTO orders VALUES (49063,'2017-10-07','02:36',5,'COMPLETE','b2x1i',712);
INSERT INTO orders VALUES (84822,'2017-10-08','21:22',4,'COMPLETE','p4m3z',504);
INSERT INTO orders VALUES (23131,'2017-10-09','11:27',3,'COMPLETE','v6v1s',973);
INSERT INTO orders VALUES (12830,'2017-10-10','01:58',1,'COMPLETE','o5o4z',271);
INSERT INTO orders VALUES (78277,'2018-01-01','02:42',4,'COMPLETE','s0k9j',650);
INSERT INTO orders VALUES (16790,'2018-01-19','20:23',1,'COMPLETE','u0q9t',373);
INSERT INTO orders VALUES (18816,'2018-01-12','00:56',6,'COMPLETE','i6v6n',300);
INSERT INTO orders VALUES (72429,'2018-01-14','17:46',6,'COMPLETE','j3w7g',149);
INSERT INTO orders VALUES (70987,'2018-02-21','02:44',5,'COMPLETE','f7i2j',224);
INSERT INTO orders VALUES (42553,'2018-02-25','10:33',1,'COMPLETE','q9e2t',744);
INSERT INTO orders VALUES (11545,'2018-03-08','10:47',1,'COMPLETE','s8l7a',907);
INSERT INTO orders VALUES (92948,'2018-03-10','01:29',1,'COMPLETE','i4a9z',712);
INSERT INTO orders VALUES (58946,'2018-03-14','23:51',1,'COMPLETE','y0v3p',504);
INSERT INTO orders VALUES (73511,'2018-03-21','22:55',2,'COMPLETE','r0h8e',973);
INSERT INTO orders VALUES (59537,'2018-04-02','21:05',4,'COMPLETE','e3r0h',271);
INSERT INTO orders VALUES (97787,'2018-04-01','13:27',1,'COMPLETE','h7e1f',650);
INSERT INTO orders VALUES (42435,'2018-05-31','14:13',1,'COMPLETE','o2y1m',373);
INSERT INTO orders VALUES (76700,'2018-06-05','13:54',1,'CANCELLED','i1b6b',744);
INSERT INTO orders VALUES (53187,'2018-07-09','14:21',4,'COMPLETE','b9q3u',907);
INSERT INTO orders VALUES (62970,'2018-08-11','10:52',4,'COMPLETE','a3a8d',712);
INSERT INTO orders VALUES (35917,'2018-08-17','04:34',5,'COMPLETE','b2x1i',504);
INSERT INTO orders VALUES (25834,'2018-08-24','06:03',4,'COMPLETE','p4m3z',973);
INSERT INTO orders VALUES (49973,'2018-08-28','17:48',1,'COMPLETE','v6v1s',271);
INSERT INTO orders VALUES (90212,'2018-08-30','01:30',6,'COMPLETE','o5o4z',650);
INSERT INTO orders VALUES (57157,'2018-08-31','04:29',5,'COMPLETE','s0k9j',373);
INSERT INTO orders VALUES (83061,'2018-10-02','00:33',1,'COMPLETE','u0q9t',300);
INSERT INTO orders VALUES (44627,'2018-12-21','04:34',4,'DELIVERED','i6v6n',149);
INSERT INTO orders VALUES (42976,'2018-12-25','06:31',1,'DELIVERING','j3w7g',224);
INSERT INTO pick_up VALUES(20309,'00:50');
INSERT INTO pick_up VALUES(95288,'00:32');
INSERT INTO pick_up VALUES(46351,'00:25');
INSERT INTO pick_up VALUES(19027,'00:00');
INSERT INTO pick_up VALUES(12799,'00:00');
INSERT INTO pick_up VALUES(53212,'00:00');
INSERT INTO pick_up VALUES(22511,'00:00');
INSERT INTO pick_up VALUES(86078,'00:00');
INSERT INTO pick_up VALUES(49960,'00:00');
INSERT INTO pick_up VALUES(90166,'00:00');
INSERT INTO pick_up VALUES(26402,'00:00');
INSERT INTO pick_up VALUES(22533,'00:00');
INSERT INTO pick_up VALUES(10792,'00:00');
INSERT INTO pick_up VALUES(20827,'00:00');
INSERT INTO pick_up VALUES(42779,'00:00');
INSERT INTO pick_up VALUES(54582,'00:00');
INSERT INTO pick_up VALUES(17858,'00:00');
INSERT INTO pick_up VALUES(44877,'00:00');
INSERT INTO pick_up VALUES(66193,'00:00');
INSERT INTO pick_up VALUES(61425,'00:00');
INSERT INTO delivery_delivers VALUES (47545,'00:15',6.2,'j2g5z','V0B 8K8','Mauris St','9070');
INSERT INTO delivery_delivers VALUES (33190,'00:26',13.44,'u8c2s','M3J 4X7','Odio Road','4187');
INSERT INTO delivery_delivers VALUES (65510,'00:00',8.13,'f8i4u','J1A 1X6','Eget Road','5866');
INSERT INTO delivery_delivers VALUES (70542,'00:00',11.01,'j8e9d','S3L 5R0','Dolor Avenue','9546');
INSERT INTO delivery_delivers VALUES (59256,'00:00',11.37,'g1w9r','S2V 8B4','Nec St','1394');
INSERT INTO delivery_delivers VALUES (75166,'00:00',6.9,'a4r1i','G2S 2T1','Augue Av','3374');
INSERT INTO delivery_delivers VALUES (11276,'00:00',5.01,'n6d1j','Y1N 6E2','Auctor Avenue','7469');
INSERT INTO delivery_delivers VALUES (63876,'1:30',5.36,'j9w5o','V0K 1E8','Lacus Road','7295');
INSERT INTO delivery_delivers VALUES (33550,'2:00',6.37,'r5n0d','J5K 6W9','Nulla Ave','5688');
INSERT INTO delivery_delivers VALUES (13959,'00:00',12.37,'q9o7h','R0A 3E3','Nullam St','3539');
INSERT INTO delivery_delivers VALUES (18692,'00:40',14.82,'a2l5m','N3J 6Y7','Elit Rd','6303');
INSERT INTO delivery_delivers VALUES (91827,'00:50',6.15,'z3n2a','R5C 6W3','Pharetra Street','7850');
INSERT INTO delivery_delivers VALUES (80243,'00:48',14.01,'k8r8b','T9N 0Z3','Sed Ave','6851');
INSERT INTO delivery_delivers VALUES (67630,'00:00',12.4,'x4n8x','H7X 7N4','Sem Av','4374');
INSERT INTO delivery_delivers VALUES (40112,'00:00',12.61,'j7m4z','S3B 5C6','Ac St','7024');
INSERT INTO delivery_delivers VALUES (83144,'00:00',7.59,'b8r6b','H4R 3R7','Bibendum Road','4885');
INSERT INTO delivery_delivers VALUES (91486,'00:00',8.68,'v3l9z','H1R 6G6','Nisl Rd','5214');
INSERT INTO delivery_delivers VALUES (11938,'00:00',8.37,'i4o2c','L2H 5T4','Magnis Rd','7271');
INSERT INTO delivery_delivers VALUES (37775,'00:24',9.39,'f4g2h','V3M 6K4','Quam Street','2923');
INSERT INTO delivery_delivers VALUES (55191,'00:00',7.96,'y3s1r','V5X 7B6','Odio Road','5779');
INSERT INTO delivery_delivers VALUES (39425,'00:00',8.18,'a0a0a','V0B 8K8','Mauris St','9070');
INSERT INTO delivery_delivers VALUES (76386,'00:00',6.6,'a0a0a','M3J 4X7','Odio Road','4187');
INSERT INTO delivery_delivers VALUES (49063,'00:00',9.99,'a0a0a','J1A 1X6','Eget Road','5866');
INSERT INTO delivery_delivers VALUES (84822,'00:00',7.48,'a0a0a','S3L 5R0','Dolor Avenue','9546');
INSERT INTO delivery_delivers VALUES (23131,'00:00',6.44,'a0a0a','S2V 8B4','Nec St','1394');
INSERT INTO delivery_delivers VALUES (12830,'00:00',12.68,'a0a0a','G2S 2T1','Augue Av','3374');
INSERT INTO delivery_delivers VALUES (78277,'00:00',5.06,'a0a0a','Y1N 6E2','Auctor Avenue','7469');
INSERT INTO delivery_delivers VALUES (16790,'00:00',6.67,'a0a0a','V0K 1E8','Lacus Road','7295');
INSERT INTO delivery_delivers VALUES (18816,'00:00',12.07,'a0a0a','J5K 6W9','Nulla Ave','5688');
INSERT INTO delivery_delivers VALUES (72429,'00:00',9.37,'a0a0a','R0A 3E3','Nullam St','3539');
INSERT INTO delivery_delivers VALUES (70987,'00:00',9.63,'a0a0a','N3J 6Y7','Elit Rd','6303');
INSERT INTO delivery_delivers VALUES (42553,'00:00',6.6,'a0a0a','R5C 6W3','Pharetra Street','7850');
INSERT INTO delivery_delivers VALUES (11545,'00:00',12.46,'a0a0a','T9N 0Z3','Sed Ave','6851');
INSERT INTO delivery_delivers VALUES (92948,'00:00',10.83,'a0a0a','H7X 7N4','Sem Av','4374');
INSERT INTO delivery_delivers VALUES (58946,'00:00',8.35,'a0a0a','S3B 5C6','Ac St','7024');
INSERT INTO delivery_delivers VALUES (73511,'00:00',10.74,'a0a0a','S2V 8B4','Nec St','1394');
INSERT INTO delivery_delivers VALUES (59537,'00:00',8.36,'a0a0a','G2S 2T1','Augue Av','3374');
INSERT INTO delivery_delivers VALUES (97787,'00:00',12.78,'a0a0a','Y1N 6E2','Auctor Avenue','7469');
INSERT INTO delivery_delivers VALUES (42435,'00:00',5.42,'a0a0a','V0K 1E8','Lacus Road','7295');
INSERT INTO delivery_delivers VALUES (76700,'00:00',10.96,'a0a0a','J5K 6W9','Nulla Ave','5688');
INSERT INTO delivery_delivers VALUES (53187,'00:00',4.33,'a0a0a','R0A 3E3','Nullam St','3539');
INSERT INTO delivery_delivers VALUES (62970,'00:00',12.34,'a0a0a','N3J 6Y7','Elit Rd','6303');
INSERT INTO delivery_delivers VALUES (35917,'00:00',8.14,'a0a0a','R5C 6W3','Pharetra Street','7850');
INSERT INTO delivery_delivers VALUES (25834,'00:00',4.51,'a0a0a','T9N 0Z3','Sed Ave','6851');
INSERT INTO delivery_delivers VALUES (49973,'00:00',9.78,'a0a0a','H7X 7N4','Sem Av','4374');
INSERT INTO delivery_delivers VALUES (90212,'00:00',7.14,'a0a0a','S3B 5C6','Ac St','7024');
INSERT INTO delivery_delivers VALUES (57157,'00:00',4.52,'a0a0a','S2V 8B4','Nec St','1394');
INSERT INTO delivery_delivers VALUES (83061,'00:00',10.33,'a0a0a','G2S 2T1','Augue Av','3374');
INSERT INTO delivery_delivers VALUES (44627,'00:00',12.68,'a0a0a','Y1N 6E2','Auctor Avenue','7469');
INSERT INTO delivery_delivers VALUES (42976,'00:15',8.41,'a0a0a','V0K 1E8','Lacus Road','7295');
INSERT INTO food VALUES('Lattes');
INSERT INTO food VALUES('Americanos');
INSERT INTO food VALUES('Cappuccinos');
INSERT INTO food VALUES('Mochas');
INSERT INTO food VALUES('Frech Fries');
INSERT INTO food VALUES('Hamburger');
INSERT INTO food VALUES('Ice Cream');
INSERT INTO food VALUES('Soft Drinks');
INSERT INTO food VALUES('Sandwich');
INSERT INTO food VALUES('Hot Chocolate');
INSERT INTO food VALUES('Sushi');
INSERT INTO food VALUES('Ramen');
INSERT INTO food VALUES('Sashimi');
INSERT INTO food VALUES('Steak');
INSERT INTO food VALUES('Pop Corn');
INSERT INTO food VALUES('Salad');
INSERT INTO food VALUES('Pasta');
INSERT INTO food VALUES('Pizza');
INSERT INTO works_for VALUES('j2g5z',744);
INSERT INTO works_for VALUES('u8c2s',907);
INSERT INTO works_for VALUES('f8i4u',712);
INSERT INTO works_for VALUES('j8e9d',504);
INSERT INTO works_for VALUES('g1w9r',973);
INSERT INTO works_for VALUES('a4r1i',271);
INSERT INTO works_for VALUES('n6d1j',650);
INSERT INTO works_for VALUES('j9w5o',373);
INSERT INTO works_for VALUES('r5n0d',300);
INSERT INTO works_for VALUES('q9o7h',149);
INSERT INTO works_for VALUES('a2l5m',224);
INSERT INTO works_for VALUES('z3n2a',938);
INSERT INTO works_for VALUES('k8r8b',771);
INSERT INTO works_for VALUES('x4n8x',444);
INSERT INTO works_for VALUES('j7m4z',225);
INSERT INTO works_for VALUES('b8r6b',970);
INSERT INTO works_for VALUES('v3l9z',672);
INSERT INTO works_for VALUES('i4o2c',683);
INSERT INTO works_for VALUES('f4g2h',589);
INSERT INTO works_for VALUES('y3s1r',336);
INSERT INTO works_for VALUES('a0a0a',744);
INSERT INTO works_for VALUES('a0a0a',907);
INSERT INTO works_for VALUES('a0a0a',712);
INSERT INTO works_for VALUES('a0a0a',504);
INSERT INTO works_for VALUES('a0a0a',973);
INSERT INTO works_for VALUES('a0a0a',271);
INSERT INTO works_for VALUES('a0a0a',650);
INSERT INTO works_for VALUES('a0a0a',373);
INSERT INTO works_for VALUES('a0a0a',300);
INSERT INTO works_for VALUES('a0a0a',149);
INSERT INTO works_for VALUES('a0a0a',224);
INSERT INTO lives_at VALUES ('V0B 8K8','Mauris St',9070,'b9q3u');
INSERT INTO lives_at VALUES ('M3J 4X7','Odio Road',4187,'a3a8d');
INSERT INTO lives_at VALUES ('J1A 1X6','Eget Road',5866,'b2x1i');
INSERT INTO lives_at VALUES ('S3L 5R0','Dolor Avenue',9546,'p4m3z');
INSERT INTO lives_at VALUES ('S2V 8B4','Nec St',1394,'v6v1s');
INSERT INTO lives_at VALUES ('G2S 2T1','Augue Av',3374,'o5o4z');
INSERT INTO lives_at VALUES ('Y1N 6E2','Auctor Avenue',7469,'s0k9j');
INSERT INTO lives_at VALUES ('V0K 1E8','Lacus Road',7295,'u0q9t');
INSERT INTO lives_at VALUES ('J5K 6W9','Nulla Ave',5688,'i6v6n');
INSERT INTO lives_at VALUES ('R0A 3E3','Nullam St',3539,'j3w7g');
INSERT INTO lives_at VALUES ('N3J 6Y7','Elit Rd',6303,'f7i2j');
INSERT INTO lives_at VALUES ('R5C 6W3','Pharetra Street',7850,'q9e2t');
INSERT INTO lives_at VALUES ('T9N 0Z3','Sed Ave',6851,'s8l7a');
INSERT INTO lives_at VALUES ('H7X 7N4','Sem Av',4374,'i4a9z');
INSERT INTO lives_at VALUES ('S3B 5C6','Ac St',7024,'y0v3p');
INSERT INTO lives_at VALUES ('H4R 3R7','Bibendum Road',4885,'r0h8e');
INSERT INTO lives_at VALUES ('H1R 6G6','Nisl Rd',5214,'e3r0h');
INSERT INTO lives_at VALUES ('L2H 5T4','Magnis Rd',7271,'h7e1f');
INSERT INTO lives_at VALUES ('V3M 6K4','Quam Street',2923,'o2y1m');
INSERT INTO lives_at VALUES ('V5X 7B6','Odio Road',5779,'i1b6b');
INSERT INTO offers VALUES ('Lattes',744,6.55);
INSERT INTO offers VALUES ('Americanos',744,7.03);
INSERT INTO offers VALUES ('Cappuccinos',744,6.25);
INSERT INTO offers VALUES ('Mochas',744,6.22);
INSERT INTO offers VALUES ('Lattes',907,8.24);
INSERT INTO offers VALUES ('Americanos',907,5.51);
INSERT INTO offers VALUES ('Cappuccinos',907,7.55);
INSERT INTO offers VALUES ('Americanos',712,6.3);
INSERT INTO offers VALUES ('Cappuccinos',712,8.28);
INSERT INTO offers VALUES ('Mochas',712,8.43);
INSERT INTO offers VALUES ('Lattes',504,8.14);
INSERT INTO offers VALUES ('Americanos',504,8.67);
INSERT INTO offers VALUES ('Cappuccinos',504,5.68);
INSERT INTO offers VALUES ('Mochas',504,7.21);
INSERT INTO offers VALUES ('Mochas',973,6.5);
INSERT INTO offers VALUES ('Frech Fries',271,3.2);
INSERT INTO offers VALUES ('Frech Fries',650,3.2);
INSERT INTO offers VALUES ('Hamburger',650,5.3);
INSERT INTO offers VALUES ('Ice Cream',650,1.5);
INSERT INTO offers VALUES ('Soft Drinks',650,1.2);
INSERT INTO offers VALUES ('Hamburger',373,4.6);
INSERT INTO offers VALUES ('Ice Cream',300,4.2);
INSERT INTO offers VALUES ('Soft Drinks',149,4.2);
INSERT INTO offers VALUES ('Sandwich',149,6.3);
INSERT INTO offers VALUES ('Hot Chocolate',149,2.26);
INSERT INTO offers VALUES ('Sushi',224,12.5);
INSERT INTO offers VALUES ('Sushi',938,16.4);
INSERT INTO offers VALUES ('Sushi',771,8.98);
INSERT INTO offers VALUES ('Sushi',444,15.8);
INSERT INTO offers VALUES ('Ramen',444,18);
INSERT INTO offers VALUES ('Sashimi',444,19);
INSERT INTO offers VALUES ('Sushi',225,17);
INSERT INTO offers VALUES ('Ramen',225,15.63);
INSERT INTO offers VALUES ('Sashimi',225,27.86);
INSERT INTO offers VALUES ('Steak',970,25);
INSERT INTO offers VALUES ('Pop Corn',672,21.32);
INSERT INTO offers VALUES ('Salad',683,6.23);
INSERT INTO offers VALUES ('Pasta',589,7.5);
INSERT INTO offers VALUES ('Pizza',336,8.25);
INSERT INTO added_in VALUES ('Lattes',20309,16);
INSERT INTO added_in VALUES ('Americanos',95288,11);
INSERT INTO added_in VALUES ('Cappuccinos',46351,8);
INSERT INTO added_in VALUES ('Mochas',19027,2);
INSERT INTO added_in VALUES ('Mochas',12799,15);
INSERT INTO added_in VALUES ('Frech Fries',53212,14);
INSERT INTO added_in VALUES ('Frech Fries',22511,4);
INSERT INTO added_in VALUES ('Hamburger',86078,5);
INSERT INTO added_in VALUES ('Ice Cream',49960,7);
INSERT INTO added_in VALUES ('Soft Drinks',90166,7);
INSERT INTO added_in VALUES ('Sushi',26402,3);
INSERT INTO added_in VALUES ('Sushi',22533,9);
INSERT INTO added_in VALUES ('Sushi',10792,2);
INSERT INTO added_in VALUES ('Ramen',20827,5);
INSERT INTO added_in VALUES ('Sashimi',42779,3);
INSERT INTO added_in VALUES ('Steak',54582,4);
INSERT INTO added_in VALUES ('Pop Corn',17858,2);
INSERT INTO added_in VALUES ('Salad',44877,7);
INSERT INTO added_in VALUES ('Pasta',66193,4);
INSERT INTO added_in VALUES ('Pizza',61425,0);
INSERT INTO added_in VALUES ('Lattes',47545,5);
INSERT INTO added_in VALUES ('Americanos',33190,2);
INSERT INTO added_in VALUES ('Cappuccinos',65510,4);
INSERT INTO added_in VALUES ('Mochas',70542,7);
INSERT INTO added_in VALUES ('Mochas',59256,3);
INSERT INTO added_in VALUES ('Frech Fries',75166,3);
INSERT INTO added_in VALUES ('Frech Fries',11276,5);
INSERT INTO added_in VALUES ('Hamburger',63876,4);
INSERT INTO added_in VALUES ('Ice Cream',33550,5);
INSERT INTO added_in VALUES ('Soft Drinks',13959,0);
INSERT INTO added_in VALUES ('Sushi',18692,4);
INSERT INTO added_in VALUES ('Sushi',91827,2);
INSERT INTO added_in VALUES ('Sushi',80243,1);
INSERT INTO added_in VALUES ('Ramen',67630,1);
INSERT INTO added_in VALUES ('Sashimi',40112,7);
INSERT INTO added_in VALUES ('Steak',83144,8);
INSERT INTO added_in VALUES ('Pop Corn',91486,1);
INSERT INTO added_in VALUES ('Salad',11938,4);
INSERT INTO added_in VALUES ('Pasta',37775,2);
INSERT INTO added_in VALUES ('Pizza',55191,0);





