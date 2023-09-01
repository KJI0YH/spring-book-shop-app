ALTER TABLE book
    ADD COLUMN popularity INTEGER DEFAULT 0;

-- Trigger function to update popularity when inserting
CREATE OR REPLACE FUNCTION update_popularity_insert()
    RETURNS TRIGGER AS
$$
BEGIN
    CASE
        WHEN NEW.type_id = 3 THEN UPDATE book
                                  SET popularity = popularity + 10
                                  WHERE id = NEW.book_id;

        WHEN NEW.type_id = 2 THEN UPDATE book
                                  SET popularity = popularity + 7
                                  WHERE id = NEW.book_id;

        WHEN NEW.type_id = 1 THEN UPDATE book
                                  SET popularity = popularity + 4
                                  WHERE id = NEW.book_id;
        END CASE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger function to update popularity when inserting
CREATE OR REPLACE FUNCTION update_popularity_delete()
    RETURNS TRIGGER AS
$$
BEGIN
    CASE
        WHEN NEW.type_id = 3 THEN UPDATE book
                                  SET popularity = popularity - 10
                                  WHERE id = NEW.book_id;

        WHEN NEW.type_id = 2 THEN UPDATE book
                                  SET popularity = popularity - 7
                                  WHERE id = NEW.book_id;

        WHEN NEW.type_id = 1 THEN UPDATE book
                                  SET popularity = popularity - 4
                                  WHERE id = NEW.book_id;
        ELSE
        END CASE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger function to update popularity when updating
CREATE OR REPLACE FUNCTION update_popularity_update()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.type_id <> NEW.type_id THEN
        CASE
            WHEN OLD.type_id = 3 THEN UPDATE book
                                         SET popularity = popularity - 10
                                         WHERE id = OLD.book_id;

            WHEN OLD.type_id = 2 THEN UPDATE book
                                          SET popularity = popularity - 7
                                          WHERE id = OLD.book_id;

            WHEN OLD.type_id = 1 THEN UPDATE book
                                          SET popularity = popularity - 4
                                          WHERE id = OLD.book_id;
            ELSE
            END CASE;

        CASE
            WHEN NEW.type_id = 3 THEN UPDATE book
                                         SET popularity = popularity + 10
                                         WHERE id = NEW.book_id;

            WHEN NEW.type_id = 2 THEN UPDATE book
                                          SET popularity = popularity + 7
                                          WHERE id = NEW.book_id;

            WHEN NEW.type_id = 1 THEN UPDATE book
                                          SET popularity = popularity + 4
                                          WHERE id = NEW.book_id;
            ELSE
            END CASE;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update popularity when inserting
CREATE TRIGGER update_popularity_insert_trigger
    AFTER INSERT
    ON book2user
    FOR EACH ROW
EXECUTE FUNCTION update_popularity_insert();

-- Trigger to update popularity when deleting
CREATE TRIGGER update_popularity_delete_trigger
    AFTER DELETE
    ON book2user
    FOR EACH ROW
EXECUTE FUNCTION update_popularity_delete();

-- Trigger to update popularity when updating
CREATE TRIGGER update_popularity_update_trigger
    AFTER UPDATE
    ON book2user
    FOR EACH ROW
EXECUTE FUNCTION update_popularity_update();