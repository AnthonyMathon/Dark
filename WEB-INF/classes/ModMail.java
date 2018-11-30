import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/ModMail")
public class ModMail extends HttpServlet{
	String user;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/ModMail");
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
			+ "<body><center>"
			+ "<h1>Changer son adresse mail.</h1>"
			+ "<form method=\"post\" action=\"../Dark/NewMail\">"
			+ "<div class=\"form-group\">"
			+ "<label for=\"mdp\" class=\"control-label\">Nouvelle adresse mail </label>"
			+ "<input type=\"password\" class=\"form-control\" id=\"mdp\" name=\"mdp\" value=\"\">"
			+ "</div>"
			+ "<a href=\"Menu\">Retour</a> "
			+ "<button type=\"submit\" class=\"btn btn-success btn-block\">Envoyer</button>"
			+ "</form>"
			+ "</center></body>"
			+ "</html>");
		}
	}
	
}