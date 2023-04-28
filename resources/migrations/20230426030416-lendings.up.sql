alter table catalog.book
 add copies integer DEFAULT 1,
 add available BOOLEAN NOT NULL DEFAULT true;
--;;
CREATE TABLE catalog.lending (
 lending_id bigint generated always as identity primary key,
 user_id integer  not null unique,
 book_id INTEGER NOT NULL REFERENCES catalog.book,
 lending_date DATE NOT NULL,
 return_date DATE NOT NULL
);
--;;
CREATE OR REPLACE FUNCTION catalog.update_book_copies()
 RETURNS TRIGGER AS $$
 BEGIN
  IF TG_OP = 'INSERT' THEN
   UPDATE books SET copies = copies + 1 WHERE id = NEW.book_id;
  ELSIF TG_OP = 'DELETE' THEN
   UPDATE books SET copies = copies - 1 WHERE id = OLD.book_id;
  END IF;
   RETURN NULL;
 END;
 $$ LANGUAGE plpgsql;
--;;
CREATE TRIGGER update_book_copies_trigger
 AFTER INSERT OR DELETE ON catalog.lending
 FOR EACH ROW
 EXECUTE FUNCTION catalog.update_book_copies();
 --;;
 CREATE OR REPLACE FUNCTION catalog.check_book_availability() 
 RETURNS TRIGGER AS $$
BEGIN
    IF NEW.copies = 0 THEN
        NEW.available = false;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
--;;
CREATE TRIGGER set_book_availability
BEFORE UPDATE ON catalog.book
FOR EACH ROW
EXECUTE FUNCTION catalog.check_book_availability();
