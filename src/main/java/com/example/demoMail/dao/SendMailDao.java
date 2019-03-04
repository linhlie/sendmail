package com.example.demoMail.dao;

import com.example.demoMail.model.SendMail;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SendMailDao extends PagingAndSortingRepository<SendMail, Integer  > {
    SendMail getOne(int id);
}
