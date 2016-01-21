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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Semih
 */
public class WorkerAnalyze extends Thread {

    private EventQueue eventQueue = null;
    private boolean running = true;
    private int count = 0;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;
int threadNumber, numberOfThreads;
    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerAnalyze(java.awt.Component target, String connectionString, int threadNumber, int numberOfThreads) {
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
        System.out.println("WorkerAnalyzer.run : Thread "
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
                = con.prepareStatement("select id,url from seedurlraw where isEndpoint is null and MOD(id,?)=? order by id asc;");
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
//    private int getUnprocessedSeedUrlId() throws SQLException {
//        PreparedStatement pstmt
//                = con.prepareStatement("select id,url from seedurlraw where isEndpoint is null order by id asc;");
//        // execute the query, and get a java resultset
//        ResultSet rs = pstmt.executeQuery();;
//
//        // iterate through the java resultset
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            rs.close();
//            pstmt.close();
//            return id;
//            //String firstName = rs.getString("url");
//        }
//        rs.close();
//        pstmt.close();
//        return 0;
//    }

    private int isKnownEndpoint(String url) throws SQLException {

        if (url.substring(url.length() - 1, url.length()).equals("/")) {
            url = url.substring(0, url.length() - 1);
        }
        int isEndpoint = 0;
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select endpointUrl from endpoints where endpointUrl ='" + url + "';");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            while (rs.next()) {
                //int id = rs.getInt("id");
                return 2;
            }
            rs.close();
            pstmt.close();
        } catch (Exception ex) {

        }

        //WHERE 'John Smith and Peter Johnson are best friends' LIKE
        //CONCAT('%', name, '%')
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select id,url,isEndpoint from seedurlraw where url ='" + url + "';");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            if (rs.next()) {
                //int id = rs.getInt("id");
                int temp = rs.getInt("isEndpoint");
                if (temp == 2) {
                    rs.close();
                    pstmt.close();
                    return temp;
                }
                //burayı sildim çünkü endpoint olsa da false işaretlediği yerler olabiliyor. 
//                } else if (temp == 1) {
//                    isEndpoint = temp;
//                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception ex) {

        }
        return isEndpoint;
    }

    private String getSeedUrlFromId(int id) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("select id,url from seedurlraw where id =" + String.valueOf(id) + ";");
        // execute the query, and get a java resultset
        ResultSet rs = pstmt.executeQuery();;

        // iterate through the java resultset
        while (rs.next()) {
            //int id = rs.getInt("id");
            String url = rs.getString("url");
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
                = con.prepareStatement("UPDATE seedurlraw set isEndpoint=? where id =?;");

        pstmt.setInt(1, isEndpointResult);
        pstmt.setInt(2, id);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }
    private void updateSeedUrlFromId(int id, int isEndpointResult, String url) throws SQLException {

        PreparedStatement pstmt
                = con.prepareStatement("UPDATE seedurlraw set isEndpoint=?, url = ? where id =?;");

        pstmt.setInt(1, isEndpointResult);
        pstmt.setString(2, url);
        pstmt.setInt(3, id);
// execute the query, and get a java resultset
        pstmt.executeUpdate();

        pstmt.close();

    }

    public static String extractUrl(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        String urlRegex2 = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
        String urlRegex3 = "_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?$_iuS";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        if ((urlMatcher.find())) {
            String url = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
            if (url.endsWith(".") || url.endsWith(";") || url.endsWith(":") || url.endsWith(".") || url.endsWith(",") ) {
                url = url.substring(0, url.length() - 1);
            }
            if (url.contains("<")) {
                url = url.split("<")[0];
            }

            return url;
        }
        return text;
    }
    public static String extractUrlRemoveEndingDash(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        String urlRegex2 = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
        String urlRegex3 = "_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?$_iuS";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        if ((urlMatcher.find())) {
            String url = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
            if (url.endsWith(".") || url.endsWith(";") || url.endsWith(":") || url.endsWith(".") || url.endsWith(",") || url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            if (url.contains("<")) {
                url = url.split("<")[0];
            }

            return url;
        }
        return text;
    }

    private boolean getNextUrlAndAnalyze() throws AbstractMethodError, SQLException, InterruptedException {

        int urlid = getUnprocessedSeedUrlId();
        if (urlid == 0) {
            return false;
        }
        updateSeedUrlFromId(urlid, 0);//update for process start (prevents other threads to use this id) 
        String url = getSeedUrlFromId(urlid);

        int isKnownEndpoint = isKnownEndpoint(url);
        if (isKnownEndpoint > 0) {
            updateSeedUrlFromId(urlid, isKnownEndpoint); //update for sparql endpoint
//            urlid = getUnprocessedSeedUrlId();
//            url = getSeedUrlFromId(urlid);
        } else {
//            Random r = new Random();
//            int randomSleepTime = r.nextInt(5000) + 200;//minimum 200 ms max 2200 ms
//            Thread.sleep(randomSleepTime);
            if (JenaSparql.isSparqlEndpoint(url)) {
                updateSeedUrlFromId(urlid, 2); //update for sparql endpoint
//                urlid = getUnprocessedSeedUrlId();
//                url = getSeedUrlFromId(urlid);
            } else {

                String tempurl = extractUrl(url);
                if (!tempurl.equals(url)) {
                    if (JenaSparql.isSparqlEndpoint(tempurl)) {
                        
                        updateSeedUrlFromId(urlid, 2,extractUrlRemoveEndingDash(tempurl));
                    } else {
                        updateSeedUrlFromId(urlid, 1,tempurl);// update for non-endpoint                    
                    }
                } else {
                    updateSeedUrlFromId(urlid, 1);
                }
//                urlid = getUnprocessedSeedUrlId();
//                url = getSeedUrlFromId(urlid);
            }
        }
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
