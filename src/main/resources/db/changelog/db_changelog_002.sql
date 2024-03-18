-- data definition
CREATE TABLE book_catalogue (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255)    NOT NULL,
    author          VARCHAR(255)    NOT NULL,
    genre           VARCHAR(255)    NULL,
    price           NUMERIC(10,2)   NULL,
    availability    BOOLEAN         NOT NULL,
    created_at      TIME            NOT NULL,
    modified_at     TIME            NULL,
    created_by      VARCHAR(255)    NOT NULL,
    modified_by     VARCHAR(255)    NULL
);

-- data seed
INSERT INTO book_catalogue(title, author, genre, price, availability, created_at, modified_at, created_by, modified_by)
VALUES ('The Fellowship of the Ring', 'J.R.R. Tolkien', 'FANTASY', 10.0, 1, current_time(), current_time(), 'admin', 'admin');

INSERT INTO book_catalogue(title, author, genre, price, availability, created_at, modified_at, created_by, modified_by)
VALUES ('The Two Towers', 'J.R.R. Tolkien', 'FANTASY', 10.0, 1, current_time(), current_time(), 'admin', 'admin');

INSERT INTO book_catalogue(title, author, genre, price, availability, created_at, modified_at, created_by, modified_by)
VALUES ('The Return of the King', 'J.R.R. Tolkien', 'FANTASY', 10.0, 1, current_time(), current_time(), 'admin', 'admin');