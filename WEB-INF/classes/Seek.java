import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;


@SuppressWarnings("serial")
@WebServlet("/Seek")
public class Seek extends HttpServlet {
	String user;
	String site;
	String pseudo;
	String q;
	boolean l=false;
	boolean s=false;
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
		HttpSession session = req.getSession(true);
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Menu");
		}else{
		user = (String) session.getAttribute("user");
		site = req.getParameter("site");
		pseudo = req.getParameter("login");
		page.println("<div class='page-header'>"
				+ "<script src=\"dist/clipboard.min.js\"></script>");
		page.println("<h1>Résultat(s) de la recherche</h1>");
		page.println("</div>");
		page.println("<div class='row'>");
		page.println("<div class='col-xs-12'>");
		Connection con =null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		try {
		    
		    // enregistrement du driver
		    Class.forName("org.sqlite.JDBC");
		    
		    // connexion a la base
		    con = DriverManager.getConnection("jdbc:sqlite:login");
		    
		    // execution de la requete
		    q="Select * from login where user=?";
		    l=false;
		    if (!pseudo.equals(" ") && !pseudo.equals("") && !pseudo.equals(null)){
		    	q+= " AND login=?";
		    	l=true;
		    }
		    s=false;
		    if (!site.equals(" ") && !site.equals("") && !site.equals(null)){
		    	q+= " AND site=?";
		    	s=true;
		    }
		    q+= " order by site asc";
		    System.out.println(pseudo+"("+l+")"+","+site+"("+s+")");
		    stmt = con.prepareStatement(q);
		    stmt.setString(1, user);
		    if (l){
		    	stmt.setString(2, pseudo);
		    }
		    if (l && s){
		    	stmt.setString(3, site);
		    }
		    if (!l && s){
		    	stmt.setString(2, site);
		    }
		   
		    rs = stmt.executeQuery();	    
		    page.println("<table class='table table-bordered table-striped'>");
		    
		    page.print("<thead>");
			page.print("<tr>");
		    page.print("<th>jeu/site</th>");
		    page.print("<th>infos</th>");
		    page.print("<th>Pseudo/mail</th>");
		    page.print("<th>mot de passe</th>");
		    //page.print("<th>Copier le mdp</th>"); 
		    page.print("<th>Suppression</th>");
		    page.print("<th>Partage</th>");
		    page.println("</tr>");
		    page.print("</thead>");
		    
		    page.print("<tbody>");
		    
		    while(rs.next())
			{
			    page.println("<tr>");
			    page.print("<td>"+rs.getString(3)+"</td>");
			    page.print("<td>"+rs.getString(4)+"</td>");
			    page.print("<td>"+rs.getString(2)+"</td>");
			    page.print("<td>"+rs.getString(5)+"</td>");
			    //page.print("<td>"+"<button id=\"copy-button\" type=\"button\" class=\"btn btn-lg btn-default dropdown-toggle red-btn zclip\" data-clipboard-text=\""+rs.getString(5)+"\">Copie</button>"+"</td>");
			    page.print("<td><a href=\"Suppr?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"\">Supprimer</a></td>");
			    page.print("<td><a href=\"Share?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"\">Partager</a></td>");
			    page.println("</tr>");
			}
		    
			page.print("</tbody>");
		    page.println("</table>");
				    
		    page.print("<tbody>");
		    page.println("<br><br><br>");
		    page.println("<a href=\"Menu\">Retour au Menu principal</a> ");
		    AdminLogs.add(user+" vient de faire une recherche.");
		}
		catch (Exception e) {			
			System.out.println("Erreur soulevée pour "+user+" :");
			e.printStackTrace();
		}
		finally
		    {
			try{con.close();} catch (Exception e){}
		    }
				
				page.println("</div>");
			page.println("</div>");
		page.println("</div>");
		page.println("<script src=\"../Dark/dist/clipboard.min.js\"></script>");
		page.println("</body></html>");
		page.close();
	    }
	}
}
