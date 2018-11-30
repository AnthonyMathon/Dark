import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Share")
public class Share extends HttpServlet{
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
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Share");
		}else{
			login = req.getParameter("login");
			mdp = req.getParameter("mdp");
			site = req.getParameter("site");
			info = req.getParameter("info");
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
			+ "<h1>Partagez la donnée avec qui ?</h1>"
			+ "<form method=\"post\" action=\"../Dark/DoShare\">"
			+ "<div class=\"form-group\">"
			+ "<label for=\"site\" class=\"control-label\">Site/Jeu</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"site\" name=\"site\" value=\""+site+"\">"
			+ "</div>"
			+ "<div class=\"form-group\">"
			+ "<label for=\"info\" class=\"control-label\">Infos supplementaires</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"info\" name=\"info\" value=\""+info+"\">"
			+ "</div>"
			+ "<div class=\"form-group\">"
			+ "<label for=\"login\" class=\"control-label\">Pseudo/mail</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"login\" name=\"login\" value=\""+login+"\">"
			+ "</div>"
			+ "<div class=\"form-group\">"
			+ "<label for=\"mdp\" class=\"control-label\">Mot de passe</label>"
			+ "<input type=\"password\" class=\"form-control\" id=\"mdp\" name=\"mdp\" value=\""+mdp+"\">"
			+ "</div>"
			+ "<div class=\"form-group\">"
			+ "<label for=\"name\" class=\"control-label\">Pseudo</label>"
			+ "<input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" value=\"\">"
			+ "</div>"
			+ "<a href='Menu'>Retour</a>"
			+ "<button type=\"submit\" class=\"btn btn-success btn-block\">Envoyer</button>"
			+ "</form>"
			+ "</center></body>"
			+ "</html>");
		}
	}
	
}