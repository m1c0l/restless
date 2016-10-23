-- Skills

CREATE TABLE Skills (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT name_unique UNIQUE(name)
);

CREATE TABLE UsersInfo (
    id INT NOT NULL AUTO_INCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    LinkedIn_profile_id TEXT, -- can be null
    bio TEXT,
    signup_time TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE ProjectsInfo (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    current_state SMALLINT NOT NULL, -- recruiting, etc.
    PRIMARY KEY(id),
    CONSTRAINT title_unique UNIQUE(title)
);
