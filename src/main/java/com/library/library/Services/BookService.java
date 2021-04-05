package com.library.library.Services;

import com.library.library.Model.Book;
import com.library.library.Rpository.BookRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRep bookRep;

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.addAll((Collection<? extends Book>) bookRep.findAll());
        return books;

    }

    public void lendBook(long bookId) {
        Book book = bookRep.findById(bookId).get();
        int oldStock =  book.getStock();
        book.setStock(oldStock - 1);
    }

    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();

        books.addAll((List<Book>) bookRep.findAll());
        List<Book> selectedBooks = new ArrayList<>();
        if(books.size() > 0) {
            for (Book book : books) {
                if(book.getTitle().contains(title)){
                    selectedBooks.add(book);
                }
            }
        }

        return selectedBooks;
    }

    public List<Book> getBooksByCategory (String category) {
        List<Book> books = new ArrayList<>();
        books.addAll((List<Book>) bookRep.findAll());
        List<Book> selectedBooks = new ArrayList<>();

        if(books.size() > 0) {
            for (Book book : books){
            if(book.getCategory().contains(category)){
                selectedBooks.add(book);
            }
            }
        }
        return selectedBooks;
    }

    public void addBook(Book book) {
        bookRep.save(book);
    }

    public void deleteBookById(long id) {
        bookRep.deleteById(id);
    }

}
