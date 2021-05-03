package com.camelodev.libraryapi.model.repository;

import com.camelodev.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        //cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //verificação
        boolean exists = repository.existsByIsbn(isbn);

        //execução
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesNotExists(){
        //cenário
        String isbn = "123";

        //verificação
        boolean exists = repository.existsByIsbn(isbn);

        //execução
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro quando buscar por id")
    public void findByIdTest(){
        //cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execução
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificações
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){

        Book book =  createNewBook("123");

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){

        Book book = createNewBook("123");
        entityManager.persist(book);
        Book foundBook = entityManager.find(Book.class, book.getId());

        repository.delete(foundBook);
        Book deletedBook = entityManager.find(Book.class, book.getId());

        assertThat(deletedBook).isNull();
    }

    private Book createNewBook(String isbn) {
        return Book.builder().title("Poemas").author("Daniel").isbn(isbn).build();
    }
}
