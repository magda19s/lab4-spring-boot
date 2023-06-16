package pl.edu.pwr.awt.books.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pwr.awt.books.Exceptions.AlreadyExistsException;
import pl.edu.pwr.awt.books.Exceptions.NotFoundException;
import pl.edu.pwr.awt.books.Exceptions.PermissionDeniedException;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
public class AuthorsController {
    @Autowired
    IAuthorService authorService;

    public AuthorsController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/authors")
    public ResponseEntity<Object> getAuthors(){
        return new ResponseEntity<>(authorService.getAuthors(), HttpStatus.OK);
    }

    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable("id") int id){
        return new ResponseEntity<>(authorService.getAuthor(id), HttpStatus.OK);
    }

    @PostMapping(value = "/authors")
    public HttpStatus createAuthor(@RequestBody Author newAuthor) {
        try{
            authorService.createAuthor(newAuthor);
        } catch (AlreadyExistsException e){
            return HttpStatus.NOT_ACCEPTABLE;
        }
        return HttpStatus.OK;
    }

    @PutMapping(value = "/authors/{id}")
    public HttpStatus updateBook(@RequestBody Author newAuthor, @PathVariable("id") int id){
        try {
            authorService.updateAuthor(id, newAuthor);
            return HttpStatus.OK;
        } catch (NotFoundException e) {
            return HttpStatus.NOT_FOUND;
        }
    }

    @DeleteMapping(value = "/authors/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(authorService.deleteAuthor(id), HttpStatus.OK);

        } catch (PermissionDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Author still has books in this database!");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/authors/count")
    public int getSizeOfAuthors(){
        return authorService.countAuthors();
    }


}
