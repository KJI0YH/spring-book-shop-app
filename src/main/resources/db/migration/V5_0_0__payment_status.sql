CREATE TABLE IF NOT EXISTS payment_status
(
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    user_id INTEGER NOT NULL,
    payment_id VARCHAR(36) NOT NULL,
    status VARCHAR(32) NOT NULL,
    is_processed BOOLEAN DEFAULT FALSE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);