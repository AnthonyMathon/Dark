import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Unshare")
public class Unshare extends HttpServlet{
	String user;
	String login;
	String mdp;
	String site;
	String info;
	String with;
	String from;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Unshare");
		}else{
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
			with = req.getParameter("with");
			from = req.getParameter("from");
			Connection connection = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmt = null;
				stmt = connection.prepareStatement("delete from share where user=? AND login=? AND site=? AND options=? AND mdp=? AND with=?");
				stmt.setString(1, from);
				stmt.setString(2, login);
				stmt.setString(3, site);
				stmt.setString(4, info);
				stmt.setString(5, mdp);
				stmt.setString(6, with);
				stmt.executeUpdate();
				connection.close();
				System.out.println("Reussite de l'arret du partage d'un mdp du site "+site+" de la base de "+from+ " a "+with);
				AdminLogs.add("Reussite de l'arret partage d'un mdp du site "+site+" de la base de "+from+ " a "+with);
				page.println("<div>Arret de partage effectué.</div><br>");
				page.println("<a href=\"Menu\">Retour</a> ");
			}catch (Exception e){
				page.println("<div>Partage non effectué. Peut être cette info est déjà partagée ?</div><br>"
						+ "<div>Contactez l'administrateur si vous pensez avoir vu ça par erreur</div><br>");
				page.println("<a href=\"Menu\">Retour</a> ");
				System.out.println("Erreur dans l'arret du partage pour "+with+" d'un mdp pour le site "+site+" appartenant "+from);
				AdminLogs.add("Erreur dans l'arret du partage pour "+with+" d'un mdp pour le site "+site+" appartenant "+from);
				e.printStackTrace();
				
			}finally{
				try{
					connection.close();
				}catch (Exception e){}
			}
		}
	}
	
}