/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array;
//import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.google.common.net.InternetDomainName;
import com.hp.hpl.jena.n3.turtle.TurtleParseException;
import com.hp.hpl.jena.n3.turtle.TurtleReader;
import com.hp.hpl.jena.n3.turtle.parser.Token;
import com.hp.hpl.jena.n3.turtle.parser.TurtleParser;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import org.apache.james.mime4j.field.datetime.DateTime;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.net.URLDecoder;
import java.util.AbstractSet;
import java.util.Set;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JFrame;
//import sun.security.krb5.JavaxSecurityAuthKerberosAccess;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdfxml.xmlinput.JenaReader;

import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.tdb.sys.Session;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFBase;
import org.w3c.dom.DOMException;
//import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Semih
 */
public class MainForm extends JFrame implements ActionListener, WindowListener {

    /**
     * Creates new form MainForm
     */
    String connectionUrl;
    Connection con;
    Connection con1;
    Connection con2;
    long searchStartTime;
    long searchEndTime;
    int maxPage;
    int totalUrlCount = 0;
    Queue searchQueue = null;
    Thread[] threadsAnalysis;
    Thread[] threadsStatusMonitoring;
    Thread[] threadsStatisticalAnalysis;
    Thread[] threadsSearchQueue;
    Thread[] threadsSparql;
    int maxThreadAnalysis = 3;
    int maxThreadSparql = 20;

    public MainForm() {
        super("SeedGenerator");
        initComponents();
        initializeSearchEngineListBox();
        initializeJdbc();
        initMultiThreading();
        loadUrlTable();
        initJComboCommonQuery();
    }

    private void initJComboCommonQuery() {
        try {
            String SQL = "SELECT id,sparqlQuery,description FROM crawler.commonsparqlquery where description like '%collector';";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            jComboCommonQueries.removeAllItems();
            while (rs.next()) {
                jComboCommonQueries.addItem(rs.getString("description"));
            }
        } catch (Exception ex) {
        }// TODO add your handling code here:
    }
    public int wNr = 0;

    private void initMultiThreading() {

        addWindowListener(this);
        enableEvents(EventSearchWorker.EVENT_ID);  // enable my event's
        threadsAnalysis = new Thread[maxThreadAnalysis];
        threadsSearchQueue = new Thread[lstSearchEngines.getSelectedIndices().length];
        threadsSparql = new Thread[maxThreadSparql];

    }

    private void initializeJdbc() {
        try {
            //.newInstance();
            Class.forName("com.mysql.jdbc.Driver");
            connectionUrl = getConnectionString();
            con = DriverManager.getConnection(connectionUrl);
            con1 = DriverManager.getConnection(connectionUrl);
            con2 = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {
            String a = ex.getMessage();
        }
    }

    private String getConnectionString() {
        String fileString;
        try {
            FileInputStream inputStream = new FileInputStream("db.conf");
            String HostName = "", Port = "", Username = "", Password = "", Schema = "";
            fileString = IOUtils.toString(inputStream);
            for (String s : fileString.split("\n")) {
                try {
                    String label = s.split(" ")[0];
                    String text = s.split(" ")[1];

                    switch (label) {
                        case "Hostname":
                            HostName = text;
                            break;
                        case "Port":
                            Port = text;
                            break;
                        case "Username":
                            Username = text;
                            break;
                        case "Password":
                            Password = text;
                            break;
                        case "Schema":
                            Schema = text;
                            break;

                    }
                } catch (Exception ex) {

                }
            }
            inputStream.close();
            return "jdbc:mysql://" + HostName + ":" + Port + "/" + Schema + "?user=" + Username + "&password=" + Password;

        } catch (Exception ex) {
            return "jdbc:mysql://localhost/crawler?user=root&password=62217769";
        } finally {

        }
    }

    private void initializeSearchEngineListBox() {
        lstSearchEngines.setModel(new javax.swing.AbstractListModel() {

            String[] strings = getSearchEngineNamesArray();//{"Google", "Bing", "Yahoo", "Yandex"};

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        int size = lstSearchEngines.getModel().getSize();
        int[] i = new int[size];
        for (int a = 0; a < size; a++) {
            i[a] = a;
        }

        lstSearchEngines.setSelectedIndices(i);

    }

    private void writeSearchEngines(SearchEngines se) {
        try {

            File file = new File("file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchEngine.class
            );
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    true);

            jaxbMarshaller.marshal(se, file);

            jaxbMarshaller.marshal(se, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private String[] getSearchEngineNamesArray() {
        try {
            List<SearchEngine> selist = getSearchEnginesFromXml().getSearchEngines();

            String[] seNames = new String[selist.size()];
            int i = 0;
            for (SearchEngine se : selist) {
                seNames[i++] = se.name;
            }
            return seNames;
        } catch (Exception ex) {
            String[] seNames = {"SearchEngine", "Config", "Failure"};
            return seNames;
        }

    }

    private SearchEngines getSearchEnginesFromXml() {

        try {

            CodeSource codeSource = MainForm.class
                    .getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();

            File file = new File(jarDir + "/SearchEngines.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchEngines.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SearchEngines searchEngineList = (SearchEngines) jaxbUnmarshaller.unmarshal(file);
            return searchEngineList;
//		System.out.println(searchEngineList.getSearchEngines());

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
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
     */
//    public static SearchEngineStruct getEngineFromName(String enginename) {
//        switch (enginename) {
//            case "Google":
//                return SearchEngineStruct.Google;
//            case "Bing":
//                return SearchEngineStruct.Bing;
//            case "Yahoo":
//                return SearchEngineStruct.Yahoo;
//            case "Yandex":
//                return SearchEngineStruct.Yandex;
//            default:
//                return null;
//        }
//    }

//    public enum SearchEngineStruct {
//
//        Google, Yahoo, Bing, Yandex;
//
//        public String getBaseUrl() {
//            switch (this) {
//                case Google:
//                    return "https://www.google.com.tr";
//                case Bing:
//                    return "https://www.bing.com";
//                case Yahoo:
//                    return "https://www.yahoo.com";
//                case Yandex:
//                    return "http://www.yandex.com.tr";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//        public String getName() {
//            switch (this) {
//                case Google:
//                    return "google.com";
//                case Bing:
//                    return "bing.com";
//                case Yahoo:
//                    return "yahoo.com";
//                case Yandex:
//                    return "yandex";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//        public String getQueryBoxName() {
//            switch (this) {
//                case Google:
//                    return "q";
//                case Bing:
//                    return "q";
//                case Yahoo:
//                    return "p";
//                case Yandex:
//                    return "text";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//        public String getSubmitButtonId() {
//            switch (this) {
//                case Google:
//                    return "gbqfb";
//                case Bing:
//                    return "sb_form_go";
//                case Yahoo:
//                    return "search-submit";
//                case Yandex:
//                    return "button";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//        public BrowserVersion getDefaultBrowser() {
//            switch (this) {
//                case Google:
//                    return BrowserVersion.getDefault();
//                case Bing:
//                    return BrowserVersion.getDefault();
//                case Yahoo:
//                    return BrowserVersion.getDefault();
//                case Yandex:
//                    return BrowserVersion.CHROME;
//                default:
//                    return BrowserVersion.getDefault();
//            }
//
//        }
//
//        public String getSubmitButtonName() {
//            switch (this) {
//                case Google:
//                    return "btnG";
//                case Bing:
//                    return "";
//                case Yahoo:
//                    return "";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//        public String getNextButtonIdentifier() {
//            switch (this) {
//                case Google:
//                    return "Sonraki";
//                case Bing:
//                    return "Next";
//                case Yahoo:
//                    return "Next";
//                //return "id=\"pg-next\" href=";
//                case Yandex:
//                    return "Sonraki";
//                default:
//                    break;
//            }
//            return "";
//        }
//
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        choiceSearchEngine = new java.awt.Choice();
        jMenu1 = new javax.swing.JMenu();
        txtSearchText = new java.awt.TextField();
        jButton1 = new javax.swing.JButton();
        btnRunSeedGenerator = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();
        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        pnlSparql = new javax.swing.JPanel();
        btnStartRemoteQueries = new javax.swing.JButton();
        btnPrepareQueryQueue = new javax.swing.JButton();
        btnParseResponse = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        pnlCrawler = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtSearchTexts = new javax.swing.JTextArea();
        lblSearchText4 = new java.awt.Label();
        jScrollPane16 = new javax.swing.JScrollPane();
        lstSearchEngines = new javax.swing.JList();
        jLabel16 = new javax.swing.JLabel();
        txtMaxPage = new javax.swing.JTextField();
        lblMessage4 = new javax.swing.JLabel();
        label9 = new java.awt.Label();
        lblCount4 = new javax.swing.JLabel();
        btnRunAnalyzer = new javax.swing.JButton();
        btnRunMultitextSearch = new javax.swing.JButton();
        btnCreateSearchQueue = new javax.swing.JButton();
        btnStopThreads4 = new javax.swing.JButton();
        btnStopSearchQueue4 = new javax.swing.JButton();
        btnStopAnalyzer4 = new javax.swing.JButton();
        btnCreateSearchQueueFromPreviousUrls = new javax.swing.JButton();
        label10 = new java.awt.Label();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        txtSeeds = new java.awt.TextArea();
        jPanel1 = new javax.swing.JPanel();
        jButtonAnalyzeEndpointHtml = new javax.swing.JButton();
        jButtonPostFilterUrls = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txtSparqlesUrl = new javax.swing.JTextField();
        txtLodstatsUrl = new javax.swing.JTextField();
        txtDatahubUrl = new javax.swing.JTextField();
        btnParseSparqles = new javax.swing.JButton();
        btnParseDatahub = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnParseLODCloud = new javax.swing.JButton();
        btnLodstats = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnStatisticalAnalysis = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnStatusMonitor = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNumOfThreadsStatisticalAnalysis = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNumOfThreadsStatusMonitoring = new javax.swing.JTextField();
        txtStatisticalAnalysisTimeout = new javax.swing.JTextField();
        txtStatisticalAnalysisCheckEveryHours = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaFederatedQuery = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        btnParseSemanticDiscovery = new javax.swing.JButton();
        jButtonGetSparqlEndpointWebPages = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonRunClassCollector = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboCommonQueries = new javax.swing.JComboBox();
        txtMaxCollectorThreads = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        btnWordnetAnalyzer = new javax.swing.JButton();
        btnTfidf = new javax.swing.JButton();
        jButtonIDF = new javax.swing.JButton();
        btnSameAs = new javax.swing.JButton();
        jButtonExportSelectedTriples = new javax.swing.JButton();
        jButtonPrepareEndpointLcnWordTable = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButtonExportRclh = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButtonwordnetTdidf = new javax.swing.JButton();
        jButtonWordnetLevel = new javax.swing.JButton();
        jButtonTabloyaAktar = new javax.swing.JButton();
        jButtonResetEndpointsForProcessing = new javax.swing.JButton();
        jButtonGetSubjectsFromLODCLOUD = new javax.swing.JButton();
        jButtonProfiler = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButtontfidftop20 = new javax.swing.JButton();
        jButtonWordnetlevel = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnIoTCollectResults = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel1 = new java.awt.Panel();
        jPanel8 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItemSaveTask = new javax.swing.JMenuItem();
        jMenuItemSaveUrlList = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemConfig = new javax.swing.JMenuItem();
        jMenuItemCreateInitialDatabase = new javax.swing.JMenuItem();

        choiceSearchEngine.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("jMenu1");

        txtSearchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchTextActionPerformed(evt);
            }
        });

        jButton1.setText("test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnRunSeedGenerator.setText("disabled-Run Seed Generator");
        btnRunSeedGenerator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunSeedGeneratorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        btnStartRemoteQueries.setBackground(new java.awt.Color(255, 0, 0));
        btnStartRemoteQueries.setText("Start Remote Sparql Query Threads");
        btnStartRemoteQueries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartRemoteQueriesActionPerformed(evt);
            }
        });

        btnPrepareQueryQueue.setText("Prepare Remote Sparql Query Queue");
        btnPrepareQueryQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrepareQueryQueueActionPerformed(evt);
            }
        });

