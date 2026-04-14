import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.time.*;

/**
 * Servlet implementation class CreatePost
 */
@WebServlet(value = "/CreatePost", loadOnStartup = 1)
public class CreatePost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatePost() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    public void init() throws ServletException {
        try (Connection conn = DBUtil.getConnection(getServletContext())) {

            String sql = "CREATE TABLE IF NOT EXISTS blogposts (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "name TEXT, " +
                         "date TEXT, " +
                         "content TEXT)";

            Statement stmt = conn.createStatement();
            stmt.execute(sql);

            System.out.println("✅ Database & table initialized");

        } catch (Exception e) {
            throw new ServletException("DB Initialization Failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String contextPath = request.getContextPath();
        out.println("<html><head>"
            + "<link rel='stylesheet' href='" + contextPath + "/css/styles.css'/>"
            + "</head><body>");
        out.println("<h2>Blog Entry Creation Form</h2>");
        out.println("<p><a href='Home'>Return to Home</a></p>");
        out.println("<form method='post' action='CreatePost'>");

        out.println("Name: <input type='text' name='name' required/><br><br>");
        out.println("Content: <textarea name='content' class='content-field' required></textarea><br><br>");
        out.println("<input type='submit' class='submit' value='Submit'/>");

        out.println("</form>");
        out.println("</body></html>");
    }

    // Handles POST request → Process form data
  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String content = request.getParameter("content");
        LocalDateTime date = LocalDateTime.now();

        try (Connection conn = DBUtil.getConnection(getServletContext())) {

            String sql = "INSERT INTO blogposts(name, date, content) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, date.toString());
            stmt.setString(3, content);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("Home");
    }
}
