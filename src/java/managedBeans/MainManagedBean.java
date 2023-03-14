/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import com.mysql.jdbc.Connection;
import entities.Journal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Transaction;
import sessionBeans.MainSessionBean;

/**
 *
 * @author RaBnHooD
 */
@Getter
@Setter
@ManagedBean
public class MainManagedBean {

//    @EJB
//    MainSessionBean mainSessionBean;
//    @PersistenceContext
//    private EntityManager em;
    private String body, title;
    private Date date, enterDate = new Date();
    private List<Journal> recordList;

    public MainManagedBean() throws ClassNotFoundException, ParseException {
        getAllRecords();
    }

    public Connection getConnection() throws ClassNotFoundException {
        Connection con = null;
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/journal";
        String user = "root";
        String password = "";

        try {
            con = (Connection) DriverManager.getConnection(url, user, password);
            System.out.println("Connection completed.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }

//    public void sendJornalPersist() {
//        System.out.println("body::" + body);
//        System.out.println("title::" + title);
//        System.out.println("date::" + date);
//        Journal journalEntry = new Journal();
//        journalEntry.setBody(body);
//        journalEntry.setEntryDate(date);
//        journalEntry.setTitle(title);
//        System.out.println("here1");
//        em.persist(journalEntry);
//    }
    public String sendJornal() throws ClassNotFoundException {
        System.out.println("body::" + body);
        System.out.println("title::" + title);
        System.out.println("date::" + date);
        String newDate = dateFormatter(date);

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO journalEntries (title,body,entry_date) VALUES(\"" + title + "\",\"" + body + "\",\"" + newDate + "\")";
            System.out.println("sql::" + sql);
            stmt.executeUpdate(sql);
            System.out.println("Inserted record into the table...");
            getAllRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public String getAllRecords() throws ClassNotFoundException, ParseException {
        String sql = "SELECT id, title, body, entry_date FROM journalentries";
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            {
                recordList = new ArrayList<>();

                while (rs.next()) {
                    Journal j = new Journal();
                    j.setId(Long.valueOf(rs.getString("id")));
                    j.setTitle(rs.getString("title"));
                    j.setBody(rs.getString("body"));
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = dt.parse(rs.getString("entry_date"));
                    System.out.println("date1::" + date1);
                    j.setEntryDate(date1);
                    recordList.add(j);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public String dateFormatter(Date d) {
        System.out.println("in date::" + d);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dt.format(d);
        System.out.println("strDate::" + strDate);
        return strDate;
    }

    public void findResultsWithSpecificDate() throws ClassNotFoundException, ParseException {
        System.out.println("enteredDate::" + enterDate);
        String sql = "SELECT id, title, body, entry_date FROM journalentries WHERE entry_date = '" + dateFormatter(enterDate) + "'";
        System.out.println("sq::" + sql);
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            {
                recordList = new ArrayList<>();
                while (rs.next()) {
                    Journal j = new Journal();
                    j.setId(Long.valueOf(rs.getString("id")));
                    j.setTitle(rs.getString("title"));
                    j.setBody(rs.getString("body"));
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = dt.parse(rs.getString("entry_date"));
                    System.out.println("date1::" + date1);
                    j.setEntryDate(date1);
                    recordList.add(j);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
