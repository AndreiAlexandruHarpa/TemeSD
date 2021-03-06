package Curs;

import ejb.CourseEntity;
import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String titular = request.getParameter("titular");
        int credite = Integer.parseInt(request.getParameter("credite"));

        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        CourseEntity curs = new CourseEntity();
        curs.setNume(nume);
        curs.setTitular(titular);
        curs.setCredite(credite);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(curs);
        transaction.commit();

        em.close();
        factory.close();

        response.setContentType("text/html");
        response.getWriter().println("Datele au fost adaugate in baza de date." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }
}
