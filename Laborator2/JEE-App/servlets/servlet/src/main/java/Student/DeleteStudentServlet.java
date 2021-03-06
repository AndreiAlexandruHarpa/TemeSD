package Student;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery("DELETE from StudentEntity WHERE id=" + request.getParameter("id")).executeUpdate();
        transaction.commit();
        // inchidere EntityManager
        em.close();
        factory.close();

        request.getRequestDispatcher("./fetch-student-list").forward(request, response);
    }
}
