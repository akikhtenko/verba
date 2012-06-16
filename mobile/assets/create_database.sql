CREATE TABLE dictionary (
	_id 			INTEGER PRIMARY KEY,
    name  			TEXT,
    description		TEXT
)/

CREATE TABLE dictionary_entry (
    offset			INTEGER,
    length			INTEGER,
    phrase			TEXT,
    dictionary_id	INTEGER,
    FOREIGN KEY(dictionary_id) REFERENCES dictionary(_id)
)/

CREATE INDEX dictionary_entry_phrase_idx on dictionary_entry (phrase)/

CREATE TABLE card_set (
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