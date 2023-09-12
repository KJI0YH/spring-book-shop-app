CREATE TABLE IF NOT EXISTS book
(
    id SERIAL NOT NULL,
    pub_date DATE NOT NULL,
    is_bestseller BOOLEAN NOT NULL,
    slug VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    description TEXT,
    price INTEGER NOT NULL,
    discount SMALLINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT book_slug_unique UNIQUE(slug)
);

CREATE TABLE IF NOT EXISTS author
(
    id SERIAL NOT NULL,
    photo VARCHAR(255),
    slug VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (id),
    CONSTRAINT author_slug_unique UNIQUE(slug)
);

CREATE TABLE IF NOT EXISTS book2author
(
    id SERIAL NOT NULL,
    book_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    sort_index INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT author_id_fkey FOREIGN KEY (author_id)
        REFERENCES author (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    id SERIAL NOT NULL,
    hash VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    reg_time TIMESTAMP NOT NULL,
    balance INTEGER NOT NULL DEFAULT 0,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_phone_unique UNIQUE (phone)
    );

CREATE TABLE IF NOT EXISTS book_review
(
    id SERIAL NOT NULL,
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    text TEXT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS book_review_like
(
    review_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    value SMALLINT NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT review_id_fkey FOREIGN KEY (review_id)
        REFERENCES book_review (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genre
(
    id SERIAL NOT NULL,
    parent_id INTEGER DEFAULT NULL,
    slug VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT genre_slug_unique UNIQUE (slug),
    CONSTRAINT parent_id_fkey FOREIGN KEY (parent_id)
        REFERENCES genre (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS book2genre
(
    id SERIAL NOT NULL,
    book_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT genre_id_fkey FOREIGN KEY (genre_id)
        REFERENCES genre (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS book2user_type
(
    id SERIAL NOT NULL,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
    );

INSERT INTO book2user_type (code, name) VALUES ('KEPT', 'KEPT');
INSERT INTO book2user_type (code, name) VALUES ('CART', 'CART');
INSERT INTO book2user_type (code, name) VALUES ('PAID', 'PAID');
INSERT INTO book2user_type (code, name) VALUES ('ARCHIVED', 'ARCHIVED');
INSERT INTO book2user_type (code, name) VALUES ('UNLINK', 'UNLINK');

CREATE TABLE IF NOT EXISTS book2user
(
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    type_id INTEGER NOT NULL,
    PRIMARY KEY (book_id, user_id),
    CONSTRAINT type_id_fkey FOREIGN KEY (type_id)
        REFERENCES book2user_type (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS balance_transaction
(
    id SERIAL NOT NULL,
    user_id INTEGER NOT NULL,
    time TIMESTAMP NOT NULL,
    value INTEGER NOT NULL DEFAULT 0,
    book_id INTEGER,
    description TEXT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS book_file_type
(
    id SERIAL NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
    );

INSERT INTO book_file_type (name, description) VALUES ('PDF', 'PDF');
INSERT INTO book_file_type (name, description) VALUES ('EPUB', 'EPUB');
INSERT INTO book_file_type (name, description) VALUES ('FB2', 'FB2');

CREATE TABLE IF NOT EXISTS book_file
(
    id SERIAL NOT NULL,
    hash VARCHAR(255) NOT NULL,
    book_id INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
    path VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT type_id_fkey FOREIGN KEY (type_id)
        REFERENCES book_file_type (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS file_download
(
    id SERIAL NOT NULL,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    count INTEGER NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS document
(
    id SERIAL NOT NULL,
    sort_index INTEGER NOT NULL DEFAULT 0,
    slug VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT document_slug_unique UNIQUE (slug)
);

CREATE TABLE IF NOT EXISTS faq
(
    id SERIAL NOT NULL,
    sort_index INTEGER NOT NULL DEFAULT 0,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS message
(
    id SERIAL NOT NULL,
    time TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS tag
(
    id SERIAL NOT NULL,
    slug VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT tag_slug_unique UNIQUE (slug)
);

CREATE TABLE IF NOT EXISTS book2tag
(
    id SERIAL NOT NULL,
    book_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT tag_id_fkey FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS book_rate
(
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    rate SMALLINT NOT NULL,
    PRIMARY KEY (book_id, user_id),
    CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
        REFERENCES book (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS jwt_black_list
(
    token VARCHAR(255) NOT NULL,
    PRIMARY KEY (token)
);