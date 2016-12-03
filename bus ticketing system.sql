CREATE TABLE staff (
  staff_id     VARCHAR(5)   NOT NULL,
  name         VARCHAR(50),
  IC           VARCHAR(14),
  email        VARCHAR(50), 
  DOB          DATE,
  contactNo   VARCHAR(12),
PRIMARY KEY (staff_id)
);

INSERT INTO staff VALUES('S0001','LEI JIAN WEN','630101-05-2013','iamaboy@gmail.com','1963-01-01','018-5986221');
INSERT INTO staff VALUES('S0002','TAN KAH HENG','750211-05-5053','tkh7251@gmail.com','1975-02-11','016-5124831');
INSERT INTO staff VALUES('S0003', 'ANDREW FISHMAN', '860508-01-5120','andrewfm@hotmail.com', '1979-08-01', '013-5485479');
INSERT INTO staff VALUES('S0004', 'KELLY KETHENSON', '801216-04-9542','kelly@gmail.com','1980-12-16', '017-4156321');
INSERT INTO staff VALUES('S0005', 'ANGELINA JOLIN', '850906-04-1252','angjolin@gmail.com', '1985-09-06', '012-8765433');
INSERT INTO staff VALUES('S0006', 'STELLA HAVARD', '810108-14-5222', 'stella@gmail.com','1981-01-08', '018-1354825');
INSERT INTO staff VALUES('S0007', 'ROBERTSON', '810920-14-9541', 'robertson@yahoo.com', '1981-09-20', '016-7677987');
INSERT INTO staff VALUES('S0008', 'COLIN WILLSON', '871130-04-1251', 'colin@yahoo.com', '1987-11-30', '016-2157125');
INSERT INTO staff VALUES('S0009', 'JOYCE LANDEREON', '880401-14-1542',  'joyce@yahoo.com','1988-04-01', '017-2468137');
INSERT INTO staff VALUES('S0010', 'LEONG WUN KEONG', '850512-10-1209', 'lwk@hotmail.com', '1985-05-12', '013-5459840');



CREATE TABLE bus (
  bus_plate_no       VARCHAR(5)   NOT NULL,
  bus_type           VARCHAR(20) CONSTRAINT "Check_bus_type" CHECK (bus_type IN('Economic','Premium')),
  bus_status         VARCHAR(20) CONSTRAINT "Check_bus_status" CHECK (bus_status IN('Available','Maintenance')),
  bus_model          VARCHAR(50),
  total_seat         VARCHAR(5),
  date_purchase      DATE,
  bus_number         VARCHAR(7),
PRIMARY KEY (bus_plate_no)
);

INSERT INTO bus VALUES('B0001', 'Economic','Available','MAN-SE','40','2016-02-03','WBK2013');
INSERT INTO bus VALUES('B0002', 'Premium', 'Available', 'ACE Cougar', '20', '2016-02-23', 'JSK5421');
INSERT INTO bus VALUES('B0003', 'Economic','Available', 'Bedford VAL', '40', '2016-03-23', 'JN1215A');
INSERT INTO bus VALUES('B0004', 'Economic', 'Available', 'DAF MB200', '40', '2016-03-11', 'JHD5421');
INSERT INTO bus VALUES('B0005', 'Economic','Available', 'Duple Dominant', '40', '2016-03-23', 'BMSA152');


