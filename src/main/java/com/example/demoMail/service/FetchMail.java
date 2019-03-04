package com.example.demoMail.service;


import com.example.demoMail.dao.EmailDao;
import com.example.demoMail.model.Email;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class FetchMail {

@Autowired
private EmailDao emailDao;
    public List<Email> fetch() {
        String pop3Host = "pop.gmail.com";
        String storeTye = "pop3";
        String username ="nguyenlinh.linhlie.it@gmail.com";
        String password = "culinh123";// change accordingly
         List<Email> list = new ArrayList<>();
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", pop3Host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3s");

            store.connect(pop3Host, username, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            for (int i = 0; i < messages.length; i++) {
                Email email= new Email();
                Message message = messages[i];

                int id = message.getMessageNumber();
                String subject = message.getSubject();
                String sender = message.getFrom()[0].toString();

                String body = getContentText(message);
                String sumary = getContentText(message);

                Document document = Jsoup.parse(body) ;
                String text = document.body().text();

                System.out.printf("===============");
                System.out.println(body);
                email.setId(id);
                email.setSubject(subject);
                email.setSender(sender);
                email.setContent(text);
                email.setSumary(sumary);


                emailDao.save(email);

            }

            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getTextContent(Part p) throws IOException, MessagingException {
        try {
            return (String)p.getContent();
        } catch (UnsupportedEncodingException e) {
            OutputStream os = new ByteArrayOutputStream();
            p.writeTo(os);
            String raw = os.toString();
            os.close();

            //cp932 -> Windows-31J
            raw = raw.replaceAll("cp932", "ms932");

            InputStream is = new ByteArrayInputStream(raw.getBytes());
            Part newPart = new MimeBodyPart(is);
            is.close();

            return (String)newPart.getContent();
        }
    }
    private static String getContentText(Part p) throws MessagingException, IOException {

        if (p.isMimeType("text/*")) {
            String s = getTextContent(p);
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getContentText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getContentText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getContentText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getContentText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
}
