-- :name insert-book! :! :1
insert into catalog.book (title, isbn,copies, available) values (:title, :isbn, :copies .: available)
returning *;

-- :name delete-book! :! :n
delete from catalog.book where isbn = :isbn;

-- :name search :? :*
select isbn, available
from catalog.book
where lower(title) like :title;

-- :name get-book :? :1
SELECT book_id, title, isbn, available
FROM catalog.book
WHERE isbn = :isbn;


-- :name get-books :? :*
select isbn, avilable
from catalog.book;
