import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/NewAccount")
public class NewAccount extends HttpServlet{
	
	String login;
	String mdp;
	String mail;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		page.println("<!DOCTYPE html>"
				+ "<html lang=\"fr\"><head>"
				+ "<!--définition du codage des caractères-->"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+"<!-- Donne le nom de l\'onglet -->"
				+"<title>Connexion</title>" 
				+"<!-- Donne le nom de l\'auteur-->"
				+"<meta name=\"auteur\" content=\"Anthony\" />"
				+"<!-- liens-->"
				+"<link rel= \"stylesheet\" href=\"data/style.css\" type=\"text/css\" media=\"screen\" />"
				+"<script src=\"Jquery-1.11.1.min.js\"></script>"
				+"</head>"
				+ "<body><center>");
		page.println("<h1>Creation d'un nouvel utilisateur</h1>");
		login = req.getParameter("login");
		mdp = req.getParameter("mdp");
		mail = req.getParameter("mail");
		if (!mail.contains("@")){
			page.println("<div>Mail invalide. Veuillez reessayer.</div>");
			page.println("<a href=\"../Dark/createAccount.html\">Retour à la page de creation de compte</a> ");
		}else{
			Connection connection = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmt = null;
				stmt = connection.prepareStatement("select * from mail where mail=?");
				stmt.setString(1, mail);
				ResultSet rs = stmt.executeQuery();
				if (!rs.next()){
					stmt = connection.prepareStatement("insert into auth values(?, ?)");
					stmt.setString(1, login);
					stmt.setString(2, mdp);
					stmt.executeUpdate();
					stmt = connection.prepareStatement("insert into mail values(?, ?)");
					stmt.setString(1, login);
					stmt.setString(2, mail);
					stmt.executeUpdate();
					stmt = connection.prepareStatement("insert into verify values(?, ?)");
					stmt.setString(1, login);
					stmt.setInt(2, 0);
					stmt.executeUpdate();
					stmt = connection.prepareStatement("insert into essais values(?, ?)");
					stmt.setString(1, login);
					stmt.setInt(2, 0);
					stmt.executeUpdate();
					connection.close();
					System.out.println("Reussite de l'ajout de "+login+" a la base d'auth");
					AdminLogs.add("Reussite de l'ajout de "+login+" a la base d'auth");
					page.println("<div>Compte créé, Vous pouvez vous connecter.</div><br>");
					page.println("<a href=\"../Dark/login.html\">Retour à la page de login</a> ");
				}else{
					page.println("<div>Ce compte ou ce mail existe déjà. si vous avez oublié vos identifiants, contactez l'administrateur.</div><br>");
					page.println("<a href=\"../Dark/login.html\">Retour à la page de login</a> ");
					AdminLogs.add("Erreur d'ajout de l'utilisateur"+login);
				}
			}catch (Exception e){
				page.println("<div>Ce compte ou ce mail existe déjà. si vous avez oublié vos identifiants, contactez l'administrateur.</div><br>");
				page.println("<a href=\"../Dark/login.html\">Retour à la page de login</a> ");
				AdminLogs.add("Erreur d'ajout de l'utilisateur"+login);
				System.out.println("Erreur");
				e.printStackTrace();
			}finally{
				try{
					connection.close();
				}catch (Exception e){}
			}
		}
	}
	
}