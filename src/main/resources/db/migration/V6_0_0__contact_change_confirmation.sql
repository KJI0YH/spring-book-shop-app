CREATE TABLE IF NOT EXISTS contact_change_confirmation
(
    id VARCHAR(64) PRIMARY KEY NOT NULL,
    user_id INTEGER NOT NULL,
    contact VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);