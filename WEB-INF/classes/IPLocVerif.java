import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial" })
@WebServlet("/IPLocVerif")
public class IPLocVerif extends HttpServlet{
	String user;
	String IP;
	String loc;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Verif");
		}else{
			page.println("<!DOCTYPE html>"
					+ "<html lang=\"fr\"><head>"
					+ "<!--d�finition du codage des caract�res-->"
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
			page.println("<h1>Verification localisation</h1>");
			Connection connection = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				VerifyIP check = new VerifyIP();
				IP = req.getRemoteAddr();
				loc = check.getRegion(IP);
				if (loc != null && !loc.equals(null)){
					PreparedStatement stmt = connection.prepareStatement("insert into IP values (?, ?)");
					stmt.setString(1, user);
					stmt.setString(2, loc);
					stmt.executeUpdate();
					System.out.println("IP de "+user+" situ� : "+loc);
					page.println("<div>Merci, nous nous occupons du reste.</div>");	
				}else{
					page.println("<div>Impossible pour nous de détecter la région précise de votre IP. Reessayez ultérieurement.");
					page.println("<div>Pour plus d'informations, contactez l'administrateur.");
				}
				page.println("<a href=\"Menu\">Retour</a> ");
				page.println("</center></body></html>");
				connection.close();
			}catch (Exception e){
				
				page.println("<div>Securisation de votre localisation non r�ussie. Contactez l'administrateur.</div>");				
				page.println("<a href=\"Menu\">Retour</a> ");
				page.println("</center></body></html>");
				e.printStackTrace();
			}
		}
	}
	
}