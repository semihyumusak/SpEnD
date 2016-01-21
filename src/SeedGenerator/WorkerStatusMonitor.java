/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Semih
 */
public class WorkerStatusMonitor extends Thread {

    private EventQueue eventQueue = null;
    private boolean running = true;
    private int count = 0;
    int threadNumber, numberOfThreads;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;

    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerStatusMonitor(java.awt.Component target, String connectionString, int threadNumber, int numberOfThreads) {
        this.target = target;
        this.connectionUrl = connectionString;
        this.threadNumber = threadNumber;
        this.numberOfThreads = numberOfThreads;
        initializeJdbc();
    }

    /**
     * stopRunning stop the SimpleWorker thread.
     */
    public void stopRunning() {
        running = false;

    }

    /**
     * run do the work.
     */
    public void run() {
        int percent = 0;
        String msg = "";
        System.out.println("WorkerStatusMonitor.run : Thread "
                + Thread.currentThread().getName() + Thread.currentThread().getId() + " started");

        eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();

        boolean wait = false;
        while (running) {
            try {
                if (wait) {
                    Thread.sleep(60000);
                    wait = false;
                }
                if (!getNextUrlAndAnalyze()) {
                    try {
                        String SQL = "update endpoints set checkFlag = null where MOD(id,?)=? ;";
                        PreparedStatement pstmt
                                = con.prepareStatement(SQL);
                        // execute the query, and get a java resultset
                        pstmt.setInt(2, threadNumber);
                        pstmt.setInt(1, numberOfThreads);
                        pstmt.executeUpdate();

                    } catch (SQLException ex) {

                    }
                    Thread.sleep(10000);
                } else {
                    Thread.sleep(1000);
                    System.gc();
                }
            } catch (SQLException s) {
                System.out.println(s.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                wait = true;
            }
        }
        System.out.println("WorkerAnalyzer.run : Thread "
                + Thread.currentThread().getName() + Thread.currentThread().getId() + " stopped");

//        target.dispatchEvent(new SimpleAWTEvent(target, "", count,this.ana+" completed.."));
//        eventQueue.postEvent(new SimpleAWTEvent(target, "Work done..", count));
//        System.out.println("SimpleWorker.run : Thread "
//                + Thread.currentThread().getName() + " stoped");
    }                       // run

    private int getUnprocessedSeedUrlId() throws SQLException {
        PreparedStatement pstmt
                = con.prepareStatement("select id,endpointUrl from endpoints where checkFlag is null and MOD(id,?)=? order by id asc;");
        // execute the query, and get a java resultset
        pstmt.setInt(2, threadNumber);
        pstmt.setInt(1, numberOfThreads);
        ResultSet rs = pstmt.executeQuery();

        // iterate through the java resultset
        while (rs.next()) {
            int id = rs.getInt("id");
            rs.close();
            pstmt.close();
            return id;
            //String firstName = rs.getString("url");
        }
        rs.close();
        pstmt.close();
        return 0;
    }

    private int isKnownEndpoint(String url) throws SQLException {
        int isEndpoint = 0;
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select id,endpointUrl,active from endpoints where endpointUrl ='" + url + "';");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            while (rs.next()) {
                //int id = rs.getInt("id");
                int temp = rs.getInt("active");
                if (temp == 2) {
                    rs.close();
                    pstmt.close();
                    return temp;
                } else if (temp == 1) {
                    isEndpoint = temp;
                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception ex) {

        }
        return isEndpoint;
    }

    private String getSeedUrlFromId(int id) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("select id,endpointUrl from endpoints where id =" + String.valueOf(id) + ";");
        // execute the query, and get a java resultset
        ResultSet rs = pstmt.executeQuery();;

        // iterate through the java resultset
        while (rs.next()) {
            //int id = rs.getInt("id");
            String url = rs.getString("endpointUrl");
            rs.close();
            pstmt.close();
            return url;
        }
        rs.close();
        pstmt.close();
        return "";
    }

    private void updateSeedUrlFromId(int id, int isEndpointResult) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE endpoints set lastCheckedDate=?, active=active+?,totalcheck=totalcheck+1 where id =?;");

        pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt.setInt(2, isEndpointResult);
        pstmt.setInt(3, id);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }

    private void updateMarkAsProcessStartedFromId(int id) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE endpoints set checkFlag=1 where id =?;");

        pstmt.setInt(1, id);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }

    private void updateSeedUrl(String url, int isEndpointResult) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE endpoints set lastCheckedDate=?, active=active+?, totalcheck=totalcheck+1 where endpointUrl =?;");

        pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt.setInt(2, isEndpointResult);
        pstmt.setString(3, url);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }

    private boolean getNextUrlAndAnalyze() throws AbstractMethodError, SQLException, InterruptedException {

        int urlid = getUnprocessedSeedUrlId();
        if (urlid == 0) {
            return false;
        }
        updateMarkAsProcessStartedFromId(urlid);//update for process start (prevents other threads to use this id) 
        String url = getSeedUrlFromId(urlid);
//        int isKnownEndpoint = isKnownEndpoint(url);
//        if (isKnownEndpoint > 0) {
//            updateSeedUrlFromId(urlid, isKnownEndpoint); //update for sparql endpoint
////            urlid = getUnprocessedSeedUrlId();
////            url = getSeedUrlFromId(urlid);
//        } else {
//            Random r = new Random();
//            int randomSleepTime = r.nextInt(5000) + 200;//minimum 200 ms max 2200 ms
//            Thread.sleep(randomSleepTime);
        JenaSparql.queryTimeOut = 120000;
        if (JenaSparql.isSparqlEndpoint(url)) {
            //  updateSeedUrlFromId(urlid, 1); //update for sparql endpoint
            updateSeedUrl(url, 1);
//                urlid = getUnprocessedSeedUrlId();
//                url = getSeedUrlFromId(urlid);
        } else {
            //updateSeedUrlFromId(urlid, 0);// update for non-endpoint
            updateSeedUrl(url, 0);
//                urlid = getUnprocessedSeedUrlId();
//                url = getSeedUrlFromId(urlid);
        }
//        }
        return true;
    }

    private void initializeJdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            String a = ex.getMessage();
        }
    }
}
