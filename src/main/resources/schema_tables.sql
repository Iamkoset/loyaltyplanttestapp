#MySQL
CREATE SCHEMA lplant;

use lplant;

CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  firstName varchar(255) NOT NULL,
  lastName varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (email)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE account (
  accountNumber bigint(20) NOT NULL AUTO_INCREMENT,
  currency bigint(20) DEFAULT '0',
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (accountNumber),
  INDEX fk_user_id (user_id) USING BTREE,
  CONSTRAINT fk_user_id
  FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;