package com.example.demoMail.service;
import com.example.demoMail.dao.SendMailDao;
import com.example.demoMail.model.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMailService {

    @Autowired
    SendMailDao sendMailDao;

    public List<SendMail> getAll(){
        return (List<SendMail>) sendMailDao.findAll();
    }
    public void save(SendMail sendMail){
        sendMailDao.save(sendMail);
    }

    public void delete(int id){
        sendMailDao.deleteById(id);
    }

    public SendMail getById(int id){
        return  sendMailDao.getOne(id);
    }
}
