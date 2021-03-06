package Curs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String titular = request.getParameter("titular");
        int credite = Integer.parseInt(request.getParameter("credite"));
        int id = Integer.parseInt(request.getParameter("id"));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery("UPDATE CourseEntity\n\t" +
                "SET nume=\"" + nume + "\", titular=\"" + titular+ "\", credite=" + credite +
                "\n\tWHERE id=" + id).executeUpdate();
        transaction.commit();

        em.close();
        factory.close();

        request.getRequestDispatcher("./update-curs.jsp").forward(request, response);
    }
}
