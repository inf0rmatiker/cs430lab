-- 1
INSERT INTO book (isbn, title, year_published, pub_id)
VALUES ('96-42013-10510', 'Growing your own Weeds', 2012, 10000);

INSERT INTO stored_on (isbn, name, s_num, total_copies)
VALUES ('96-42013-10510', 'Main', 8, 1);

-- 2
UPDATE stored_on SET total_copies = 8
WHERE isbn = '96-42103-10907' AND name = 'Main';

-- 3
DELETE FROM author
WHERE first_name = 'Grace' AND last_name = 'Slick';

-- 4
INSERT INTO author (author_id, first_name, last_name)
VALUES (305, 'Commander', 'Adams');

INSERT IGNORE INTO phone (p_number, type)
VALUES ('970-555-5555', 'o');

INSERT INTO author_phone (author_id, p_number)
VALUES (305, '970-555-5555');

-- 5
INSERT INTO stored_on (isbn, name, s_num, total_copies)
VALUES ('96-42013-10510', 'South Park', 8, 1);


-- 6
DELETE FROM stored_on
WHERE name = 'Main' AND stored_on.isbn =
  (SELECT isbn FROM book AS b WHERE title LIKE '%Missing Tomorrow%');

-- 7
UPDATE stored_on SET total_copies = total_copies + 2 
WHERE stored_on.isbn = 
  (SELECT isbn FROM book WHERE title LIKE '%Eating in the Fort%');


-- 8
INSERT INTO book (isbn, title, year_published, pub_id)
VALUES ('96-42013-10513', 'Growing your own Weeds', 2012, 90000);

INSERT INTO stored_on (isbn, name, s_num, total_copies)
VALUES ('96-42013-10513', 'Main', 8, 1);

-- 9
SELECT * FROM audit;
