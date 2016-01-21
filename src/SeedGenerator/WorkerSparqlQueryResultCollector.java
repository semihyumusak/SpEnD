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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 *
 * @author Semih
 */
public class WorkerSparqlQueryResultCollector extends Thread {

    private static final int maxResults = 10000;
    private EventQueue eventQueue = null;
    private boolean running = true;
    private int count = 0;
    int threadNumber;
    int timeout, checkHours;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;
    private int PAGING = 1000;
    private String query;
    private String defaultPredicate;
    private int queryId;
    // The component which will receive the event.
    private java.awt.Component target = null;
    

    /**
     * The event destination is target !
     */
    WorkerSparqlQueryResultCollector(java.awt.Component target, int timeout, String connectionString, int paging, String queryDescription) {
        this.target = target;
        this.checkHours = checkHours;
        this.timeout = timeout;
        this.threadNumber = threadNumber;
        this.connectionUrl = connectionString;
        this.PAGING = paging;
        initializeJdbc();
        initializeSparqlQuery(queryDescription);
    }

    private void initializeSparqlQuery(String queryDescription) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select id, sparqlQuery,predicate from commonsparqlquery where description=?;");
            pstmt.setString(1, queryDescription);
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.query = rs.getString("sparqlQuery");
                this.defaultPredicate = rs.getString("predicate");
                this.queryId = rs.getInt("id");
            }
        } catch (Exception ex) {
            System.out.println("sparql query alınamadı: " + queryDescription);
        }
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
                = con.prepareStatement("select id,endpointUrl,lastCheckedDate from endpoints where lastCheckedDate is null limit 1");// or lastCheckedDate < DATE_ADD(now(), INTERVAL -" + this.checkHours + " HOUR ) order by lastCheckedDate asc 
//        PreparedStatement pstmt
//                = con.prepareStatement("select id,endpointUrl,lastCheckedDate from endpoints where id =31");
        // execute the query, and get a java resultset
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
    HashSet previousTriplesSet = new HashSet();

    private void initPreviousUrlHashSet(int urlid, int queryId) throws SQLException, NumberFormatException {
        previousTriplesSet.clear();
        previousTriplesSet = new HashSet();
        String SQL = "SELECT endpointid,queryid,s,p,o FROM endpointtriples where endpointid=? and queryid=?;";
        PreparedStatement pstmt = con.prepareStatement(SQL);
        pstmt.setInt(1, urlid);
        pstmt.setInt(2, queryId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String hash = String.valueOf(rs.getInt("endpointid"))+String.valueOf(rs.getInt("queryid"));
            if (rs.getString("s") != null) {
                hash += rs.getString("s");
            }
            if (rs.getString("p") != null) {
                hash += rs.getString("p");
            }
            if (rs.getString("o") != null) {
                hash += rs.getString("o");
            }
            previousTriplesSet.add(hash.hashCode());
        }
        rs.close();
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
        initPreviousUrlHashSet(urlid,queryId);
        JenaSparql.queryTimeOut = timeout;

        Boolean hasNextSparqlResult = true;
        // iterate through the java resultset
        int OFFSET = 0;
        //int id = rs.getInt("id");
        while (hasNextSparqlResult && maxResults>OFFSET) {
            //String columnName = rs.getString("endpointsColumnName");
            try {
                String xmlResult = JenaSparql.getSparqlXMLResult(url, this.query, OFFSET, PAGING);
                OFFSET += PAGING;
                if (xmlResult != null) {
                    DocumentBuilderFactory dbf
                            = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(xmlResult));
                    Document doc = db.parse(is);
                    boolean bool = true;
                    Node n = doc.getFirstChild();
                    NodeList nl = doc.getElementsByTagName("result");
                    if (nl.getLength() < 1000) {
                        hasNextSparqlResult = false;
                    }
                    if (nl.getLength() > 0) {
                        Node firstnode = doc.getFirstChild();
                        firstnode.getNextSibling();

                        NodeList nList = doc.getElementsByTagName("result");

                        for (int i = 0; i < nList.getLength(); i++) {

                            Node nNode = nList.item(i);

                           // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element eElement = (Element) nNode;
                                int spoCount = eElement.getElementsByTagName("binding").getLength();
                                String s = null, p = null, o = null, hash = String.valueOf(urlid)+String.valueOf(queryId);

                                for (int j = 0; j < spoCount; j++) {
                                    Element spoElement = (Element) eElement.getElementsByTagName("binding").item(j);
                                    String spo = spoElement.getAttributes().getNamedItem("name").getTextContent();
                                    switch (spo) {
                                        case "s":
                                            s = spoElement.getFirstChild().getNextSibling().getTextContent();// getElementsByTagName("uri").item(0).getTextContent().trim();
                                            break;
                                        case "p":
                                            p = spoElement.getFirstChild().getNextSibling().getTextContent();
                                     
                                            break;
                                        case "o":
                                            o = spoElement.getFirstChild().getNextSibling().getTextContent();
                                         
                                            break;
                                    }
                                }
                                if (s != null) {
                                    hash += s;
                                }
                                if (p == null) {
                                    p = defaultPredicate;
                                }
                                hash += p;
                                if (o != null) {
                                    hash += o;
                                }

//                                System.out.println("s : " + s);
//                                System.out.println("p : " + p);
//                                System.out.println("o : " + o);
//                                System.out.println("--------");
                                if (!previousTriplesSet.contains(hash.hashCode())) {
                                    previousTriplesSet.add(hash.hashCode());
                                    insertTripleForEndpoint(s, p, o, urlid);
                                } else {
//                                    System.out.println("-Zaten eklenmiş-");
                                }
                            }
                        }
                    } else {

                    }

//recursiveXmlParse(firstnode, urlid, false, columnName,source);
                } else {
                    hasNextSparqlResult = false;
                }
            } catch (Exception ex) {
                System.out.println("in while:" + ex.getMessage());
            }
        }

        return true;
    }
 public static String limit(String value, int length)
  {
    StringBuilder buf = new StringBuilder(value);
    if (buf.length() > length)
    {
      buf.setLength(length);
      buf.append("...");
    }

    return buf.toString();
  }
    private void insertTripleForEndpoint(String s, String p, String o, int endpointID) {
        try {
            int count = 0;
            String valuesString = "";
            String SQLi = "INSERT INTO endpointtriples (queryid,";
            if (s != null) {
                SQLi += "s,";
                count++;
                valuesString += "?,";//                valuesString += "'"+s + "',";
            }
            if (p != null) {
                SQLi += "p,";
                count++;
                valuesString += "?,";//                valuesString += "'"+p + "',";
            }
            if (o != null) {
                SQLi += "o,";
                count++;
                valuesString += "?,";//                 valuesString += "'"+o + "',";
            }
            SQLi+= "endpointid) VALUES ("+String.valueOf(queryId)+",";
            valuesString+=endpointID;
            
            SQLi += valuesString;
            SQLi += ");";
            //className,endpointID) VALUES (?,?)";
            PreparedStatement pstmt
                    = con.prepareStatement(SQLi);
           
            if (o != null) {
                pstmt.setString(count--, limit(o,245));
            }
            if (p != null) {
               pstmt.setString(count--, limit(p,245));
            }
            if (s != null) {
                pstmt.setString(count--, limit(s,245));
            }

            pstmt.executeUpdate();
            pstmt.close();
            
        } catch (Exception ex) {
            System.out.println("insert: " + ex.getMessage());
        }
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
                    while (rs.next()) {
                        sameasid = rs.getInt("id");
                    }
                    rs.close();
                    pstmt.close();
                }
                if (id != sameasid && sameasid != 0) {
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
