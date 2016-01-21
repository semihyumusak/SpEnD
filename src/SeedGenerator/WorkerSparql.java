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
import com.hp.hpl.jena.query.ResultSetFormatter;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Semih
 */
public class WorkerSparql extends Thread {

    private EventQueue eventQueue = null;
    private boolean running = true;
    private int count = 0;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;

    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerSparql(java.awt.Component target,String connectionString) {
        this.connectionUrl= connectionString;
        initializeJdbc();
        this.target = target;
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

        System.out.println("SimpleWorker.run : Thread "
                + Thread.currentThread().getName() + Thread.currentThread().getId() + " started");

        eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();

        while (running) {
            try {
                if (!queryNext()) {
                    // stopRunning();
                    Thread.sleep(2000);
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception ex) {
            }
        }
        System.out.println("WorkerSparql.run : Thread "
                + Thread.currentThread().getName() + Thread.currentThread().getId() + " stopped");

//        target.dispatchEvent(new SimpleAWTEvent(target, "", count,this.ana+" completed.."));
//        eventQueue.postEvent(new SimpleAWTEvent(target, "Work done..", count));
//        System.out.println("SimpleWorker.run : Thread "
//                + Thread.currentThread().getName() + " stoped");
    }                       // run

    private boolean getNextUrlAndAnalyze() {

        return false;
    }

    private boolean queryNext() throws InterruptedException {
        int queryId = getUnprocessedQueryQueueId();

        if (queryId != 0) {
            processSparqlQuery(queryId);
            //processAliHocaQuery(queryId);
            System.gc();
            Thread.sleep(500);
            return true;
        }
        return false;
    }
    final WebClient webClient = new WebClient();//BrowserVersion.FIREFOX_24);

    private boolean processAliHocaQuery(int id) {
        try {
            webClient.getOptions().setJavaScriptEnabled(false);
            PreparedStatement pstmt
                    = con.prepareStatement("SELECT * FROM crawler.queryqueue where id ="+id+";");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            //int id = rs.getInt("id");
            String url = "";
            String query = "";
            if (rs.next()) {
                url = rs.getString("endpointUrl");
           //     query = rs.getString("sparqlQuery");
            }
            rs.close();
            pstmt.close();
            HtmlPage page = webClient.getPage(url);
            if(page.asXml().contains("intihal"))
            {
                updateQueueResult(id,"kotu");
            }
            else
                updateQueueResult(id, "iyi");
                

            return true;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            //  System.err.println(e.getMessage());
            updateQueueResult(id, "Sparql Query Exception");
        }
        return false;

    }

    private boolean processSparqlQuery(int id) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select queryqueue.endpointUrl as endpointUrl,commonsparqlquery.sparqlQuery as sparqlQuery  from queryqueue inner join commonsparqlquery on queryqueue.commonQueryId = commonsparqlquery.id where queryqueue.id=" + String.valueOf(id) + ";");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            //int id = rs.getInt("id");
            String url = "";
            String query = "";
            if (rs.next()) {
                url = rs.getString("endpointUrl");
                query = rs.getString("sparqlQuery");
            }
            rs.close();
            pstmt.close();

            String results = JenaSparql.getSparqlXMLResult(url, query);

            updateQueueResult(id, results);

            return true;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            //  System.err.println(e.getMessage());
            updateQueueResult(id, "Sparql Query Exception");
        }
        return false;
    }

    private String updateQueueStartingProcess(int id) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("UPDATE queryqueue set isProcessStarted=?, processStartDate=? where id =?;");

            pstmt.setInt(1, 1);
            pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);
// execute the query, and get a java resultset
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            String a = "1";
        }
        return "";
    }

    private String updateQueueResult(int id, String resultText) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("UPDATE queryqueue set resultSet=?, processEndDate=?, tryCount=tryCount+1 where id =?;");

            pstmt.setString(1, resultText);
            pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, id);
// execute the query, and get a java resultset
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {

        }
        return "";
    }

    private int getUnprocessedQueryQueueId() {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select id,endpointUrl from queryqueue where isProcessStarted is null or isProcessStarted=0 order by id desc;");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("id");
                updateQueueStartingProcess(id);
                pstmt.close();
                rs.close();
                return id;
                //String firstName = rs.getString("url");
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return 0;
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
