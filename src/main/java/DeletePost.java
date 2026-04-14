import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/DeletePost")
public class DeletePost extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null) { 
            int id = Integer.parseInt(idParam);

            try (Connection conn = DBUtil.getConnection(getServletContext())) {
                String sql = "DELETE FROM blogposts WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                System.out.println("Deleted rows: " + rows);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Redirect back to the blog list
        response.sendRedirect("Home");
    }
}