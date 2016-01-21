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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Semih
 */
public class WorkerSearch extends Thread {

    public static int MAXWORK = 10;
    private int crawlId;
    private int maxPage;
    private String query;
    private EventQueue eventQueue = null;

    private boolean running = true;
    private int count = 0;
    private SearchEngine searchEngine;
    String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    Connection con;
    // The component which will receive the event.
    private java.awt.Component target = null;

    /**
     * The event destination is target !
     */
    WorkerSearch(java.awt.Component target, String se, int crawlId, String searchText) {
        this.target = target;
        this.searchEngine = getSearchEngineFromName(se);
        this.maxPage = ((MainForm) target).maxPage;
        this.query = searchText;
        this.crawlId = crawlId;

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
        System.out.println("SimpleWorker.run : Thread "
                + Thread.currentThread().getName() + " started");

        eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();

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
        search(searchEngine);
        target.dispatchEvent(new EventSearchWorker(target, "", count, this.searchEngine.getName() + " completed.."));

//        eventQueue.postEvent(new EventSearchWorker(target, "Work done..", count));
        System.out.println("SimpleWorker.run : Thread "
                + Thread.currentThread().getName() + " stoped");
    }                       // run

    private HtmlPage getPage(String url) throws IOException {
        final WebClient webClient = new WebClient(getBrowserVersionFromName(searchEngine.getDefaultBrowser()));//BrowserVersion.FIREFOX_24);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient.getPage(url);
    }

    private void search(SearchEngine se) {
        try {

            initializeJdbc();
            final WebClient webClient = new WebClient(getBrowserVersionFromName(se.getDefaultBrowser()));//BrowserVersion.FIREFOX_24);
            // webClient.getOptions().setThrowExceptionOnScriptError(false);
            String htmlSource;
            HtmlPage searchResultPage = null;
//            String baseUrl = url;

            int numberOfUrlsExtracted;
            searchResultPage = clickSearchButtonMainPage(webClient, se);
            htmlSource = searchResultPage.getWebResponse().getContentAsString();

            numberOfUrlsExtracted = ExtractAndInsertSeedUrls(searchResultPage, htmlSource, 1, 0, se.getName());
            target.dispatchEvent(new EventSearchWorker(target, 1));

            try {
                for (int i = 1; i < maxPage; i++) {
                    HtmlAnchor ha = (HtmlAnchor) getNextButtonOrLink(searchResultPage, se.getNextButtonIdentifier());

                    searchResultPage = ha.click();
                    htmlSource = searchResultPage.getWebResponse().getContentAsString();
                    numberOfUrlsExtracted = ExtractAndInsertSeedUrls(searchResultPage, htmlSource, i + 1, numberOfUrlsExtracted, se.getName());
                    // nextUrl = getNextUrl(b, se, baseUrl);
                    target.dispatchEvent(new EventSearchWorker(target, 1));

                    Random r = new Random();
                    int randomSleepTime = r.nextInt(2000) + 200;//minimum 200 ms max 2200 ms
                    Thread.sleep(randomSleepTime);
                }
                // fw.close();
            } catch (Exception ex) {
                String x = ex.getMessage();
            }
            webClient.closeAllWindows();
        } catch (Exception ex) {
            String x = ex.getMessage();
        }

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

    private HtmlPage clickSearchButtonMainPage(final WebClient webClient, SearchEngine se) {

        String url = se.getBaseUrl();
        String queryTextboxName = se.getQueryTextBoxName();
        String searchButtonId = se.getSubmitButtonId();
        String searchButtonName = se.getSubmitButtonName();
        Object tempSubmitFromName, tempSubmitFromId, tempSubmitFromTagName;

        try {
            HtmlPage page1 = webClient.getPage(url);
            HtmlInput input1 = page1.getElementByName(queryTextboxName);
            input1.setValueAttribute(query);
            tempSubmitFromName = page1.getElementByName(searchButtonName);
            return clickButtonReturnPage(tempSubmitFromName);
//                baseUrl = page1.getUrl().toString();
        } catch (Exception ex) {
            String aaa = "deneme";
        }
        try {
            HtmlPage page1 = webClient.getPage(url);
            HtmlInput input1 = page1.getElementByName(queryTextboxName);
            input1.setValueAttribute(query);
            tempSubmitFromId = page1.getElementById(searchButtonId);
            return clickButtonReturnPage(tempSubmitFromId);
            //               baseUrl = page1.getUrl().toString();
        } catch (Exception ex2) {
            String aaa = "deneme";
        }
        try {
            HtmlPage page1 = webClient.getPage(url);
            HtmlInput input1 = page1.getElementByName(queryTextboxName);
            input1.setValueAttribute(query);
            tempSubmitFromTagName = page1.getElementsByTagName("button").get(0);
            return clickButtonReturnPage(tempSubmitFromTagName);
//                baseUrl = page1.getUrl().toString();
        } catch (Exception ex2) {
            String aaa = "deneme";
        }
        return null;
    }

    private HtmlPage clickButtonReturnPage(Object tempsubmit) throws IOException {
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

    private int ExtractAndInsertSeedUrls(HtmlPage page, String text, int pageNumber, int prevExtractedUrlCount, String searchEngineName) {

        int pageContentId = insertPageContent(text, pageNumber, searchEngineName, crawlId);

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
                        insertSeedLink(url, searchEngineName, prevExtractedUrlCount + count, pageContentId);
                        previousUrls.add(url);
                        target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
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
                            insertSeedLink(url, searchEngineName, prevExtractedUrlCount + count, pageContentId);
                            previousUrls.add(url);
                            target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
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

        int pageContentId = insertPageContent(text, pageNumber, searchEngineName, crawlId);

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
                        insertSeedLink(url, searchEngineName, prevExtractedUrlCount + count, pageContentId);
                        previousUrls.add(url);
                        target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
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
                        insertSeedLink(url, searchEngineName, prevExtractedUrlCount + count, pageContentId);
                        previousUrls.add(url);
                        target.dispatchEvent(new EventSearchWorker(target, searchEngineName + ":" + url + "\n", count, ""));
//           
                    }
                }
            } catch (Exception ex) {

            }
        }

        return count;

    }

    private void insertSeedLink(String url, String searchEngineName, int resultOrder, int pageContentId) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("INSERT INTO seedurlraw (url, searchEngine,resultOrder,pageContentId) VALUES (?,?,?,?);");
            pstmt.setString(1, url);
            pstmt.setString(2, searchEngineName);
            pstmt.setString(3, String.valueOf(resultOrder));
            pstmt.setInt(4, pageContentId);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception ex) {
            String a = "";
            String b = "";

        }

    }

    private int insertPageContent(String text, int resultPageNumber, String searchEngineName, int crawlRecordId) {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("INSERT INTO pagecontent (htmlcontent, resultPageNumber, searchEngineName, crawlRecordId,queryText) VALUES (?,?,?,?,?);",
                            Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, text);
            pstmt.setInt(2, resultPageNumber);
            pstmt.setString(3, searchEngineName);
            pstmt.setInt(4, crawlRecordId);
            pstmt.setString(5, query);

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
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchEngines.class);

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
