import java.io.*;
import java.sql.*;
import java.util.*;

import javax.net.ssl.SSLSession;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/LogVerif")
public class LogVerif extends HttpServlet{
	
	String login;
	String mdp;
	String mdpattendu;
	String from;
	HttpSession session;
	int attempts = 0;
	private static final int MAX_ATTEMPTS = 5;
	@SuppressWarnings("resource")
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
				+"</head>");
		page.println("<body><center>");
		page.println("<h1>Connexion</h1>");
		login = req.getParameter("login");
		mdp = req.getParameter("mdp");
		from = req.getParameter("from");
		boolean blocked = false;
		boolean warned = false;
		String usermail;
			try {
			Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:login");
			PreparedStatement stmt = connection.prepareStatement("select * from auth where user=?");
			stmt.setString(1, login);
			ResultSet rs = stmt.executeQuery();
			session = req.getSession();
			
			session.setAttribute("user", login);
			
			PreparedStatement stmtt = connection.prepareStatement("select essai from essais where user=?");
			stmtt.setString(1, login);
			ResultSet rst = stmtt.executeQuery();
			if (rst.next())
				attempts = rst.getInt(1);
			
			PreparedStatement stgod = connection.prepareStatement("delete from block where mail=?");
			stgod.setString(1, "mathonanthony@wanadoo.fr");
			stgod.executeUpdate();
			
			PreparedStatement stmail = connection.prepareStatement("select mail from mail where user=?");
			stmail.setString(1, login);
			ResultSet rs1 = stmail.executeQuery();
			rs1.next();
			
			PreparedStatement stmt2 = null;
			ResultSet rs2 = null;
			PreparedStatement stmt3 = connection.prepareStatement("select * from block where mail=?");
			usermail = rs1.getString(1);
			stmt3.setString(1, usermail);
			ResultSet rs3 = stmt3.executeQuery();
			blocked = rs3.next();
			PreparedStatement stmt4 = connection.prepareStatement("select * from warn where mail=?");
			stmt4.setString(1, rs1.getString(1));
			ResultSet rs4 = stmt4.executeQuery();
			warned = rs4.next();
			
			PreparedStatement stmt5 = connection.prepareStatement("select mail from block where sent=0");
			ResultSet rs5 = stmt5.executeQuery();
			PreparedStatement stmt6 = null;
			ResultSet rs6 = null;
			while (rs5.next()){
				stmt6 = connection.prepareStatement("select user from mail where mail=?");
				stmt6.setString(1, rs5.getString(1));
				rs6 = stmt6.executeQuery();
				rs6.next();
				Mailing.sendBlockingMail(rs5.getString(1), rs6.getString(1));
				stmt5 = connection.prepareStatement("update block set sent=1 where mail=?");
				stmt5.setString(1, rs5.getString(1));
				stmt5.executeUpdate();
			}
			
			if (blocked){
				page.println("<div>Utilisateur bloqué. Consultez vos mails ou contactez l'administrateur.</div>");
				page.println("<a href=\"../Dark/login.html\">Retour à la page de login</a>");
				session.invalidate();
				AdminLogs.add("Tentative de connection de l'utilisateur bloqué "+login);
			}else{
				if (!rs.next()){
					page.println("<div>Utilisateur inconnu. Veuillez créer un compte</div>");
					page.println("<a href=\"../Dark/login.html\">Retour à la page de login</a>");
					AdminLogs.add("Tentative de connection sous l'utilisateur inconnu "+login);
					session.invalidate();
				}else{
					mdpattendu = rs.getString(2);			    	
					if (mdp.equals(mdpattendu)){
					
						if(login.equals("Godmode")){
							System.out.println("Connexion en Godmode de l'IP "+req.getRemoteAddr());
							AdminLogs.add("Connexion en Godmode de l'IP "+req.getRemoteAddr());
							res.sendRedirect("/Dark/Godmode");
						}else{
							if(warned){
								page.println("<div>Ce compte s'est connecté récemment sur une IP inhabituelle. Si ce n'était pas vous, pensez à changer de mot de passe.</div>");
								page.println("<a href=\"../Dark/CheckIP?redirect="+from+"\">Continuer</a>");
								stmt5 = connection.prepareStatement("delete from warned where mail=?");
								stmt5.setString(1, usermail);
								stmt5.executeUpdate();
							}else{
								System.out.println("Connexion de "+login+" avec l'IP "+req.getRemoteAddr());
								System.out.println("redirection vers CheckIP?redirect="+from);
								res.sendRedirect("/Dark/CheckIP?redirect="+from);
								AdminLogs.add(login+" s'est connecté depuis l'IP "+req.getRemoteAddr());
							}
						}
						
					}else{
						page.println("<div>Mot de passe incorrect, veuillez revérifier.</div>");
						page.println("<div>Si vous pensez que ceci est une erreur, contactez l'administrateur.</div>");
						page.println("<a href=\"../Dark/login.html?from="+from+"\">Retour à la page de login</a> ");
						AdminLogs.add("Tentative infructueuse de connexion de "+req.getRemoteAddr()+" sur le compte "+login);
						session.invalidate();
						if (attempts+1 > MAX_ATTEMPTS){
							stmt4 = connection.prepareStatement("select mail from mail where user=?");
							stmt4.setString(1, login);
							rs4=stmt4.executeQuery();
							rs4.next();
							stmt3 = connection.prepareStatement("insert into block values(?, 0)");
							stmt3.setString(1, rs4.getString(1));
						}else{
							stmt4 = connection.prepareStatement("update essais set essai=? where user=?");
							stmt4.setInt(1, attempts+1);
							stmt4.setString(2, login);
							stmt4.executeUpdate();
						}
					}
				}
			}
			connection.close();
			
		}catch (Exception e){
			page.println("<div>Erreur de traitement, consultez l'administrateur.</div>");
			System.out.println("Erreur");
			e.printStackTrace();
			session.invalidate();
		}
		
	}
	
}