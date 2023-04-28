alter table catalog.book
  drop column copies,
  drop column available;
--;;
drop table catalog.lending;
--;;
DROP catalog.update_book_copies CASCADE;
--;;
DROP catalog.update_book_copies_trigger CASCADE;
--;;
DROP FUNCTION catalog.check_book_availability() CASCADE;
