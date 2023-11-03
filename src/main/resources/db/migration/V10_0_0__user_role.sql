CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL       NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO role (name)
VALUES ('USER'),
       ('ADMIN');

CREATE TABLE IF NOT EXISTS role2user
(
    id      SERIAL  NOT NULL,
    role_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT role_id_fkey FOREIGN KEY (role_id)
        REFERENCES role (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);