CREATE TABLE schedule (
  schedule_id      VARCHAR(6)   NOT NULL,
  departure_date   DATE,
  departure_time   TIME,
  departure_place  VARCHAR(50),
  destination      VARCHAR(50),
  price            NUMERIC(10,2),
  no_of_available_seat   NUMERIC(5),
  bus_plate_no     VARCHAR(5),
  bus_seat_availability  VARCHAR(15)CONSTRAINT "Check_bus_seat_availability" CHECK (bus_seat_availability IN('Haveseat','Nothaveseat')),
PRIMARY KEY (schedule_id),
FOREIGN KEY (bus_plate_no) REFERENCES bus(bus_plate_no)
);
INSERT INTO schedule VALUES('H00001','2016-03-01','19:00:00','Kuala Lumpur','Seremban',6.00,39,'B0001','Haveseat');
INSERT INTO schedule VALUES('H00002','2016-04-01','18:00:00','Kuala Lumpur','Seremban',6.00,40,'B0004','Haveseat');
INSERT INTO schedule VALUES('H00003', '2016-03-28', '10:00:00', 'Pudu Sentral', 'Ipoh', 11.00, 36, 'B0004', 'Haveseat');
INSERT INTO schedule VALUES('H00004', '2016-04-06', '14:00:00', 'Seremban', 'Ipoh', 13.00, 32, 'B0003', 'Haveseat');
INSERT INTO schedule VALUES('H00005', '2016-04-08', '17:00:00', 'Ipoh', 'Pudu Sentral', 11.00, 12, 'B0002', 'Haveseat');


CREATE TABLE seat (
  seat_id         VARCHAR(5),
  seat_no         VARCHAR(5),
  seat_status    VARCHAR(20) CONSTRAINT "Check_seat_status" CHECK (seat_status IN('Available','Unavailable')), 
  bus_plate_no   VARCHAR(5),
  schedule_id    VARCHAR(6),
PRIMARY KEY (seat_id),
FOREIGN KEY (bus_plate_no) REFERENCES bus(bus_plate_no),
FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);

INSERT INTO seat VALUES('E0001','1','Unavailable','B0001','H00001');
INSERT INTO seat VALUES('E0002','2','Available','B0001','H00001');
INSERT INTO seat VALUES('E0003','3','Available','B0001','H00001');
INSERT INTO seat VALUES('E0004','4','Available','B0001','H00001');
INSERT INTO seat VALUES('E0005','5','Available','B0001','H00001');
INSERT INTO seat VALUES('E0006','6','Available','B0001','H00001');
INSERT INTO seat VALUES('E0007','7','Available','B0001','H00001');
INSERT INTO seat VALUES('E0008','8','Available','B0001','H00001');
INSERT INTO seat VALUES('E0009','9','Available','B0001','H00001');
INSERT INTO seat VALUES('E0010','10','Available','B0001','H00001');
INSERT INTO seat VALUES('E0011','11','Available','B0001','H00001');
INSERT INTO seat VALUES('E0012','12','Available','B0001','H00001');
INSERT INTO seat VALUES('E0013','13','Available','B0001','H00001');
INSERT INTO seat VALUES('E0014','14','Available','B0001','H00001');
INSERT INTO seat VALUES('E0015','15','Available','B0001','H00001');
INSERT INTO seat VALUES('E0016','16','Available','B0001','H00001');
INSERT INTO seat VALUES('E0017','17','Available','B0001','H00001');
INSERT INTO seat VALUES('E0018','18','Available','B0001','H00001');
INSERT INTO seat VALUES('E0019','19','Available','B0001','H00001');
INSERT INTO seat VALUES('E0020','20','Available','B0001','H00001');
INSERT INTO seat VALUES('E0021','21','Available','B0001','H00001');
INSERT INTO seat VALUES('E0022','22','Available','B0001','H00001');
INSERT INTO seat VALUES('E0023','23','Available','B0001','H00001');
INSERT INTO seat VALUES('E0024','24','Available','B0001','H00001');
INSERT INTO seat VALUES('E0025','25','Available','B0001','H00001');
INSERT INTO seat VALUES('E0026','26','Available','B0001','H00001');
INSERT INTO seat VALUES('E0027','27','Available','B0001','H00001');
INSERT INTO seat VALUES('E0028','28','Available','B0001','H00001');
INSERT INTO seat VALUES('E0029','29','Available','B0001','H00001');
INSERT INTO seat VALUES('E0030','30','Available','B0001','H00001');
INSERT INTO seat VALUES('E0031','31','Available','B0001','H00001');
INSERT INTO seat VALUES('E0032','32','Available','B0001','H00001');
INSERT INTO seat VALUES('E0033','33','Available','B0001','H00001');
INSERT INTO seat VALUES('E0034','34','Available','B0001','H00001');
INSERT INTO seat VALUES('E0035','35','Available','B0001','H00001');
INSERT INTO seat VALUES('E0036','36','Available','B0001','H00001');
INSERT INTO seat VALUES('E0037','37','Available','B0001','H00001');
INSERT INTO seat VALUES('E0038','38','Available','B0001','H00001');
INSERT INTO seat VALUES('E0039','39','Available','B0001','H00001');
INSERT INTO seat VALUES('E0040','40','Available','B0001','H00001');

