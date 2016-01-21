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
import java.io.StringReader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Semih
 */
public class WorkerStatisticalAnalysis extends Thread {

    private EventQueue eventQueue = null;
    private boolean running = true;
    private int count = 0;
    int threadNumber;
    int numberOfThreads = 0;
    int timeout, checkHours;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;

    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerStatisticalAnalysis(java.awt.Component target, int timeout, int checkHours, String connectionString, int threadNumber, int numberOfThreads) {
        this.target = target;
        this.checkHours = checkHours;
        this.timeout = timeout;
        this.threadNumber = threadNumber;
        this.connectionUrl = connectionString;
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
        System.out.println("WorkerStatisticalAnalyzer.run : Thread "
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
                    Thread.sleep(10000);//stopRunning();
                    //Thread.sleep(1000);
                } else {

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
                = con.prepareStatement("select id,endpointUrl,lastCheckedDate from endpoints where triples is null and lastCheckedDate is null and MOD(id,?)=? order by lastCheckedDate asc limit 1");
//        PreparedStatement pstmt
//                = con.prepareStatement("select id,endpointUrl,lastCheckedDate from endpoints where (lastCheckedDate is null or lastCheckedDate < DATE_ADD(now(), INTERVAL -" + this.checkHours + " HOUR )) and MOD(id,?)=? order by lastCheckedDate asc limit 1");
        // execute the query, and get a java resultset
        pstmt.setInt(2, threadNumber);
        pstmt.setInt(1, numberOfThreads);
        ResultSet rs = pstmt.executeQuery();;

        // iterate through the java resultset
        while (rs.next()) {
            int id = rs.getInt("id");
            rs.close();
            pstmt.close();
            return id;

        }
        rs.close();
        pstmt.close();
        return 0;
    }

    private String getSeedUrlFromId(int id) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("select id,endpointUrl,source from endpoints where id =" + String.valueOf(id) + " and threadNumber=" + String.valueOf(threadNumber) + ";");
        // execute the query, and get a java resultset
        ResultSet rs = pstmt.executeQuery();;

        // iterate through the java resultset
        while (rs.next()) {
            //int id = rs.getInt("id");
            String url = rs.getString("endpointUrl");
            String source = rs.getString("source");
            rs.close();
            pstmt.close();
            return url + "semihsplit" + source;
        }
        rs.close();
        pstmt.close();
        return "";
    }

    private void updateSeedUrlFromId(int id, int isEndpointResult) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE endpoints set lastCheckedDate=?, active=? where id =?;");

        pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt.setInt(2, isEndpointResult);
        pstmt.setInt(3, id);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }

    private void updateMarkAsProcessStartedFromId(int id) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE endpoints set lastCheckedDate=? ,threadNumber=? where id =?;");
        pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        pstmt.setInt(2, threadNumber);
        pstmt.setInt(3, id);
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
        String temp = getSeedUrlFromId(urlid);
        String url = temp.split("semihsplit")[0];
        String source = temp.split("semihsplit")[1];

        JenaSparql.queryTimeOut = timeout;

        PreparedStatement pstmt
                = con.prepareStatement("select sparqlQuery,endpointsColumnName from commonsparqlquery where enabled=1;");
        // execute the query, and get a java resultset
        ResultSet rs = pstmt.executeQuery();;

        // iterate through the java resultset
        while (rs.next()) {
            //int id = rs.getInt("id");
            String query = rs.getString("sparqlQuery");
            String columnName = rs.getString("endpointsColumnName");
            String s;
            try {
                s = JenaSparql.getSparqlXMLResult(url, query);
                if (s != null && s.length() < 500) {
                    DocumentBuilderFactory dbf
                            = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(s));
                    Document doc = db.parse(is);
                    boolean bool = true;
                    Node n = doc.getFirstChild();
                    Node firstnode = doc.getFirstChild();
                    recursiveXmlParse(firstnode, urlid, false, columnName, source);

                }
            } catch (Exception ex) {
                System.out.println("in while:" + ex.getMessage());
            }
        }
        rs.close();
        pstmt.close();

        return true;
    }

    private void recursiveXmlParse(Node node, int id, boolean found, String columnName, String source) throws DOMException, SQLException {
        NodeList nodeList = node.getChildNodes();
        if (node.getNodeName().equals("binding")) {
            found = true;
        } else if (node.getNodeName().equals("literal") && found) {
            String value = node.getTextContent();
            if (value.matches("\\d+")) {
                //
                int sameasid = 0;
                if (Integer.parseInt(value) != 0 && columnName.equals("triples")) {
                    String SQL = "select id,endpointUrl,length(endpointUrl),triples from endpoints where triples=? and source=? order by length(endpointUrl) asc limit 1";
                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setInt(1, Integer.parseInt(value));
                    pstmt.setString(2, source);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        sameasid = rs.getInt("id");
                    }
                    rs.close();
                    pstmt.close();
                }
                //  if (id != sameasid && sameasid != 0) {
                if (sameasid != 0) {
                    String SQL = "UPDATE endpoints SET " + columnName + " =? ," + columnName + "LCD=?, sameAs=? where id=" + id + ";";
                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setInt(1, Integer.parseInt(value));
                    pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                    pstmt.setInt(3, sameasid);
                    pstmt.executeUpdate();
                    pstmt.close();
                } else {
                    String SQL = "UPDATE endpoints SET " + columnName + " =? ," + columnName + "LCD=? where id=" + id + ";";
                    PreparedStatement pstmt = con.prepareStatement(SQL);
                    pstmt.setInt(1, Integer.parseInt(value));
                    pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            try {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    recursiveXmlParse(currentNode, id, found, columnName, source);
                }
            } catch (Exception ex) {
                System.out.println("recursive xml parse:" + ex.getMessage());
            }
        }
//return result;
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
