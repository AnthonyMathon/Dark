import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Search")
public class Search extends HttpServlet{
	String user;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Search");
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
			+ "<body><center>"
			+ "<h1>Quels termes dois-je rechercher ?<br>Laissez vide si info inutile</h1>"
			+ "<form method=\"post\" action=\"../Dark/Seek\">"
			+ "<div class=\"form-group\">"
			+ "<label for=\"site\" class=\"control-label\">Site/Jeu</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"site\" name=\"site\">"
			+ "</div>"
			+ "<div class=\"form-group\">"
			+ "<label for=\"login\" class=\"control-label\">Pseudo/mail</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"login\" name=\"login\">"
			+ "</div>"
			+ "<a href='Menu'>Retour</a>"
			+ "<button type=\"submit\" class=\"btn btn-success btn-block\">Envoyer</button>"
			+ "</form>"
			+ "</center></body>"
			+ "</html>");
		}
	}
	
}