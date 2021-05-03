package com.camelodev.libraryapi.service;

import com.camelodev.libraryapi.exception.BusinessException;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.model.repository.BookRepository;
import com.camelodev.libraryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        // cenário
        Book book = createValidBook();
        when(repository.existsByIsbn(anyString())).thenReturn(false);
        Mockito.when(repository.save(book))
                .thenReturn(Book.builder()
                                .id(1L)
                                .isbn("123")
                                .author("Fulano")
                                .title("Poemas e Poesias")
                                .build());

        // execução
        Book savedBook = service.save(book);

        // verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("Poemas e Poesias");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");

    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        // cenario
        Book book = createValidBook();
        when(repository.existsByIsbn(anyString())).thenReturn(true);

        // execução
        Throwable exception = catchThrowable(() -> service.save(book));

        // verificações
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        verify(repository, never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getByIdTest(){
        // cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(book));

        // execução
        Optional<Book> foundBook = service.getById(id);

        // verificações
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro que não existe na base")
    public void bookNotFoundByIdTest(){
        // cenario
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        // execução
        Optional<Book> book = service.getById(id);

        // verificações
        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        // cenario
        Book book = Book.builder().id(1L).build();

        // execução
        assertDoesNotThrow( () -> service.delete(book));

        // verificações
        verify(repository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar deletar um livro inexistente")
    public void deleteInvalidBookTest(){
        // cenario
        Book book = new Book();

        // execução
        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        // verificações
        verify(repository, never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest(){
        // cenario
        Long id = 1L;

        //livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        //simulação
        Book updatedBook = createValidBook();
        updatedBook.setId(id);
        when(repository.save(updatingBook)).thenReturn(updatedBook);

        // execução
        Book book = service.update(updatingBook);

        // verificações
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar um livro inexistente")
    public void updateInvalidBookTest(){
        // cenario
        Book book = new Book();

        // execução
        assertThrows(IllegalArgumentException.class, () -> service.update(book));

        // verificações
        verify(repository, never()).save(book);
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("Poemas e Poesias").build();
    }
}
