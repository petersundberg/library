package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.entities.User;
import com.example.demo.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;


    @Cacheable(value = "bookCache")
    public List<Book> findAll(String name, boolean sortOnAuthor){
        log.info("Request to find all books");
        log.warn("New data...");
        //return bookRepository.findAll();
        var books = bookRepository.findAll();
        if(name != null) {
            books = books.stream()
                    .filter(book -> book.getName().startsWith(name) ||
                            book.getAuthor().startsWith(name))
                    .collect(Collectors.toList());
        }
        if(sortOnAuthor) {
            //books.sort((book1, book2) -> book1.getAuthor().compareTo(book2.getAuthor()));
            books.sort(Comparator.comparing(Book::getAuthor));
        }
        return books;

    }

    @Cacheable(value = "bookCache", key = "#id")
    public Book findById(String id){
        log.warn("New data...");
        return bookRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find book by id %s.", id)));
    }


    @CachePut(value = "bookCache", key = "#result.id")
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void update(String id, Book book) {

        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find/update book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find/update book by id %s.", id));
        }
        log.info("Updating book.");
        book.setId(id);
        bookRepository.save(book);
    }


    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find/delete book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find/delete book by id %s, id"));
        }
        log.info("Deleting book.");
        bookRepository.deleteById(id);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void loanBook(String id, Book book) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find book by id %s.", id));
        }
        log.info("Loaning book.");
        book.setId(id);
        book.setAvailable(false);
        bookRepository.save(book);
    }

    @CachePut(value = "bookCache", key = "#id")
    public void returnBook(String id, Book book) {
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find book by id %s.", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 -> Not found
                    String.format("Could not find book by id %s.", id));
        }
        log.info("Returning book.");
        book.setId(id);
        book.setAvailable(true);
        bookRepository.save(book);
    }




}
