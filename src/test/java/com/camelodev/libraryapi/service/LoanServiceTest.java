package com.camelodev.libraryapi.service;


import com.camelodev.libraryapi.api.dto.LoanFilterDTO;
import com.camelodev.libraryapi.exception.BusinessException;
import com.camelodev.libraryapi.model.entity.Book;
import com.camelodev.libraryapi.model.entity.Loan;
import com.camelodev.libraryapi.model.repository.LoanRepository;
import com.camelodev.libraryapi.service.impl.LoanServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    private LoanService loanService;

    @MockBean
    private LoanRepository repository;

    @BeforeEach
    public void setUp(){
        this.loanService = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest(){
        String customer = "Daniel";
        Book book = Book.builder().id(1L).build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .book(book)
                .customer(customer)
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        when(repository.save(savingLoan)).thenReturn(savedLoan);
        Loan loan = loanService.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado")
    public void loanedBookSaveTest(){
        Book book = Book.builder().id(1L).build();

        Loan savingLoan = createLoan();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

        Throwable exception = catchThrowable(() -> loanService.save(savingLoan));

        assertThat(exception).isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        verify(repository, never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um emprestimo pelo id")
    public void getLoanDetailsTest(){
        Long id = 1L;

        Loan loan = createLoan();
        loan.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(loan));

        Optional<Loan> result = loanService.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo")
    public void updateLoanTest(){
        Loan loan = createLoan();
        loan.setId(1L);
        loan.setReturned(true);

        when(repository.save(loan)).thenReturn(loan);

        Loan updatedLoan = loanService.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();
        verify(repository).save(loan);

    }

    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades")
    public void findLoanTest(){

        //cenário
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Daniel").isbn("321").build();

        Loan loan = createLoan();
        loan.setId(1L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<Loan>(lista,  pageRequest, lista.size());
        when(repository.findByBookIsbnOrCustomer(
                anyString(),
                anyString(),
                any(PageRequest.class)))
            .thenReturn(page);

        //execução
        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        //verificações
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(lista);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    public static Loan createLoan(){
        String customer = "Daniel";
        Book book = Book.builder().id(1L).build();

        return Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
    }


}
