-- USERS
insert into USERS (name, password, email) values ('oa', 'password', 'oa@gmail.com')
insert into USERS (name, password, email) values ('ob', 'password', 'ob@gmail.com')
insert into USERS (name, password, email) values ('ox', 'password', 'ox@gmail.com')
insert into USERS (name, password, email) values ('oz', 'password', 'oz@gmail.com')

-- PORTS
insert into PORTS (PORT, OWNER_ID) values ('8080', 4)
insert into PORTS (PORT, OWNER_ID) values ('8081', 2)
insert into PORTS (PORT, OWNER_ID) values ('8082', 1)
insert into PORTS (PORT, OWNER_ID) values ('8083', 3)

-- REQUESTS
insert into REQUESTS (PORT_ID, OPERATOR, PORTOWNER, STATUS, DELETE_FLAG, UPDATE_TIME) values ('8080', 1, 4, 'Pending', '0', CURRENT_TIMESTAMP)
insert into REQUESTS (PORT_ID, OPERATOR, PORTOWNER, STATUS, DELETE_FLAG, UPDATE_TIME) values ('8081', 1, 2, 'Accepted', '0', CURRENT_TIMESTAMP)
insert into REQUESTS (PORT_ID, OPERATOR, PORTOWNER, STATUS, DELETE_FLAG, UPDATE_TIME) values ('8082', 3, 1, 'Pending', '0', CURRENT_TIMESTAMP)
