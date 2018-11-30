import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/login.html")
public class Login extends HttpServlet{
	String from;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		
		from = req.getParameter("from");
		if (from == null)
			from = "/Dark/Menu";
		
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
				+"<body><center>");
		page.println("<h1>Connexion</h1>");
		page.println("<form action=\"./LogVerif?from="+from+"\" method=\"post\">");
		page.println("<div class=\"form-group\">");
		page.println("<label for=\"login\" class=\"control-label\">Login :</label>");
		page.println("<input type=\"text\" class=\"form-control\" id=\"login\" name=\"login\" value=\"\">");
		page.println("</div>");
		page.println("<div class=\"form-group\">");
		page.println("<label for=\"password\" class=\"control-label\">Mot de passe</label>");
		page.println("<input type=\"password\" class=\"form-control\" id=\"mdp\" name=\"mdp\" value=\"\">");
		page.println("</div>");
		
		page.println("<button type=\"submit\" class=\"btn btn-success btn-block\">Envoyer</button>");
		page.println("<a href=\"createAccount.html\" class=\"btn btn-default btn-block\">Creer un compte !</a>");
		page.println("</form>");
		page.println("</center>");
		page.println("</body>");
		page.println("</html>");
	}
}