CREATE TABLE IF NOT EXISTS sms_code
(
    id SERIAL NOT NULL,
    code VARCHAR(7) NOT NULL,
    expired_time TIMESTAMP NOT NULL
);