        btnParseResponse.setText("Parse Responses");
        btnParseResponse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseResponseActionPerformed(evt);
            }
        });

        jButton2.setText("ali hoca queue yarat");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("start ali hoca remote query");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSparqlLayout = new javax.swing.GroupLayout(pnlSparql);
        pnlSparql.setLayout(pnlSparqlLayout);
        pnlSparqlLayout.setHorizontalGroup(
            pnlSparqlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSparqlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSparqlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSparqlLayout.createSequentialGroup()
                        .addGroup(pnlSparqlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnStartRemoteQueries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrepareQueryQueue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnParseResponse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 416, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSparqlLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlSparqlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        pnlSparqlLayout.setVerticalGroup(
            pnlSparqlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSparqlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrepareQueryQueue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStartRemoteQueries)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnParseResponse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(233, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sparql Endpoint Crawler and Analyzer(SPECAN) v2.0");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFocusCycleRoot(true);
        jTabbedPane1.setInheritsPopupMenu(true);

        txtSearchTexts.setColumns(20);
        txtSearchTexts.setRows(5);
        jScrollPane15.setViewportView(txtSearchTexts);

        lblSearchText4.setText("Search Texts (Seperated by lines)");

        lstSearchEngines.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Google", "Bing", "Yahoo", "Yandex" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane16.setViewportView(lstSearchEngines);

        jLabel16.setText("Max. Page");

        txtMaxPage.setText("10");
        txtMaxPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaxPageActionPerformed(evt);
            }
        });

        lblMessage4.setForeground(new java.awt.Color(255, 0, 51));

        label9.setText("Search Engines");

        lblCount4.setForeground(new java.awt.Color(255, 0, 0));
        lblCount4.setText("Count");
        lblCount4.setName(""); // NOI18N

        btnRunAnalyzer.setText("Run URL Analyzer");
        btnRunAnalyzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunAnalyzerActionPerformed(evt);
            }
        });

        btnRunMultitextSearch.setText("Run Search Queue");
        btnRunMultitextSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunMultitextSearchActionPerformed(evt);
            }
        });

        btnCreateSearchQueue.setText("Search");
        btnCreateSearchQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateSearchQueueActionPerformed(evt);
            }
        });

        btnStopThreads4.setText("Stop Threads");
        btnStopThreads4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopThreads4ActionPerformed(evt);
            }
        });

        btnStopSearchQueue4.setBackground(new java.awt.Color(255, 0, 0));
        btnStopSearchQueue4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopSearchQueue4ActionPerformed(evt);
            }
        });

        btnStopAnalyzer4.setBackground(new java.awt.Color(255, 0, 0));
        btnStopAnalyzer4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopAnalyzer4ActionPerformed(evt);
            }
        });

        btnCreateSearchQueueFromPreviousUrls.setText("Domain Learning");
        btnCreateSearchQueueFromPreviousUrls.setActionCommand("Create Search In Previous Urls");
        btnCreateSearchQueueFromPreviousUrls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateSearchQueueFromPreviousUrlsActionPerformed(evt);
            }
        });

        label10.setText("Generated Urls");

        jLabel17.setText("# of Extracted URLs:");

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SearchEngine", "Search Text", "URL", "Date Created"
            }
        ));
        jScrollPane17.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        }

        txtSeeds.setVisible(false);

        javax.swing.GroupLayout pnlCrawlerLayout = new javax.swing.GroupLayout(pnlCrawler);
        pnlCrawler.setLayout(pnlCrawlerLayout);
        pnlCrawlerLayout.setHorizontalGroup(
            pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrawlerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSearchText4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCount4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(btnCreateSearchQueue, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnRunMultitextSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnStopSearchQueue4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtMaxPage, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnCreateSearchQueueFromPreviousUrls, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnRunAnalyzer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnStopAnalyzer4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(322, 322, 322))
                            .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                .addGap(127, 127, 127)
                                .addComponent(txtSeeds, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMessage4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(599, 599, 599))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCrawlerLayout.createSequentialGroup()
                        .addComponent(jScrollPane17)
                        .addGap(200, 200, 200)
                        .addComponent(btnStopThreads4)
                        .addGap(37, 37, 37)))
                .addContainerGap())
        );
        pnlCrawlerLayout.setVerticalGroup(
            pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCrawlerLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSearchText4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addComponent(btnCreateSearchQueue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStopSearchQueue4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRunMultitextSearch))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRunAnalyzer)
                            .addComponent(btnStopAnalyzer4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCreateSearchQueueFromPreviousUrls))
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(lblCount4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMessage4)
                            .addGroup(pnlCrawlerLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(txtSeeds, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addComponent(btnStopThreads4))
                    .addGroup(pnlCrawlerLayout.createSequentialGroup()
                        .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlCrawlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMaxPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16))
                            .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlCrawler, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5525, 5525, 5525))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(pnlCrawler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Crawler", jPanel2);

        jButtonAnalyzeEndpointHtml.setText("Analyze Endpoint HTML");
        jButtonAnalyzeEndpointHtml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnalyzeEndpointHtmlActionPerformed(evt);
            }
        });

        jButtonPostFilterUrls.setText("Mark Same URLs with same #triples");
        jButtonPostFilterUrls.setToolTipText("Mark Same URLs with same #triples");
        jButtonPostFilterUrls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPostFilterUrlsActionPerformed(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Dataset Collection"));

        txtSparqlesUrl.setText("http://sparqles.okfn.org/api/endpoint/list");

        txtLodstatsUrl.setText("http://stats.lod2.eu");

        txtDatahubUrl.setText("http://datahub.io/api/3/action/resource_search?query=format:sparql");
        txtDatahubUrl.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btnParseSparqles.setText("SPARQLES");
        btnParseSparqles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseSparqlesActionPerformed(evt);
            }
        });

        btnParseDatahub.setText("Datahub");
        btnParseDatahub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseDatahubActionPerformed(evt);
            }
        });

        jTextField1.setText("http://lod-cloud.net/data/void.ttl");

        btnParseLODCloud.setText("LOD Cloud");
        btnParseLODCloud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseLODCloudActionPerformed(evt);
            }
        });

        btnLodstats.setText("LODStats");
        btnLodstats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLodstatsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnParseSparqles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnParseLODCloud, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnParseDatahub, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLodstats, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtLodstatsUrl, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDatahubUrl, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(txtSparqlesUrl, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnLodstats, btnParseLODCloud, btnParseSparqles});

        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnParseSparqles)
                    .addComponent(txtSparqlesUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnParseLODCloud)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLodstats)
                    .addComponent(txtLodstatsUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnParseDatahub)
                    .addComponent(txtDatahubUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitoring & Analysis"));
        jPanel10.setLayout(null);

        btnStatisticalAnalysis.setBackground(new java.awt.Color(0, 255, 0));
        btnStatisticalAnalysis.setText("Statistical Analysis");
        btnStatisticalAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatisticalAnalysisActionPerformed(evt);
            }
        });
        jPanel10.add(btnStatisticalAnalysis);
        btnStatisticalAnalysis.setBounds(10, 60, 175, 29);

        jLabel4.setText("#ofThreads");
        jPanel10.add(jLabel4);
        jLabel4.setBounds(190, 25, 82, 17);

        btnStatusMonitor.setBackground(new java.awt.Color(0, 255, 0));
        btnStatusMonitor.setText("Status Monitoring");
        btnStatusMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatusMonitorActionPerformed(evt);
            }
        });
        jPanel10.add(btnStatusMonitor);
        btnStatusMonitor.setBounds(10, 20, 175, 29);

        jLabel7.setText("Check Every");
        jPanel10.add(jLabel7);
        jLabel7.setBounds(190, 95, 85, 17);

        jLabel8.setText("Hours");
        jPanel10.add(jLabel8);
        jLabel8.setBounds(310, 95, 42, 17);

        txtNumOfThreadsStatisticalAnalysis.setText("1");
        jPanel10.add(txtNumOfThreadsStatisticalAnalysis);
        txtNumOfThreadsStatisticalAnalysis.setBounds(280, 60, 26, 27);

        jLabel3.setText("#ofThreads");
        jPanel10.add(jLabel3);
        jLabel3.setBounds(190, 65, 82, 17);

        jLabel5.setText("| Timeout");
        jPanel10.add(jLabel5);
        jLabel5.setBounds(310, 65, 65, 17);

        jLabel6.setText("Seconds");
        jPanel10.add(jLabel6);
        jLabel6.setBounds(430, 65, 59, 17);

        txtNumOfThreadsStatusMonitoring.setText("1");
        jPanel10.add(txtNumOfThreadsStatusMonitoring);
        txtNumOfThreadsStatusMonitoring.setBounds(280, 20, 26, 27);

        txtStatisticalAnalysisTimeout.setText("120");
        jPanel10.add(txtStatisticalAnalysisTimeout);
        txtStatisticalAnalysisTimeout.setBounds(390, 60, 26, 27);

        txtStatisticalAnalysisCheckEveryHours.setText("6");
        jPanel10.add(txtStatisticalAnalysisCheckEveryHours);
        txtStatisticalAnalysisCheckEveryHours.setBounds(280, 90, 26, 27);

        label1.setBackground(new java.awt.Color(1, 1, 1));
        label1.setText("label1");
        jPanel10.add(label1);
        label1.setBounds(10, 55, 480, 1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAnalyzeEndpointHtml, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonPostFilterUrls, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButtonAnalyzeEndpointHtml)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPostFilterUrls)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.getAccessibleContext().setAccessibleName("Collect Other Projects");

        jTabbedPane1.addTab("Analysis", jPanel1);

        jTextField2.setText("jTextField2");

        jTextAreaFederatedQuery.setColumns(20);
        jTextAreaFederatedQuery.setRows(5);
        jScrollPane3.setViewportView(jTextAreaFederatedQuery);

        jButton4.setText("Send");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnParseSemanticDiscovery.setText("Parse SpEnD");
        btnParseSemanticDiscovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseSemanticDiscoveryActionPerformed(evt);
            }
        });

        jButtonGetSparqlEndpointWebPages.setText("Get Sparql Endpoint WebPages");
        jButtonGetSparqlEndpointWebPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetSparqlEndpointWebPagesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(btnParseSemanticDiscovery, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonGetSparqlEndpointWebPages, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(220, 220, 220))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnParseSemanticDiscovery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGetSparqlEndpointWebPages)))
                .addGap(0, 159, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Federation", jPanel3);

        jButtonRunClassCollector.setText("Run Collector");
        jButtonRunClassCollector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunClassCollectorActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane5.setViewportView(jTextArea1);

        jComboCommonQueries.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtMaxCollectorThreads.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxCollectorThreads.setText("1");

        jLabel9.setText("Threads");

        jButton6.setText("Reset Endpoint Updates");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Export All Triples");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        btnWordnetAnalyzer.setText("3-Wordnet Analyzer");
        btnWordnetAnalyzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWordnetAnalyzerActionPerformed(evt);
            }
        });

        btnTfidf.setText("1-TF Calculator");
        btnTfidf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTfidfActionPerformed(evt);
            }
        });

        jButtonIDF.setText("2-IDF Calculator");
        jButtonIDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIDFActionPerformed(evt);
            }
        });

        btnSameAs.setText("4-sameAs URL PLD count");
        btnSameAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSameAsActionPerformed(evt);
            }
        });

        jButtonExportSelectedTriples.setText("Export Selected Triples");
        jButtonExportSelectedTriples.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportSelectedTriplesActionPerformed(evt);
            }
        });

        jButtonPrepareEndpointLcnWordTable.setText("Prepare Endpoint LCN Word Table");
        jButtonPrepareEndpointLcnWordTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrepareEndpointLcnWordTableActionPerformed(evt);
            }
        });

        jButton8.setText("TF IDF -NEW");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButtonExportRclh.setText("Export RCLH");
        jButtonExportRclh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportRclhActionPerformed(evt);
            }
        });

        jButton9.setText("Analyze tf idf");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButtonwordnetTdidf.setText("5-Wordnet TF-IDF");
        jButtonwordnetTdidf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonwordnetTdidfActionPerformed(evt);
            }
        });

        jButtonWordnetLevel.setText("Wordnet level");
        jButtonWordnetLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWordnetLevelActionPerformed(evt);
            }
        });

        jButtonTabloyaAktar.setText("6-Tabloya Aktar");
        jButtonTabloyaAktar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTabloyaAktarActionPerformed(evt);
            }
        });

        jButtonResetEndpointsForProcessing.setText("Reset Endpoints For Processing");
        jButtonResetEndpointsForProcessing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetEndpointsForProcessingActionPerformed(evt);
            }
        });

        jButtonGetSubjectsFromLODCLOUD.setText("6-LOD Cloud Subjects");
        jButtonGetSubjectsFromLODCLOUD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetSubjectsFromLODCLOUDActionPerformed(evt);
            }
        });

        jButtonProfiler.setText("Temp Profiler");
        jButtonProfiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProfilerActionPerformed(evt);
            }
        });

        jButton5.setText("domain");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButtontfidftop20.setText("Wordnet Tf top 20");
        jButtontfidftop20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtontfidftop20ActionPerformed(evt);
            }
        });

        jButtonWordnetlevel.setText("3.1 Wordnet Level");
        jButtonWordnetlevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWordnetlevelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jButtonRunClassCollector)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboCommonQueries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtMaxCollectorThreads, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButtonResetEndpointsForProcessing)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonWordnetlevel))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButtonIDF)
                                        .addGap(10, 10, 10)
                                        .addComponent(jButtonwordnetTdidf))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(btnTfidf)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnWordnetAnalyzer)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButtontfidftop20)
                                        .addGap(32, 32, 32)
                                        .addComponent(btnSameAs, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButtonGetSubjectsFromLODCLOUD)
                                        .addGap(44, 44, 44)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButtonWordnetLevel)
                                            .addComponent(jButtonTabloyaAktar))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonPrepareEndpointLcnWordTable, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9)
                            .addComponent(jButtonExportRclh)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonProfiler)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonExportSelectedTriples)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jButton8)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRunClassCollector)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboCommonQueries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaxCollectorThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButtonExportSelectedTriples)
                    .addComponent(jButton8))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonProfiler)
                                    .addComponent(jButton5)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonResetEndpointsForProcessing)
                                    .addComponent(jButtonWordnetLevel)
                                    .addComponent(jButtonWordnetlevel))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonTabloyaAktar)
                            .addComponent(btnWordnetAnalyzer)
                            .addComponent(btnTfidf)
                            .addComponent(jButtonGetSubjectsFromLODCLOUD)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPrepareEndpointLcnWordTable)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSameAs)
                    .addComponent(jButtonExportRclh)
                    .addComponent(jButtonwordnetTdidf)
                    .addComponent(jButtonIDF)
                    .addComponent(jButtontfidftop20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Recommender", jPanel4);

        btnIoTCollectResults.setText("Start");
        btnIoTCollectResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIoTCollectResultsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIoTCollectResults)
                .addContainerGap(538, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnIoTCollectResults)
                .addContainerGap(337, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("IoT Crawler", jPanel5);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        panel1.setName("test"); // NOI18N

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Test"));
        jPanel8.setToolTipText("ttt");
        jPanel8.setName("test"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(102, 102, 102)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", jPanel6);

        jMenu2.setText("File");

        jMenuItem2.setText("Load Previous Search Task");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItemSaveTask.setText("Save Search Task");
        jMenuItemSaveTask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveTaskActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemSaveTask);

        jMenuItemSaveUrlList.setText("Save Url List");
        jMenu2.add(jMenuItemSaveUrlList);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Config");

        jMenuItemConfig.setText("Database Connection");
        jMenuItemConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConfigActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemConfig);

        jMenuItemCreateInitialDatabase.setText("Create Initial Database");
        jMenuItemCreateInitialDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCreateInitialDatabaseActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemCreateInitialDatabase);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    Thread[] threadArray;

    int getCrawlId() {
        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //          String connectionUrl = "jdbc:sqlserver://localhost\\sql2008;user=java;password=62217769;databaseName=Crawler";
            //        Connection con = DriverManager.getConnection(connectionUrl);
            String SQLcrawlid = "SELECT max(crawlid) FROM crawlrecord";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLcrawlid);
            int id = 0;
            if (rs.next()) {
                try {
                    id = rs.getInt(1);
                } catch (Exception ex) {
                }

            }
            rs.close();
            stmt.close();
            return id;
        } catch (SQLException ex) {
            return 99999;
        }
    }

    public int createCrawl(String queryText, String crawlerName) {
        try {
            String SQL = "SELECT max(crawlid) FROM crawlrecord";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            int id = 0;
            if (rs.next()) {
                try {
                    id = rs.getInt(1);

                } catch (Exception ex) {
                }

            }
            rs.close();

            String SQLi = "INSERT INTO crawlrecord (crawlid,crawlerName,queryText) VALUES (?,?,?)";
            PreparedStatement pstmt
                    = con.prepareStatement(SQLi);
            pstmt.setInt(1, id + 1);
            pstmt.setString(2, crawlerName);
            pstmt.setString(3, queryText);
//Statement stmt = con.createStatement();
            pstmt.executeUpdate();
            pstmt.close();
            stmt.close();
            return id + 1;
        } catch (Exception ex) {

        }
        return 0;
    }
    /*
     private String clickButton(Object tempsubmit) {
     HtmlSubmitInput htmlsubmit;
     HtmlButton htmlbutton;
     String a = "";
     try {
     if (tempsubmit.getClass().getName().contains("HtmlButton")) {
     htmlbutton = (HtmlButton) tempsubmit;
     a = htmlbutton.click().getWebResponse().getContentAsString();
     } else if (tempsubmit.getClass().getName().contains("HtmlSubmitInput")) {
     htmlsubmit = (HtmlSubmitInput) tempsubmit;
     a = htmlsubmit.click().getWebResponse().getContentAsString();
     }
     } catch (Exception ex) {
     String temp = ex.getMessage();
     }
     return a;

     }
     */
    /*
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
     */
    /*
     private HtmlPage clickSearchButtonMainPage(final WebClient webClient, SearchEngine se) {

     String url = se.getBaseUrl();
     String queryTextboxName = se.getQueryTextBoxName();
     String searchButtonId = se.getSubmitButtonId();
     String searchButtonName = se.getSubmitButtonName();
     Object tempSubmitFromName, tempSubmitFromId, tempSubmitFromTagName;

     try {
     HtmlPage page1 = webClient.getPage(url);
     HtmlInput input1 = page1.getElementByName(queryTextboxName);
     input1.setValueAttribute(txtSearchText.getText());
     tempSubmitFromName = page1.getElementByName(searchButtonName);
     return clickButtonReturnPage(tempSubmitFromName);
     //                baseUrl = page1.getUrl().toString();
     } catch (Exception ex) {
     String aaa = "deneme";
     }
     try {
     HtmlPage page1 = webClient.getPage(url);
     HtmlInput input1 = page1.getElementByName(queryTextboxName);
     input1.setValueAttribute(txtSearchText.getText());
     tempSubmitFromId = page1.getElementById(searchButtonId);
     return clickButtonReturnPage(tempSubmitFromId);
     //               baseUrl = page1.getUrl().toString();
     } catch (Exception ex2) {
     String aaa = "deneme";
     }
     try {
     HtmlPage page1 = webClient.getPage(url);
     HtmlInput input1 = page1.getElementByName(queryTextboxName);
     input1.setValueAttribute(txtSearchText.getText());
     tempSubmitFromTagName = page1.getElementsByTagName("button").get(0);
     return clickButtonReturnPage(tempSubmitFromTagName);
     //                baseUrl = page1.getUrl().toString();
     } catch (Exception ex2) {
     String aaa = "deneme";
     }
     return null;
     }
     */

    /*
     private void searchGoogle() {
     try {
     //String url = "https://www.google.com.tr";
     String url;
     String queryTextboxName = "q";
     String searchButtonName = "btnG";

     final WebClient webClient = new WebClient();

     HtmlPage page1 = webClient.getPage(SearchEngineStruct.Google.getBaseUrl());
     HtmlInput input1 = page1.getElementByName(queryTextboxName);
     input1.setValueAttribute(txtSearchText.getText());

     HtmlSubmitInput submit1 = page1.getElementByName(searchButtonName);

     //page1 = submit1.click();
     String a = submit1.click().getWebResponse().getContentAsString();
     //String a = page1.asXml();
     url = page1.getUrl().toString();

     ExtractSeedUrlsGoogle(a);

     //File newTextFile = new File("D:/" + System.currentTimeMillis() + ".txt");
     // FileWriter fw = new FileWriter(newTextFile);
     //        HtmlSubmitInput submit2 = page1.getElementByName("pnnext");
     //        page1=submit2.click();
     String nextUrl = getNextUrl(a, SearchEngineStruct.Google);
     try {
     for (int i = 1; i < 10; i++) {
     HtmlPage page2 = webClient.getPage(nextUrl);

     String b = page2.getWebResponse().getContentAsString();
     //String b = page2.asXml();

     ExtractSeedUrlsGoogle(b);
     nextUrl = getNextUrl(b, SearchEngineStruct.Google);

     //fw.write(page2.asXml());
     Thread.sleep(4000);

     // fw.write("SAYFA GECISI " + String.valueOf(i * 10));
     }
     // fw.close();
     } catch (Exception x) {
     // fw.close();
     }
     webClient.closeAllWindows();
     } catch (Exception ex) {

     }
     }
     */
    /*
     private String getNextUrlYahoo(String htmltext) {
     int nexturlstartindex = htmltext.indexOf("id=\"pg-next\" href=") + 19;
     int urlendindex = htmltext.indexOf('"', nexturlstartindex + 20);

     String nextUrl = htmltext.substring(nexturlstartindex, urlendindex);
     return nextUrl;
     }
     */
