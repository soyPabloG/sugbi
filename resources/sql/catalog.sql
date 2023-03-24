-- :name insert-book! :! :1
insert into catalog.book (title) values (:title)
returning *;

-- :name insert-author! :! :1
insert into catalog.author (full_name) values (:full-name)
returning *;

-- :name link-book-author! :! :n
insert into catalog.book_author (author_id, book_id) values (:author-id, :book-id);

-- :name delete-book! :! :n
delete from catalog.book where title = :title;

-- :name search :? :*
select title, full_name
from catalog.book
/*~
(when (:full-name params)
  (str "join catalog.book_author using (book_id)\n"
       "join catalog.author using (author_id)"))
~*/
where title like :title;

-- :name get-books :? :*
select title, full_name
from catalog.book
join catalog.book_author using (book_id)
join catalog.author using (author_id);