INSERT INTO seat VALUES('E0041','1','Available','B0004','H00002');
INSERT INTO seat VALUES('E0042','2','Available','B0004','H00002');
INSERT INTO seat VALUES('E0043','3','Available','B0004','H00002');
INSERT INTO seat VALUES('E0044','4','Available','B0004','H00002');
INSERT INTO seat VALUES('E0045','5','Available','B0004','H00002');
INSERT INTO seat VALUES('E0046','6','Available','B0004','H00002');
INSERT INTO seat VALUES('E0047','7','Available','B0004','H00002');
INSERT INTO seat VALUES('E0048','8','Available','B0004','H00002');
INSERT INTO seat VALUES('E0049','9','Available','B0004','H00002');
INSERT INTO seat VALUES('E0050','10','Available','B0004','H00002');
INSERT INTO seat VALUES('E0051','11','Available','B0004','H00002');
INSERT INTO seat VALUES('E0052','12','Available','B0004','H00002');
INSERT INTO seat VALUES('E0053','13','Available','B0004','H00002');
INSERT INTO seat VALUES('E0054','14','Available','B0004','H00002');
INSERT INTO seat VALUES('E0055','15','Available','B0004','H00002');
INSERT INTO seat VALUES('E0056','16','Available','B0004','H00002');
INSERT INTO seat VALUES('E0057','17','Available','B0004','H00002');
INSERT INTO seat VALUES('E0058','18','Available','B0004','H00002');
INSERT INTO seat VALUES('E0059','19','Available','B0004','H00002');
INSERT INTO seat VALUES('E0060','20','Available','B0004','H00002');
INSERT INTO seat VALUES('E0061','21','Available','B0004','H00002');
INSERT INTO seat VALUES('E0062','22','Available','B0004','H00002');
INSERT INTO seat VALUES('E0063','23','Available','B0004','H00002');
INSERT INTO seat VALUES('E0064','24','Available','B0004','H00002');
INSERT INTO seat VALUES('E0065','25','Available','B0004','H00002');
INSERT INTO seat VALUES('E0066','26','Available','B0004','H00002');
INSERT INTO seat VALUES('E0067','27','Available','B0004','H00002');
INSERT INTO seat VALUES('E0068','28','Available','B0004','H00002');
INSERT INTO seat VALUES('E0069','29','Available','B0004','H00002');
INSERT INTO seat VALUES('E0070','30','Available','B0004','H00002');
INSERT INTO seat VALUES('E0071','31','Available','B0004','H00002');
INSERT INTO seat VALUES('E0072','32','Available','B0004','H00002');
INSERT INTO seat VALUES('E0073','33','Available','B0004','H00002');
INSERT INTO seat VALUES('E0074','34','Available','B0004','H00002');
INSERT INTO seat VALUES('E0075','35','Available','B0004','H00002');
INSERT INTO seat VALUES('E0076','36','Available','B0004','H00002');
INSERT INTO seat VALUES('E0077','37','Available','B0004','H00002');
INSERT INTO seat VALUES('E0078','38','Available','B0004','H00002');
INSERT INTO seat VALUES('E0079','39','Available','B0004','H00002');
INSERT INTO seat VALUES('E0080','40','Available','B0004','H00002');

