-- :name insert-book! :! :1
insert into catalog.book (title, isbn) values (:title, :isbn)
returning *;

-- :name delete-book! :! :n
delete from catalog.book where title = :title;

-- :name search :? :*
select isbn, true as "available"
from catalog.book
where title like :title;

-- :name get-books :? :*
select isbn
from catalog.book;
