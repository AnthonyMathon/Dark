import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;


@SuppressWarnings("serial")
@WebServlet("/Godmode")
public class Godmode extends HttpServlet {
	String ip;
	String myIP;
	String godname;
	String godmail = "mathonanthony@wanadoo.fr";
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	res.setContentType("text/html;charset=UTF-8");
    	HttpSession session = req.getSession();
    	godname = (String) session.getAttribute("user");
		PrintWriter page = res.getWriter();
		page.println("<!DOCTYPE html><head><meta charset=\"utf-8\" /></head><body><center>");
		ip = req.getRemoteAddr(); 
		myIP = req.getLocalAddr();
		
	if (!myIP.equals(ip) || !godname.equals("Godmode") || session.getAttribute("user")==null){
		AdminLogs.add("TENTATIVE DE CONNEXION DE "+req.getRemoteAddr()+" SUR LE GODMODE !!!");
		session.invalidate();
		res.sendRedirect("../Dark/login.html");
	}else{
		
	page.println("<div class='page-header'>");
	page.println("<h1>Toutes les données :</h1>");
	page.println("</div>");
		
	
	page.println("<div class='row'>");
	page.println("<div class='col-xs-12'>");
	Connection con =null;
	try {
	    
	    // enregistrement du driver
	    Class.forName("org.sqlite.JDBC");
	    
	    // connexion a la base
	    con = DriverManager.getConnection("jdbc:sqlite:login");
	    
	    // comptes
	    PreparedStatement stmt = con.prepareStatement("Select * from login order by user asc, site asc");
	    PreparedStatement stmt2 = null;
	    ResultSet rs2 = null;
	    String verifie = "OUI";
	    String warned = "NON";
	    String blocked = "NON";
	    String loc = "Unknown";
	    ResultSet rs = stmt.executeQuery();	    
	    page.println("<table class='table table-bordered table-striped'>");
	    
	    page.print("<thead>");
		page.print("<tr>");
		page.print("<th>utilisateur</th>");
	    page.print("<th>jeu/site</th>");
	    page.print("<th>infos</th>");
	    page.print("<th>Pseudo/mail</th>");
	    page.println("</tr>");
	    page.print("</thead>");
	    
	    page.print("<tbody>");
	    
	    while(rs.next())
		{
		    page.println("<tr>");
		    page.print("<td>"+rs.getString(1)+"</td>");
		    page.print("<td>"+rs.getString(3)+"</td>");
		    page.print("<td>"+rs.getString(4)+"</td>");
		    page.print("<td>"+rs.getString(2)+"</td>");
		    page.println("</tr>");
		}
	    
		page.print("</tbody>");
		
	    page.println("</table>");
	    
	    page.println("<br><br><div>Utilisateurs :<br>");
	    //logins
	    stmt = con.prepareStatement("Select * from mail order by user asc");
	    rs = stmt.executeQuery();
	    page.println("<table class='table table-bordered table-striped'>");
	    
	    page.print("<thead>");
		page.print("<tr>");
	    page.print("<th>login</th>");
	    page.print("<th>mail</th>");
	    page.print("<th>averti</th>");
	    page.print("<th>bloqué</th>");
	    page.print("<th>vérifié</th>");
	    page.print("<th>essais</th>");
	    page.print("<th>localisation</th>");
	    page.println("</tr>");
	    page.print("</thead>");
	    
	    page.print("<tbody>");
	    
	    while(rs.next())
		{
	    	verifie = "OUI";
	    	warned = "NON";
	    	blocked = "NON";
	    	loc = "Unknown";
	    	stmt2 = con.prepareStatement("select verif from verify where user=?");
	    	stmt2.setString(1, rs.getString(1));
	    	rs2 = stmt2.executeQuery();
	    	rs2.next();
	    	if (rs2.getInt(1)==0){
	    		verifie = "NON";
	    	}
	    	stmt2= con.prepareStatement("select * from warn where mail=?");
	    	stmt2.setString(1, rs.getString(2));
	    	rs2 = stmt2.executeQuery();
	    	if (rs2.next()){
	    		warned = "OUI";
	    	}
	    	stmt2= con.prepareStatement("select * from block where mail=?");
	    	stmt2.setString(1, rs.getString(2));
	    	rs2 = stmt2.executeQuery();
	    	if (godmail.equals(rs.getString(2))){
	    		blocked = "Can't be";
	    	}else if (rs2.next()){
	    		blocked = "OUI";
	    	}
	    	stmt2=	con.prepareStatement("select localise from IP where user=?");
	    	stmt2.setString(1, rs.getString(1));
	    	rs2 = stmt2.executeQuery();
	    	if(rs2.next()){
	    		loc = rs2.getString(1);
	    	}
	    	stmt2= con.prepareStatement("select essai from essais where user=?");
	    	stmt2.setString(1, rs.getString(1));
	    	rs2 = stmt2.executeQuery();
	    	rs2.next();
		    page.println("<tr>");
		    page.print("<td>"+rs.getString(1)+"</td>");
		    page.print("<td>"+rs.getString(2)+"</td>");
		    page.print("<td>"+warned+"</td>");
		    page.print("<td>"+blocked+"</td>");
		    page.print("<td>"+verifie+"</td>");
		    page.print("<td>"+rs2.getInt(1)+"</td>");
		    page.print("<td>"+loc+"</td>");
		    page.println("</tr>");
		}
	    
		page.print("</tbody>");
	    page.println("</table>");
	    page.println("<br><br><h2>logs</h2><br>");
	    page.println(AdminLogs.getlogs());
	    page.println("<br><br><br>");
	    page.println("<a href='Deconnect'>Déconnexion</a></li>");
	}
	catch (Exception e) {
		System.out.println("Erreur soulevée :");
		e.printStackTrace();
	}
	finally
	    {
		try{con.close();} catch (Exception e){}
	    }
			
		page.println("</div>");
		page.println("</div>");
		page.println("</div>");

		page.println("</body></html>");
		}
    }
}
