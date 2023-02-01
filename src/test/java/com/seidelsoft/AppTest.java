package com.seidelsoft;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void testEmail() {
        Email email = new Email("Mensagem", "assunto", "Luis", "luis.guilherme.seidel@gmail.com, luis.seidel@celk.net");
        email.enviarEmail();
    }

    @Test
    public void testConfigs() {

    }
}
