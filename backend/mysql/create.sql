# Movie

CREATE TABLE Movie (
	id INT,
	title VARCHAR(100),
	year INT,
	rating VARCHAR(10),
	company VARCHAR(50),
  PRIMARY KEY(id) # each Movie has a unique ID
) ENGINE=INNODB;
