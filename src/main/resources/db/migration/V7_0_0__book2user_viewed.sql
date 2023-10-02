CREATE TABLE IF NOT EXISTS book2user_viewed
(
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    PRIMARY KEY (book_id, user_id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);