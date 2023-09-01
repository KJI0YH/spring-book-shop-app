ALTER TABLE book_review
    ADD COLUMN rating INTEGER DEFAULT 0;

-- Trigger function to update review rating when inserting
CREATE OR REPLACE FUNCTION update_review_rating_insert()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE book_review
        SET rating = rating + NEW.value
        WHERE id = NEW.review_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger function to update popularity when inserting
CREATE OR REPLACE FUNCTION update_review_rating_delete()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE book_review
    SET rating = rating - NEW.value
    WHERE id = NEW.review_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger function to update popularity when updating
CREATE OR REPLACE FUNCTION update_review_rating_update()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.value <> NEW.value THEN
        CASE
            WHEN OLD.value = 1 THEN UPDATE book_review
                                      SET rating = rating - 2
                                      WHERE id = OLD.review_id;

            WHEN OLD.value = -1 THEN UPDATE book_review
                                      SET rating = rating + 2
                                      WHERE id = OLD.review_id;
        ELSE
        END CASE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update review rating when inserting
CREATE TRIGGER update_review_rating_insert_trigger
    AFTER INSERT
    ON book_review_like
    FOR EACH ROW
EXECUTE FUNCTION update_review_rating_insert();

-- Trigger to update review rating when deleting
CREATE TRIGGER update_review_rating_delete_trigger
    AFTER DELETE
    ON book_review_like
    FOR EACH ROW
EXECUTE FUNCTION update_review_rating_delete();

-- Trigger to update review rating when updating
CREATE TRIGGER update_review_rating_update_trigger
    AFTER UPDATE
    ON book_review_like
    FOR EACH ROW
EXECUTE FUNCTION update_review_rating_update();