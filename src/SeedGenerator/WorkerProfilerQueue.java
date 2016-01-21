/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.google.common.net.InternetDomainName;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Semih
 */
public class WorkerProfilerQueue extends Thread {

    private int failIncrementalSleep = 0;
    public static int MAXWORK = 10;
    private int crawlId;
    private int maxPage;
    // private String query;
    private EventQueue eventQueue = null;
    private int MAXSLEEP = 3000;
    private boolean running = true;
    private int count = 0;
    private SearchEngine searchEngine;
    String connectionUrl = "jdbc:mysql://localhost/profiler?" + "user=root&password=62217769";
    Connection con;
    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerProfilerQueue(int crawlId) {
      //  this.target = target;
        // this.searchEngine = getSearchEngineFromName(se);
        //    this.maxPage = ((MainForm) target).maxPage;
        // this.query = searchText;
        //   this.MAXSLEEP = this.searchEngine.waitIntervalMs;
        this.crawlId = crawlId;
        // this.connectionUrl = connectionString;
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
    private void initQueue() {

    }

    public void run() {
        initializeJdbc();
        int percent = 0;
        String msg = "";
        System.out.println("SimpleWorker.run : Thread "
                + Thread.currentThread().getName() + " started");

   //     eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();

//        while (running) {
//            count++;
//            if (count >= MAXWORK) {
//                running = false;
//            }
//
//            msg = "Message from " + Thread.currentThread().getName()
//                    + " : " + count;
//
//            eventQueue.postEvent(new EventSearchWorker(target, msg, count));
//
//            try {
//                this.sleep(800);
//            } catch (Exception e) {
//                System.out.println("E:run : " + e.toString());
//            }
//        }
//        eventQueue.postEvent(new EventSearchWorker(target, "deneme\n", count));
        // final WebClient webClient = new WebClient();//BrowserVersion.FIREFOX_24);
        while (true) {
//            try {
                //  if (hasSearchQuery()) {
                //Thread.sleep(10);
                
                search();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(WorkerProfilerQueue.class.getName()).log(Level.SEVERE, null, ex);
//            }
              //  System.gc();
//            } else {
//                try {
//                    Thread.sleep(10000);
//                } catch (Exception ex) {
//                }
//            }
        }
        //   webClient.closeAllWindows();

     //   target.dispatchEvent(new EventSearchWorker(target, "", count, this.searchEngine.getName() + " completed.."));

//        eventQueue.postEvent(new EventSearchWorker(target, "Work done..", count));
//        System.out.println("SimpleWorker.run : Thread "
//                + Thread.currentThread().getName() + " stoped");
    }                       // run

    private boolean hasSearchQuery() {
        try {
            String SQL = "SELECT idprofilerurl,url FROM profilerurl where processed is null LIMIT 1;";
            PreparedStatement selectstmt = con.prepareStatement(SQL);
            ResultSet rs = selectstmt.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    class searchEngineQuery {

        public searchEngineQuery(int maxPage, String queryText) {
            this.maxPage = maxPage;
            this.queryText = queryText;
        }
        int maxPage;
        String queryText;
    };

    private searchEngineQuery getNextSearchQuery() {
        try {
            String SQL = "SELECT id, searchText, maxSearchPage FROM searchqueue where disabled=0 and isProcessStarted is null and searchEngineName=? LIMIT 1;";
            PreparedStatement selectstmt = con.prepareStatement(SQL);
            selectstmt.setString(1, searchEngine.name);
            ResultSet rs = selectstmt.executeQuery();

            while (rs.next()) {
                try {
                    int id = rs.getInt(1);
                    String searchText = rs.getString(2);
                    int maxPage = rs.getInt(3);
                    if (maxPage == 0) {
                        maxPage = this.maxPage;
                    } else {
                        this.maxPage = maxPage;
                    }
                    PreparedStatement updateStmt
                            = con.prepareStatement("UPDATE searchqueue set isProcessStarted=?, processStartDate=? where id =?;");

                    updateStmt.setInt(1, 1);
                    updateStmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                    updateStmt.setInt(3, id);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                    return new searchEngineQuery(maxPage, searchText);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            selectstmt.close();
            rs.close();
            // return id + 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private HtmlPage getPage(String url) throws IOException {
        final WebClient webClient = new WebClient(getBrowserVersionFromName(searchEngine.getDefaultBrowser()));//BrowserVersion.FIREFOX_24);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient.getPage(url);
    }

    public String executePost(String targetURL) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

//            connection.setRequestProperty("Content-Length",
//                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
//            DataOutputStream wr = new DataOutputStream(
//                    connection.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.close();
            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void search() {

        try {
            //   System.out.println(searchEngine.name + " ilk aramaya girdi: " + (new Timestamp(System.currentTimeMillis())).toString());
            String url;
            try {
                String SQL = "SELECT idprofilerurl,url FROM profilerurl where category is null LIMIT 1;";
                PreparedStatement selectstmt = con.prepareStatement(SQL);
                ResultSet rs = selectstmt.executeQuery();

                while (rs.next()) {
                     String SQL12 = "UPDATE profilerurl set processed=? where idprofilerurl = ?;";
                    PreparedStatement selectstmt12 = con.prepareStatement(SQL12);
                    selectstmt12.setInt(1, 1);
                    selectstmt12.setInt(2, rs.getInt("idprofilerurl"));
                    selectstmt12.executeUpdate();
                    
                    url = rs.getString("url");
                    url = "http://85.111.3.86:9192/services/query/" + url;
                    String result = executePost(url);
                    //  result.replace('|','!').split("!")
                    String SQL1 = "UPDATE profilerurl set processed=?, category=? where idprofilerurl = ?;";
                    PreparedStatement selectstmt1 = con.prepareStatement(SQL1);
                    selectstmt1.setInt(1, 1);
                    try
                    {
                        selectstmt1.setString(2, result.replace('|', '!').split("!")[3]);
                    }
                    catch(Exception ex)
                    {
                        if(result == null)
                                                    selectstmt1.setString(2, "EXCEPTION");

else
                            selectstmt1.setString(2, result);
                    
                    }
                    selectstmt1.setInt(3, rs.getInt("idprofilerurl"));
                    selectstmt1.executeUpdate();

                    System.out.println(result);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            // webClient.closeAllWindows();
        } catch (Exception ex) {
            String x = ex.getMessage();
            try {
                System.out.println(" hataya girdi 60 saniye bekleyecek. "+x);
                Thread.sleep(60000);
            } catch (Exception eex) {
            }
        }

        //   webClient.closeAllWindows();
    }

    public Object getNextButtonOrLink(HtmlPage page, final String id) {
        return getAnchorByAnyIdentifyingSingleWord(page, id);
    }

    public HtmlAnchor getAnchorByAnyIdentifyingSingleWord(HtmlPage page, final String id) throws ElementNotFoundException {
        WebAssert.notNull("text", id);

        for (final HtmlAnchor anchor : page.getAnchors()) {

            //txtSeeds.append(anchor.asXml());
            //System.out.println(anchor.asXml());
            String anchortext = anchor.asXml();
            try {
                anchortext = URLDecoder.decode(anchortext, "ISO-8859-1");

//                System.out.println(anchor.asXml());
            } catch (Exception ex) {

            }
            if (anchortext.contains(id)) {
                return anchor;
            }
        }
        throw new ElementNotFoundException("a", "<text>", id);
    }

    public List<String> getAnchorLinks(HtmlPage page, final String id) throws ElementNotFoundException {
        WebAssert.notNull("text", id);

//         List<HtmlAnchor> anchorlist= new ArrayList<HtmlAnchor>();
//        for (final HtmlAnchor anchor : page.getAnchors()) {
//
//            //txtSeeds.append(anchor.asXml());
//            System.out.println(anchor.asXml());
//            String anchortext = anchor.asXml();
//            try {
//                anchortext = URLDecoder.decode(anchortext, "ISO-8859-1");
//
////                System.out.println(anchor.asXml());
//            } catch (Exception ex) {
//
//            }
//            if (anchortext.contains("url")) {
//                anchorlist.add(anchor);
//            }
//        }
//        return anchorlist;
        //throw new ElementNotFoundException("a", "<text>", id);
        WebAssert.notNull("text", id);
        List<String> urls = new ArrayList<String>();
        for (final HtmlAnchor anchor : page.getAnchors()) {

            //txtSeeds.append(anchor.asXml());
            String anchortext = anchor.asXml();
            if (anchortext.contains(id)) {
                //  System.out.println(anchor.asXml());

                try {
                    anchortext = URLDecoder.decode(anchortext, "ISO-8859-1");
                    int start = 0;

                    while (start != -1) {
                        start = anchortext.indexOf("href=\"http", start);
                        int end = anchortext.indexOf('"', start + 10);

                        if (start != -1) {
                            urls.add(anchortext.substring(start + 6, end));
                            start = end;
//                System.out.println(anchor.asXml());
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        return urls;
        //throw new ElementNotFoundException("a", "<text>", id);
    }

    private HtmlPage clickSearchButtonMainPage(final WebClient webClient, SearchEngine se, String query) {

        String url = se.getBaseUrl();
        String queryTextboxName = se.getQueryTextBoxName();
        String searchButtonId = se.getSubmitButtonId();
        String searchButtonName = se.getSubmitButtonName();
        Object tempSubmitFromName, tempSubmitFromId, tempSubmitFromTagName;
        try {
//            Random r = new Random();
//            int randomSleepTime = r.nextInt(MAXSLEEP) + 2000;//minimum 200 ms max 2200 ms
//            Thread.sleep(randomSleepTime);

            HtmlPage page1 = webClient.getPage(url);

            try {
                HtmlInput input1 = page1.getElementByName(queryTextboxName);
                input1.setValueAttribute(query);
                tempSubmitFromName = page1.getElementByName(searchButtonName);
                return clickButtonReturnPage(tempSubmitFromName);
//                baseUrl = page1.getUrl().toString();
            } catch (Exception ex) {
                String aaa = "deneme";
            }
            try {
                HtmlInput input1 = page1.getElementByName(queryTextboxName);
                input1.setValueAttribute(query);
                tempSubmitFromId = page1.getElementById(searchButtonId);
                return clickButtonReturnPage(tempSubmitFromId);
                //               baseUrl = page1.getUrl().toString();
            } catch (Exception ex2) {
                String aaa = "deneme";
            }
            try {
                HtmlInput input1 = page1.getElementByName(queryTextboxName);
                input1.setValueAttribute(query);
                tempSubmitFromTagName = page1.getElementsByTagName("button").get(0);
                return clickButtonReturnPage(tempSubmitFromTagName);
//                baseUrl = page1.getUrl().toString();
            } catch (Exception ex2) {
                String aaa = "deneme";
            }
        } catch (Exception ex) {

        }
        return null;
    }

    private HtmlPage clickButtonReturnPage(Object tempsubmit) throws IOException {

        try {

            Thread.sleep(failIncrementalSleep);

            HtmlSubmitInput htmlsubmit;
            HtmlButton htmlbutton;
            //   a = "";
            if (tempsubmit.getClass().getName().contains("HtmlButton")) {
                htmlbutton = (HtmlButton) tempsubmit;
                return htmlbutton.click();//.getWebResponse().getContentAsString();
            } else if (tempsubmit.getClass().getName().contains("HtmlSubmitInput")) {
                htmlsubmit = (HtmlSubmitInput) tempsubmit;
                return htmlsubmit.click();//.getWebResponse().getContentAsString();
            }

        } catch (FailingHttpStatusCodeException fex) {
            if (failIncrementalSleep == 0) {
                failIncrementalSleep = 600000;//5 dakika
            } else {
                failIncrementalSleep += 600000;
            }
            System.out.println(searchEngine.name + " " + String.valueOf(failIncrementalSleep / 1000 / 60) + " Dakika bekleyecek. Hata Detay: " + fex.getMessage().substring(0, 50));
            //stopRunning();
        } catch (Exception ex) {
            System.out.println(searchEngine.name + " button click hata verdi. Detay: " + ex.getMessage().substring(0, 50));
        }
        return null;

    }

    private void initializeJdbc() {
        try {
            Class.forName("com.mysql.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            String a = ex.getMessage();
        }
    }

    private int returnMinPositiveInteger(int x1, int x2) {
        if (x1 != -1 && x2 != -1) {
            if (x1 > x2) {
                return x2;
            } else {
                return x1;
            }
        } else if (x1 == -1) {
            return x2;
        } else if (x2 == -1) {
            return x1;
        }
        return 0;
    }

    private int ExtractAndInsertSeedUrls(HtmlPage page, String text, int pageNumber, int prevExtractedUrlCount, String searchEngineName, String queryText) {

        int pageContentId = insertPageContent(text, pageNumber, searchEngineName, crawlId, queryText);

        HashSet<String> previousUrls = new HashSet<String>();

        int count = 0;
        int urlStartIndex = 0;

        List<String> links = getAnchorLinks(page, "url");

        for (String url : links) {
            try {
                if (!isExcludedFileType(url, searchEngineName)) {
                    if (getSearchEngineFromName(searchEngineName).getUseUrlRedirection().equals("true") && url.contains("url")) {
                        HtmlPage p = getPage(url);
                        url = p.getUrl().toString();
                        Thread.sleep(200);
                    }
                    if (!includesExcludedKeyword(url, searchEngineName) && !previousUrls.contains(url)) {
                        String encodedurl = "";
                        try {
                            encodedurl = URLDecoder.decode(url, "ISO-8859-1");
                            url = encodedurl;
                        } catch (Exception ex) {

                        }
                        count++;
                        insertSeedLink(url, searchEngineName, queryText, prevExtractedUrlCount + count, pageContentId);
                        previousUrls.add(url);
                        //   target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
//           
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        while (urlStartIndex > -1) {
            // index = text.indexOf("href=\"/url?q=http", index + 1);
            int indexencoded = text.indexOf("http://", urlStartIndex + 1);
            int indexnotencoded = text.indexOf("http%3a%2f%2f", urlStartIndex + 1);
            urlStartIndex = returnMinPositiveInteger(indexencoded, indexnotencoded);

            int indexOfNextQuote = text.indexOf('"', urlStartIndex + 10);
            int indexOfNextCloseParanthesis = text.indexOf(')', urlStartIndex + 10);

            int urlEndIndex = returnMinPositiveInteger(indexOfNextCloseParanthesis, indexOfNextQuote);

            try {
                String rawurl = text.substring(urlStartIndex, urlEndIndex);
                if (urlStartIndex > -1) {
                    String url;
                    if (indexencoded == urlStartIndex) {
                        url = refineUrlString(rawurl, true);
                    } else {
                        url = refineUrlString(rawurl, false);
                    }

                    if (!isExcludedFileType(url, searchEngineName)) {

                        if (!includesExcludedKeyword(url, searchEngineName) && !previousUrls.contains(url)) {
                            String encodedurl = "";
                            try {
                                encodedurl = URLDecoder.decode(url, "ISO-8859-1");
                                url = encodedurl;
                            } catch (Exception ex) {

                            }
                            count++;
                            insertSeedLink(url, searchEngineName, queryText, prevExtractedUrlCount + count, pageContentId);
                            previousUrls.add(url);
                            // target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
//           
                        }
                    }
                }
            } catch (Exception ex) {

            }
        }

        return count;

    }

    private int ExtractAndInsertSeedUrlsOld(String text, int pageNumber, int prevExtractedUrlCount, String searchEngineName) {

        int pageContentId = insertPageContent(text, pageNumber, searchEngineName, crawlId, "");

        HashSet<String> previousUrls = new HashSet<String>();

        int count = 0;
        int index = 0;
        while (index > -1) {
            // index = text.indexOf("href=\"/url?q=http", index + 1);
            index = text.indexOf("http://", index + 1);
            int temp = text.indexOf('"', index + 10);

            int tempparant = text.indexOf(')', index + 10);

            try {
                String rawurl = text.substring(index, temp);

                if (index > -1) {
                    String url = refineUrlString(rawurl, true);
                    String encodedurl = "";

                    if (!includesExcludedKeyword(url, searchEngineName) && !previousUrls.contains(url)) {
                        count++;
                        // insertSeedLink(url, searchEngineName,queryText, prevExtractedUrlCount + count, pageContentId);
                        previousUrls.add(url);
                        //target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
                    }
                }
            } catch (Exception ex) {

            }
        }
        index = 0;
        while (index > -1) {
            // index = text.indexOf("href=\"/url?q=http", index + 1);
            index = text.indexOf("http%3a%2f%2f", index + 1);

            int temp = text.indexOf('"', index + 10);
            try {
                String rawurl = text.substring(index, temp);
                if (index > -1) {
                    String url = refineUrlString(rawurl, false);

                    boolean excludeMatch = includesExcludedKeyword(url, searchEngineName);
                    if (!excludeMatch && !previousUrls.contains(url)) {
                        String encodedurl = "";
                        try {
                            encodedurl = URLDecoder.decode(url, "ISO-8859-1");
                            url = encodedurl;
                        } catch (Exception ex) {

                        }
                        count++;
                        //insertSeedLink(url, searchEngineName, queryText, prevExtractedUrlCount + count, pageContentId);

                        previousUrls.add(url);

//           
                    }
                }
            } catch (Exception ex) {

            }
        }

        return count;

    }

    private void insertSeedLink(String url, String searchEngineName, String text, int resultOrder, int pageContentId) {
        try {
            URL u = new URL(url);
            String host = u.getHost();
            if (InternetDomainName.isValid(host) || com.google.common.net.HostSpecifier.isValid(host)) {

                //InternetDomainName.from(host).topPrivateDomain().toString();
                PreparedStatement pstmt
                        = con.prepareStatement("INSERT INTO seedurlraw (url, searchEngine,resultOrder,pageContentId) VALUES (?,?,?,?);");
                pstmt.setString(1, url);
                pstmt.setString(2, searchEngineName);
                pstmt.setString(3, String.valueOf(resultOrder));
                pstmt.setInt(4, pageContentId);

                pstmt.executeUpdate();
                pstmt.close();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Object[] row = {searchEngineName, text, url, dateFormat.format(new Date())};
                target.dispatchEvent(new EventSearchWorker(target, row, count, ""));

            }
        } catch (Exception ex) {
            String a = "";
            String b = "";

        }
    }

    private int insertPageContent(String text, int resultPageNumber, String searchEngineName, int crawlRecordId, String queryText) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("INSERT INTO pagecontent (htmlcontent, resultPageNumber, searchEngineName, crawlRecordId,queryText) VALUES (?,?,?,?,?);",
                            Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, text);
            pstmt.setInt(2, resultPageNumber);
            pstmt.setString(3, searchEngineName);
            pstmt.setInt(4, crawlRecordId);
            pstmt.setString(5, queryText);

            pstmt.executeUpdate();

            java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            pstmt.close();

        } catch (Exception ex) {
            String a = "";
            String b = "";

        }
        return 0;
    }

    private boolean includesExcludedKeyword(String url, String searchEngineName) {
        String excludeString = getSearchEngineFromName(searchEngineName).getExcludedWords();
        boolean excludeMatch = false;
        if (!excludeString.equals("")) {
            excludeMatch = url.matches(".*(" + excludeString + ").*");
        }
        if (!excludeMatch) {
            excludeMatch = url.matches(".*(.gif|.png|.jpg).*");
        }
        return excludeMatch;
    }

    private boolean isExcludedFileType(String url, String searchEngineName) {
        boolean excludeMatch = false;
        excludeMatch = url.matches(".*(.gif|.png|.jpg).*");
        return excludeMatch;
    }

    private SearchEngine getSearchEngineFromName(String enginename) {
        List<SearchEngine> selist = getSearchEnginesFromXml().getSearchEngines();

        String[] seNames = new String[selist.size()];
        int i = 0;
        for (SearchEngine se : selist) {
            if (se.getName().equals(enginename)) {
                return se;
            }
        }
        return null;
    }

    private SearchEngines getSearchEnginesFromXml() {
        try {
            File file = new File("SearchEngines.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchEngines.class
            );

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SearchEngines searchEngineList = (SearchEngines) jaxbUnmarshaller.unmarshal(file);
            return searchEngineList;
//		System.out.println(searchEngineList.getSearchEngines());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String refineUrlString(String rawurl, boolean isEncoded) {
        int endIndex = 0;

        if (isEncoded) {
            if (!rawurl.contains("?")) {
                int ampersandSign = rawurl.indexOf("&");
                int percentageSign = rawurl.indexOf("%");

                if (ampersandSign != -1 && percentageSign != -1) {
                    if (ampersandSign > percentageSign) {
                        endIndex = percentageSign;
                    } else if (ampersandSign < percentageSign) {
                        endIndex = ampersandSign;
                    }
                } else if (ampersandSign != -1) {
                    endIndex = ampersandSign;
                } else if (percentageSign != -1) {
                    endIndex = percentageSign;
                }

            } else {
                int percentageSign = rawurl.indexOf("%");
                if (percentageSign != -1) {
                    endIndex = percentageSign;
                }
            }
        } else {
            int slashSign = rawurl.indexOf("/");
            if (slashSign != -1) {
                endIndex = slashSign;
            }
        }
        if (endIndex != 0) {
            return rawurl.substring(0, endIndex);
        } else {
            return rawurl;
        }

    }

    private BrowserVersion getBrowserVersionFromName(String browsername) {
        switch (browsername) {
            case "chrome":
                return BrowserVersion.CHROME;
            case "googlechrome":
                return BrowserVersion.CHROME;
            case "google":
                return BrowserVersion.CHROME;
            case "firefox":
                return BrowserVersion.FIREFOX_24;
            case "mozilla":
                return BrowserVersion.FIREFOX_24;
            case "explorer":
                return BrowserVersion.INTERNET_EXPLORER_11;
            case "internetexplorer":
                return BrowserVersion.INTERNET_EXPLORER_11;
            default:
                return BrowserVersion.getDefault();

        }

    }

}
