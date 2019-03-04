package com.example.demoMail.dao;


import com.example.demoMail.model.Email;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmailDao extends PagingAndSortingRepository<Email, Integer> {
    Email getOne(int id);
}
