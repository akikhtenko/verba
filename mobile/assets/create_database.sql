CREATE TABLE dictionary (
	_id 			INTEGER PRIMARY KEY,
    name  			TEXT,
    description		TEXT
)/

CREATE VIRTUAL TABLE dictionary_entry USING fts3 (
    offset			INTEGER,
    length			INTEGER,
    phrase			TEXT,
    dictionary_id	INTEGER,
    FOREIGN KEY(dictionary_id) REFERENCES dictionary(_id)
)/

CREATE TABLE cards_set (
	_id 	INTEGER PRIMARY KEY,
    name  	TEXT
)/

CREATE TABLE card (
	_id 		INTEGER PRIMARY KEY,
    phrase 		TEXT,
    definition	TEXT,
	set_id		INTEGER,
	FOREIGN KEY(set_id) REFERENCES cards_set(_id)
)/