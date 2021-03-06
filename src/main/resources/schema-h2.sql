DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(ID INT PRIMARY KEY auto_increment,NAME VARCHAR(255), PASSWORD VARCHAR(255), EMAIL VARCHAR(255));
DROP TABLE IF EXISTS PORTS;
CREATE TABLE PORTS(ID INT PRIMARY KEY auto_increment,PORT VARCHAR(255), OWNER_ID INT);
DROP TABLE IF EXISTS REQUESTS;
CREATE TABLE REQUESTS(ID INT PRIMARY KEY auto_increment,PORT_ID VARCHAR(255), OPERATOR INT, PORTOWNER INT, STATUS VARCHAR(255), DELETE_FLAG VARCHAR(1), UPDATE_TIME TIMESTAMP);