//    private String getNextUrl(String htmltext, SearchEngine searchEngine, String baseUrl) {
//        String nextUrl = "";
//        int endtagindex, startindex, endindex;
//
//        endtagindex = htmltext.indexOf(searchEngine.getNextButtonIdentifier());
//        //urlyi endindeksten bulabiliyorum
//        //öncesi 207 karakter, kabaca 250 chr öncesini alıp içerisindeki href alınır
//        startindex = htmltext.indexOf("href=", endtagindex - 300) + 6;
//        endindex = htmltext.indexOf('"', startindex + 10);// başlangıçtaki tırnağı atlaması için +10
//        nextUrl = htmltext.substring(startindex, endindex);
//        if (!nextUrl.startsWith("http")) {
//            nextUrl = baseUrl + nextUrl;
//        }
//
//        return nextUrl;
//    }
//    private String getNextUrlold(String htmltext, SearchEngineStruct searchEngine) {
//        String nextUrl = "";
//        int endtagindex, startindex, endindex;
//        switch (searchEngine) {
//            case Yahoo:
//                startindex = htmltext.indexOf("id=\"pg-next\" href=") + 19;
//                endindex = htmltext.indexOf('"', startindex + 20);
//                nextUrl = htmltext.substring(startindex, endindex);
//                break;
//            case Bing:
//                endtagindex = htmltext.indexOf("<div class=\"sw_next\">Next</div></a>");
//                //urlyi endindeksten bulabiliyorum
//                //öncesi 207 karakter, kabaca 250 chr öncesini alıp içerisindeki href alınır
//                startindex = htmltext.indexOf("href=", endtagindex - 250) + 6;
//                endindex = htmltext.indexOf('"', startindex + 10);// başlangıçtaki tırnağı atlaması için +10
//                nextUrl = htmltext.substring(startindex, endindex);
//                nextUrl = SearchEngineStruct.Bing.getBaseUrl() + nextUrl;
//                break;
//            case Google:
//                endtagindex = htmltext.indexOf("Sonraki</span></a>");
//                //urlyi endindeksten bulabiliyorum
//                //öncesi 207 karakter, kabaca 250 chr öncesini alıp içerisindeki href alınır
//                startindex = htmltext.indexOf("href=", endtagindex - 300) + 6;
//                endindex = htmltext.indexOf('"', startindex + 10);// başlangıçtaki tırnağı atlaması için +10
//                nextUrl = htmltext.substring(startindex, endindex);
//                nextUrl = SearchEngineStruct.Google.getBaseUrl() + nextUrl;
//                break;
//            case Yandex:
//                endtagindex = htmltext.indexOf(SearchEngineStruct.Yandex.getNextButtonIdentifier());
//                //urlyi endindeksten bulabiliyorum
//                //öncesi 207 karakter, kabaca 250 chr öncesini alıp içerisindeki href alınır
//                startindex = htmltext.indexOf("href=", endtagindex - 300) + 6;
//                endindex = htmltext.indexOf('"', startindex + 10);// başlangıçtaki tırnağı atlaması için +10
//                nextUrl = htmltext.substring(startindex, endindex);
//                nextUrl = SearchEngineStruct.Google.getBaseUrl() + nextUrl;
//                break;
//            default:
//                break;
//        }
//        return nextUrl;
//    }
    /*
     private void searchBing() {
     try {
     String url = "https://www.bing.com";
     final WebClient webClient = new WebClient();

     HtmlPage page1 = webClient.getPage(url);
     HtmlInput input1 = page1.getElementByName("q");
     input1.setValueAttribute(txtSearchText.getText());

     HtmlSubmitInput submit1 = (HtmlSubmitInput) page1.getElementById("sb_form_go");
     String a = submit1.click().getWebResponse().getContentAsString();
     ExtractSeedUrls(a);

     String nextUrl = getNextUrl(a, SearchEngineStruct.Bing);
     //String googleurl = page1.getUrl() + "&start=";
     try {
     for (int i = 1; i < 10; i++) {
     HtmlPage page2 = webClient.getPage(nextUrl);
     //                    String b = page2.asXml();
     String b = page2.getWebResponse().getContentAsString();
     nextUrl = getNextUrl(b, SearchEngineStruct.Bing);
     ExtractSeedUrls(b);
     Thread.sleep(4000);
     }
     } catch (Exception x) {
     String xx = x.getMessage();
     }
     webClient.closeAllWindows();
     } catch (Exception ex) {
     String a = ex.getMessage();
     }
     }

     private void searchYahoo() {
     try {
     final WebClient webClient = new WebClient();

     HtmlPage page1 = webClient.getPage("https://www.yahoo.com");
     HtmlInput input1 = page1.getElementByName("p");
     input1.setValueAttribute(txtSearchText.getText());

     HtmlButton submit1 = (HtmlButton) page1.getElementById("search-submit");

     page1 = submit1.click();
     String a = page1.asXml();

     ExtractSeedUrls(a);

     String nextUrl = getNextUrlYahoo(a);
     //String googleurl = page1.getUrl() + "&start=";
     try {
     for (int i = 1; i < 10; i++) {

     HtmlPage page2 = webClient.getPage(nextUrl);
     String b = page2.asXml();
     nextUrl = getNextUrlYahoo(b);
     ExtractSeedUrls(b);
     Thread.sleep(4000);
     }
     } catch (Exception x) {
     }
     webClient.closeAllWindows();
     } catch (Exception ex) {
     String a = ex.getMessage();
     }
     }
     */
    /*
     private boolean isExcluded(String url, String searchEngineName) {
     String excludeString = getSearchEngineFromName(searchEngineName).getExcludedWords();
     boolean excludeMatch = false;
     if (!excludeString.equals("")) {
     excludeMatch = url.matches(".*(" + excludeString + ").*");
     }
     return excludeMatch;
     }
     */
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

    private void txtSearchTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchTextActionPerformed
    private String readFile(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            return stringBuilder.toString();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();

            } catch (IOException ex) {
                Logger.getLogger(MainForm.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRunSeedGeneratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunSeedGeneratorActionPerformed

        int crawlId = createCrawl(getQueryText(), "SearchEngineCrawler");
        maxPage = Integer.decode(txtMaxPage.getText());

        threadArray = new Thread[maxPage];
        List selist = lstSearchEngines.getSelectedValuesList();

        jProgressBar1.setMaximum(maxPage * selist.size());

        int i = 0;
        for (Object se : selist) {
            WorkerSearch sworker = new WorkerSearch(this, se.toString(), crawlId, getQueryText());
            sworker.setName("Worker " + wNr);
            //threadArray[i++] = sworker;
            sworker.start();
            try {
                Thread.sleep(300);
            } catch (Exception ex) {
            }//  search(getSearchEngineFromName((String) se));
        }

    }//GEN-LAST:event_btnRunSeedGeneratorActionPerformed

    private void jMenuItemConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigActionPerformed

        FormDatabaseConfig configform = new FormDatabaseConfig();
        configform.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemConfigActionPerformed

    private void jMenuItemCreateInitialDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCreateInitialDatabaseActionPerformed

        FormConfirmation confirm = new FormConfirmation(con);
        confirm.setVisible(true);

//end sql file parsers
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemCreateInitialDatabaseActionPerformed

    private void btnParseSemanticDiscoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParseSemanticDiscoveryActionPerformed
        try {
            String SQL = "SELECT url FROM crawler.list_distinct_sparqlendpoint_urls;";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                SparqlEndpoint.insertNewEndpoint("", rs.getString("url").toString(), "spend", 0);
            }
        } catch (Exception ex) {
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnParseSemanticDiscoveryActionPerformed

    private void btnStatusMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatusMonitorActionPerformed

        int numofthreads = Integer.parseInt(txtNumOfThreadsStatusMonitoring.getText());
        threadsStatusMonitoring = new Thread[numofthreads];
        if (btnStatusMonitor.getBackground().equals(Color.green)) {
            btnStatusMonitor.setBackground(Color.red);
            btnStatusMonitor.setText("Stop Status Monitoring");

//            String SQL = "update endpoints set checkFlag = null;";
//            Statement stmt;
//            try {
//                stmt = con.createStatement();
//                stmt.execute(SQL);
//
//            } catch (SQLException ex) {
//                Logger.getLogger(MainForm.class
//                        .getName()).log(Level.SEVERE, null, ex);
//            }
            //
            for (int i = 0; i < numofthreads; i++) {
                WorkerStatusMonitor sworker = new WorkerStatusMonitor(this, connectionUrl, i, numofthreads);
                sworker.setName("Worker Status Monitor Analyzer " + String.valueOf(i));
                threadsStatusMonitoring[i] = sworker;
                //threadArray[i++] = sworker;
                sworker.start();
            }
        } else {
            btnStatusMonitor.setBackground(Color.green);
            btnStatusMonitor.setText("Start Status Monitoring");
            for (Thread t : threadsStatusMonitoring) {
                ((WorkerStatusMonitor) t).stopRunning();
            }

        }// TODO add your handling code here:
    }//GEN-LAST:event_btnStatusMonitorActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        for (int i = 0; i < 20; i++) {
            WorkerSparql sworker = new WorkerSparql(this, connectionUrl);
            sworker.setName("Worker Remote Sparql");
            threadsSparql[i] = sworker;
            //threadArray[i++] = sworker;
            sworker.start();
            try {
                //   Thread.sleep(157);
            } catch (Exception ex) {

            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {

            String SQLurl = "SELECT DISTINCT url FROM crawler.seedurlraw where id >=174849;";
            Statement stmturl = con1.createStatement();
            ResultSet rsurl = stmturl.executeQuery(SQLurl);
            while (rsurl.next()) {
                try {
                    String url = rsurl.getString(1);
                    String SQLi = "INSERT INTO queryqueue (endpointUrl,commonQueryId, disabled) VALUES (?,?,0)";
                    PreparedStatement pstmt = con2.prepareStatement(SQLi);
                    pstmt.setString(1, url);
                    pstmt.setInt(2, 0);

                    pstmt.executeUpdate();
                    pstmt.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            stmturl.close();
            rsurl.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnParseResponseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParseResponseActionPerformed
        try {
            String SQLurl = "SELECT id,resultSet FROM queryqueue where commonQueryId between 7 and 13;";
            Statement stmturl = con1.createStatement();
            ResultSet rsurl = stmturl.executeQuery(SQLurl);

            while (rsurl.next()) {
                try {
                    String s = rsurl.getString("resultSet");
                    int id = rsurl.getInt("id");
                    //get the factory
                    DocumentBuilderFactory dbf
                            = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(s));

                    Document doc = db.parse(is);
                    boolean bool = true;

                    Node n = doc.getFirstChild();

                    Node firstnode = doc.getFirstChild();
                    recursiveXmlParse(firstnode, id, false);
                    //                    NodeList headnodes = doc.getElementsByTagName("head");
                    //                    NodeList resultnodes = doc.getElementsByTagName("result");
                    //                    for (int i = 0; i < headnodes.getLength(); i++) {
                    //                        Element element = (Element) headnodes.item(i);
                    //                        String b = element.getElementsByTagName("binding").item(0).getTextContent();
                    //                        System.out.println(b);
                    //                        //            System.out.println(element.getNodeName());
                    //                    }
                    //                    for (int i = 0; i < resultnodes.getLength(); i++) {
                    //                        Element element = (Element) resultnodes.item(i);
                    //                        System.out.println(element.getTextContent());
                    //                    }
                } catch (Exception ex) {
                    //             System.out.println(ex.getMessage());
                }
            }
            stmturl.close();
            rsurl.close();

            // return id + 1;
        } catch (Exception ex) {
            //    System.out.println(ex.getMessage());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnParseResponseActionPerformed

    private void btnPrepareQueryQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrepareQueryQueueActionPerformed
        try {
            //            String SQL = "UPDATE queryqueue SET disabled =1;";
            //            Statement stmt = con.createStatement();
            //            stmt.execute(SQL);

            String SQL = "SELECT id, sparqlQuery FROM commonsparqlquery where enabled=1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                try {
                    int commonQueryId = rs.getInt(1);

                    String SQLurl = "select id,url from distinct_sparqlendpoint_urls;";
                    Statement stmturl = con1.createStatement();
                    ResultSet rsurl = stmturl.executeQuery(SQLurl);
                    while (rsurl.next()) {
                        try {
                            String url = rsurl.getString(2);
                            String SQLi = "INSERT INTO queryqueue (endpointUrl,commonQueryId, disabled) VALUES (?,?,0)";
                            PreparedStatement pstmt = con2.prepareStatement(SQLi);
                            pstmt.setString(1, url);
                            pstmt.setInt(2, commonQueryId);

                            pstmt.executeUpdate();
                            pstmt.close();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    stmturl.close();
                    rsurl.close();

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            stmt.close();
            rs.close();
            // return id + 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrepareQueryQueueActionPerformed

    private void btnStartRemoteQueriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartRemoteQueriesActionPerformed

        if (isStarted) {
            btnStartRemoteQueries.setBackground(Color.RED);
            btnStartRemoteQueries.setText("Start Remote Queries");
            for (int i = 0; i < maxThreadSparql; i++) {
                try {
                    ((WorkerSparql) threadsSparql[i]).stopRunning();
                    //threadArray[i++] = sworker;
                } catch (Exception ex) {

                }
            }

        } else {
            btnStartRemoteQueries.setText("Stop Remote Queries");
            btnStartRemoteQueries.setBackground(Color.GREEN);
            for (int i = 0; i < maxThreadSparql; i++) {
                WorkerSparql sworker = new WorkerSparql(this, connectionUrl);
                sworker.setName("Worker Remote Sparql");
                threadsSparql[i] = sworker;
                //threadArray[i++] = sworker;
                sworker.start();
                try {
                    //   Thread.sleep(157);
                } catch (Exception ex) {

                }
            }
        }
        isStarted = !isStarted;
    }//GEN-LAST:event_btnStartRemoteQueriesActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        boolean open = false;
        if (threadsSparql != null) {
            for (Thread t : threadsSparql) {
                try {
                    if (((WorkerSparql) t).isAlive()) {
                        open = true;
                    }
                } catch (Exception ex) {
                }
            }
        }
        if (threadsSearchQueue != null) {
            for (Thread t : threadsSearchQueue) {
                try {
                    if (((WorkerSearchQueue) t).isAlive()) {
                        open = true;
                    }
                } catch (Exception ex) {
                }
            }
        }
        if (threadsAnalysis != null) {
            for (Thread t : threadsAnalysis) {
                try {
                    if (((WorkerAnalyze) t).isAlive()) {
                        open = true;
                    }

                } catch (Exception ex) {
                }
            }
        }
        if (open) {
            int safe = JOptionPane.showConfirmDialog(null, "Please close all threads before closing. Do you want to enforce closing?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);

            if (safe == JOptionPane.YES_OPTION) {
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);//yes

            } else if (safe == JOptionPane.CANCEL_OPTION) {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//cancel
            } else {
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);//no
            }
        }

// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JFrame frame = new JFrame("JFileChooser Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        final JLabel directoryLabel = new JLabel(" ");
        directoryLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        contentPane.add(directoryLabel, BorderLayout.NORTH);

        final JLabel filenameLabel = new JLabel(" ");
        filenameLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        contentPane.add(filenameLabel, BorderLayout.SOUTH);

        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(true);
        contentPane.add(fileChooser, BorderLayout.CENTER);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser theFileChooser = (JFileChooser) actionEvent
                        .getSource();
                String command = actionEvent.getActionCommand();
                if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                    File selectedFile = theFileChooser.getSelectedFile();
                    directoryLabel.setText(selectedFile.getParent());
                    filenameLabel.setText(selectedFile.getName());
                } else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
                    directoryLabel.setText(" ");
                    filenameLabel.setText(" ");
                }
                frame.setVisible(false);
            }
        };
        fileChooser.addActionListener(actionListener);

        frame.pack();
        frame.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItemSaveTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveTaskActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemSaveTaskActionPerformed

    private void btnStatisticalAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatisticalAnalysisActionPerformed

        int numofthreads = Integer.parseInt(txtNumOfThreadsStatisticalAnalysis.getText());
        int timeout = Integer.parseInt(txtStatisticalAnalysisTimeout.getText()) * 1000;
        int checkHours = Integer.parseInt(txtStatisticalAnalysisCheckEveryHours.getText());

        threadsStatisticalAnalysis = new Thread[numofthreads];

        if (btnStatisticalAnalysis.getBackground().equals(Color.green)) {
            btnStatisticalAnalysis.setBackground(Color.red);
            btnStatisticalAnalysis.setText("Stop StatisticalAnalysis");

            //
            for (int i = 0; i < numofthreads; i++) {
                WorkerStatisticalAnalysis sworker = new WorkerStatisticalAnalysis(this, timeout, checkHours, connectionUrl, i, numofthreads);
                sworker.setName("Worker Endpoint Analyzer " + String.valueOf(i));
                threadsStatisticalAnalysis[i] = sworker;
                //threadArray[i++] = sworker;
                sworker.start();
            }
        } else {
            btnStatisticalAnalysis.setBackground(Color.green);
            btnStatisticalAnalysis.setText("Start Statistical Analysis");
            for (Thread t : threadsStatisticalAnalysis) {
                ((WorkerStatisticalAnalysis) t).stopRunning();
            }

        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnStatisticalAnalysisActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButtonExportRclhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportRclhActionPerformed
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select count(*) as number from recommender_class_label_hypernym;");
            int SIZE = 0;
            if (rs.next()) {
                SIZE = rs.getInt("number") / 1000000 + 1;
            }

            //   ;
            for (int i = 0; i < SIZE; i++) {
                PreparedStatement pstmt
                        = con.prepareStatement("SELECT e.endpointUrl, h.local_class_name,h.class_count,h.word,h.hypernym,h.tf,h.idf\n"
                                + "FROM crawler.recommender_class_label_hypernym  h inner join endpoints e on h.endpointid= e.id limit 1000000 offset " + String.valueOf(i * 1000000) + "\n"
                                + "INTO OUTFILE 'LABEL" + String.valueOf(i) + ".csv'\n"
                                + "FIELDS TERMINATED BY ','\n"
                                + "ENCLOSED BY '\"'\n"
                                + "LINES TERMINATED BY '\\n';"
                        );
                pstmt.execute();
                pstmt.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "HATA:" + ex.getMessage());
        }// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonExportRclhActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select endpointid, word as w from crawler.recommender_class_label_hypernym group by endpointid,w\n"
                            + "order by endpointid;");
            ResultSet rs = pstmtendpoints.executeQuery();
            HashMap<String, Integer> tfCount = new HashMap();
            HashMap<String, Integer> idfCount = new HashMap();

            //            while (rs.next()) {
            //                int endpointid = rs.getInt("endpointid");
            //
            //                String word = rs.getString("w").toLowerCase();
            //
            //                if (idfCount.containsKey(word)) {
            //                    idfCount.replace(word, idfCount.get(word) + 1);
            //                } else {
            //                    idfCount.put(word, 1);
            //                }
            //            }
            //            rs.close();
            PreparedStatement pstmtendpoints2
                    = con.prepareStatement("select endpointid, word as w,local_class_name as l from crawler.recommender_class_label_hypernym group by endpointid,w,l order by endpointid,l;");
            rs = pstmtendpoints2.executeQuery();
            String className = "";
            while (rs.next()) {
                int endpointid = rs.getInt("endpointid");
                String curclassName = rs.getString("l");
                String word = rs.getString("w").toLowerCase();

                String idword = String.valueOf(endpointid) + "-" + word;
                if (tfCount.containsKey(idword)) {
                    if (!curclassName.equals(className)) {
                        tfCount.replace(idword, tfCount.get(idword) + 1);
                        className = curclassName;
                    }
                } else {
                    tfCount.put(idword, 1);
                }

                if (idfCount.containsKey(word)) {
                    idfCount.replace(word, idfCount.get(word) + 1);
                } else {
                    idfCount.put(word, 1);
                }
            }
            Iterator ittf = tfCount.entrySet().iterator();
            Iterator itidf = idfCount.entrySet().iterator();
            try {
                PreparedStatement updatestmt
                        = con.prepareStatement("update recommender_class_label_hypernym set tf=? where endpointid=? and word=?");
                int count = 0;
                while (ittf.hasNext()) {
                    Map.Entry pair = (Map.Entry) ittf.next();
                    //<>
                    if (Integer.parseInt(pair.getValue().toString()) > 0) {
                        updatestmt.setInt(1, Integer.parseInt(pair.getValue().toString()));
                        updatestmt.setInt(2, Integer.parseInt(pair.getKey().toString().split("-")[0]));
                        updatestmt.setString(3, pair.getKey().toString().split("-")[1]);
                        updatestmt.addBatch();
                        count++;
                    }
                }
                updatestmt.executeBatch();
            } catch (Exception ex) {
                System.out.println("tf update edilemedi");
            }
            try {
                PreparedStatement updatestmt
                        = con.prepareStatement("update recommender_class_label_hypernym set idf=? where word=?");

                while (itidf.hasNext()) {
                    Map.Entry pair = (Map.Entry) itidf.next();
                    //<>
                    if (Integer.parseInt(pair.getValue().toString()) > 0) {
                        updatestmt.setInt(1, Integer.parseInt(pair.getValue().toString()));
                        updatestmt.setString(2, pair.getKey().toString());
                        updatestmt.addBatch();
                    }
                }
                updatestmt.executeBatch();
            } catch (Exception ex) {
                System.out.println("tf update edilemedi");
            }
        } catch (Exception e) {

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButtonPrepareEndpointLcnWordTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrepareEndpointLcnWordTableActionPerformed
        try {
            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select id, endpointurl from endpoints where lastCheckedDate is null;");
            ResultSet rs1 = pstmtendpoints.executeQuery();
            int endpointid = 0;
            int queryid = 27;//class with label collector
            while (rs1.next()) {

                try {
                    endpointid = rs1.getInt("id");

                    PreparedStatement pstmt
                            = con.prepareStatement("select s,p,o from endpointtriples where queryid=? and endpointid=?;");
                    pstmt.setInt(1, queryid);
                    pstmt.setInt(2, endpointid);

                    // execute the query, and get a java resultset
                    ResultSet rs = pstmt.executeQuery();

                    PreparedStatement insertpstmt
                            = con.prepareStatement("insert into recommender_class_label_hypernym (endpointid,local_class_name, class_count, word, hypernym) values(?,?,?,?,?);");

                    // iterate through the java resultset
                    HashMap<String, Integer> wordCount = new HashMap();
                    HashMap<String, Integer> lcnWordCount = new HashMap();
                    int totalNumberOfWords = 0;
                    while (rs.next()) {
                        String classURI = rs.getString("s");
                        int classcount = Integer.parseInt(rs.getString("o"));
                        PreparedStatement pstmtcounts
                                = con.prepareStatement("select s,o from endpointtriples where queryid=? and endpointid=? and s =?;");
                        pstmtcounts.setInt(1, 26);
                        pstmtcounts.setInt(2, endpointid);
                        pstmtcounts.setString(3, classURI);

                        ResultSet rs2 = pstmtcounts.executeQuery();
                        String className, label;
                        if (rs2.next()) {

                            label = rs2.getString("o");

                            className = limitString(classURI.split("/")[classURI.split("/").length - 1], 45);

                            pstmtcounts.cancel();
                            rs2.close();

                            //                    String p = rs.getString("p");
                            //                    String o = rs.getString("o");
                            if (label != null) {
                                String words[] = label.split(" ");
                                for (String word : words) {
                                    String cleanword;

                                    cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
                                    if (!cleanword.equals("")) {
                                        if (!word.equals(cleanword)) {
                                            word = cleanword;//System.out.println(word+"--"+cleanword);
                                        }

                                        if (wordCount.containsKey(word)) {
                                            int currentCount = wordCount.get(word);
                                            wordCount.replace(word, currentCount + 1);

                                        } else {
                                            wordCount.put(word, 1);
                                        }

                                        for (String hyp : getHypernyms(word)) {
                                            insertpstmt.setInt(1, endpointid);
                                            //                                    if (s.split("/")[s.split("/").length - 1].length() > 45) {
                                            //                                        insertpstmt.setString(2, s.split("/")[s.split("/").length - 1].substring(0, 44));
                                            //                                    } else {
                                            //                                        insertpstmt.setString(2, s.split("/")[s.split("/").length - 1]);
                                            //                                    }
                                            insertpstmt.setString(2, className);
                                            insertpstmt.setInt(3, classcount);
                                            insertpstmt.setString(4, word);
                                            insertpstmt.setString(5, hyp);
                                            insertpstmt.addBatch();
                                            // insertpstmt.setInt(6,);
                                        }
                                    }
                                    totalNumberOfWords++;
                                }
                            }
                        } else {
                            //  System.out.println("bos");
                        }

                    }
                    insertpstmt.executeBatch();

                    PreparedStatement pstmtupdateendpoint = con.prepareStatement("update endpoints set lastCheckedDate=? where id=?;");
                    pstmtupdateendpoint.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                    pstmtupdateendpoint.setInt(2, endpointid);
                    pstmtupdateendpoint.execute();
                    pstmtupdateendpoint.close();
                    //String firstName = rs.getString("url");
                    pstmt.close();
                    rs.close();
                    //                Iterator it = lcnWordCount.entrySet().iterator();
                    //                while (it.hasNext()) {
                    //                    Map.Entry pair = (Map.Entry) it.next();
                    //                    //<>
                    //                    if (Integer.parseInt(pair.getValue().toString()) > 1) {
                    //                        PreparedStatement insertpstmt
                    //                                = con.prepareStatement("insert into recommender_class_label_hypernym (endpointid,local_class_name, class_count, word, hypernym,tf) values(?,?,?,?,?);");
                    //                        if (pair.getKey().toString().length() > 44) {
                    //                            insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
                    //                        } else {
                    //                            insertpstmt.setString(1, pair.getKey().toString());
                    //                        }
                    //                        insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));
                    //                        insertpstmt.setInt(3, endpointid);
                    //                        insertpstmt.setInt(4, queryid);
                    //                        insertpstmt.executeUpdate();
                    //                        insertpstmt.close();
                    //                    }
                    //                    it.remove(); // avoids a ConcurrentModificationException
                    //                }

                    //                PreparedStatement updatepstmt
                    //                        = con.prepareStatement("update endpoints set commentsWordCount=? where id=?;");
                    //                updatepstmt.setInt(1, totalNumberOfWords);
                    //                updatepstmt.setInt(2, endpointid);
                    //                updatepstmt.executeUpdate();
                    //                updatepstmt.close();
                } catch (Exception e) {
                    System.err.println("inner while" + e.getMessage());
                }

            }
            pstmtendpoints.close();

        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrepareEndpointLcnWordTableActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            PreparedStatement pstmtIDF
                    = con.prepareStatement("SELECT * FROM crawler.recommender_comment_idf;");
            PreparedStatement pstmtTF
                    = con.prepareStatement("SELECT * FROM crawler.recommender_comment_tf;");

            ResultSet rsIDF = pstmtIDF.executeQuery();
            ResultSet rsTF = pstmtTF.executeQuery();

            HashMap<String, Integer> tfCount = new HashMap();
            HashMap<String, Integer> idfCount = new HashMap();

            while (rsTF.next()) {
                String idword = String.valueOf(rsTF.getInt("endpointid")) + "-" + rsTF.getString("word");
                int count = rsTF.getInt("count");
                tfCount.put(idword, count);
            }
            while (rsIDF.next()) {
                String word = rsIDF.getString("word");
                int count = rsIDF.getInt("count");
                idfCount.put(word, count);
            }
            int idflimit = 100;

            HashMap<String, Integer> toberemoved = new HashMap();

            Iterator ittf = tfCount.entrySet().iterator();
            while (ittf.hasNext()) {
                Map.Entry pair = (Map.Entry) ittf.next();
                String idword = pair.getKey().toString();
                int count = Integer.parseInt(pair.getValue().toString());
                if (idfCount.get(idword.split("-")[1]) != null) {
                    if (idfCount.get(idword.split("-")[1]) > idflimit || idfCount.get(idword.split("-")[1]) < 10 || count < 10) {
                        toberemoved.put(idword, 0);
                    }
                }
            }

            Iterator itremove = toberemoved.entrySet().iterator();
            while (itremove.hasNext()) {
                Map.Entry pair = (Map.Entry) itremove.next();
                try {
                    tfCount.remove(pair.getKey().toString());
                } catch (Exception ex) {

                }
            }

            Iterator itidf = idfCount.entrySet().iterator();
            try {
                PreparedStatement updatestmt
                        = con.prepareStatement("update recommender_class_label_hypernym set tf=? where endpointid=? and word=?");
                int count = 0;
                while (ittf.hasNext()) {
                    Map.Entry pair = (Map.Entry) ittf.next();
                    //<>
                    if (Integer.parseInt(pair.getValue().toString()) > 0) {
                        updatestmt.setInt(1, Integer.parseInt(pair.getValue().toString()));
                        updatestmt.setInt(2, Integer.parseInt(pair.getKey().toString().split("-")[0]));
                        updatestmt.setString(3, pair.getKey().toString().split("-")[1]);
                        updatestmt.addBatch();
                        count++;
                    }
                }
                updatestmt.executeBatch();
            } catch (Exception ex) {
                System.out.println("tf update edilemedi");
            }
            try {
                PreparedStatement updatestmt
                        = con.prepareStatement("update recommender_class_label_hypernym set idf=? where word=?");

                while (itidf.hasNext()) {
                    Map.Entry pair = (Map.Entry) itidf.next();
                    //<>
                    if (Integer.parseInt(pair.getValue().toString()) > 0) {
                        updatestmt.setInt(1, Integer.parseInt(pair.getValue().toString()));
                        updatestmt.setString(2, pair.getKey().toString());
                        updatestmt.addBatch();
                    }
                }
                updatestmt.executeBatch();
            } catch (Exception ex) {
                System.out.println("tf update edilemedi");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // TODO add your handling code here:

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButtonExportSelectedTriplesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportSelectedTriplesActionPerformed
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select count(*) as numberoftriples from endpointtriples where queryid=" + jComboCommonQueries.getSelectedIndex() + ";");
            int SIZE = 0;
            if (rs.next()) {
                SIZE = rs.getInt("numberoftriples") / 1000000 + 1;
            }
            for (int i = 0; i < SIZE; i++) {
                PreparedStatement pstmt
                        = con.prepareStatement("SELECT *\n"
                                + "FROM endpointtriples limit 1000000 offset " + String.valueOf(i * 1000000) + "\n"
                                + "INTO OUTFILE 'endpointriples" + String.valueOf(i) + ".csv'\n"
                                + "FIELDS TERMINATED BY ','\n"
                                + "ENCLOSED BY '\"'\n"
                                + "LINES TERMINATED BY '\\n';"
                        );
                pstmt.execute();
                pstmt.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "HATA:" + ex.getMessage());
        }// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonExportSelectedTriplesActionPerformed

    private void btnSameAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSameAsActionPerformed

        try {
            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select id, endpointurl from endpoints where outLinksCount is null;");
            ResultSet rs1 = pstmtendpoints.executeQuery();
            int endpointid = 0;
            int queryid = 23;//sameas ext
            while (rs1.next()) {
                endpointid = rs1.getInt("id");
                int hashTotalCount = 0;
                PreparedStatement pstmt
                        = con.prepareStatement("select s,p,o from endpointtriples where queryid=? and endpointid=?;");
                pstmt.setInt(1, queryid);
                pstmt.setInt(2, endpointid);

                // execute the query, and get a java resultset
                ResultSet rs = pstmt.executeQuery();;

                // iterate through the java resultset
                HashMap<String, Integer> hash = new HashMap();
                int totalNumberOfWords = 0;
                while (rs.next()) {
                    String s = rs.getString("s");
                    String p = rs.getString("p");
                    String o = rs.getString("o");
                    if (o != null) {
                        String host = "";
                        try {
                            URL u = new URL(o);
                            host = u.getHost();
                            if (InternetDomainName.isValid(host)) {
                                host = InternetDomainName.from(host).topPrivateDomain().toString();
                            } else {
                                if (com.google.common.net.HostSpecifier.isValid(host)) {
                                    host = com.google.common.net.HostSpecifier.from(host).toString();
                                } else {
                                    host = "";
                                }
                            }
                        } catch (Exception ex) {
                        }

                        if (hash.containsKey(host)) {
                            hash.replace(host, hash.get(host) + 1);
                        } else {
                            hash.put(host, 1);
                        }

                    }
                    //String firstName = rs.getString("url");
                }
                pstmt.close();
                rs.close();

                Iterator it = hash.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    //<>
                    if (Integer.parseInt(pair.getValue().toString()) > 1) {
                        PreparedStatement insertpstmt
                                = con.prepareStatement("insert into recommender_sameas_pldcount (pld,endpointid,count,queryid) values(?,?,?,?);");
                        if (pair.getKey().toString().length() > 44) {
                            insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
                        } else {
                            insertpstmt.setString(1, pair.getKey().toString());
                        }
                        insertpstmt.setInt(3, Integer.parseInt(pair.getValue().toString()));
                        insertpstmt.setInt(2, endpointid);
                        insertpstmt.setInt(4, queryid);
                        insertpstmt.executeUpdate();
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }
                PreparedStatement updatepstmt
                        = con.prepareStatement("update endpoints set outLinksCount=? where id=?;");
                updatepstmt.setInt(1, hash.size());
                updatepstmt.setInt(2, endpointid);
                updatepstmt.executeUpdate();
            }
        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSameAsActionPerformed

    private void jButtonIDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIDFActionPerformed

        //       calculateIDF(25, "label");
        calculateIDF(26, "classwithlabel");

//
//        try {
//
//            PreparedStatement pstmt
//                    = con.prepareStatement("select s,p,o, endpointid from endpointtriples where queryid=? order by endpointid;");
//            pstmt.setInt(1, 24);
//
////            
////update recommender_comment_tf set idfid = (select id from recommender_comment_idf 
////where word = recommender_comment_tf.word limit 1) where id>0 and idfid is  null limit 500000;
//            // execute the query, and get a java resultset
//            ResultSet rs = pstmt.executeQuery();;
//
//            // iterate through the java resultset
//            HashMap<String, Integer> wordCount = new HashMap();
//            HashMap<String, Integer> localwordCount = new HashMap();
//            int totalNumberOfWords = 0;
//            int endpointid = 0;
//
//            while (rs.next()) {
//                int currentEndpointid = rs.getInt("endpointid");
//                String s = rs.getString("s");
//                String p = rs.getString("p");
//                String o = rs.getString("o");
//                if (endpointid != currentEndpointid) {
//                    localwordCount.clear();
//                    endpointid = currentEndpointid;
//                }
//                if (o != null) {
//                    String words[] = o.split(" ");
//                    for (String word : words) {
//                        String cleanword;
//
//                        cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
//                        if (!cleanword.equals("")) {
//                            if (!word.equals(cleanword)) {
//                                word = cleanword;//System.out.println(word+"--"+cleanword);
//                            }
//                            word = word.toLowerCase().replace("ı", "i");
//
//                            if (wordCount.containsKey(word)) {
//                                if (!localwordCount.containsKey(word)) {
//                                    wordCount.replace(word, wordCount.get(word) + 1);
//                                }
//                            } else {
//                                wordCount.put(word, 1);
//                            }
//                            if (!localwordCount.containsKey(word)) {
//
//                                localwordCount.put(word, 1);
//                            }
//
//                            totalNumberOfWords++;
//                        } else {
//                            //  System.out.println("bos");
//                        }
//
//                    }
//                }
//                //String firstName = rs.getString("url");
//            }
//
//            pstmt.close();
//            rs.close();
//            Iterator it = wordCount.entrySet().iterator();
//            PreparedStatement insertpstmt
//                    = con.prepareStatement("insert into recommender_comment_idf (word,count,queryid) values(?,?,?);");
//
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//                //<>
//                if (Integer.parseInt(pair.getValue().toString()) > 0) {
//                    if (pair.getKey().toString().length() > 44) {
//                        insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
//                    } else {
//                        insertpstmt.setString(1, pair.getKey().toString());
//                    }
//                    insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));
//                    insertpstmt.setInt(3, 24);
//                    insertpstmt.addBatch();
//                }
//                it.remove(); // avoids a ConcurrentModificationException
//            }
//
//            //                pstmt.close();
//            //                rs.close();
//            //        updatepstmt.executeBatch();
//            insertpstmt.executeBatch();
//        } catch (Exception e) {
//            //System.err.println("Got an exception! ");
//            System.err.println(e.getMessage());
//        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonIDFActionPerformed

    private void btnTfidfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTfidfActionPerformed
//        calculateTF(24, "comment");
        calculateTF(26, "classwithlabel");

//        try {
//            PreparedStatement pstmtendpoints
//                    = con.prepareStatement("select id, endpointurl from endpoints where commentsWordCount is null;");
//            ResultSet rs1 = pstmtendpoints.executeQuery();
//            int endpointid = 0;
//            int queryid = 24;
//            while (rs1.next()) {
//                endpointid = rs1.getInt("id");
//
//                PreparedStatement pstmt
//                        = con.prepareStatement("select s,p,o from endpointtriples where queryid=? and endpointid=?;");
//                pstmt.setInt(1, queryid);
//                pstmt.setInt(2, endpointid);
//
//                // execute the query, and get a java resultset
//                ResultSet rs = pstmt.executeQuery();;
//
//                // iterate through the java resultset
//                HashMap<String, Integer> wordCount = new HashMap();
//                int totalNumberOfWords = 0;
//                while (rs.next()) {
//                    String s = rs.getString("s");
//                    String p = rs.getString("p");
//                    String o = rs.getString("o");
//                    if (o != null) {
//                        String words[] = o.split(" ");
//                        for (String word : words) {
//                            String cleanword;
//
//                            cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
//                            if (!cleanword.equals("")) {
//                                if (!word.equals(cleanword)) {
//                                    word = cleanword;//System.out.println(word+"--"+cleanword);
//                                }
//                                word = word.toLowerCase().replace("ı", "i");
//                                if (wordCount.containsKey(word)) {
//                                    wordCount.replace(word, wordCount.get(word) + 1);
//                                } else {
//                                    wordCount.put(word, 1);
//                                }
//                                totalNumberOfWords++;
//                            } else {
//                                //  System.out.println("bos");
//                            }
//
//                        }
//                    }
//                    //String firstName = rs.getString("url");
//                }
//
//                pstmt.close();
//                rs.close();
//                Iterator it = wordCount.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//
//                    if (Integer.parseInt(pair.getValue().toString()) > 1) {
//                        PreparedStatement insertpstmt
//                                = con.prepareStatement("insert into recommender_comment_tf (word,count,endpointid,queryid,totalNumberofWords) values(?,?,?,?,?);");
//                        if (pair.getKey().toString().length() > 44) {
//                            insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
//                        } else {
//                            insertpstmt.setString(1, pair.getKey().toString());
//                        }
//                        insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));
//                        insertpstmt.setInt(3, endpointid);
//                        insertpstmt.setInt(4, queryid);
//                        insertpstmt.setInt(5, totalNumberOfWords);
//
//                        insertpstmt.executeUpdate();
//                        insertpstmt.close();
//                    }
//                    it.remove(); // avoids a ConcurrentModificationException
//                }
//
//                PreparedStatement updatepstmt
//                        = con.prepareStatement("update endpoints set commentsWordCount=? where id=?;");
//                updatepstmt.setInt(1, totalNumberOfWords);
//                updatepstmt.setInt(2, endpointid);
//                updatepstmt.executeUpdate();
//                updatepstmt.close();
//            }
//            pstmtendpoints.close();
//        } catch (Exception e) {
//            //System.err.println("Got an exception! ");
//            System.err.println(e.getMessage());
//        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTfidfActionPerformed
    private void calculateIDF(int queryid, String objectName) {
        try {
            try {
                PreparedStatement createtablepstmt
                        = con.prepareStatement(
                                "CREATE TABLE `recommender_" + objectName + "_idf` (\n"
                                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                                + "  `word` varchar(45) DEFAULT NULL,\n"
                                + "  `queryid` int(11) DEFAULT NULL,\n"
                                + "  `count` int(11) DEFAULT NULL,\n"
                                + "  `wordnetProcessed` int(11) DEFAULT NULL,\n"
                                + "  `wordnetHypernym` varchar(45) DEFAULT NULL,\n"
                                + "  PRIMARY KEY (`id`),\n"
                                + "  KEY `word` (`word`),\n"
                                + "  KEY `wordnetHypernym` (`wordnetHypernym`)\n"
                                + ") ENGINE=InnoDB AUTO_INCREMENT=1695533 DEFAULT CHARSET=utf8;");
                createtablepstmt.execute();
            } catch (Exception ex) {

            }

            PreparedStatement pstmt
                    = con.prepareStatement("select s,p,o, endpointid from endpointtriples where queryid=? order by endpointid;");
            pstmt.setInt(1, queryid);

//            
//update recommender_comment_tf set idfid = (select id from recommender_comment_idf 
//where word = recommender_comment_tf.word limit 1) where id>0 and idfid is  null limit 500000;
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            HashMap<String, Integer> wordCount = new HashMap();
            HashMap<String, Integer> localwordCount = new HashMap();
            int totalNumberOfWords = 0;
            int endpointid = 0;

            while (rs.next()) {
                int currentEndpointid = rs.getInt("endpointid");
                String s = rs.getString("s");
                String p = rs.getString("p");
                String o = rs.getString("o");
                if (endpointid != currentEndpointid) {
                    localwordCount.clear();
                    endpointid = currentEndpointid;
                }
                if (o != null) {
                    String words[] = o.split(" ");
                    for (String word : words) {
                        String cleanword;

                        cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
                        if (!cleanword.equals("")) {
                            if (!word.equals(cleanword)) {
                                word = cleanword;//System.out.println(word+"--"+cleanword);
                            }
                            word = word.toLowerCase().replace("ı", "i");

                            if (wordCount.containsKey(word)) {
                                if (!localwordCount.containsKey(word)) {
                                    wordCount.replace(word, wordCount.get(word) + 1);
                                }
                            } else {
                                wordCount.put(word, 1);
                            }
                            if (!localwordCount.containsKey(word)) {

                                localwordCount.put(word, 1);
                            }

                            totalNumberOfWords++;
                        } else {
                            //  System.out.println("bos");
                        }

                    }
                }
                //String firstName = rs.getString("url");
            }

            pstmt.close();
            rs.close();
            Iterator it = wordCount.entrySet().iterator();
            PreparedStatement insertpstmt
                    = con.prepareStatement("insert into recommender_" + objectName + "_idf (word,count,queryid) values(?,?,?);");

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //<>
                if (Integer.parseInt(pair.getValue().toString()) > 0) {
                    if (pair.getKey().toString().length() > 44) {
                        insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
                    } else {
                        insertpstmt.setString(1, pair.getKey().toString());
                    }
                    insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));
                    insertpstmt.setInt(3, 24);
                    insertpstmt.addBatch();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

            //                pstmt.close();
            //                rs.close();
            //        updatepstmt.executeBatch();
            insertpstmt.executeBatch();
            PreparedStatement s = con.prepareStatement("SET SQL_SAFE_UPDATES = 0; "
                    + "UPDATE recommender_" + objectName + "_tf as t, recommender_" + objectName + "_idf as i "
                    + "SET t.idfid = i.id "
                    + "where t.word=i.word;");
            s.executeQuery();

        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    private void calculateTF(int queryid, String objectName) {
        try {
            try {
                PreparedStatement createtablepstmt
                        = con.prepareStatement(
                                "CREATE TABLE `recommender_" + objectName + "_tf` (\n"
                                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                                + "  `word` varchar(45) DEFAULT NULL,\n"
                                + "  `endpointid` int(11) DEFAULT NULL,\n"
                                + "  `queryid` int(11) DEFAULT NULL,\n"
                                + "  `count` int(11) DEFAULT NULL,\n"
                                + "  `idf` int(11) DEFAULT NULL,\n"
                                + "  `idfid` int(11) DEFAULT NULL,\n"
                                + "  `totalNumberofWords` int(11) DEFAULT NULL,\n"
                                + "  PRIMARY KEY (`id`),\n"
                                + "  KEY `word` (`word`),\n"
                                + "  KEY `idf` (`idfid`),\n"
                                + "  KEY `endpointid` (`endpointid`)\n"
                                + ") ENGINE=InnoDB AUTO_INCREMENT=1261099 DEFAULT CHARSET=utf8;"
                        );
                createtablepstmt.execute();
            } catch (Exception ex) {

            }

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select id, endpointurl from endpoints where tfidfprocessed is null;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            int endpointid = 0;
            while (rs1.next()) {
                endpointid = rs1.getInt("id");

                PreparedStatement pstmt
                        = con.prepareStatement("select s,p,o from endpointtriples where queryid=? and endpointid=?;");
                pstmt.setInt(1, queryid);
                pstmt.setInt(2, endpointid);

                // execute the query, and get a java resultset
                ResultSet rs = pstmt.executeQuery();;

                // iterate through the java resultset
                HashMap<String, Integer> wordCount = new HashMap();
                int totalNumberOfWords = 0;
                while (rs.next()) {
                    String s = rs.getString("s");
                    String p = rs.getString("p");
                    String o = rs.getString("o");
                    if (o != null) {
                        String words[] = o.split(" ");
                        for (String word : words) {
                            String cleanword;

                            cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
                            if (!cleanword.equals("")) {
                                if (!word.equals(cleanword)) {
                                    word = cleanword;//System.out.println(word+"--"+cleanword);
                                }
                                word = word.toLowerCase().replace("ı", "i");
                                if (wordCount.containsKey(word)) {
                                    wordCount.replace(word, wordCount.get(word) + 1);
                                } else {
                                    wordCount.put(word, 1);
                                }
                                totalNumberOfWords++;
                            } else {
                            }

                        }
                    }
                }

                pstmt.close();
                rs.close();
                Iterator it = wordCount.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    if (Integer.parseInt(pair.getValue().toString()) > 1) {
                        PreparedStatement insertpstmt
                                = con.prepareStatement("insert into recommender_" + objectName + "_tf (word,count,endpointid,queryid,totalNumberofWords) values(?,?,?,?,?);");
                        if (pair.getKey().toString().length() > 44) {
                            insertpstmt.setString(1, pair.getKey().toString().substring(0, 44));
                        } else {
                            insertpstmt.setString(1, pair.getKey().toString());
                        }
                        insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));
                        insertpstmt.setInt(3, endpointid);
                        insertpstmt.setInt(4, queryid);
                        insertpstmt.setInt(5, totalNumberOfWords);

                        insertpstmt.executeUpdate();
                        insertpstmt.close();
                    }
                    it.remove(); // avoids a ConcurrentModificationException
                }

                PreparedStatement updatepstmt
                        = con.prepareStatement("update endpoints set tfidfprocessed=1 where id=?;");
                // updatepstmt.setInt(1, totalNumberOfWords);
                updatepstmt.setInt(1, endpointid);
                updatepstmt.executeUpdate();
                updatepstmt.close();
            }
            pstmtendpoints.close();

        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    private void updateDomainsOfEndpoints() {
        try {

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select id, endpointUrl from endpoints;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            while (rs1.next()) {
                String endpointUrl = rs1.getString("endpointUrl");
                int id = rs1.getInt("id");
                URL u = new URL(endpointUrl);
                String host = u.getHost();
                if (InternetDomainName.isValid(host)) {
                    host = InternetDomainName.from(host).topPrivateDomain().toString();
                } else {
                    if (com.google.common.net.HostSpecifier.isValid(host)) {
                        host = com.google.common.net.HostSpecifier.from(host).toString();
                    } else {
                        host = "";
                    }
                }
                PreparedStatement updatepstmt
                        = con.prepareStatement("update endpoints set domain = ? where id = ?");
                updatepstmt.setString(1, host);
                updatepstmt.setInt(2, id);
                updatepstmt.executeUpdate();
            }
            pstmtendpoints.close();
            rs1.close();

        } catch (Exception ex) {

        }
        try {

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("select domain from endpoints group by domain;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            while (rs1.next()) {
                String domain = rs1.getString("domain");

                PreparedStatement pstmtendpointsdomains
                        = con.prepareStatement("select domain,endpointUrl,source from endpoints where domain=?;");
                pstmtendpointsdomains.setString(1, domain);
                ResultSet rs2 = pstmtendpointsdomains.executeQuery();

                String sourceDomain = "";
                while (rs2.next()) {
                    String source = rs2.getString("source");
                    String[] sources = source.split("-");
                    for (String s : sources) {
                        if (!sourceDomain.contains(s)) {
                            if (sourceDomain != "") {
                                sourceDomain += "-" + s;
                            } else {
                                sourceDomain += s;
                            }
                        }
                    }
                }
                PreparedStatement updatepstmt
                        = con.prepareStatement("update endpoints set domainSource = ? where domain = ?");
                updatepstmt.setString(1, sourceDomain);
                updatepstmt.setString(2, domain);
                updatepstmt.executeUpdate();
            }
            pstmtendpoints.close();
            rs1.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updateEndpointsFromRawTable() {
        try {

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("SELECT url FROM crawler.seedurlraw where isEndpoint =2  group by url;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            while (rs1.next()) {
                String endpointUrl = rs1.getString("url");
                if (endpointUrl.trim().substring(endpointUrl.trim().length() - 1).equals("/")) {
                    endpointUrl = endpointUrl.trim().substring(0, endpointUrl.trim().length() - 1);
                }

                PreparedStatement pstmtendpointsingle
                        = con.prepareStatement("SELECT endpointUrl,source FROM crawler.endpoints where endpointUrl =? ;");
                pstmtendpointsingle.setString(1, endpointUrl);
                ResultSet rsesingle = pstmtendpointsingle.executeQuery();
                if (rsesingle.next()) {
                    String source = rsesingle.getString("source");
                    if (source.contains("spend")) {
                        // do nothing
                    } else {
                        DateFormat df = new SimpleDateFormat("yyyyMMdd");
                        java.util.Date today = java.util.Calendar.getInstance().getTime();
                        String reportDate = df.format(today);
                        PreparedStatement updatepstmt
                                = con.prepareStatement("update endpoints set source = concat(source, ?) where endpointUrl = ?");
                        updatepstmt.setString(1, "-spendauto" + reportDate);
                        updatepstmt.setString(2, endpointUrl);
                        updatepstmt.executeUpdate();
                    }
                } else {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    java.util.Date today = java.util.Calendar.getInstance().getTime();
                    String reportDate = df.format(today);
                    PreparedStatement updatepstmt
                            = con.prepareStatement("insert into endpoints (source,endpointUrl,active,totalcheck) values (?,?,0,0)");
                    updatepstmt.setString(1, "spendauto" + reportDate);
                    updatepstmt.setString(2, endpointUrl);
                    updatepstmt.executeUpdate();
                }
//                URL u = new URL(endpointUrl);
//                String host = u.getHost();
//                if (InternetDomainName.isValid(host)) {
//                    host = InternetDomainName.from(host).topPrivateDomain().toString();
//                } else {
//                    if (com.google.common.net.HostSpecifier.isValid(host)) {
//                        host = com.google.common.net.HostSpecifier.from(host).toString();
//                    } else {
//                        host = "";
//                    }
//                }
//                PreparedStatement updatepstmt
//                        = con.prepareStatement("update endpoints set domain = ? where id = ?");
//                updatepstmt.setString(1, host);
//                updatepstmt.setInt(2, id);
//                updatepstmt.executeUpdate();
            }
            pstmtendpoints.close();
            rs1.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void calculateEndpointWordsTF() {
        try {
            try {
                PreparedStatement createtablepstmt
                        = con.prepareStatement(
                                "CREATE TABLE `recommender_endpoints_words_tf` (\n"
                                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                                + "  `word` LONGTEXT DEFAULT NULL,\n"
                                + "  `endpointid` int(11) DEFAULT NULL,\n"
                                + "  `queryid` int(11) DEFAULT NULL,\n"
                                + "  `count` int(11) DEFAULT NULL,\n"
                                + "  `idf` int(11) DEFAULT NULL,\n"
                                + "  `idfid` int(11) DEFAULT NULL,\n"
                                + "  `totalNumberofWords` int(11) DEFAULT NULL,\n"
                                + "  PRIMARY KEY (`id`),\n"
                                + "  KEY `idf` (`idfid`),\n"
                                + "  KEY `endpointid` (`endpointid`)\n"
                                + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;"
                        );
                createtablepstmt.execute();
            } catch (Exception ex) {

            }

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("SELECT * from crawler.endpoints where sourceCode is not null and sourceCode NOT LIKE 'EMPT%' and source != 'spendold' and source != 'spendnew' group by domain ORDER BY id asc;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            String htmlsource = "";
            HashMap<String, Integer> wordCount = new HashMap();
            while (rs1.next()) {

                HashMap<String, Integer> localwordCount = new HashMap();
                htmlsource = rs1.getString("sourceCode");
                String words[] = htmlsource.split("\\s+");
                for (String word : words) {
                    String cleanword;

                    cleanword = word.replaceAll("[^\\p{L}\\p{Nd}]+", "");
                    if (!cleanword.equals("")) {
                        if (!word.equals(cleanword)) {
                            word = cleanword;//System.out.println(word+"--"+cleanword);
                        }
                        word = word.toLowerCase().replace("ı", "i");

                        if (wordCount.containsKey(word) && !localwordCount.containsKey(word)) {
                            wordCount.replace(word, wordCount.get(word) + 1);
                        } else if (!wordCount.containsKey(word)) {
                            wordCount.put(word, 1);
                        }

                        if (localwordCount.containsKey(word)) {
//                            wordCount.replace(word, wordCount.get(word) + 1);
                        } else {
                            localwordCount.put(word, 1);
                        }

                    } else {
                    }

                }
            }

            pstmtendpoints.close();
            rs1.close();
            Iterator it = wordCount.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (Integer.parseInt(pair.getValue().toString()) > 1) {
                    PreparedStatement insertpstmt
                            = con.prepareStatement("insert into recommender_endpoints_words_tf (word,count) values(?,?);");
                    if (pair.getKey().toString().length() > 44) {
                        insertpstmt.setString(1, pair.getKey().toString());
                    } else {
                        insertpstmt.setString(1, pair.getKey().toString());
                    }
                    insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));

                    insertpstmt.executeUpdate();
                    insertpstmt.close();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    private void calculateEndpointTagsTF() {
        try {
            try {
                PreparedStatement createtablepstmt
                        = con.prepareStatement(
                                "CREATE TABLE `recommender_endpoints_tf` (\n"
                                + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                                + "  `word` varchar(45) DEFAULT NULL,\n"
                                + "  `endpointid` int(11) DEFAULT NULL,\n"
                                + "  `queryid` int(11) DEFAULT NULL,\n"
                                + "  `count` int(11) DEFAULT NULL,\n"
                                + "  `idf` int(11) DEFAULT NULL,\n"
                                + "  `idfid` int(11) DEFAULT NULL,\n"
                                + "  `totalNumberofWords` int(11) DEFAULT NULL,\n"
                                + "  PRIMARY KEY (`id`),\n"
                                + "  KEY `word` (`word`),\n"
                                + "  KEY `idf` (`idfid`),\n"
                                + "  KEY `endpointid` (`endpointid`)\n"
                                + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;"
                        );
                createtablepstmt.execute();
            } catch (Exception ex) {

            }

            PreparedStatement pstmtendpoints
                    = con.prepareStatement("SELECT * from crawler.endpoints where sourceCodeHTML is not null and source != 'spendold' and source != 'spendnew' group by domain ORDER BY id asc;");
            ResultSet rs1 = pstmtendpoints.executeQuery();

            String htmlsource = "";
            HashMap<String, Integer> wordCount = new HashMap();

            while (rs1.next()) {
                htmlsource = rs1.getString("sourceCodeHTML");
                HashMap<String, Integer> localwordCount = new HashMap();
                org.jsoup.nodes.Document doc = Jsoup.parse(htmlsource);//.connect("http://en.wikipedia.org/").get();
                //Elements newsHeadlines = doc.select("#mp-itn b a");

                Elements links = doc.getElementsByTag("a");
                Elements labels = doc.getElementsByTag("Label");
                Elements spans = doc.getElementsByTag("span");
                Elements titles = doc.getElementsByTag("title");
                Elements meta = doc.getElementsByTag("meta");
                Elements h2 = doc.getElementsByTag("h2");
                Elements h1 = doc.getElementsByTag("h1");
                Elements h3 = doc.getElementsByTag("h3");
                Elements li = doc.getElementsByTag("li");
                Elements dt = doc.getElementsByTag("dt");
                Elements p = doc.getElementsByTag("p");
                Elements option = doc.getElementsByTag("option");

                links.addAll(labels);
                links.addAll(spans);
                links.addAll(titles);
                links.addAll(meta);
                links.addAll(h2);
                links.addAll(h1);
                links.addAll(h3);
                links.addAll(li);
                links.addAll(dt);
                links.addAll(p);
                links.addAll(option);

                for (Element link : links) {
                    String word = link.toString();
                    if (wordCount.containsKey(word) && !localwordCount.containsKey(word)) {
                        wordCount.replace(word, wordCount.get(word) + 1);
                    } else if (!wordCount.containsKey(word)) {
                        wordCount.put(word, 1);
                    }

                    if (localwordCount.containsKey(word)) {
//                            wordCount.replace(word, wordCount.get(word) + 1);
                    } else {
                        localwordCount.put(word, 1);
                    }

                    String linkHref = link.attr("href");
//                    String linkText = link.text();
                }
//
//                String words[] = htmlsource.split("\n");//\\s+");
//                for (String word : words) {
//                    String cleanword;
//
//                    cleanword = word.replaceAll("\r", "");//"[^\\p{L}\\p{Nd}]+", "");
//                    if (!cleanword.equals("")) {
//                        if (!word.equals(cleanword)) {
//                            word = cleanword;//System.out.println(word+"--"+cleanword);
//                        }
//                        word = word.toLowerCase().replace("ı", "i");
//
//                        if (wordCount.containsKey(word) && !localwordCount.containsKey(word)) {
//                            wordCount.replace(word, wordCount.get(word) + 1);
//                        } else if (!wordCount.containsKey(word)) {
//                            wordCount.put(word, 1);
//                        }
//
//                        if (localwordCount.containsKey(word)) {
////                            wordCount.replace(word, wordCount.get(word) + 1);
//                        } else {
//                            localwordCount.put(word, 1);
//                        }
//
//                    } else {
//                    }
//
//                }

            }

            pstmtendpoints.close();
            rs1.close();
            Iterator it = wordCount.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (Integer.parseInt(pair.getValue().toString()) > 1) {
                    PreparedStatement insertpstmt
                            = con.prepareStatement("insert into recommender_endpoints_tf (word,count) values(?,?);");
                    if (pair.getKey().toString().length() > 44) {
                        insertpstmt.setString(1, pair.getKey().toString());
                    } else {
                        insertpstmt.setString(1, pair.getKey().toString());
                    }
                    insertpstmt.setInt(2, Integer.parseInt(pair.getValue().toString()));

                    insertpstmt.executeUpdate();
                    insertpstmt.close();
                }
                it.remove(); // avoids a ConcurrentModificationException
            }

        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

    private void insertNounSynset(NounSynset[] nounSynset, int wordId, String tableName) {
        try {
            for (NounSynset h : nounSynset) {
                for (String s : h.getWordForms()) {
                    PreparedStatement insert
                            = con.prepareStatement("insert into recommender_comment_" + tableName + " (term, idfid) values (?,?);");
                    insert.setString(1, s);
                    insert.setInt(2, wordId);
                    insert.executeUpdate();
                    insert.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void insertNounSynset(NounSynset[] nounSynset, int wordId, String objectName, String tableName) {
        try {
            for (NounSynset h : nounSynset) {
                for (String s : h.getWordForms()) {
                    PreparedStatement existingTerm = con.prepareStatement("select id from recommender_" + objectName + "_" + tableName + " where term = ?;");
                    existingTerm.setString(1, s);
                    ResultSet rs = existingTerm.executeQuery();
                    int termid = 0;
                    if (rs.next()) {
                        termid = rs.getInt("id");
                    } else {
                        PreparedStatement insert1
                                = con.prepareStatement("insert into recommender_" + objectName + "_" + tableName + " (term) values (?);", Statement.RETURN_GENERATED_KEYS);
                        insert1.setString(1, s);
                        insert1.executeUpdate();

                        try (ResultSet generatedKeys = insert1.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                termid = generatedKeys.getInt(1);
                            } else {
                                // throw new SQLException("Creating user failed, no ID obtained.");
                            }
                        }
                    }
                    PreparedStatement insert
                            = con.prepareStatement("insert into recommender_" + objectName + "_" + tableName + "_word " + " (termid, idfid) values (?,?);");
                    insert.setInt(1, termid);
                    insert.setInt(2, wordId);
                    insert.executeUpdate();
                    insert.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void insertNounSynsetLevel(NounSynset[] nounSynset, int wordId, int mainupperid, int level, String objectName, String tableName) {
        try {
            for (NounSynset h : nounSynset) {
                for (String s : h.getWordForms()) {
                    PreparedStatement insert
                            = con.prepareStatement("insert into recommender_" + objectName + "_" + tableName + "_levels (term, upperid,mainupperid,level) values (?,?,?,?);");
                    insert.setString(1, s);
                    insert.setInt(2, wordId);
                    insert.setInt(3, mainupperid);
                    insert.setInt(4, level + 1);
                    insert.executeUpdate();
                    insert.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void wordNetAnalyzer(String objectName, String SemanticType, int maxIdf) {
        System.setProperty("wordnet.database.dir", "C:\\Users\\Administrator\\OneDrive\\SPEC\\WordNet-3.0\\dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();

        NounSynset nounSynset;
        NounSynset[] hypernyms;
        NounSynset[] topics;
        NounSynset[] terms;

        try {

            PreparedStatement createtablepstmt00
                    = con.prepareStatement("DROP TABLE IF EXISTS `recommender_" + objectName + "_" + SemanticType + "_word" + "`;\n");
            PreparedStatement createtablepstmt01
                    = con.prepareStatement("DROP TABLE IF EXISTS `recommender_" + objectName + "_" + SemanticType + "`;\n");
            createtablepstmt00.executeUpdate();
            createtablepstmt01.executeUpdate();

            PreparedStatement createtablepstmt
                    = con.prepareStatement(//"DROP TABLE IF EXISTS `recommender_" + objectName + "_word_" + SemanticType + "`;\n"
                            "CREATE TABLE `recommender_" + objectName + "_" + SemanticType + "_word" + "` (\n"
                            + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                            + "  `idfid` int(11) DEFAULT NULL,\n"
                            + "  `termid` int(11) DEFAULT NULL,\n"
                            + "  `processed` int(11) DEFAULT NULL,\n"
                            + "  PRIMARY KEY (`id`)\n"
                            + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            createtablepstmt.executeUpdate();
            PreparedStatement createtablepstmt1
                    = con.prepareStatement(//"DROP TABLE IF EXISTS `recommender_" + objectName + "_" + SemanticType + "`;\n"
                            "CREATE TABLE `recommender_" + objectName + "_" + SemanticType + "` (\n"
                            + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                            + "  `term` varchar(45) DEFAULT NULL,\n"
                            + "  `processed` int(11) DEFAULT NULL,\n"
                            + "  PRIMARY KEY (`id`),\n"
                            + "  KEY `term` (`term`)\n"
                            + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            createtablepstmt1.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {
            PreparedStatement reset
                    = con.prepareStatement("update recommender_" + objectName + "_idf set wordnetProcessed = null;");
            reset.execute();
            PreparedStatement pstmtidfs
                    = con.prepareStatement("select id,word from recommender_" + objectName + "_idf where wordnetProcessed is null and count<?;");
            pstmtidfs.setInt(1, maxIdf);
            ResultSet rs1 = pstmtidfs.executeQuery();
            while (rs1.next()) {
                int wordid = rs1.getInt("id");
                try {
                    Synset[] synsets = database.getSynsets(rs1.getString("word"), SynsetType.NOUN);
                    for (int i = 0; i < synsets.length; i++) {
                        nounSynset = (NounSynset) (synsets[i]);
                        if (nounSynset.getTopics().length > 0) {
                            if (SemanticType.equals("wordnet_hypernym")) {
                                terms = nounSynset.getHypernyms();
                                insertNounSynset(terms, wordid, objectName, "wordnet_hypernym");
                            } else if (SemanticType.equals("wordnet_topic")) {
                                terms = nounSynset.getTopics();
                                insertNounSynset(terms, wordid, objectName, "wordnet_topic");
                            }
//                            terms = nounSynset.getInstanceHypernyms();
//                            insertNounSynset(terms, wordid, "wordnet_instancehypernym");
//                            terms = nounSynset.getInstanceHyponyms();
//                            insertNounSynset(terms, wordid, "wordnet_instancehyponym");
//                            terms = nounSynset.getPartHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_partholonym");
//                            terms = nounSynset.getPartMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_partmeronym");
//                            terms = nounSynset.getMemberHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_memberholonym");
//                            terms = nounSynset.getMemberMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_membermeronym");
//                            terms = nounSynset.getRegions();
//                            insertNounSynset(terms, wordid, "wordnet_region");
//                            terms = nounSynset.getSubstanceHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_substanceholonym");
//                            terms = nounSynset.getSubstanceMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_substancemeronym");
//                            terms = nounSynset.getUsages();
//                            insertNounSynset(terms, wordid, "wordnet_usage");
                        }
                    }
                    PreparedStatement update
                            = con.prepareStatement("update recommender_" + objectName + "_idf set wordnetProcessed = 1 where id=?;");
                    update.setInt(1, rs1.getInt("id"));
                    update.executeUpdate();
                    update.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void wordNetAnalyzerLevel(String objectName, String SemanticType, boolean isFirstLevel) {
        System.setProperty("wordnet.database.dir", "C:\\Users\\Administrator\\OneDrive\\SPEC\\WordNet-3.0\\dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();

        NounSynset nounSynset;
        NounSynset[] hypernyms;
        NounSynset[] topics;
        NounSynset[] terms;

        try {

            if (isFirstLevel) {
                PreparedStatement createtablepstmt01
                        = con.prepareStatement("DROP TABLE IF EXISTS `recommender_" + objectName + "_" + SemanticType + "_levels`;\n");
                createtablepstmt01.executeUpdate();
            }
            PreparedStatement createtablepstmt1
                    = con.prepareStatement(//"DROP TABLE IF EXISTS `recommender_" + objectName + "_" + SemanticType + "`;\n"
                            "CREATE TABLE `recommender_" + objectName + "_" + SemanticType + "_levels` (\n"
                            + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                            + "  `term` varchar(45) DEFAULT NULL,\n"
                            + "  `upperid` int(11) DEFAULT NULL,\n"
                            + "  `mainupperid` int(11) DEFAULT NULL,\n"
                            + "  `level` int(11) DEFAULT NULL,\n"
                            + "  `processed` int(11) DEFAULT NULL,\n"
                            + "  PRIMARY KEY (`id`)\n"
                            + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            createtablepstmt1.executeUpdate();
            PreparedStatement pstmtidfs
                    = con.prepareStatement("insert INTO recommender_" + objectName + "_" + SemanticType + "_levels (term,mainupperid)"
                            + " SELECT term,id FROM crawler.recommender_" + objectName + "_" + SemanticType + " order by id asc;");
            // pstmtidfs.setInt(1, maxIdf);
            pstmtidfs.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {

            PreparedStatement pstmts
                    = con.prepareStatement("select * from recommender_" + objectName + "_" + SemanticType + "_levels where processed is null;");
            // pstmtidfs.setInt(1, maxIdf);
            ResultSet rs1 = pstmts.executeQuery();

            while (rs1.next()) {
                int wordid = rs1.getInt("id");
                int mainupperid = rs1.getInt("mainupperid");
                int level = rs1.getInt("level");
                if (mainupperid == 0) {
                    mainupperid = wordid;
                }

                PreparedStatement pstmtupdate
                        = con.prepareStatement("update recommender_" + objectName + "_" + SemanticType + "_levels set processed = 1 where id = ?;");
                pstmtupdate.setInt(1, wordid);
                pstmtupdate.executeUpdate();

                try {
                    Synset[] synsets = database.getSynsets(rs1.getString("term"), SynsetType.NOUN);

                    for (int i = 0; i < synsets.length; i++) {
                        nounSynset = (NounSynset) (synsets[i]);
                        if (nounSynset.getTopics().length > 0) {

//                            terms = nounSynset.getHyponyms();
//                            insertNounSynset(terms, wordid, "wordnet_hyponym");
                            if (SemanticType.equals("wordnet_hypernym")) {
                                terms = nounSynset.getHypernyms();

                                insertNounSynsetLevel(terms, wordid, mainupperid, level, objectName, "wordnet_hypernym");
                            } else if (SemanticType.equals("wordnet_topic")) {
                                terms = nounSynset.getTopics();
                                insertNounSynsetLevel(terms, wordid, mainupperid, level, objectName, "wordnet_topic");
                            }
//                            terms = nounSynset.getInstanceHypernyms();
//                            insertNounSynset(terms, wordid, "wordnet_instancehypernym");
//                            terms = nounSynset.getInstanceHyponyms();
//                            insertNounSynset(terms, wordid, "wordnet_instancehyponym");
//                            terms = nounSynset.getPartHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_partholonym");
//                            terms = nounSynset.getPartMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_partmeronym");
//                            terms = nounSynset.getMemberHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_memberholonym");
//                            terms = nounSynset.getMemberMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_membermeronym");
//                            terms = nounSynset.getRegions();
//                            insertNounSynset(terms, wordid, "wordnet_region");
//                            terms = nounSynset.getSubstanceHolonyms();
//                            insertNounSynset(terms, wordid, "wordnet_substanceholonym");
//                            terms = nounSynset.getSubstanceMeronyms();
//                            insertNounSynset(terms, wordid, "wordnet_substancemeronym");
//                            terms = nounSynset.getUsages();
//                            insertNounSynset(terms, wordid, "wordnet_usage");
                        }
                    }
//                    PreparedStatement update
//                            = con.prepareStatement("update recommender_" + objectName + "_idf set wordnetProcessed = 1 where id=?;");
//                    update.setInt(1, rs1.getInt("id"));
//                    update.executeUpdate();
//                    update.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void btnWordnetAnalyzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWordnetAnalyzerActionPerformed

//        wordNetAnalyzer("comment", "wordnet_hypernym", 400);
//        wordNetAnalyzer("comment", "wordnet_topic", 400);
//        wordNetAnalyzer("label", "wordnet_hypernym", 400);
//        wordNetAnalyzer("label", "wordnet_topic", 400);
//        jButtonWordnetlevelActionPerformed(evt);
//        jButtonWordnetlevelActionPerformed(evt);
//        jButtonWordnetlevelActionPerformed(evt);
//        jButtonWordnetlevelActionPerformed(evt);
//        wordNetAnalyzerLevel("comment", "wordnet_topic", true);
//        wordNetAnalyzerLevel("comment", "wordnet_topic", false);
//        wordNetAnalyzerLevel("comment", "wordnet_topic", false);
//        wordNetAnalyzerLevel("comment", "wordnet_topic", false);
//        wordNetAnalyzerLevel("comment", "wordnet_hypernym", true);
//        wordNetAnalyzerLevel("comment", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("comment", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("comment", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("label", "wordnet_hypernym", true);
//        wordNetAnalyzerLevel("label", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("label", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("label", "wordnet_hypernym", false);
//        wordNetAnalyzerLevel("label", "wordnet_topic", true);
//        wordNetAnalyzerLevel("label", "wordnet_topic", false);
//        wordNetAnalyzerLevel("label", "wordnet_topic", false);
//        wordNetAnalyzerLevel("label", "wordnet_topic", false);
//        wordNetTFIDFLevel("comment", "wordnet_topic");
//        wordNetTFIDFLevel("comment", "wordnet_hypernym");
//        wordNetTFIDFLevel("label", "wordnet_hypernym");
//        wordNetTFIDFLevel("label", "wordnet_topic");
//
//        wordNetTFIDF("comment", "wordnet_hypernym");
//        wordNetTFIDF("comment", "wordnet_topic");
//        wordNetTFIDF("label", "wordnet_hypernym");
//        wordNetTFIDF("label", "wordnet_topic");
//        exportfrequencyCounts("comment", "wordnet_hypernym");
//        exportfrequencyCounts("comment", "wordnet_topic");
//        exportfrequencyCounts("label", "wordnet_hypernym");
//        exportfrequencyCounts("label", "wordnet_topic");
//        
//        exportfrequencyCountsLevel("comment", "wordnet_hypernym");
//        exportfrequencyCountsLevel("comment", "wordnet_topic");
//        exportfrequencyCountsLevel("label", "wordnet_hypernym");
//        exportfrequencyCountsLevel("label", "wordnet_topic");
//        
//        exportSTFSIDFScore("comment", "wordnet_hypernym");
//        exportSTFSIDFScore("comment", "wordnet_topic");
//        exportSTFSIDFScore("label", "wordnet_hypernym");
//        exportSTFSIDFScore("label", "wordnet_topic");
//        
        exportSTFSIDFScoreLevel("comment", "wordnet_hypernym");
        exportSTFSIDFScoreLevel("comment", "wordnet_topic");
        exportSTFSIDFScoreLevel("label", "wordnet_hypernym");
        exportSTFSIDFScoreLevel("label", "wordnet_topic");

        exportSTFSIDFScoreTop20TfIdfLevel("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdfLevel("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20TfIdfLevel("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdfLevel("label", "wordnet_topic");

        exportSTFSIDFScoreTop20StfSidfLevel("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidfLevel("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20StfSidfLevel("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidfLevel("label", "wordnet_topic");

        exportSTFSIDFScoreTop20TfIdf("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdf("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20TfIdf("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdf("label", "wordnet_topic");

        exportSTFSIDFScoreTop20StfSidf("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidf("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20StfSidf("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidf("label", "wordnet_topic");

    }//GEN-LAST:event_btnWordnetAnalyzerActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select count(*) as numberoftriples from endpointtriples;");
            int SIZE = 0;
            if (rs.next()) {
                SIZE = rs.getInt("numberoftriples") / 1000000 + 1;
            }
            for (int i = 0; i < SIZE; i++) {
                PreparedStatement pstmt
                        = con.prepareStatement("SELECT *\n"
                                + "FROM endpointtriples limit 1000000 offset " + String.valueOf(i * 1000000) + "\n"
                                + "INTO OUTFILE 'endpointriples" + String.valueOf(i) + ".csv'\n"
                                + "FIELDS TERMINATED BY ','\n"
                                + "ENCLOSED BY '\"'\n"
                                + "LINES TERMINATED BY '\\n';"
                        );
                pstmt.execute();
                pstmt.close();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "HATA:" + ex.getMessage());
        }// TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("update endpoints set lastCheckedDate=null;");
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "SIFIRLANDI..");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButtonRunClassCollectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunClassCollectorActionPerformed

        WorkerSparqlQueryResultCollector sworker = new WorkerSparqlQueryResultCollector(this, 120000, connectionUrl, 1000, jComboCommonQueries.getSelectedItem().toString());
        sworker.setName("Worker Class");
        //threadArray[i++] = sworker;
        //  threadsSearchQueue[i++] = sworker;
        sworker.start();

        //        String a = JenaSparql.getSparqlXMLResult("http://dbpedia.org/sparql", "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> SELECT distinct ?o {?s rdf:type ?o}", Integer.parseInt(jTextField3.getText()), Integer.parseInt(jTextField4.getText()));
        //        jTextArea1.setText(a);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonRunClassCollectorActionPerformed
    private void wordNetTFIDF(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtReset = con.prepareStatement("UPDATE crawler.recommender_" + objectName + "_" + semanticType + " SET `processed` = null;");
            pstmtReset.execute();
            PreparedStatement pstmtDrop = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_stf_sidf;");
            pstmtDrop.execute();

            PreparedStatement pstmt = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_stf_sidf` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `termid` int(11) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `stf` int(11) DEFAULT NULL,\n"
                    + "  `sidf` int(11) DEFAULT NULL,\n"
                    + "  `totaltermcount` int(11) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`id`),\n"
                    + "  KEY `endpointid` (`endpointid`),\n"
                    + "  KEY `termid` (`termid`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            HashMap<Integer, Integer> endpointtotaltermcount = new HashMap();

            PreparedStatement pstmtHypernyms = con.prepareStatement("SELECT id FROM crawler.recommender_" + objectName + "_" + semanticType + " where processed is null;");
            ResultSet rsHypernyms = pstmtHypernyms.executeQuery();
            while (rsHypernyms.next()) {
                //int id = rsHypernyms.getInt("id");
                int termid = rsHypernyms.getInt("id");

                PreparedStatement pstmtTfs = con.prepareStatement("SELECT endpointid, count(*) tf FROM crawler.recommender_" + objectName + "_" + semanticType + "_word" + " as h inner join \n"
                        + "recommender_" + objectName + "_tf as i on h.idfid = i.idfid\n"
                        + "where termid=? group by endpointid;");

                pstmtTfs.setInt(1, termid);
                ResultSet rsTfs = pstmtTfs.executeQuery();
                int idf = 0;
                ArrayList<String> h = new ArrayList<String>();

                while (rsTfs.next()) {
                    int tf = rsTfs.getInt("tf");
                    int endpointid = rsTfs.getInt("endpointid");
                    h.add(String.valueOf(tf) + "-" + String.valueOf(endpointid));
                    idf++;
                    if (endpointtotaltermcount.containsKey(endpointid)) {
                        endpointtotaltermcount.replace(endpointid, endpointtotaltermcount.get(endpointid) + tf);
                    } else {
                        endpointtotaltermcount.put(endpointid, tf);
                    }
                }

                PreparedStatement pstmtInsert = con.prepareStatement("INSERT INTO recommender_" + objectName + "_" + semanticType + "_stf_sidf "
                        + "(termid, endpointid,stf,sidf) VALUES (?,?,?,?);");

                for (String s : h) {
                    pstmtInsert.setInt(1, termid);
                    pstmtInsert.setInt(2, Integer.parseInt(s.split("-")[1]));
                    pstmtInsert.setInt(3, Integer.parseInt(s.split("-")[0]));
                    pstmtInsert.setInt(4, idf);
                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
                PreparedStatement pstmtUpdateProcessFlag = con.prepareStatement("UPDATE recommender_" + objectName + "_" + semanticType + " SET `processed` = 1 where id=?;");
                pstmtUpdateProcessFlag.setInt(1, termid);
                pstmtUpdateProcessFlag.execute();
            }

            Iterator ittw = endpointtotaltermcount.entrySet().iterator();
            try {

                while (ittw.hasNext()) {
                    Map.Entry pair = (Map.Entry) ittw.next();
                    //<>

                    PreparedStatement pstmtUpdate = con.prepareStatement("UPDATE recommender_" + objectName + "_" + semanticType + "_stf_sidf "
                            + "SET totaltermcount=? WHERE endpointid =?;");
                    pstmtUpdate.setInt(1, Integer.parseInt(pair.getValue().toString()));
                    pstmtUpdate.setInt(2, Integer.parseInt(pair.getKey().toString()));
                    pstmtUpdate.execute();
                }
            } catch (Exception ex) {
                //  System.out.println("tf update edilemedi");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }// TODO add your handling code here:
    }

    private void wordNetTFIDFLevel(String objectName, String semanticType) {
        try {
//            PreparedStatement pstmtReset = con.prepareStatement("UPDATE crawler.recommender_" + objectName + "_" + semanticType + " SET `processed` = null;");
//            pstmtReset.execute();
            PreparedStatement pstmtDrop = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_stf_sidf_level;");
            pstmtDrop.execute();

            PreparedStatement pstmt = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_stf_sidf_level` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `stf` int(11) DEFAULT NULL,\n"
                    + "  `sidf` int(11) DEFAULT NULL,\n"
                    + "  `totaltermcount` int(11) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`id`),\n"
                    + "  KEY `endpointid` (`endpointid`),\n"
                    + "  KEY `term` (`term`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            HashMap<Integer, Integer> endpointtotaltermcount = new HashMap();

            PreparedStatement pstmtHypernyms = con.prepareStatement("SELECT term FROM crawler.recommender_" + objectName + "_" + semanticType + "_levels group by term;");
            ResultSet rsHypernyms = pstmtHypernyms.executeQuery();
            while (rsHypernyms.next()) {
                //int id = rsHypernyms.getInt("id");
                String term = rsHypernyms.getString("term");
                PreparedStatement pstmtTfs = con.prepareStatement("  SELECT endpointid, count(*) tf from "
                        + "recommender_" + objectName + "_tf as tf inner join \n"
                        + "   (select idfid,termid,mainterm from crawler.recommender_" + objectName + "_" + semanticType + "_word as word inner join \n"
                        + "(SELECT l.term,h.term as mainterm,mainupperid FROM crawler.recommender_" + objectName + "_" + semanticType + "_levels l \n"
                        + "inner join crawler.recommender_" + objectName + "_" + semanticType + " h on h.id = l.mainupperid where l.term = ? group by term,mainterm,mainupperid) as term\n"
                        + "on term.mainupperid = word.termid) as termsidf\n"
                        + "on termsidf.idfid = tf.idfid group by endpointid;");
                pstmtTfs.setString(1, term);
                ResultSet rsTfs = pstmtTfs.executeQuery();
                int idf = 0;
                ArrayList<String> h = new ArrayList<String>();

                while (rsTfs.next()) {
                    int tf = rsTfs.getInt("tf");
                    int endpointid = rsTfs.getInt("endpointid");
                    h.add(String.valueOf(tf) + "-" + String.valueOf(endpointid));
                    idf++;
                    if (endpointtotaltermcount.containsKey(endpointid)) {
                        endpointtotaltermcount.replace(endpointid, endpointtotaltermcount.get(endpointid) + tf);
                    } else {
                        endpointtotaltermcount.put(endpointid, tf);
                    }
                }

                PreparedStatement pstmtInsert = con.prepareStatement("INSERT INTO recommender_" + objectName + "_" + semanticType + "_stf_sidf_level "
                        + "(term, endpointid,stf,sidf) VALUES (?,?,?,?);");

                for (String s : h) {
                    pstmtInsert.setString(1, term);
                    pstmtInsert.setInt(2, Integer.parseInt(s.split("-")[1]));
                    pstmtInsert.setInt(3, Integer.parseInt(s.split("-")[0]));
                    pstmtInsert.setInt(4, idf);
                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
            }

            Iterator ittw = endpointtotaltermcount.entrySet().iterator();
            try {

                while (ittw.hasNext()) {
                    Map.Entry pair = (Map.Entry) ittw.next();
                    //<>

                    PreparedStatement pstmtUpdate = con.prepareStatement("UPDATE recommender_" + objectName + "_" + semanticType + "_stf_sidf_level "
                            + "SET totaltermcount=? WHERE endpointid =?;");
                    pstmtUpdate.setInt(1, Integer.parseInt(pair.getValue().toString()));
                    pstmtUpdate.setInt(2, Integer.parseInt(pair.getKey().toString()));
                    pstmtUpdate.execute();
                }
            } catch (Exception ex) {
                //  System.out.println("tf update edilemedi");
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }// TODO add your handling code here:
    }
    private void jButtonwordnetTdidfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonwordnetTdidfActionPerformed

//        exportSTFSIDFExcel("comment", "wordnet_hypernym");
//        exportSTFSIDFExcel("comment", "wordnet_topic");
//        exportSTFSIDFExcel("label", "wordnet_hypernym");
//        exportSTFSIDFExcel("label", "wordnet_topic");

    }//GEN-LAST:event_jButtonwordnetTdidfActionPerformed
    private void exportfrequencyCounts(String objectName, String semanticType) {

        try {

            PreparedStatement pstmtDrop = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys;");
            pstmtDrop.execute();

            PreparedStatement pstmt = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + "  `totalNumberofWords` int(11) DEFAULT NULL,\n"
                    + "  `totaldocumentcount` int(11) DEFAULT NULL,\n"
                    + "  `totaltermcount` int(11) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tf` int(11) DEFAULT NULL,\n"
                    + "  `idf` int(11) DEFAULT NULL,\n"
                    + "  `stf` int(11) DEFAULT NULL,\n"
                    + "  `sidf` int(11) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt.execute();

            PreparedStatement pstmtReset = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys` (totaldocumentcount,word,endpointid,tf,totalNumberofWords,idf,term,sidf,stf,totaltermcount) \n"
                    + "Select (select count(distinct endpointid) from recommender_comment_tf) as totaldocumentcount, "
                    + "t7.word,t7.endpointid,t7.count as tf,t7.totalNumberofWords,t8.idf,t8.term,t8.sidf,t8.stf,t8.totaltermcount from recommender_" + objectName + "_tf as t7 inner join\n"
                    + "(select t5.word,t5.count as idf,term, idfid,sidf,stf,endpointid,totaltermcount from recommender_" + objectName + "_idf as t5 inner join (select * from recommender_" + objectName + "_" + semanticType + " as t3 inner join\n"
                    + "(SELECT t1.termid, t1.sidf,t1.stf,t1.totaltermcount, t1.endpointid,t2.idfid FROM crawler.recommender_" + objectName + "_" + semanticType + "_stf_sidf as t1\n"
                    + "inner join recommender_" + objectName + "_" + semanticType + "_word as t2 on t1.termid= t2.termid) as t4\n"
                    + "on t3.id = t4.termid) as t6\n"
                    + "on t5.id=t6.idfid) as t8\n"
                    + "on\n"
                    + "t7.endpointid = t8.endpointid and t7.idfid = t8.idfid");

            pstmtReset.execute();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportfrequencyCountsLevel(String objectName, String semanticType) {

        try {

            PreparedStatement pstmtDrop = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_lvl;");
            pstmtDrop.execute();

            PreparedStatement pstmt = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_lvl` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + "  `totalNumberofWords` int(11) DEFAULT NULL,\n"
                    + "  `totaldocumentcount` int(11) DEFAULT NULL,\n"
                    + "  `totaltermcount` int(11) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tf` int(11) DEFAULT NULL,\n"
                    + "  `idf` int(11) DEFAULT NULL,\n"
                    + "  `stf` int(11) DEFAULT NULL,\n"
                    + "  `sidf` int(11) DEFAULT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt.execute();

            PreparedStatement pstmtReset = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_lvl` (totaldocumentcount,word,endpointid,tf,totalNumberofWords,idf,term,sidf,stf,totaltermcount) \n"
                    + "Select (select count(distinct endpointid) from recommender_comment_tf) as totaldocumentcount, "
                    + "t7.word,t7.endpointid,t7.count as tf,t7.totalNumberofWords,t8.idf,t8.term,t8.sidf,t8.stf,t8.totaltermcount from recommender_" + objectName + "_tf as t7 inner join\n"
                    + "(select t5.word,t5.count as idf,term, idfid,sidf,stf,endpointid,totaltermcount from recommender_" + objectName + "_idf as t5 inner join (select * from recommender_" + objectName + "_" + semanticType + " as t3 inner join\n"
                    + "(SELECT t1.termid, t1.sidf,t1.stf,t1.totaltermcount, t1.endpointid,t2.idfid FROM crawler.recommender_" + objectName + "_" + semanticType + "_stf_sidf as t1\n"
                    + "inner join recommender_" + objectName + "_" + semanticType + "_word as t2 on t1.termid= t2.termid) as t4\n"
                    + "on t3.id = t4.termid) as t6\n"
                    + "on t5.id=t6.idfid) as t8\n"
                    + "on\n"
                    + "t7.endpointid = t8.endpointid and t7.idfid = t8.idfid");

            pstmtReset.execute();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScore(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                    + "(select  word,term,endpointid, tf/idf as tfidfscore, stf/sidf as stfsidfscore "
                    + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys where idf<100 or sidf<200) ");
//            PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_analysis_score` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
//                    + "(select  word,term,endpointid, tf/idf as tfidfscore, stf/sidf as stfsidfscore "
//                    + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_analysis) ");

            pstmtinseranalysisscore.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScoreLevel(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                    + "(select  word,term,endpointid, tf/idf as tfidfscore, stf/sidf as stfsidfscore "
                    + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys where idf<100 or sidf<200) ");
//            PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_analysis_score` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
//                    + "(select  word,term,endpointid, tf/idf as tfidfscore, stf/sidf as stfsidfscore "
//                    + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_analysis) ");

            pstmtinseranalysisscore.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScoreTop20TfIdf(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr_ti_t20;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr_ti_t20` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement selectendpointid = con.prepareStatement("select endpointid from `recommender_" + objectName + "_" + semanticType + "_anlys_scr` group by endpointid");
            ResultSet rs = selectendpointid.executeQuery();
            while (rs.next()) {
                int endpointid = rs.getInt("endpointid");
                PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr_ti_t20` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                        + "(select word,term,endpointid,max(tfidfscore) as tfidfscore,stfsidfscore from (select word,term,endpointid,max(tfidfscore) as tfidfscore,stfsidfscore "
                        + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys_scr where endpointid = ? group by word) as t1 group by term order by tfidfscore desc LIMIT 20)");
                pstmtinseranalysisscore.setInt(1, endpointid);
                pstmtinseranalysisscore.execute();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScoreTop20StfSidf(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr_stsi_t20;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr_stsi_t20` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement selectendpointid = con.prepareStatement("select endpointid from `recommender_" + objectName + "_" + semanticType + "_anlys_scr` group by endpointid");
            ResultSet rs = selectendpointid.executeQuery();
            while (rs.next()) {
                int endpointid = rs.getInt("endpointid");
                PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr_stsi_t20` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                        + "(select word,term,endpointid,tfidfscore,max(stfsidfscore) as stfsidfscore from (select word,term,endpointid,tfidfscore,max(stfsidfscore) as stfsidfscore "
                        + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys_scr where endpointid = ? group by term) as t1 group by word order by stfsidfscore desc LIMIT 20)");
                pstmtinseranalysisscore.setInt(1, endpointid);
                pstmtinseranalysisscore.execute();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScoreTop20TfIdfLevel(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_ti_t20;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_ti_t20` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement selectendpointid = con.prepareStatement("select endpointid from `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl` group by endpointid");
            ResultSet rs = selectendpointid.executeQuery();
            while (rs.next()) {
                int endpointid = rs.getInt("endpointid");
                PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_ti_t20` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                        + "(select word,term,endpointid,max(tfidfscore) as tfidfscore,stfsidfscore from (select word,term,endpointid,max(tfidfscore) as tfidfscore,stfsidfscore "
                        + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl where endpointid = ? group by word) as t1 group by term order by tfidfscore desc LIMIT 20)");
                pstmtinseranalysisscore.setInt(1, endpointid);
                pstmtinseranalysisscore.execute();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFScoreTop20StfSidfLevel(String objectName, String semanticType) {
        try {
            PreparedStatement pstmtDrop2 = con.prepareStatement(" DROP TABLE IF EXISTS recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_stsi_t20;");
            pstmtDrop2.execute();

            PreparedStatement pstmt2 = con.prepareStatement("CREATE TABLE `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_stsi_t20` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + " `word` varchar(45) DEFAULT NULL,\n"
                    + " `term` varchar(45) DEFAULT NULL,\n"
                    + "  `endpointid` int(11) DEFAULT NULL,\n"
                    + "  `tfidfscore` FLOAT NOT NULL,\n"
                    + "  `stfsidfscore` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`id`)\n"
                    + ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;");
            pstmt2.execute();
            PreparedStatement selectendpointid = con.prepareStatement("select endpointid from `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl` group by endpointid");
            ResultSet rs = selectendpointid.executeQuery();
            while (rs.next()) {
                int endpointid = rs.getInt("endpointid");
                PreparedStatement pstmtinseranalysisscore = con.prepareStatement("INSERT INTO `recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl_stsi_t20` (word,term,endpointid,tfidfscore,stfsidfscore) \n"
                        + "(select word,term,endpointid,tfidfscore,max(stfsidfscore) as stfsidfscore from (select word,term,endpointid,tfidfscore,max(stfsidfscore) as stfsidfscore "
                        + "FROM crawler.recommender_" + objectName + "_" + semanticType + "_anlys_scr_lvl where endpointid = ? group by term) as t1 group by word order by stfsidfscore desc LIMIT 20)");
                pstmtinseranalysisscore.setInt(1, endpointid);
                pstmtinseranalysisscore.execute();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void exportSTFSIDFExcel(String objectName, String semanticType) {
        try {

            PreparedStatement pstmtReset = con.prepareStatement("select t7.word,t7.endpointid,t7.count as tf,t7.totalNumberofWords,t8.idf,t8.term,t8.sidf,t8.stf from recommender_" + objectName + "_tf as t7 inner join\n"
                    + "(select t5.word,t5.count as idf,term, idfid,sidf,stf,endpointid from recommender_" + objectName + "_idf as t5 inner join (select * from recommender_" + objectName + "_" + semanticType + " as t3 inner join\n"
                    + "(SELECT t1.termid, t1.sidf,t1.stf,t1.endpointid,t2.idfid FROM crawler.recommender_" + objectName + "_" + semanticType + "_stf_sidf as t1\n"
                    + "inner join recommender_" + objectName + "_" + semanticType + "_word as t2 on t1.termid= t2.termid) as t4\n"
                    + "on t3.id = t4.termid) as t6\n"
                    + "on t5.id=t6.idfid) as t8\n"
                    + "on\n"
                    + "t7.endpointid = t8.endpointid and t7.idfid = t8.idfid"
                    + " into outfile '" + objectName + "_" + semanticType + ".csv' FIELDS TERMINATED BY ',' ENCLOSED BY '\\\"' LINES TERMINATED BY '\\n';");
            pstmtReset.execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void jButtonWordnetLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWordnetLevelActionPerformed
        System.setProperty("wordnet.database.dir", "C:\\Users\\Administrator\\OneDrive\\SPEC\\WordNet-3.0\\dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();

        NounSynset nounSynset;
        NounSynset[] hypernyms;
        int level = 1;
        try {
            PreparedStatement pstmtHypernyms
                    = con.prepareStatement("select id,hypernym from recommender_comment_hypernym_level where processed is null and hyplevel=?;");
            pstmtHypernyms.setInt(1, level);

            ResultSet rs1 = pstmtHypernyms.executeQuery();
            while (rs1.next()) {
                try {
                    int lowerid = rs1.getInt("id");
                    Synset[] synsets = database.getSynsets(rs1.getString("hypernym"), SynsetType.NOUN);
                    PreparedStatement insert
                            = con.prepareStatement("insert into recommender_comment_hypernym_level (hypernym, hyplevel,lowerid) values (?,?,?);");

                    for (int i = 0; i < synsets.length; i++) {
                        nounSynset = (NounSynset) (synsets[i]);
                        hypernyms = nounSynset.getHypernyms();
                        for (NounSynset h : hypernyms) {
                            for (String s : h.getWordForms()) {
                                insert.setString(1, s);
                                insert.setInt(2, level + 1);
                                insert.setInt(3, lowerid);
                                insert.addBatch();
                            }
                            //                    System.err.println(nounSynset.getWordForms()[0]
                            //                            + ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
                        } // TODO add your handling code here:

                    }
                    insert.executeBatch();
                    insert.close();
                    PreparedStatement update
                            = con.prepareStatement("update recommender_comment_hypernym_level set processed = 1 where id=?;");
                    update.setInt(1, lowerid);
                    update.executeUpdate();
                    update.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonWordnetLevelActionPerformed

    private void jButtonTabloyaAktarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTabloyaAktarActionPerformed
        try {
//            PreparedStatement pstmtAll
//                    = con.prepareStatement("select endpointid, word, totalwordcount, wordtf,wordidf, hypernym, htf,hidf from idf_tf_hypernyms_hf_hidf_score;");
            PreparedStatement pstmtAll
                    = con.prepareStatement("SELECT * FROM crawler.idf_tf_hypernyms_score limit 999999 offset 238357;");

            ResultSet rs1 = pstmtAll.executeQuery();

            int count = 1;
            while (rs1.next()) {
                try {
                    if (count > 10000) {
                        int endpointid = rs1.getInt("endpointid");
                        String word = rs1.getString("word");
                        String hypernym = rs1.getString("hypernym");
                        int totalwordcount = rs1.getInt("c");
                        int wordtf = rs1.getInt("tf");
                        int wordidf = rs1.getInt("idf");

                        PreparedStatement pstmtHypernymTfIdf
                                = con.prepareStatement("SELECT endpointid,tf,idf,hypernym FROM "
                                        + "crawler.recommender_comment_hypernym_tf_idf "
                                        + "where hypernym =? and endpointid = ? limit 1;");
                        pstmtHypernymTfIdf.setInt(2, endpointid);
                        pstmtHypernymTfIdf.setString(1, hypernym);

                        ResultSet rs2 = pstmtHypernymTfIdf.executeQuery();
                        while (rs2.next()) {
                            int htf = rs2.getInt("tf");
                            int hidf = rs2.getInt("idf");

                            PreparedStatement insert
                                    = con.prepareStatement("INSERT INTO crawler.hf_idf_final_table\n"
                                            + "(endpointid,\n"
                                            + "word,\n"
                                            + "hypernym,\n"
                                            + "totalwordcount,\n"
                                            + "wordtf,\n"
                                            + "wordidf,\n"
                                            + "htf,\n"
                                            + "hidf) values (?,?,?,?,?,?,?,?);");

                            insert.setInt(1, endpointid);
                            insert.setString(2, word);
                            insert.setString(3, hypernym);
                            insert.setInt(4, totalwordcount);
                            insert.setInt(5, wordtf);
                            insert.setInt(6, wordidf);
                            insert.setInt(7, htf);
                            insert.setInt(8, hidf);
                            insert.executeUpdate();
                            insert.close();
                        }
                        rs2.close();
                        pstmtHypernymTfIdf.close();
                    }
                    count++;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonTabloyaAktarActionPerformed

    private void jButtonResetEndpointsForProcessingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetEndpointsForProcessingActionPerformed
        String SQL = "update endpoints set tfidfprocessed = null;";
        Statement stmt;
        try {
            stmt = con.createStatement();
            stmt.execute(SQL);

        } catch (SQLException ex) {

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonResetEndpointsForProcessingActionPerformed

    private void jButtonGetSubjectsFromLODCLOUDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetSubjectsFromLODCLOUDActionPerformed
        try {
            final String query
                    = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "prefix void: <http://rdfs.org/ns/void#>\n"
                    + "prefix dcterms: <http://purl.org/dc/terms/>\n"
                    + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                    + "prefix foaf: <http://xmlns.com/foaf/0.1/>\n"
                    + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                    + "prefix skos: <http://www.w3.org/2004/02/skos/core#>\n"
                    + "prefix tag: <http://www.holygoat.co.uk/owl/redwood/0.1/tags/>\n"
                    + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
                    + "\n"
                    + "select ?subject ?dataset ?title ?sparqlEndpoint ?tags where {\n"
                    + "  ?dataset a void:Dataset ;\n"
                    + "             void:sparqlEndpoint ?sparqlEndpoint ;"
                    //      + "             tag:taggedWithTag ?tags ;"
                    + "             dcterms:subject ?subject ;"
                    + "             dcterms:title ?title.\n"
                    + "}";
            //                    + "  ?void:Dataset a dcterms:title ?title ;\n"
            //                    + "             void:sparqlEndpoint ?sparqlEndpoint.\n"
            //                    + "}";

            Model model = ModelFactory.createDefaultModel();
            try (final InputStream in = new FileInputStream("datasets/LODCLOUDvoid.ttl");) {
                model.read(in, null, "TTL");
            }

            final QueryExecution exec = QueryExecutionFactory.create(query, model);
            final com.hp.hpl.jena.query.ResultSet rs = (com.hp.hpl.jena.query.ResultSet) exec.execSelect();
            while (rs.hasNext()) {
                final QuerySolution qs = rs.next();

                SparqlEndpoint.updateEndpoint(qs.get("title").toString(), qs.get("sparqlEndpoint").toString(), "lodcloud", qs.get("subject").toString(), 0);//qs.get("triples").asLiteral().getInt());
                System.out.println("Dataset:" + qs.get("dataset")
                        + "\n\tTitle: " + qs.get("title")
                        // + "\n\tTriples" + qs.get("triples")
                        + "\n\t Sparql Endpoint: " + qs.get("sparqlEndpoint")
                        //+ "\n\t Tags " + qs.get("tags")
                        + "\n\t Subject: " + qs.get("subject"));

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonGetSubjectsFromLODCLOUDActionPerformed

    private void jButtonProfilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfilerActionPerformed

        try {
            //   System.out.println(searchEngine.name + " ilk aramaya girdi: " + (new Timestamp(System.currentTimeMillis())).toString());
            String url;
            try {
                String SQL = "SELECT user, sum(connect) c FROM profiler.tumkayit group by user;";
                PreparedStatement selectstmt = con.prepareStatement(SQL);
                ResultSet rs = selectstmt.executeQuery();

                while (rs.next()) {
                    String user = rs.getString("user");
                    float sumofconnect = (float) rs.getInt("c");

                    String SQLs = "SELECT mainCategory, user , sum(connect) as c FROM profiler.tumkayit where user = ? and\n"
                            + "domain NOT like '%duba.net%' and domain NOT like '%sahibinden.com%' and domain NOT like '%vine.co%' and domain NOT like '%mediafire.com%' and domain NOT like '%filmindirmobil.com%' and domain NOT like '%twitch.tv%' and domain NOT like '%ttvnw.net%' and domain NOT like '%hurriyet.com.tr%' and domain NOT like '%gstatic.com%' and domain NOT like '%google%' and domain NOT like '%4sqi.net%' and domain NOT like '%doubleclick.net%' and domain NOT like '%clipconverter.cc%' and domain NOT like '%spotify.com%' and domain NOT like '%microsoft.com%' and domain NOT like '%vk.me%' and domain NOT like '%apple.com%' and domain NOT like '%dailymotion.com%' and domain NOT like '%mncdn.com%' and domain NOT like '%windowsupdate.com%' and domain NOT like '%instagram.com%' and domain NOT like '%mail.ru%'and domain NOT like '%google.com%' and domain NOT like '%googlevideo.com%' and mainCategory != 'Unknown' and mainCategory != 'Empty Site or Under Construction' and mainCategory != 'Search Engines' and mainCategory != 'Advertisements' \n"
                            + "group by maincategory, user order by c desc;";
                    PreparedStatement selectstmts = con.prepareStatement(SQLs);
                    selectstmts.setString(1, user);
                    ResultSet rss = selectstmts.executeQuery();
                    int i = 1;
                    PreparedStatement selectstmt12;
                    String SQL12 = "INSERT INTO profiler.analiz2 (user,category,freq,ord) values (?,?,?,?);";
                    selectstmt12 = con.prepareStatement(SQL12);

                    while (rss.next()) {

                        selectstmt12.setString(1, rss.getString("user"));
                        selectstmt12.setString(2, rss.getString("mainCategory"));
                        selectstmt12.setFloat(3, ((float) rss.getInt("c")) / sumofconnect);
                        selectstmt12.setInt(4, i);
                        selectstmt12.addBatch();
                        i++;
                    }
                    selectstmt12.executeBatch();

                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            // webClient.closeAllWindows();
        } catch (Exception ex) {
            String x = ex.getMessage();
            try {
                System.out.println(" hataya girdi 60 saniye bekleyecek. " + x);
                Thread.sleep(60000);
            } catch (Exception eex) {
            }
        }
//        try {
//            //   System.out.println(searchEngine.name + " ilk aramaya girdi: " + (new Timestamp(System.currentTimeMillis())).toString());
//            String url;
//            try {
//                String SQL = "SELECT idprofilerurl,url,category FROM profiler.profilerurl where mainCategory is null;";
//                PreparedStatement selectstmt = con.prepareStatement(SQL);
//                ResultSet rs = selectstmt.executeQuery();
//
//                while (rs.next()) {
//                    if (rs.getString("category").split(";")[0].contains("\r")) {
//                        System.out.println("");
//                    }
//                    String SQL12 = "UPDATE profiler.profilerurl set mainCategory=? where idprofilerurl = ?;";
//                    PreparedStatement selectstmt12 = con.prepareStatement(SQL12);
//                    selectstmt12.setString(1, rs.getString("category").split(";")[0].replaceAll("\r", ""));
//                    selectstmt12.setInt(2, rs.getInt("idprofilerurl"));
//                    selectstmt12.executeUpdate();
//
//                }
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }
//
//            // webClient.closeAllWindows();
//        } catch (Exception ex) {
//            String x = ex.getMessage();
//            try {
//                System.out.println(" hataya girdi 60 saniye bekleyecek. " + x);
//                Thread.sleep(60000);
//            } catch (Exception eex) {
//            }
//        }
//
//        jButtonProfiler.setBackground(Color.GREEN);
//        //  int crawlId = createCrawl(getQueryText(), "SearchEngineCrawler");
//        //  maxPage = Integer.decode(txtMaxPage.getText());
//
//        threadArray = new Thread[maxPage];
//
//        //   List selist = lstSearchEngines.getSelectedValuesList();
//        //  jProgressBar1.setMaximum(maxPage * selist.size());
//        int i = 0;
////        for(i= 0;i<20;i++)
////        {
//
////        WorkerProfilerQueue sworker = new WorkerProfilerQueue(this, i);
////        sworker.setName("Worker "+i);
////        //threadArray[i++] = sworker;
////        threadsSearchQueue[i] = sworker;
////        sworker.start();
//        WorkerProfilerQueue sworker = new WorkerProfilerQueue(i);
//        sworker.setName("Worker 1");
//        sworker.start();
////        WorkerProfilerQueue sworker1 = new WorkerProfilerQueue( i);
////        sworker1.setName("Worker 2");
////        sworker1.start();
////        WorkerProfilerQueue sworker2 = new WorkerProfilerQueue( i);
////        sworker2.setName("Worker 3");
////        sworker2.start();
////        WorkerProfilerQueue sworker3 = new WorkerProfilerQueue(i);
////        sworker3.setName("Worker 4");
////        sworker3.start();
////        WorkerProfilerQueue sworker4 = new WorkerProfilerQueue(i);
////        sworker4.setName("Worker 5");
////        sworker4.start();
////        }
//        try {
//
//            Thread.sleep(300);
//        } catch (Exception ex) {
//        }//  search(getSearchEngineFromName((String) se));
//        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonProfilerActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            String host = "";
            String SQL = "SELECT * FROM profiler.tumkayit where domain is null;";
            PreparedStatement selectstmt = con.prepareStatement(SQL);
            ResultSet rs = selectstmt.executeQuery();
            int i = 0;
            String SQLu = "UPDATE profiler.tumkayit SET domain = ? where idtumkayit = ?;";
            PreparedStatement selectstmtu = con.prepareStatement(SQLu);

            while (rs.next()) {

                try {
                    host = rs.getString("url");
                    URL u = new URL("http://" + rs.getString("url"));
                    host = u.getHost();
                    if (InternetDomainName.isValid(host)) {
                        host = InternetDomainName.from(host).topPrivateDomain().toString();
                    } else {
                        if (com.google.common.net.HostSpecifier.isValid(host)) {
                            host = com.google.common.net.HostSpecifier.from(host).toString();
                        } else {
                            host = "";
                        }
                    }
                } catch (Exception ex) {

                }
                selectstmtu.setString(1, host);
                selectstmtu.setInt(2, rs.getInt("idtumkayit"));
                selectstmtu.addBatch();
                if (i > 10000) {
                    selectstmtu.executeBatch();
                    selectstmtu = con.prepareStatement(SQLu);

                    i = 0;
                }
                i++;
//String query = txtSearchTexts.getText().split("\\n")[0] + " site:" + host;
            }
            selectstmtu.executeBatch();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonGetSparqlEndpointWebPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetSparqlEndpointWebPagesActionPerformed
        while (true) {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);//BrowserVersion.FIREFOX_24);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            try {
                String SQLicurrentuser = "SELECT * from endpoints where sourceCodeHTML is null and source != 'spendold' and source != 'spendnew' ORDER BY id asc;";
                PreparedStatement pstmtcurrentuser
                        = con.prepareStatement(SQLicurrentuser);

                ResultSet rsuser = pstmtcurrentuser.executeQuery();
                int id = 0;
                String url = "";
                while (rsuser.next()) {

                    try {
                        url = rsuser.getString("endpointUrl");
                        id = rsuser.getInt("id");
                        HtmlPage page = webClient.getPage(url);

                        String updatestr = "UPDATE endpoints set sourceCode = ?,sourceCodeXML=?,sourceCodeHTML=? where id = ?;";
                        PreparedStatement pstmtupdate
                                = con.prepareStatement(updatestr);
                        pstmtupdate.setString(1, page.asText());
                        pstmtupdate.setString(2, page.asXml());
                        pstmtupdate.setString(3, page.getWebResponse().getContentAsString());
                        pstmtupdate.setInt(4, id);
                        pstmtupdate.executeUpdate();
                    } catch (Exception ex2) {
//                    String updatestr = "UPDATE endpoints set sourceCode = 'EMPTI',sourceCodeXML = 'EMPTI' where id = ?;";
//                    PreparedStatement pstmtupdate
//                            = con.prepareStatement(updatestr);
//                    pstmtupdate.setInt(1, id);
//                    pstmtupdate.executeUpdate();

                        System.out.println(ex2.getMessage());
                    }
                }
                pstmtcurrentuser.close();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            webClient.closeAllWindows();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonGetSparqlEndpointWebPagesActionPerformed

    private void jButtontfidftop20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtontfidftop20ActionPerformed

        exportSTFSIDFScoreTop20TfIdf("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdf("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20TfIdf("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20TfIdf("label", "wordnet_topic");

        exportSTFSIDFScoreTop20StfSidf("comment", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidf("comment", "wordnet_topic");
        exportSTFSIDFScoreTop20StfSidf("label", "wordnet_hypernym");
        exportSTFSIDFScoreTop20StfSidf("label", "wordnet_topic");

        // TODO add your handling code here:
    }//GEN-LAST:event_jButtontfidftop20ActionPerformed

    private void jButtonWordnetlevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWordnetlevelActionPerformed

        wordNetAnalyzerLevel("comment", "wordnet_hypernym", true);
        wordNetAnalyzerLevel("comment", "wordnet_topic", true);
        wordNetAnalyzerLevel("label", "wordnet_hypernym", true);
        wordNetAnalyzerLevel("label", "wordnet_topic", true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonWordnetlevelActionPerformed

    private void jButtonAnalyzeEndpointHtmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnalyzeEndpointHtmlActionPerformed
        updateEndpointsFromRawTable();
        updateDomainsOfEndpoints();

        calculateEndpointWordsTF(); // TODO add your handling code here:
        calculateEndpointTagsTF(); // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAnalyzeEndpointHtmlActionPerformed

    private void jButtonPostFilterUrlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPostFilterUrlsActionPerformed
        try {
            String SQLall = "select id,endpointUrl,triples from endpoints ";
            PreparedStatement pstmtall = con.prepareStatement(SQLall);
            ResultSet rsall = pstmtall.executeQuery();
            while (rsall.next()) {
                int currentid = rsall.getInt("id");
                String currentendpointUrl = rsall.getString("endpointUrl");
                int currenttriples = rsall.getInt("triples");
         
                    int sameasid = 0;
                    if (currenttriples != 0) {
                        String SQL = "select id,endpointUrl,length(endpointUrl),triples from endpoints where triples=? order by length(endpointUrl) asc limit 1";
                        PreparedStatement pstmt = con.prepareStatement(SQL);
                        pstmt.setInt(1, currenttriples);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            sameasid = rs.getInt("id");
                        }
                        rs.close();
                        pstmt.close();
                    }
                    //  if (id != sameasid && sameasid != 0) {
                    if (sameasid != 0) {
                        String SQL = "UPDATE endpoints SET sameAs=? where id=" + currentid + ";";
                        PreparedStatement pstmt = con.prepareStatement(SQL);
                        pstmt.setInt(1, sameasid);
                        pstmt.executeUpdate();
                        pstmt.close();
                    }
                }
            
        } catch (Exception ex) {

        }
//        List<String> extractedUrls = extractUrls(txtSearchTexts4.getText());
//        String safeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~:/?#[]@!$&'()*+,;=%";
////for (String url : extractedUrls)
////{
////    if(url.endsWith(".")||url.endsWith(";")||url.endsWith(":")||url.endsWith(".")||url.endsWith(",")||url.endsWith("/"))
////        url = url.substring(0,url.length()-1);
////    if(url.contains("<"))
////        url = url.split("<")[0];
////    System.out.println(url);
//        //}
//        String url = extractUrl(txtSearchTexts4.getText());
//        System.out.println(url);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPostFilterUrlsActionPerformed

    private void btnIoTCollectResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIoTCollectResultsActionPerformed
        WorkerSparql sworker = new WorkerSparql(this, connectionUrl);
        sworker.start();
//        for (int i = 0; i < 20; i++) {
//            WorkerSparql sworker = new WorkerSparql(this, connectionUrl);
//            sworker.setName("Worker Remote Sparql");
//            threadsSparql[i] = sworker;
//            //threadArray[i++] = sworker;
//            sworker.start();
//            try {
//                //   Thread.sleep(157);
//            } catch (Exception ex) {
//
//            }
//        }
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIoTCollectResultsActionPerformed

    private void btnCreateSearchQueueFromPreviousUrlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateSearchQueueFromPreviousUrlsActionPerformed
        try {
            String SQLurl = "SELECT url FROM distinct_all_urls";
            Statement stmturl = con1.createStatement();
            ResultSet rsurl = stmturl.executeQuery(SQLurl);
            HashSet<String> previousQueries = new HashSet<String>();
            while (rsurl.next()) {

                try {
                    URL u = new URL(rsurl.getString("url"));
                    String host = u.getHost();
                    if (InternetDomainName.isValid(host)) {
                        host = InternetDomainName.from(host).topPrivateDomain().toString();
                    } else {
                        if (com.google.common.net.HostSpecifier.isValid(host)) {
                            host = com.google.common.net.HostSpecifier.from(host).toString();
                        } else {
                            host = "";
                        }
                    }
                    String query = txtSearchTexts.getText().split("\\n")[0] + " site:" + host;
                    if (!previousQueries.contains(query) && !host.equals("")) {
                        previousQueries.add(query);
                        //System.out.println(host);
                        //  System.out.println(query);
                        int maxSearchPage = Integer.valueOf(txtMaxPage.getText());
                        if (!isQueryExistInQueryQueue(query)) {
                            try {
                                //   System.out.println(host);
                                for (Object searchEngineName : lstSearchEngines.getSelectedValuesList()) {

                                    //                                    String url = rsurl.getString(1);
                                    //                                    String SQLi = "INSERT INTO searchqueue (searchText,searchEngineName, maxSearchPage,disabled) VALUES (?,?,?,0)";
                                    //                                    PreparedStatement pstmt = con2.prepareStatement(SQLi);
                                    //                                    pstmt.setString(1, query);
                                    //                                    pstmt.setString(2, searchEngineName.toString());
                                    //                                    pstmt.setInt(3, maxSearchPage);
                                    //                                    pstmt.executeUpdate();
                                    //                                    pstmt.close();
                                }
                            } catch (Exception ex) {
                                //   System.out.println(ex.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    //System.out.println(ex.getMessage());
                }
            }
            stmturl.close();
            rsurl.close();

            // return id + 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnCreateSearchQueueFromPreviousUrlsActionPerformed

    private void btnStopAnalyzer4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopAnalyzer4ActionPerformed
        // TODO add your handling code here:

        btnStopAnalyzer4.setBackground(Color.RED);
        for (Thread t : threadsAnalysis) {
            ((WorkerAnalyze) t).stopRunning();
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnStopAnalyzer4ActionPerformed

    private void btnStopSearchQueue4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopSearchQueue4ActionPerformed
        // TODO add your handling code here:
        btnStopSearchQueue4.setBackground(Color.RED);

        for (Thread t : threadsSearchQueue) {
            try {
                ((WorkerSearchQueue) t).stopRunning();
            } catch (Exception ex) {
            }
        }
    }//GEN-LAST:event_btnStopSearchQueue4ActionPerformed

    private void btnStopThreads4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopThreads4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnStopThreads4ActionPerformed

    private void btnCreateSearchQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateSearchQueueActionPerformed

        createSearchQueue();
    }//GEN-LAST:event_btnCreateSearchQueueActionPerformed

    private void btnRunMultitextSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunMultitextSearchActionPerformed

        btnStopSearchQueue4.setBackground(Color.GREEN);
        int crawlId = createCrawl(getQueryText(), "SearchEngineCrawler");
        maxPage = Integer.decode(txtMaxPage.getText());

        threadArray = new Thread[maxPage];

        List selist = lstSearchEngines.getSelectedValuesList();

        jProgressBar1.setMaximum(maxPage * selist.size());

        int i = 0;
        for (Object se : selist) {
            WorkerSearchQueue sworker = new WorkerSearchQueue(this, se.toString(), crawlId, connectionUrl);
            sworker.setName("Worker " + se.toString());
            //threadArray[i++] = sworker;
            threadsSearchQueue[i++] = sworker;
            sworker.start();
            try {

                Thread.sleep(300);
            } catch (Exception ex) {
            }//  search(getSearchEngineFromName((String) se));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRunMultitextSearchActionPerformed

    private void btnRunAnalyzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunAnalyzerActionPerformed

        btnStopAnalyzer4.setBackground(Color.GREEN);
        for (int i = 0; i < maxThreadAnalysis; i++) {
            WorkerAnalyze sworker = new WorkerAnalyze(this, connectionUrl, i, maxThreadAnalysis);
            sworker.setName("Worker Endpoint Analyzer " + String.valueOf(i));
            threadsAnalysis[i] = sworker;
            //threadArray[i++] = sworker;
            sworker.start();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRunAnalyzerActionPerformed

    private void txtMaxPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaxPageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaxPageActionPerformed

    private void btnLodstatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLodstatsActionPerformed

        try {
            final WebClient webClient = new WebClient();//BrowserVersion.FIREFOX_24);
        for (int i = 1; i < 22; i++) {
            HtmlPage page = webClient.getPage(txtLodstatsUrl.getText() + "/rdfdocs?page=" + i);
            DocumentBuilderFactory dbf
            = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(page.asXml()));
            Document doc = db.parse(is);

            NodeList nodeList = doc.getElementsByTagName("tr");
            String datasetName;
            String url;
            String format;
            String rowFormat;
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node node = nodeList.item(j);
                try {
                    NodeList tdNodeList = node.getChildNodes();
                    url = tdNodeList.item(1).getChildNodes().item(1).getAttributes().getNamedItem("href").getNodeValue().trim();
                    datasetName = tdNodeList.item(1).getChildNodes().item(1).getTextContent().trim();
                    format = tdNodeList.item(7).getTextContent().trim();
                    if (format.equals("sparql")) {
                        HtmlPage page2 = webClient.getPage(txtLodstatsUrl.getText() + url);
                        File f = new File("datasets/lodstats/stats.lod2.eu" + url + "-" + (new java.util.Date(System.nanoTime())).toString().replaceAll(" ", "").replaceAll(":", "") + ".html");
                        f.getParentFile().mkdirs();
                        f.createNewFile();
                        FileUtils.writeStringToFile(f, page2.getWebResponse().getContentAsString());

                        DocumentBuilderFactory dbf2
                        = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db2 = dbf.newDocumentBuilder();
                        InputSource is2 = new InputSource();
                        is2.setCharacterStream(new StringReader(page2.asXml()));
                        Document doc2 = db.parse(is2);

                        NodeList liNodeList = doc2.getElementsByTagName("div");
                        for (int k = 0; k < nodeList.getLength(); k++) {
                            try {
                                if (liNodeList.item(k).getAttributes().getNamedItem("class").getTextContent().equals("content")) {
                                    String endpointurl = liNodeList.item(k).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getTextContent().trim();
                                    if (endpointurl.endsWith("/")) {
                                        endpointurl = endpointurl.substring(0, endpointurl.length() - 1);
                                    }

                                    String SQL = "SELECT * FROM endpoints where endpointUrl='" + endpointurl + "' and source='lodstats'";
                                    Statement stmt = con.createStatement();
                                    ResultSet rs = stmt.executeQuery(SQL);
                                    if (!rs.next()) {

                                        String SQLi = "INSERT INTO endpoints (datasetName,endpointUrl,source) VALUES (?,?,?);";
                                        PreparedStatement pstmt
                                        = con.prepareStatement(SQLi);
                                        pstmt.setString(1, datasetName);
                                        pstmt.setString(2, endpointurl);
                                        pstmt.setString(3, "lodstats");
                                        //Statement stmt = con.createStatement();
                                        pstmt.executeUpdate();
                                        pstmt.close();

                                    }
                                    rs.close();
                                    stmt.close();
                                    System.out.println(liNodeList.item(k).getChildNodes().item(3).getChildNodes().item(1).getChildNodes().item(1).getTextContent().trim());
                                }
                            } catch (Exception ex) {

                            }
                            // .getChildNodes().item(1).getTextContent();
                        }
                    }
                    //System.out.println(format + " " + datasetName + " " + url);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // do something with the current element
                    //                 System.out.println(node.getNodeName());
                }
            }
            //    recursiveXmlParse(doc, i, isStarted);
        }
        } catch (Exception ex) {

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLodstatsActionPerformed

    private void btnParseLODCloudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParseLODCloudActionPerformed
        try {
            final String query
            = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "prefix void: <http://rdfs.org/ns/void#>\n"
            + "prefix dcterms: <http://purl.org/dc/terms/>\n"
            + "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "prefix foaf: <http://xmlns.com/foaf/0.1/>\n"
            + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "prefix skos: <http://www.w3.org/2004/02/skos/core#>\n"
            + "prefix tag: <http://www.holygoat.co.uk/owl/redwood/0.1/tags/>\n"
            + "prefix owl: <http://www.w3.org/2002/07/owl#>\n"
            + "\n"
            + "select ?subject ?dataset ?title ?sparqlEndpoint where {\n"
            + "  ?dataset a void:Dataset ;\n"
            + "             void:sparqlEndpoint ?sparqlEndpoint ;"
            //   + "             tag:taggedWithTag ?tags ;"
            + "             dcterms:subject ?subject ;"
            + "             dcterms:title ?title.\n"
            + "}";
            //                    + "  ?void:Dataset a dcterms:title ?title ;\n"
            //                    + "             void:sparqlEndpoint ?sparqlEndpoint.\n"
            //                    + "}";

            Model model = ModelFactory.createDefaultModel();
            try (final InputStream in = new FileInputStream("datasets/LODCLOUDvoid.ttl");) {
                model.read(in, null, "TTL");
            }

            final QueryExecution exec = QueryExecutionFactory.create(query, model);
            final com.hp.hpl.jena.query.ResultSet rs = (com.hp.hpl.jena.query.ResultSet) exec.execSelect();
            while (rs.hasNext()) {
                final QuerySolution qs = rs.next();

                SparqlEndpoint.insertNewEndpoint(qs.get("title").toString(), qs.get("sparqlEndpoint").toString(), "lodcloud", 0);//qs.get("triples").asLiteral().getInt());
            System.out.println("Dataset:" + qs.get("dataset")
                + "\n\tTitle: " + qs.get("title")
                // + "\n\tTriples" + qs.get("triples")
                + "\n\t Sparql Endpoint: " + qs.get("sparqlEndpoint")
                //+ "\n\t Tags " + qs.get("tags")
                + "\n\t Subject: " + qs.get("subject"));
        }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnParseLODCloudActionPerformed

    private void btnParseDatahubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParseDatahubActionPerformed

        try {
            URL url;
            HttpURLConnection connection = null;
            //Create connection
            url = new URL(txtDatahubUrl.getText());
            connection = (HttpURLConnection) url.openConnection();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            //       PrintWriter out = new PrintWriter("sparqles" + (new java.util.Date(System.nanoTime())).toString() + ".txt",);

            final WebClient webClient = new WebClient();
            String pageresp = webClient.getPage(txtDatahubUrl.getText()).getWebResponse().getContentAsString();
            //  txtSeeds.setText(pageresp);
            //  String pageresp = page1.getWebResponse().getContentAsString();
            //            while ((line = rd.readLine()) != null) {
                //                response.append(line);
                //
                //                //    response.append('\r');
                //            }

            JSONObject obj = new JSONObject(pageresp);
            //            if (!obj.getString("status").equals("OK")) {
                //                return;
                //            }

            JSONArray arr = ((JSONObject) obj.get("result")).getJSONArray("results");

            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).get("format").toString().equals("sparql") || arr.getJSONObject(i).get("format").toString().equals("api/sparql")) {
                    SparqlEndpoint.insertNewEndpoint("", arr.getJSONObject(i).get("url").toString(), "datahub.io", 0);//qs.get("triples").asLiteral().getInt());

                //                    txtSeeds.append(arr.getJSONObject(i).get("format").toString());
                //                    txtSeeds.append(":");
                //                    txtSeeds.append(arr.getJSONObject(i).get("url").toString());
                //                    txtSeeds.append("\n");
            }
        }
        // get the first result

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnParseDatahubActionPerformed

    private void btnParseSparqlesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParseSparqlesActionPerformed
        final WebClient webClient = new WebClient();
        try {
            URL url;
            HttpURLConnection connection = null;
            //Create connection
            url = new URL(txtSparqlesUrl.getText());
            connection = (HttpURLConnection) url.openConnection();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            //       PrintWriter out = new PrintWriter("sparqles" + (new java.util.Date(System.nanoTime())).toString() + ".txt",);

            while ((line = rd.readLine()) != null) {
                response.append(line);

                //    response.append('\r');
            }
            File f = new File("datasets/sparqles" + (new java.util.Date(System.nanoTime())).toString().replaceAll(" ", "").replaceAll(":", "") + ".txt");
            f.createNewFile();
            f.getParentFile().mkdirs();
            FileUtils.writeStringToFile(f, response.toString());

            rd.close();
            String[] s = response.toString().replace(((char) 13), ' ').replace((char) 10, ' ').split("  ]   }, ");
            //    response.toString().replace((char)13, ' ').replace(char) 10, ' ').split("  ]  },");
        //            HtmlPage page1 = webClient.getPage("http://sparqles.okfn.org/api/endpoint/list");
        //            String content = page1.getWebResponse().getContentAsString();
        //            String[] s = content.split("},");
        for (int i = 0; i < s.length; i++) {
            int sparqluristart = s[i].indexOf("uri") + 7;
            int dataseturistart = s[i].indexOf("uri", sparqluristart + 6) + 7;
            int labelstart = s[i].indexOf("label") + 8;
            String sparqlurl = "";
            String datasetUrl = "";
            String label = "";

            try {
                sparqlurl = s[i].substring(sparqluristart, s[i].indexOf(",", sparqluristart) - 1).replace('"', ' ').trim();
                } catch (Exception ex) {
                }
                try {
                    datasetUrl = s[i].substring(dataseturistart, s[i].indexOf(",", dataseturistart) - 1).replace('"', ' ').trim();
                    } catch (Exception ex) {
                    }
                    try {
                        label = s[i].substring(labelstart, s[i].indexOf("}", labelstart)).replace('"', ' ').trim();
                        } catch (Exception ex) {
                        }
                        SparqlEndpoint.insertNewEndpoint(label, sparqlurl, "sparqles", 0);
                        System.out.println(sparqlurl + datasetUrl + label);
                    }
                    String a = s[1];
                    //               baseUrl = page1.getUrl().toString();
                } catch (Exception ex2) {
                    String aaa = "deneme";
                }        // TODO add your handling code here:
    }//GEN-LAST:event_btnParseSparqlesActionPerformed

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        String urlRegex2 = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
        String urlRegex3 = "_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?$_iuS";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
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

    private String limitString(String s, int length) {
        if (s.length() > length) {
            return s.substring(0, length - 1);
        } else {
            return s;
        }

    }

    public ArrayList<String> getHypernyms(String word) {
        System.setProperty("wordnet.database.dir", "C:\\Users\\Administrator\\OneDrive\\SPEC\\WordNet-3.0\\dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();

        NounSynset nounSynset;
        NounSynset[] hypernyms;
        ArrayList<String> hypList = new ArrayList<>();
        try {
            try {
                Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
                for (int i = 0; i < synsets.length; i++) {
                    nounSynset = (NounSynset) (synsets[i]);
                    hypernyms = nounSynset.getHypernyms();

                    String[] hypernymsArray = new String[hypernyms.length];
                    int index = 0;
                    for (NounSynset h : hypernyms) {
                        for (String hypWord : h.getWordForms()) {
                            hypList.add(hypWord);//hypernymsArray[index++]=hypWord;
                        }
//                    System.err.println(nounSynset.getWordForms()[0]
//                            + ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
                    } // TODO add your handling code here:
                }
                return hypList;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
        return null;
    }

    private void loadUrlTable() {
        try {
            PreparedStatement pstmt
                    = con.prepareStatement("select url,searchEngine,dateCreated,queryText from form_url_view;");
            // execute the query, and get a java resultset
            ResultSet rs = pstmt.executeQuery();;

            // iterate through the java resultset
            while (rs.next()) {
                String url = rs.getString("url");
                String searchEngine = rs.getString("searchEngine");
                Timestamp time = rs.getTimestamp("dateCreated");
                String queryText = rs.getString("queryText");

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                Object[] row = {searchEngine, queryText, url, dateFormat.format(time)};
                DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                //model.addRow();
                model.insertRow(0, row);

                //String firstName = rs.getString("url");
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            //System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    boolean isStarted = false;

    private void createSearchQueue() {
        try {

            String[] searchTexts = txtSearchTexts.getText().split("\\n");
            List selist = lstSearchEngines.getSelectedValuesList();
            for (Object se : selist) {
                for (String st : searchTexts) {
                    String SQLi = "INSERT INTO searchqueue (searchText,searchEngineName, disabled) VALUES (?,?,0)";
                    PreparedStatement pstmt = con2.prepareStatement(SQLi);
                    pstmt.setString(1, st);
                    pstmt.setString(2, se.toString());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
            }
            // return id + 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private boolean isQueryExistInQueryQueue(String query) {
        try {

            String SQL = "SELECT id FROM searchqueue where searchText='" + query + "';";
            Statement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {

                stmt.close();
                rs.close();
                return true;
            }

            stmt.close();
            rs.close();
            // return id + 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return false;
    }

    private void recursiveXmlParse(Node node, int id, boolean found) throws DOMException, SQLException {
        NodeList nodeList = node.getChildNodes();
        if (node.getNodeName().equals("binding")) {
            found = true;
        } else if (node.getNodeName().equals("literal") && found) {
            String value = node.getTextContent();
            if (value.matches("\\d+")) {
                String SQL = "UPDATE queryqueue SET resultSetData =" + value + " where id=" + id + ";";
                Statement stmt = con.createStatement();
                stmt.execute(SQL);
                System.out.println(String.valueOf(id) + ": " + node.getTextContent());
            }
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            try {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    //calls this method for all the children which is Element
                    recursiveXmlParse(currentNode, id, found);
                }
            } catch (Exception ex) {

            }
        }
//return result;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });

//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MainForm().setVisible(true);
//            }
//        });
    }

    /*
     * Over load processEvent which is inherited from class java.awt.Window
     * Our defined EventSearchWorker will be handled here.
     */
    protected void processEvent(AWTEvent event) {

        if (!(event instanceof WindowEvent)) {
            if (event instanceof EventSearchWorker) {
                EventSearchWorker ev = (EventSearchWorker) event;
                if (ev.getPercent() > 0) {
                    jProgressBar1.setValue(jProgressBar1.getValue() + ev.getPercent());//ev.getPercent());

                } else {
                    lblCount4.setText(String.valueOf(++totalUrlCount));
                    //txtSeeds.append(ev.getStr());

                    DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                    //model.addRow();
                    model.insertRow(0, ev.getRowValues());

                }
                if (ev.hasMessage()) {
                    lblMessage4.setText(ev.getMessage());
                }
            }

        } else if (event instanceof EventSparqlWorker) {// other events go to the system default process event handler

        } else {
            super.processEvent(event);
        }
    }                                     // processEvent

    // ------------------ ActionListener --------------------------
    public void actionPerformed(ActionEvent e) {
    }
    // windowClosing

    public String getQueryText() {
        return txtSearchText.getText();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateSearchQueue;
    private javax.swing.JButton btnCreateSearchQueueFromPreviousUrls;
    private javax.swing.JButton btnIoTCollectResults;
    private javax.swing.JButton btnLodstats;
    private javax.swing.JButton btnParseDatahub;
    private javax.swing.JButton btnParseLODCloud;
    private javax.swing.JButton btnParseResponse;
    private javax.swing.JButton btnParseSemanticDiscovery;
    private javax.swing.JButton btnParseSparqles;
    private javax.swing.JButton btnPrepareQueryQueue;
    private javax.swing.JButton btnRunAnalyzer;
    private javax.swing.JButton btnRunMultitextSearch;
    private javax.swing.JButton btnRunSeedGenerator;
    private javax.swing.JButton btnSameAs;
    private javax.swing.JButton btnStartRemoteQueries;
    private javax.swing.JButton btnStatisticalAnalysis;
    private javax.swing.JButton btnStatusMonitor;
    private javax.swing.JButton btnStopAnalyzer4;
    private javax.swing.JButton btnStopSearchQueue4;
    private javax.swing.JButton btnStopThreads4;
    private javax.swing.JButton btnTfidf;
    private javax.swing.JButton btnWordnetAnalyzer;
    private java.awt.Choice choiceSearchEngine;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonAnalyzeEndpointHtml;
    private javax.swing.JButton jButtonExportRclh;
    private javax.swing.JButton jButtonExportSelectedTriples;
    private javax.swing.JButton jButtonGetSparqlEndpointWebPages;
    private javax.swing.JButton jButtonGetSubjectsFromLODCLOUD;
    private javax.swing.JButton jButtonIDF;
    private javax.swing.JButton jButtonPostFilterUrls;
    private javax.swing.JButton jButtonPrepareEndpointLcnWordTable;
    private javax.swing.JButton jButtonProfiler;
    private javax.swing.JButton jButtonResetEndpointsForProcessing;
    private javax.swing.JButton jButtonRunClassCollector;
    private javax.swing.JButton jButtonTabloyaAktar;
    private javax.swing.JButton jButtonWordnetLevel;
    private javax.swing.JButton jButtonWordnetlevel;
    private javax.swing.JButton jButtontfidftop20;
    private javax.swing.JButton jButtonwordnetTdidf;
    private javax.swing.JComboBox jComboCommonQueries;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemConfig;
    private javax.swing.JMenuItem jMenuItemCreateInitialDatabase;
    private javax.swing.JMenuItem jMenuItemSaveTask;
    private javax.swing.JMenuItem jMenuItemSaveUrlList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaFederatedQuery;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label9;
    private javax.swing.JLabel lblCount4;
    private javax.swing.JLabel lblMessage4;
    private java.awt.Label lblSearchText4;
    private javax.swing.JList lstSearchEngines;
    private java.awt.Panel panel1;
    private javax.swing.JPanel pnlCrawler;
    private javax.swing.JPanel pnlSparql;
    private javax.swing.JTextField txtDatahubUrl;
    private javax.swing.JTextField txtLodstatsUrl;
    private javax.swing.JTextField txtMaxCollectorThreads;
    private javax.swing.JTextField txtMaxPage;
    private javax.swing.JTextField txtNumOfThreadsStatisticalAnalysis;
    private javax.swing.JTextField txtNumOfThreadsStatusMonitoring;
    private java.awt.TextField txtSearchText;
    private javax.swing.JTextArea txtSearchTexts;
    private java.awt.TextArea txtSeeds;
    private javax.swing.JTextField txtSparqlesUrl;
    private javax.swing.JTextField txtStatisticalAnalysisCheckEveryHours;
    private javax.swing.JTextField txtStatisticalAnalysisTimeout;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent we) {
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent we) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent we) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent we) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
