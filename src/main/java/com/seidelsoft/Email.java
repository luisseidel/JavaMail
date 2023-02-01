package com.seidelsoft;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Email {

    private String mensagem;
    private String assunto;
    private String nomeRemetente;
    private String destinatarios;

    List<FileInputStream> listAnexos = new ArrayList<>();
    private static Message message;
    private EmailConfigs configs;

    public Email(String mensagem, String assunto, String nomeRemetente, String destinatarios) {
        this.mensagem = mensagem;
        this.assunto = assunto;
        this.nomeRemetente = nomeRemetente;
        this.destinatarios = destinatarios;
        this.configs = new EmailConfigs();
        this.message = configs.getMimeMessage();

        criarMensagem();
    }

    private void criarMensagem() {
        try {
            Address[] toUsers = InternetAddress.parse(destinatarios);

            message.setFrom(new InternetAddress(configs.getUser(), nomeRemetente));
            message.setRecipients(Message.RecipientType.TO, toUsers);
            message.setSubject(assunto);
            message.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(addCorpoEmail());
            addAnexosEmail(multipart);

            message.setContent(multipart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MimeBodyPart addCorpoEmail() throws MessagingException {
        MimeBodyPart corpoEmail = new MimeBodyPart();
        corpoEmail.setContent(mensagem, "text/html; charset=utf-8");

        return corpoEmail;
    }

    private void addAnexosEmail(Multipart multipart) throws IOException, MessagingException, DocumentException {
        int index = 0;
        for (FileInputStream fis : getListPdfExemplo()) {
            MimeBodyPart anexoEmail = new MimeBodyPart();
            anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(fis, "application/pdf")));
            anexoEmail.setFileName("anexo"+index+".pdf");
            multipart.addBodyPart(anexoEmail);
            index++;
        }
    }

    public static void enviarEmail() {
        try {
            Transport.send(message);
            System.out.println("Mensagem Enviada!");
            System.out.println(message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FileInputStream getPdfExemplo() throws IOException, DocumentException {
        Document document = new Document();
        File file = new File("anexo.pdf");
        file.createNewFile();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        document.add(new Paragraph("TEste do texto em pdf"));
        document.close();

        return new FileInputStream(file);
    }

    private List<FileInputStream> getListPdfExemplo() throws DocumentException, IOException {
        listAnexos = new ArrayList<>();
        listAnexos.add(getPdfExemplo());
        listAnexos.add(getPdfExemplo());
        listAnexos.add(getPdfExemplo());
        listAnexos.add(getPdfExemplo());

        return listAnexos;
    }
}
