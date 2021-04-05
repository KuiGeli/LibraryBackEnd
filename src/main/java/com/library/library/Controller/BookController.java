package com.library.library.Controller;

import com.library.library.Model.Book;
import com.library.library.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BookController {

    @Autowired
    BookService bookService;


    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks () {
        return bookService.getAllBooks();
    }

    @GetMapping("/getBooksByTitle/{title}")
    public List<Book> getBooksByTitle(@PathVariable String title) {
        return bookService.getBooksByTitle(title);
    }

    @DeleteMapping("/deleteBookById/{id}")
    @PreAuthorize("hasRole('ADMIN)")
    public void deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
    }

    @PostMapping("/addBook/{title}/{category}/{stock}")
    public void addBook(@PathVariable String title,@PathVariable String category,@PathVariable int stock) {
        Book book = new Book();

        book.setTitle(title);
        book.setStock(stock);
        book.setCategory(category);

        bookService.addBook(book);

    }

}
