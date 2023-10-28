-- Trigger function to update user balance when inserting to balance_transaction
CREATE OR REPLACE FUNCTION update_balance_insert_balance_transaction()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE users
    SET balance = balance + NEW.value
    WHERE id = NEW.user_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_book2user_insert_balance_transaction()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.book_id IS NOT NULL THEN
        UPDATE book2user
        SET type_id = 3
        WHERE book_id = NEW.book_id AND user_id = NEW.user_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update user balance when inserting to balance_transaction
CREATE TRIGGER update_balance_insert_balance_transaction_trigger
    AFTER INSERT
    ON balance_transaction
    FOR EACH ROW
EXECUTE FUNCTION update_balance_insert_balance_transaction();

-- Trigger to update book2user when inserting to balance_transaction
CREATE TRIGGER update_book2user_insert_balance_transaction
    AFTER INSERT
    ON balance_transaction
    FOR EACH ROW
EXECUTE FUNCTION update_book2user_insert_balance_transaction();