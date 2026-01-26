package com.asur.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailSender {
    private static EmailSender instancia = null;
    private final String smtpHost;
    private final int smtpPort;
    private final String usuario;
    private final String password;
    private final String remitente;
    private final boolean usarTLS;
    private final boolean usarSSL;

    private EmailSender() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("no se encontro config.properties");
            }

            Properties prop = new Properties();
            prop.load(input);

            this.smtpHost = prop.getProperty("mail.smtp.host", "smtp.gmail.com");
            this.smtpPort = Integer.parseInt(prop.getProperty("mail.smtp.port", "587"));
            this.usuario = prop.getProperty("mail.smtp.user", "");
            this.password = prop.getProperty("mail.smtp.password", "");
            this.remitente = prop.getProperty("mail.from", "noreply@asur.uy");
            this.usarTLS = Boolean.parseBoolean(prop.getProperty("mail.smtp.starttls", "true"));
            this.usarSSL = Boolean.parseBoolean(prop.getProperty("mail.smtp.ssl", "false"));

        } catch (IOException e) {
            throw new RuntimeException("error leyendo config smtp", e);
        }
    }

    public static EmailSender getInstancia() {
        if (instancia == null) {
            instancia = new EmailSender();
        }
        return instancia;
    }

    public boolean enviarEmail(String destinatario, String asunto, String cuerpo) {
        return enviarEmail(destinatario, asunto, cuerpo, false);
    }

    public boolean enviarEmail(String destinatario, String asunto, String cuerpo, boolean esHtml) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.auth", "true");
        
        if (usarTLS) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        if (usarSSL) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, password);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);

            if (esHtml) {
                mensaje.setContent(cuerpo, "text/html; charset=utf-8");
            } else {
                mensaje.setText(cuerpo);
            }

            Transport.send(mensaje);
            return true;

        } catch (MessagingException e) {
            System.err.println("error enviando email: " + e.getMessage());
            return false;
        }
    }

    public boolean enviarConfirmacionRegistro(String destinatario, String nombre, String codigoConfirmacion) {
        String asunto = "ASUR - Confirma tu registro";
        String cuerpo = String.format("""
            <html>
            <body>
            <h2>Hola %s!</h2>
            <p>Gracias por registrarte en el sistema de ASUR.</p>
            <p>Tu codigo de confirmacion es: <strong>%s</strong></p>
            <p>Por favor ingresa este codigo en el sistema para activar tu cuenta.</p>
            <br>
            <p>Saludos,<br>Asociacion de Sordos del Uruguay</p>
            </body>
            </html>
            """, nombre, codigoConfirmacion);

        return enviarEmail(destinatario, asunto, cuerpo, true);
    }

    public boolean enviarRecuperacionContrasenia(String destinatario, String nombre, String token) {
        String asunto = "ASUR - Recuperacion de contrasena";
        String cuerpo = String.format("""
            <html>
            <body>
            <h2>Hola %s!</h2>
            <p>Recibimos una solicitud para restablecer tu contrasena.</p>
            <p>Tu token de recuperacion es: <strong>%s</strong></p>
            <p>Si no solicitaste esto, ignora este mensaje.</p>
            <br>
            <p>Saludos,<br>Asociacion de Sordos del Uruguay</p>
            </body>
            </html>
            """, nombre, token);

        return enviarEmail(destinatario, asunto, cuerpo, true);
    }
}
