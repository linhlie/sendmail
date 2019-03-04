package com.example.demoMail.controller;

import com.example.demoMail.dao.SendMailDao;
import com.example.demoMail.filestorage.FileStorage;
import com.example.demoMail.model.FileInfo;
import com.example.demoMail.model.PagerModel;
import com.example.demoMail.model.SendMail;
import com.example.demoMail.service.EmailService;
import com.example.demoMail.service.FetchMail;
import com.example.demoMail.service.SendMailService;
import org.apache.commons.mail.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SendMailController {

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10};

    String urlfile;
    private final Path rootLocation = Paths.get("filestorage");
    @Autowired
    SendMailDao sendMailDao;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    FileStorage fileStorage;

    @GetMapping("/sent/form")
    public String create(Model model) {
        model.addAttribute("sendMail", new SendMail());
        return "form";
    }

    @PostMapping("/sent/save")
    public String save(@RequestParam("uploadfile") MultipartFile file,  Model model, HttpServletRequest request) {
        String myEmail = "nguyenlinh.linhlie.it@gmail.com";
        String myPassword = "culinh123";
        String title = request.getParameter("title");
        String email1 = request.getParameter("receiver");
        String content = request.getParameter("content");
        SendMail sendMail = new SendMail();
        System.out.println(file);
        try {
            Path fp = rootLocation.resolve(System.currentTimeMillis()+"");
            if (!Files.exists(fp)){
                Files.createDirectory(fp);
            }
            Path p =fp.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(),p);
            urlfile = p.toString();
            System.out.println(urlfile);
            System.out.println("++++++++++++++++++++");
            System.out.println(file);
            model.addAttribute("message", "File uploaded successfully! -> filename = " + file.getOriginalFilename());
        } catch (Exception e) {
            model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
        }

        if (urlfile==null){
            try {
                HtmlEmail email = new HtmlEmail();
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator(myEmail, myPassword));


                email.setSSLOnConnect(true);
                email.setFrom(email1);
                email.setSubject(title);
                email.setHtmlMsg(content);
                Date date = new Date();

                sendMail.setTitel(title);
                sendMail.setContent(content);
                sendMail.setReceiver(email1);
                sendMail.setSendtime(date);

                email.setFrom(myEmail);
                email.setSubject(title);
                email.setHtmlMsg(content);

                sendMailDao.save(sendMail);
                email.addTo(email1);
                email.send();
                System.out.println("Sent");
            } catch (EmailException e) {
                e.printStackTrace();
            }

        }
        else {
            FileInfo f = new FileInfo();
            try {
                System.out.println(file.getOriginalFilename());
                String url2 ="E:\\Spring\\demoMail\\"+urlfile ;
                Date date = new Date();

                Document document = Jsoup.parse(content);
                String text = document.body().text();
                sendMail.setTitel(title);
                sendMail.setContent(text);
                sendMail.setReceiver(email1);
                sendMail.setSendtime(date);
                sendMail.setFilename(file.getOriginalFilename());
                sendMail.setUrl(url2);
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(url2);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(f.getFilename());
                //tao doi tuong
                MultiPartEmail email = new MultiPartEmail();
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setSSLOnConnect(true);
                email.setAuthenticator(new DefaultAuthenticator(myEmail, myPassword));
                email.setFrom(myEmail);
                email.setSubject(title);

                email.setMsg(text);
                email.attach(attachment);

                sendMailDao.save(sendMail);
                email.addTo(email1);
                email.send();
                System.out.println("Sent");
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("sendMail", new SendMail());
        return "redirect:/sent";
    }
    @GetMapping("/sent")
    public ModelAndView homepage(@RequestParam("pageSize") Optional<Integer> pageSize,
                                 @RequestParam("page") Optional<Integer> page) {
        if (sendMailDao.count() != 0) {
            ;//pass
        } else {
            sendMailDao.findAll();
        }
//        fecthMail.fetch();

        ModelAndView modelAndView = new ModelAndView( "sent");
        //
        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        // print repo
        System.out.println("here is client repo " + sendMailDao.findAll());
        Page<SendMail> sent = (Page<SendMail>) sendMailDao.findAll(   new PageRequest(evalPage, evalPageSize));
        System.out.println("client list get total pages" + sent.getTotalPages() + "client list get number " + sent.getNumber());
        PagerModel pager = new PagerModel(sent.getTotalPages(), sent.getNumber(), BUTTONS_TO_SHOW);
        // add clientmodel
        modelAndView.addObject("sent", sent);
        // evaluate page size
        modelAndView.addObject("selectedPageSize", evalPageSize);
        // add page sizes
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        // add pager
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }
    @GetMapping("/sent/{id}/delete")
    public String delete(@PathVariable int id, RedirectAttributes redirect) {
        sendMailService.delete(id);
        redirect.addFlashAttribute("success", "Deleted employee successfully!");
        return "redirect:/sent";
    }
    @GetMapping("/sent/{id}")
    public String thismail(@PathVariable int id, Model model) {
        model.addAttribute("sent", sendMailService.getById(id));
        return "readmail";
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
