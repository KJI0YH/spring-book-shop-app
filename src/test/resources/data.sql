insert into book (id, pub_date, is_bestseller, slug, title, image, description, price, discount, popularity) values (1, '2021-01-01', true, 'Slug-1', 'Title-1', 'https://dummyimage.com/100/f1f1f1', 'Description-1', 1000, 10, 0);
insert into book (id, pub_date, is_bestseller, slug, title, image, description, price, discount, popularity) values (2, '2022-01-01', true, 'Slug-2', 'Title-2', 'https://dummyimage.com/100/f1f1f1', 'Description-2', 1000, 10, 0);
insert into book (id, pub_date, is_bestseller, slug, title, image, description, price, discount, popularity) values (3, '2023-01-01', true, 'Slug-3', 'Title-3', 'https://dummyimage.com/100/f1f1f1', 'Description-3', 1000, 10, 0);

insert into author (id, photo, slug, first_name, last_name, description) VALUES (1, 'https://dummyimage.com/100/f2f2f2', 'Slug-1', 'FirstName-1', 'LastName-1', 'Description-1');
insert into author (id, photo, slug, first_name, last_name, description) VALUES (2, 'https://dummyimage.com/100/f2f2f2', 'Slug-2', 'FirstName-2', 'LastName-2', 'Description-2');

insert into book2author (book_id, author_id, sort_index) values (1, 1, 0);
insert into book2author (book_id, author_id, sort_index) values (2, 1, 0);
insert into book2author (book_id, author_id, sort_index) values (3, 1, 0);

INSERT INTO book2user_type (id, code, name) VALUES (1, 'KEPT', 'KEPT');
INSERT INTO book2user_type (id, code, name) VALUES (2, 'CART', 'CART');
INSERT INTO book2user_type (id, code, name) VALUES (3, 'PAID', 'PAID');
INSERT INTO book2user_type (id, code, name) VALUES (4, 'ARCHIVED', 'ARCHIVED');
INSERT INTO book2user_type (id, code, name) VALUES (5, 'UNLINK', 'UNLINK');

insert into users (id, hash, password_hash, reg_time, email, phone, name) values (1, 'hash', '$2a$10$kkW783J8YXI5TXmM6Ll0h.8fW0kwS8KYtskGhbbcsErYG85QOf4RK', now(), 'email@email.com', '+711111111111', 'name');

insert into book2user (book_id, user_id, time, type_id) VALUES (1, 1, now(), 1);
insert into book2user (book_id, user_id, time, type_id) VALUES (2, 1, now(), 2);
insert into book2user (book_id, user_id, time, type_id) VALUES (3, 1, now(), 3);

insert into genre (id, parent_id, slug, name) VALUES (1, null, 'Slug-1', 'SlugOne');

insert into book2genre (book_id, genre_id) VALUES (1, 1);
insert into book2genre (book_id, genre_id) VALUES (2, 1);
insert into book2genre (book_id, genre_id) VALUES (3, 1);

insert into tag (id, slug, name) values (1, 'Slug-1', 'SlugOne');

insert into book2tag (book_id, tag_id) VALUES (1, 1);
insert into book2tag (book_id, tag_id) VALUES (2, 1);
insert into book2tag (book_id, tag_id) VALUES (3, 1);