INSERT INTO seat VALUES('E0081', '1', 'Unavailable', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0082', '2', 'Unavailable', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0083', '3', 'Unavailable', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0084', '4', 'Unavailable', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0085', '5', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0086', '6', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0087', '7', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0088', '8', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0089', '9', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0090', '10', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0091', '11', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0092', '12', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0093', '13', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0094', '14', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0095', '15', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0096', '16', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0097', '17', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0098', '18', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0099', '19', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0100', '20', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0101', '21', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0102', '22', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0103', '23', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0104', '24', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0105', '25', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0106', '26', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0107', '27', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0108', '28', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0109', '29', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0110', '30', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0111', '31', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0112', '32', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0113', '33', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0114', '34', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0115', '35', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0116', '36', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0117', '37', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0118', '38', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0119', '39', 'Available', 'B0004', 'H00003');
INSERT INTO seat VALUES('E0120', '40', 'Available', 'B0004', 'H00003');

INSERT INTO seat VALUES('E0121', '1', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0122', '2', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0123', '3', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0124', '4', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0125', '5', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0126', '6', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0127', '7', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0128', '8', 'Unavailable', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0129', '9', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0130', '10', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0131', '11', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0132', '12', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0133', '13', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0134', '14', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0135', '15', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0136', '16', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0137', '17', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0138', '18', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0139', '19', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0140', '20', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0141', '21', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0142', '22', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0143', '23', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0144', '24', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0145', '25', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0146', '26', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0147', '27', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0148', '28', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0149', '29', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0150', '30', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0151', '31', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0152', '32', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0153', '33', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0154', '34', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0155', '35', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0156', '36', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0157', '37', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0158', '38', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0159', '39', 'Available', 'B0003', 'H00004');
INSERT INTO seat VALUES('E0160', '40', 'Available', 'B0003', 'H00004');

INSERT INTO seat VALUES('E0161', '1', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0162', '2', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0163', '3', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0164', '4', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0165', '5', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0166', '6', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0167', '7', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0168', '8', 'Unavailable', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0169', '9', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0170', '10', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0171', '11', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0172', '12', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0173', '13', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0174', '14', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0175', '15', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0176', '16', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0177', '17', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0178', '18', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0179', '19', 'Available', 'B0002', 'H00005');
INSERT INTO seat VALUES('E0180', '20', 'Available', 'B0002', 'H00005');



CREATE TABLE transaction1 (
  transaction_id     VARCHAR(5)   NOT NULL,
  transaction_date   DATE,
  seat_purchase      NUMERIC(1),
  no_of_seat         VARCHAR(20),
  no_of_seat1         VARCHAR(20),
  no_of_seat2         VARCHAR(20),
  no_of_seat3         VARCHAR(20),
  booking_status      VARCHAR(20) CONSTRAINT "Check_booking_status" CHECK (booking_status IN('Complete','InProcess','Cancel')), 
  payment_status VARCHAR(20) CONSTRAINT "Check_payment_status" CHECK (payment_status IN('HaventPay','AlreadyPay')), 
  schedule_id        VARCHAR(6),
PRIMARY KEY (transaction_id),
FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id)
);

INSERT INTO transaction1 VALUES('T0001','2016-02-25', 1,'E0001','No available','No available','No available','Complete','AlreadyPay','H00001');




CREATE TABLE payment (
    payment_id     VARCHAR(5) NOT NULL,
    payment_date   DATE,
    payment_type   VARCHAR(15),
    total_amount       NUMERIC(10,2),
    payment_amount NUMERIC(10,2),
    balance        NUMERIC(10,2),
    card_no        VARCHAR(20),
    card_holder    VARCHAR(20),
    expiry_date    VARCHAR(20),
    card_type      VARCHAR(20),
    bank_name      VARCHAR(50),
    transaction_id           VARCHAR(5),
PRIMARY KEY (payment_id),
FOREIGN KEY (transaction_id) REFERENCES transaction1(transaction_id)
);

INSERT INTO payment VALUES('P0001','2016-02-27', 'Cash', 6.00, 6.00, 0.00,'No available','No available','No available','No available','No available', 'T0001');
