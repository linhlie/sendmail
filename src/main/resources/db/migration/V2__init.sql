CREATE TABLE sendemail (
id int(20) auto_increment NOT NULL ,
title  varchar(100),
receiver text ,
content text,
sendtime date,
PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;