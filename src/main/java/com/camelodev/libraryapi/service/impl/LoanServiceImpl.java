package com.camelodev.libraryapi.service.impl;

import com.camelodev.libraryapi.exception.BusinessException;
import com.camelodev.libraryapi.model.entity.Loan;
import com.camelodev.libraryapi.model.repository.LoanRepository;
import com.camelodev.libraryapi.service.LoanService;

import java.util.Optional;

public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return Optional.of(new Loan());
    }

    @Override
    public Loan update(Loan loan) {
        return null;
    }
}
