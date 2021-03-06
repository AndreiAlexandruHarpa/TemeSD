package ejb;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CourseEntity {
    @Id
    @GeneratedValue
    private int id;
    private String nume;
    private String titular;
    private int credite;

    public CourseEntity(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume(){
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTitular(){
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public int getCredite() {
        return credite;
    }

    public void setCredite(int credite) {
        this.credite = credite;
    }
}
