MERGE INTO mpa(mpa_id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG13'),
       (4, 'R'),
       (5, 'NC17');

MERGE INTO genre(genre_id, name)
VALUES (1, 'Comedy'),
       (2, 'Horror'),
       (3, '18+'),
       (4, 'Drama'),
       (5, 'Fantasy'),
       (6, 'Documentary'),
       (7, 'Science fiction');