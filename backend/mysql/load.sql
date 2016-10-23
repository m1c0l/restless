# Movie

#LOAD DATA LOCAL INFILE '~/data/movie.del' INTO TABLE Movie
	#FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
INSERT INTO UsersInfo
VALUES(id=LAST_INSERT_ID(), first_name="John", last_name="Dough", email="blah", LinkedIn_profile_id=null, bio=null, signup_time=NOW());
