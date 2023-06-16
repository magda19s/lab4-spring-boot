package pl.edu.pwr.awt.books.book;

import pl.edu.pwr.awt.books.author.Author;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.awt.books.Exceptions.NotFoundException;
import pl.edu.pwr.awt.books.author.IAuthorService;

import java.util.Iterator;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class BooksController {
    final IBooksService booksService;
    final IAuthorService authorService;

    public BooksController(IBooksService booksService, IAuthorService authorService) {
        this.booksService = booksService;
        this.authorService = authorService;
    }


    @GetMapping(value = "/books")
    public ResponseEntity<Object> getBooks(){
        return new ResponseEntity<>(booksService.getBooks(), HttpStatus.OK);
    }

    @GetMapping(value = "/books/{id}")
    public ResponseEntity<Object> getBook(@PathVariable("id") int id){
        return new ResponseEntity<>(booksService.getBook(id), HttpStatus.OK);
    }

    @PostMapping(value = "/books")
    public HttpStatus createBook(@RequestBody ObjectNode json){
        Book newBook = new Book();
        newBook.setTitle(json.get("title").asText());
        newBook.setPages(json.get("pages").asInt());
        newBook.setPrice(json.get("price").asDouble());
        if (json.get("authorID").isArray()) {
            Iterator<JsonNode> iterator = json.get("authorID").elements();
            while (iterator.hasNext()) {
                int id = iterator.next().asInt();
                Author author = authorService.getAuthor(id);
                authorService.addBook(id, newBook);
                newBook.addAuthor(author);
            }
        } else {
            Author author = authorService.getAuthor(json.get("authorID").asInt());
            newBook.addAuthor(author);
        }
        booksService.createBook(newBook);
        return HttpStatus.CREATED;

    }

    @PutMapping(value = "/books/{id}")
    public ResponseEntity<Object> updateBook(@RequestBody Book newBook, @PathVariable("id") int id){
        try {
            booksService.updateBook(id, newBook);
            return new ResponseEntity<>(newBook, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Invalid data", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/books/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") int id) {
        try {
            Book book = booksService.deleteBook(id);
            authorService.removeBook(book);
            return new ResponseEntity<>(book, HttpStatus.OK);

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/books/count")
    public int getSizeOfBooks(){
        return booksService.countBook();
    }
}
