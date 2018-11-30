import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Menu")
public class Menu extends HttpServlet{
	
	String user;
	String mail;
	public void service( HttpServletRequest req, HttpServletResponse res ){
		res.setContentType("text/html;charset=UTF-8");
		Connection connection = null;
		try{
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
			page.println("<h1>Menu principal</h1>");
			HttpSession session = req.getSession(true);
			user = (String) session.getAttribute("user");
			if (session.getAttribute("user")==null){
				res.sendRedirect("../Dark/login.html?from=/Dark/Menu");
			}else{				
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				
				PreparedStatement stmt = connection.prepareStatement("select verif from verify where user=?");
				stmt.setString(1, user);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				
				PreparedStatement stmt1 = connection.prepareStatement("select localise from IP where user=?");
				stmt1.setString(1, user);
				ResultSet rs1 = stmt1.executeQuery();
				boolean loc = rs1.next();
				
				page.println("<div class='page-header'>");
				page.println("<h1>Bonjour "+ user + ", que voulez faire ?</h1>");
				page.println("</div>");
				
				page.println("<div class='row'");
				page.println("<div class='col-xs-6 col-xs-offset-3'");
				page.println("<nav>");
				page.println("<ul class='nav nav-pills nav-justified'>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Voir'>Voir les comptes</a></li>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Add'>Ajouter un compte</a></li>");
				page.println("<br>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Search'>Rechercher</a></li>");
				page.println("<br>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Mod'>Modifier mon mot de passe</a></li>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='ModMail'>Modifier mon adresse mail</a></li>");
				page.println("<br>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='DelAcc'><font color=red>SUPPRIMER MON COMPTE</font></a></li>");
				page.println("<br>");
				page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Deconnect'>Déconnexion</a></li>");
				page.println("<br>");
				page.println("<br>");
				page.println("<br>");
				if (rs.getInt(1)==0){
					page.println("<div>Votre mail n'a pas encore été vérifié.<div>");
					page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='Verif'>Verifier mon adresse mail.</a></li>");
				}
				page.println("<br>");
				if (!loc){
					page.println("<div>Etes vous à l'endroit où vous êtes sensé(e) être ?<div>");
					page.println("<li role='presentation' class='btn btn-default btn-lg'><a href='IPLocVerif'>Si oui, cliquez ici pour nous le dire.</a></li>");
				}
				page.println("</ul>");
				page.println("</nav>");
			    page.println("</div>");
			    page.println("</div>");
			    page.println("</body>");
			    page.println("</html>");
			}
		}catch (Exception e){
			System.out.println("Problème soulevé :");
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch (Exception e){}
		}
		
	}
	
}