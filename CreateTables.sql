DROP TABLE IF EXISTS borrowed_by,
                     written_by,
                     author_phone,
                     publisher_phone,
                     phone,
                     author,
                     book,
                     publisher,
                     member;
                    
--         MAIN ENTITY TABLES     

CREATE TABLE IF NOT EXISTS author (
  author_id       INTEGER     NOT NULL,
  first_name      VARCHAR(20) NOT NULL,
  last_name       VARCHAR(20) NOT NULL,
  PRIMARY KEY (author_id)
);

CREATE TABLE IF NOT EXISTS publisher (
  pub_id          INTEGER     NOT NULL,
  pub_name        VARCHAR(40) NOT NULL,
  PRIMARY KEY (pub_id)
);

CREATE TABLE IF NOT EXISTS book (
  isbn            CHAR(14)    NOT NULL,
  title           VARCHAR(40) NOT NULL,
  year_published  INTEGER     NOT NULL,
  pub_id          INTEGER     NOT NULL,
  PRIMARY KEY (isbn),
  FOREIGN KEY (pub_id) REFERENCES publisher(pub_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS member (
  member_id       INTEGER     NOT NULL,
  first_name      VARCHAR(20) NOT NULL,
  last_name       VARCHAR(20) NOT NULL,
  dob             DATE        NOT NULL,
  PRIMARY KEY (member_id)
);

CREATE TABLE IF NOT EXISTS phone (
  p_number        CHAR(12)    NOT NULL,
  type            CHAR(1)     NOT NULL,
  PRIMARY KEY (p_number)
);


--         RELATION TABLES

CREATE TABLE IF NOT EXISTS written_by (
  author_id       INTEGER     NOT NULL,
  isbn            CHAR(14)    NOT NULL,
  PRIMARY KEY (author_id, isbn),
  FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE,
  FOREIGN KEY (isbn)      REFERENCES book(isbn)        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS borrowed_by (
  member_id       INTEGER     NOT NULL,
  isbn            CHAR(14)    NOT NULL,
  checkout_date   DATE        NOT NULL,
  checkin_date    DATE        ,
  PRIMARY KEY (member_id, isbn, checkout_date),
  FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
  FOREIGN KEY (isbn)      REFERENCES book(isbn)        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS author_phone (
  author_id       INTEGER     NOT NULL,
  p_number        CHAR(12)    NOT NULL,
  PRIMARY KEY (author_id, p_number),
  FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE,
  FOREIGN KEY (p_number)  REFERENCES phone(p_number)   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS publisher_phone (
  pub_id          INTEGER     NOT NULL,
  p_number        CHAR(12)    NOT NULL,
  PRIMARY KEY (pub_id, p_number),
  FOREIGN KEY (pub_id)    REFERENCES publisher(pub_id) ON DELETE CASCADE,
  FOREIGN KEY (p_number)  REFERENCES phone(p_number)   ON DELETE CASCADE
);

