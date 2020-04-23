DROP TABLE IF EXISTS borrowed_by,
                     written_by,
                     author_phone,
                     publisher_phone,
                     phone,
                     author,
                     stored_on,
                     book,
                     publisher,
                     member,
                     shelf,
                     library;
                    
-- ##################### MAIN ENTITY TABLES ####################

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
  gender          CHAR(1)     NOT NULL,
  PRIMARY KEY (member_id)
);

CREATE TABLE IF NOT EXISTS phone (
  p_number        CHAR(12)    NOT NULL,
  type            CHAR(1)     NOT NULL,
  PRIMARY KEY (p_number)
);

CREATE TABLE IF NOT EXISTS library (
  name            VARCHAR(20) NOT NULL,
  street          VARCHAR(20) NOT NULL,
  city            VARCHAR(20) NOT NULL,
  state           VARCHAR(20) NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS shelf (
  name            VARCHAR(20) NOT NULL,
  s_num           INTEGER     NOT NULL,
  s_floor         INTEGER     NOT NULL,
  PRIMARY KEY (name, s_num),
  FOREIGN KEY (name) REFERENCES library(name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audit (
  id              INTEGER     NOT NULL AUTO_INCREMENT,
  table_name      VARCHAR(20) NOT NULL,
  action          VARCHAR(10) NOT NULL,
  time            DATETIME    NOT NULL,
  PRIMARY KEY (id)
);


-- ######################### RELATION TABLES #############################

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

CREATE TABLE IF NOT EXISTS stored_on (
  isbn            CHAR(14)    NOT NULL,
  name            VARCHAR(20) NOT NULL,
  s_num           INTEGER     NOT NULL,
  total_copies    INTEGER     NOT NULL,
  PRIMARY KEY (isbn, name, s_num),
  FOREIGN KEY (isbn)  REFERENCES book(isbn)     ON DELETE CASCADE,
  FOREIGN KEY (name)  REFERENCES library(name)  ON DELETE CASCADE,
  FOREIGN KEY (name, s_num) REFERENCES shelf(name, s_num)   ON DELETE CASCADE            
);

-- ##################### VIEWS ##########################

CREATE SQL SECURITY INVOKER VIEW list_books_csv AS 
  SELECT CONCAT(title,',',author_list,',',s_num,',',s_floor,',',name) AS result FROM
    (SELECT title, author_list, s.s_num, s.s_floor, s.name FROM shelf AS s
      INNER JOIN
      (SELECT title, author_list, s_num, name FROM stored_on AS so
        INNER JOIN
        (SELECT title, isbn, GROUP_CONCAT(full_name) AS author_list FROM
          (SELECT x_0.title, x_0.isbn, CONCAT(first_name, ' ', last_name) AS full_name FROM author AS a
            INNER JOIN
            (SELECT b.title, b.isbn, wb.author_id FROM book AS b
              INNER JOIN written_by AS wb
              ON b.isbn = wb.isbn) AS x_0
            ON a.author_id = x_0.author_id) AS x_1
          GROUP BY isbn) AS res_0
        ON res_0.isbn = so.isbn) AS res_1
      ON s.s_num = res_1.s_num AND s.name = res_1.name) AS q
    ORDER BY result;
