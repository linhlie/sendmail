package com.example.demoMail.model;

import javax.persistence.*;

@Entity
@Table(name="email")
public class Email {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "sender")
    private String sender;

    @Column(name = "content")
    private String content;
    @Column(name = "sumary")
    private String sumary;


    public Email() {
        super();
    }

    public Email(int id, String subject, String sender, String content, String sumary) {
        super();
        this.id = id;
        this.subject = subject;
        this.sender = sender;
        this.content=content;
        this.sumary=sumary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getSumary(){
        return sumary;
    }
    public void setSumary(String sumary){
        this.sumary=sumary;
    }


    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", mailfrom='" + sender + '\'' +
                ", text='" + content + '\'' +
                '}';
    }
}
