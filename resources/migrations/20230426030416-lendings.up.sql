alter table catalog.book
 add copies integer DEFAULT 1,
 add available  BOOLEAN NOT NULL DEFAULT true;
--;;
CREATE TABLE catalog.lending (
lending_id bigint generated always as identity primary key,
user_id integer  not null unique,
book_id INTEGER NOT NULL REFERENCES catalog.book,
lending_date DATE NOT NULL,
return_date DATE NOT NULL
);

