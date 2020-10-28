package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller // MVC med statiska html-sidor
@RestController //REST API Endpoints
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(@RequestParam(required = false) String name, @RequestParam(required = false) boolean sort) {
        return ResponseEntity.ok(bookService.findAll(name, sort));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity <Book> findBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Book> saveBook(@Validated @RequestBody Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@Validated @PathVariable String id, @RequestBody Book book) {
        bookService.update(id, book);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.delete(id);
    }

    @PutMapping("/loan/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loanBook(@Validated @PathVariable String id, @RequestBody Book book) {
        bookService.loanBook(id, book);
    }

    @PutMapping("/return/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@Validated @PathVariable String id, @RequestBody Book book) {
        bookService.returnBook(id, book);
    }

}


