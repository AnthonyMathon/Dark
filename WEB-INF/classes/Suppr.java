import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Suppr")
public class Suppr extends HttpServlet{
	String user;
	String login;
	String mdp;
	String site;
	String info;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Suppr");
		}else{
			user = (String) session.getAttribute("user");
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
			site = req.getParameter("site");
			info = req.getParameter("info");
			Connection connection = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmt = connection.prepareStatement("delete from login "
						+ "where user=? and login=? and site=? and options=? and mdp=?");
				stmt.setString(1, user);
				stmt.setString(2, login);
				stmt.setString(3, site);
				stmt.setString(4, info);
				stmt.setString(5, mdp);
				stmt.executeUpdate();
				connection.close();
				System.out.println("Reussite de la suppression d'un mdp du site "+site+" a la base de "+user);
				AdminLogs.add("Reussite de la suppression d'un mdp du site "+site+" a la base de "+user);
				page.println("<div>Suppression effectuée.</div><br>");
				page.println("<a href=\"Voir\">Retour</a> ");
			}catch (Exception e){
				page.println("<div>Suppression non effectuée. Données incorrectes ?</div><br>"
						+ "<div>Contactez l'administrateur si vous pensez avoir vu ça par erreur</div><br>");
				page.println("<a href=\"Voir\">Retour</a> ");
				System.out.println("Erreur dans la suppression pour le site "+site+" pour "+user);
				AdminLogs.add("Erreur dans la suppression pour le site "+site+" pour "+user);
				e.printStackTrace();
				
			}finally{
				try{
					connection.close();
				}catch (Exception e){}
			}
		}
	}
	
}