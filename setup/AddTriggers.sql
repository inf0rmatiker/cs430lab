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

