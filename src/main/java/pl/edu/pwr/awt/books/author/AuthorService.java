package pl.edu.pwr.awt.books.author;

import pl.edu.pwr.awt.books.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import pl.edu.pwr.awt.books.Exceptions.AlreadyExistsException;
import pl.edu.pwr.awt.books.Exceptions.NotFoundException;
import pl.edu.pwr.awt.books.Exceptions.PermissionDeniedException;


import java.util.Collection;

@Service
public class AuthorService implements IAuthorService{

    @Autowired
    private AuthorRepository authorsRepo;

    @Override
    public Collection<Author> getAuthors() {
        return authorsRepo.findAll();
    }

    @Override
    public Author getAuthor(int id) {
        return authorsRepo.findById(id)
                .orElse(null);
    }

    @Override
    public void createAuthor(Author newAuthor) throws AlreadyExistsException {
        for (Author a: authorsRepo.findAll()){
            if (a.getName().equals(newAuthor.getName()) && a.getLastName().equals(newAuthor.getLastName())){
                throw new AlreadyExistsException();
            }
        }
        Author newAuthor2 = new Author();
        newAuthor2.setName(newAuthor.getName());
        newAuthor2.setLastName(newAuthor.getLastName());
        newAuthor2.setBooks(newAuthor.getBooks());
        authorsRepo.saveAndFlush(newAuthor2);
    }

    @Override
    public void updateAuthor(int id, Author newAuthor) throws NotFoundException {
        Author to_be_updated = authorsRepo.findById(id)
                .orElse(null);
        if (to_be_updated != null) {
            to_be_updated.setName(newAuthor.getName());
            to_be_updated.setLastName(newAuthor.getLastName());
        } else {
            throw new NotFoundException("nie ma takiego autora");
        }
        authorsRepo.saveAndFlush(to_be_updated);
    }

    @Override
    public Author deleteAuthor(int id) throws NotFoundException, PermissionDeniedException {
        Author to_be_deleted = authorsRepo.findById(id)
                .orElse(null);
        if (to_be_deleted != null) {
            if (!to_be_deleted.getBooks().isEmpty()) {
                throw new PermissionDeniedException();
            }
            authorsRepo.deleteById(id);
            return to_be_deleted;
        } else {
            throw new NotFoundException("nie ma takiego autora");
        }
    }


    @Override
    public void addBook(int id, Book book) {
        Author authorBook = authorsRepo.findById(id)
                .orElse(null);

        assert authorBook != null;
        authorBook.addBook(book);
    }

    @Override
    public void removeBook(Book book) {
        for (Author author: authorsRepo.findAll()) {
            author.getBooks().remove(book);
        }
    }


    @Override
    public int countAuthors()
    {
        return (int) authorsRepo.count();
    }
}
