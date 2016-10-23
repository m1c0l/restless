DB = restless

reload:
	mysql $(DB) < clean.sql
	mysql $(DB) < create.sql
	mysql $(DB) < load.sql
