package com.example.demoMail.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sendemail")
public class SendMail {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "sendtime")
    private Date sendtime;

    @Column(name = "receiver")
    private String receiver;
    @Column(name = "url")
    private String url;
    @Column(name = "filename")
    private String filename;

    public SendMail() {
    }

    public SendMail(int id, String title, String content, Date sendtime, String receiver, String url, String filename) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sendtime = sendtime;
        this.receiver= receiver;
        this.url=url;
        this.filename=filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitel(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
