DROP TABLE IF EXISTS sms_code;

CREATE TABLE IF NOT EXISTS approve_contact
(
    id SERIAL NOT NULL,
    code VARCHAR(6) NOT NULL,
    expired_time TIMESTAMP NOT NULL,
    resend_time TIMESTAMP NOT NULL,
    contact VARCHAR(255) NOT NULL,
    attempts INTEGER NOT NULL DEFAULT 0,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT approve_contact_unique UNIQUE(contact)
);