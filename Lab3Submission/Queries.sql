-- Question 5
SELECT * FROM book ORDER BY isbn;

SELECT * FROM member ORDER BY last_name, first_name;

SELECT * FROM author ORDER BY last_name, first_name;

SELECT * FROM publisher ORDER BY pub_name;

SELECT * FROM phone ORDER BY p_number;

-- Question 6
SELECT * FROM borrowed_by;

SELECT * FROM written_by;

SELECT * FROM author_phone;

SELECT * FROM publisher_phone;

-- Question 7
SELECT first_name, last_name FROM member
  WHERE member.last_name LIKE 'b%';

-- Question 8
SELECT b.isbn, b.title, b.year_published, b.pub_id FROM book AS b
    INNER JOIN (SELECT * FROM publisher WHERE pub_name = 'Coyote Publishing') AS p
    ON b.pub_id = p.pub_id
    ORDER BY b.title;

-- Question 9
SELECT checkouts.member_id, checkouts.first_name, checkouts.last_name, b.isbn, b.title FROM book AS b
    INNER JOIN (
        SELECT m.member_id, m.first_name, m.last_name, current.isbn FROM member AS m
            INNER JOIN (SELECT * FROM borrowed_by WHERE checkin_date IS NULL) AS current
            ON m.member_id = current.member_id) AS checkouts
    ON b.isbn = checkouts.isbn;

-- Question 10
SELECT a.first_name, a.last_name, a.author_id, b.title FROM book AS b
    INNER JOIN written_by AS w
    ON b.isbn = w.isbn
    INNER JOIN author AS a
    ON w.author_id = a.author_id;

-- Question 11
SELECT first_name, last_name, a_ids.p_number FROM author
    INNER JOIN
    (SELECT author_id, ap2.p_number FROM author_phone AS ap2
        INNER JOIN
        (SELECT p_number FROM author_phone AS ap 
            INNER JOIN author AS a 
            ON ap.author_id = a.author_id 
            GROUP BY p_number HAVING count(*) > 1) AS p
        ON ap2.p_number = p.p_number) AS a_ids
    ON a_ids.author_id = author.author_id;
