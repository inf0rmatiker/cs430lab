-- ########## QUESTION 5 ###########

-- List the contents of the Library relation in order according to name. 
SELECT * FROM library ORDER BY name;

-- List the contents of the Shelf relation in alphabetic order according to Libary then floor. 
SELECT * FROM shelf ORDER BY name, s_floor;

-- List the contents of the Stored on relation in alphabetic order according to ISBN. 
SELECT * FROM stored_on ORDER BY isbn;

-- For each book that has copies in both libraries, list the book name, shelf #, and library sorted by book name.
SELECT shared_books.title, s_o.s_num, s_o.name 
  FROM stored_on AS s_o
  INNER JOIN
  (SELECT b.isbn, b.title FROM book AS b
    INNER JOIN
      (SELECT isbn, count(*) FROM stored_on
        GROUP BY isbn
        HAVING count(*) > 1) AS shared
    ON b.isbn = shared.isbn) AS shared_books
  ON shared_books.isbn = s_o.isbn
  ORDER BY shared_books.title;

-- For each shelf, list the shelf, library, and number of titles sorted by library and shelf.
SELECT s_num, name, count(*) FROM stored_on
  GROUP BY s_num, name 
  ORDER BY name, s_num;

-- ############# QUESTION 6 #############

-- Create a set of triggers that stores action, date and time anyone successfully adds an author, adds or deletes a book from a shelf, or modifies the number of copies of a book.
DROP TRIGGER IF EXISTS add_author_trigger;
DROP TRIGGER IF EXISTS add_book_to_shelf;
DROP TRIGGER IF EXISTS del_book_from_shelf;
DROP TRIGGER IF EXISTS update_book_copies;

delimiter //
CREATE TRIGGER add_author_trigger AFTER INSERT ON author
  FOR EACH ROW
    BEGIN
      INSERT INTO audit (table_name, action, time)
      VALUES ('author', 'INSERT', NOW());
    END;//
delimiter ;

delimiter //
CREATE TRIGGER add_book_to_shelf AFTER INSERT ON stored_on
  FOR EACH ROW
    BEGIN
      INSERT INTO audit (table_name, action, time)
      VALUES ('stored_on', 'INSERT', NOW());
    END;//
delimiter ;

delimiter //
CREATE TRIGGER del_book_from_shelf AFTER DELETE ON stored_on
  FOR EACH ROW
    BEGIN
      INSERT INTO audit (table_name, action, time)
      VALUES ('stored_on', 'DELETE', NOW());
    END;//
delimiter ;

delimiter //
CREATE TRIGGER update_book_copies AFTER UPDATE ON stored_on
  FOR EACH ROW
    BEGIN
      INSERT INTO audit (table_name, action, time)
      VALUES ('stored_on', 'UPDATE', NOW());
    END;//
delimiter ;

-- Question 7 View Created in CreateTables.sql
-- ################# QUESTION 8 ##############

SELECT * FROM list_books_csv;



