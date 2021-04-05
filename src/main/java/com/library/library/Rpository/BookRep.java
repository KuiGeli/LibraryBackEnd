package com.library.library.Rpository;

import com.library.library.Model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRep extends CrudRepository<Book, Long> {
}
