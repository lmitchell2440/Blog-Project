import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/ViewPost")
public class ViewPost extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String contextPath = request.getContextPath();
        out.println("<html><head>"
            + "<link rel='stylesheet' href='" + contextPath + "/css/styles.css'/>"
            + "</head><body>");

        try (Connection conn = DBUtil.getConnection(getServletContext())) {
        	int id = Integer.parseInt(request.getParameter("id"));
        	String thisSql = "SELECT id, name, date, content FROM blogposts WHERE id = ?";
        	PreparedStatement thisStmt = conn.prepareStatement(thisSql);
        	thisStmt.setInt(1, id);
        	ResultSet thisRs = thisStmt.executeQuery();
            
            
        	out.println("<h1> Blog Post #" + thisRs.getString("id") + "</h1>");
        	out.println("<i> Posted: " + thisRs.getString("date") + "</i>");
        	out.println("<hr/>");
        	out.println("<p>" + thisRs.getString("content") + "</p>");
        	out.println("<p class='return-home'><a href='Home'>Return to Home</a></p>");
        	
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("</body></html>");
	}
}