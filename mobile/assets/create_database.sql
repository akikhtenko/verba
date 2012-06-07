CREATE TABLE dictionary (
	_id 			INTEGER PRIMARY KEY,
    name  			TEXT,
    description		TEXT
);

CREATE TABLE dictionary_index (
	_id 			INTEGER PRIMARY KEY,
    phrase 			TEXT,
    offset			INTEGER,
    length			INTEGER
);

CREATE UNIQUE INDEX idx_dictionary_index ON dictionary_index (phrase);

CREATE TABLE cards_set (
	_id 	INTEGER PRIMARY KEY,
    name  	TEXT
);

CREATE TABLE card (
	_id 		INTEGER PRIMARY KEY,
    phrase 		TEXT,
    definition	TEXT,
	set_id		INTEGER,
	FOREIGN KEY(set_id) REFERENCES cards_set(_id)
);