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

-- Trigger function to update popularity when inserting to book2user_viewed
CREATE OR REPLACE FUNCTION update_popularity_insert_book2user_viewed()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE book
    SET popularity = popularity + 1
    WHERE id = NEW.book_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger function to update popularity when deleting from book2user_viewed
CREATE OR REPLACE FUNCTION update_popularity_delete_book2user_viewed()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE book
    SET popularity = popularity - 1
    WHERE id = NEW.book_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE delete_old_book2user_views()
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM book2user_viewed
    WHERE time < now() - interval '1 week';
END;
$$;

-- Trigger to update popularity when inserting
CREATE TRIGGER update_popularity_insert_book2user_viewed_trigger
    AFTER INSERT
    ON book2user_viewed
    FOR EACH ROW
EXECUTE FUNCTION update_popularity_insert_book2user_viewed();

-- Trigger to update popularity when deleting
CREATE TRIGGER update_popularity_delete_book2user_viewed_trigger
    AFTER DELETE
    ON book2user_viewed
    FOR EACH ROW
EXECUTE FUNCTION update_popularity_delete_book2user_viewed();