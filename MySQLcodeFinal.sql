CREATE TABLE Conf_Center
    (
     center_ID            INTEGER  NOT NULL ,
     name                 VARCHAR (40)  NOT NULL ,
     adress               VARCHAR (40)  NOT NULL ,
     city                 VARCHAR (40)  NOT NULL ,
     phone_number         VARCHAR (40)  NOT NULL ,
     email                VARCHAR (40)  NOT NULL ,
     available_facilities VARCHAR (40)  NOT NULL
    )
;

ALTER TABLE Conf_Center
    ADD /* Name ignored: CONSTRAINT Conf_Center_PK */ PRIMARY KEY ( center_ID ) ;

CREATE TABLE Conf_Room
    (
     room_ID               INTEGER  NOT NULL ,
     Conf_Center_center_ID INTEGER  NOT NULL ,
     name                  VARCHAR (40)  NOT NULL ,
     max_capacity          INTEGER  NOT NULL ,
     seat_type             VARCHAR (40)  NOT NULL ,
     equipment             VARCHAR (40)  NOT NULL ,
     WiFi_availability     CHAR (1)  NOT NULL ,
     Price_Per_Hour        INTEGER  NOT NULL ,
     availability          CHAR (1)  NOT NULL
    )
;

ALTER TABLE Conf_Room
    ADD /* Name ignored: CONSTRAINT Conf_Room_PK */ PRIMARY KEY ( room_ID ) ;

CREATE TABLE Customer
    (
     customer_ID  INTEGER  NOT NULL ,
     full_name    VARCHAR (40)  NOT NULL ,
     email        VARCHAR (40)  NOT NULL ,
     phone_number VARCHAR (40)  NOT NULL
    )
;

ALTER TABLE Customer
    ADD /* Name ignored: CONSTRAINT Customer_PK */ PRIMARY KEY ( customer_ID ) ;

CREATE TABLE Payment_Info
    (
     Payment_info_ID   INTEGER  NOT NULL ,
     amount            INTEGER  NOT NULL ,
     payment_method    VARCHAR (40)  NOT NULL ,
     payment_date      DATETIME(6) ,
     payment_condition CHAR (1)  NOT NULL
    )
;

ALTER TABLE Payment_Info
    ADD /* Name ignored: CONSTRAINT Payment_Info_PK */ PRIMARY KEY ( Payment_info_ID ) ;

CREATE TABLE Reservation
    (
     reservation_ID               INTEGER  NOT NULL ,
     city                         VARCHAR (40)  NOT NULL ,
     prefered_center              VARCHAR (40) ,
     date_and_starting_time       DATETIME(6) ,
     date_and_finishing_time      DATETIME(6) ,
     number_of_participators      INTEGER ,
     prefrences_in_equipment      VARCHAR (40) ,
     invoice                      CHAR (1) ,
     Customer_customer_ID         INTEGER  NOT NULL ,
     Conf_Room_room_ID            INTEGER ,
     Payment_Info_Payment_info_ID INTEGER  NOT NULL
    )
;
CREATE UNIQUE INDEX Reservation__IDX ON Reservation
    (
     Payment_Info_Payment_info_ID ASC
    )
;

ALTER TABLE Reservation
    ADD /* Name ignored: CONSTRAINT Reservation_PK */ PRIMARY KEY ( reservation_ID ) ;

ALTER TABLE Conf_Room
    ADD CONSTRAINT Conf_Room_Conf_Center_FK FOREIGN KEY
    (
     Conf_Center_center_ID
    )
    REFERENCES Conf_Center
    (
     center_ID
    )
;

ALTER TABLE Reservation
    ADD CONSTRAINT Reservation_Conf_Room_FK FOREIGN KEY
    (
     Conf_Room_room_ID
    )
    REFERENCES Conf_Room
    (
     room_ID
    )
;

ALTER TABLE Reservation
    ADD CONSTRAINT Reservation_Customer_FK FOREIGN KEY
    (
     Customer_customer_ID
    )
    REFERENCES Customer
    (
     customer_ID
    )
;

ALTER TABLE Reservation
    ADD CONSTRAINT Reservation_Payment_Info_FK FOREIGN KEY
    (
     Payment_Info_Payment_info_ID
    )
    REFERENCES Payment_Info
    (
     Payment_info_ID
    )
;