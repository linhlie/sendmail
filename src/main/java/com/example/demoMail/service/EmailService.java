package com.example.demoMail.service;

import com.example.demoMail.dao.EmailDao;
import com.example.demoMail.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    EmailDao emailDao;

        public List<Email> getAll(){
            return (List<Email>) emailDao.findAll();
        }
        public void save(Email email){
            emailDao.save(email);
        }

        public void delete(int id){
            emailDao.deleteById(id);
        }

        public Email getById(int id){
            return  emailDao.getOne(id);
        }
}
