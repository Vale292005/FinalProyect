package com.example.finalproyect.UserTree;
import com.example.finalproyect.MyMap.MyTreeMap;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class ManagementUser {
    private MyTreeMap<User, String> userMap; // Mapa de clave-user
    public MyTreeMap<User, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(MyTreeMap<User, String> userMap) {
		this.userMap = userMap;
	}

	public MyTreeMap<User, UserTree> getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(MyTreeMap<User, UserTree> treeMap) {
		this.treeMap = treeMap;
	}

	private MyTreeMap<User, UserTree> treeMap; // Mapa de clave - UserTree
    private MyTreeMap<User, String> gmailMap; // Mapa de user-correo

    public ManagementUser() {
        this.userMap = new MyTreeMap<>();
        this.treeMap = new MyTreeMap<>();
        this.gmailMap = new MyTreeMap<>();
    }

    public void addUser(String userId, User user, UserTree userTree, String gmail) {
        userMap.put(user, userId);
        treeMap.put(user, userTree);
        gmailMap.put(user, gmail); 
        sendConfirmationEmail(user, gmail);
    }

    public String getUser(User userId) {
        return userMap.get(userId);
    }

    public UserTree getUserTree(User user) {
        return treeMap.get(user);
    }

    public void printAllUsers() {
        for (User user : userMap.keySet()) {
            System.out.println("User : " + user);
            System.out.println(userMap.get(user));
            System.out.println("User Tree:");
            treeMap.get(user).printTree();
            System.out.println();
        }
    }



    public MyTreeMap<User, String> getGmailMap() {
		return gmailMap;
	}

	public void setGmailMap(MyTreeMap<User, String> gmailMap) {
		this.gmailMap = gmailMap;
	}

	public User getUserByGmail(String gmail) {
        for (User user : gmailMap.keySet()) {
            if (gmailMap.get(user).equals(gmail)) {
                return user; // Devuelve el usuario si el correo coincide
            }
        }
        return null; // Si no se encuentra el correo, retorna null
    }

    // Método para eliminar un usuario por su clave
	public boolean removeUser(User userId) {
	    if (userMap.get(userId) != null) {
	        String user = userMap.get(userId);
	        userMap.remove(userId);  // Elimina la entrada de userMap
	        treeMap.remove(userId);  // Elimina la entrada de treeMap
	        gmailMap.remove(userId);   // Elimina la entrada de gmailMap
	        System.out.println("Usuario eliminado exitosamente.");
	        return true;
	    } else {
	        System.out.println("Usuario no encontrado.");
	        return false;
	    }
	}

    public static void sendConfirmationEmail(User user, String gmail) {
        // Configuración del servidor SMTP de Gmail
        String host = "smtp.gmail.com";
        String from = "confirmarUser@gmail.com";  // Tu correo de Gmail
        String password = "kpi12345678v";     // Tu contraseña de Gmail

        // Propiedades del correo
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Crear una sesión con autenticación
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Crear el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(gmail));
            message.setSubject("Confirmación de Registro");
            message.setText("Hola " + user + ",\n\nTu registro ha sido exitoso. ¡Bienvenido!");

            // Enviar el mensaje
            Transport.send(message);
            System.out.println("Correo de confirmación enviado a: " + gmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
