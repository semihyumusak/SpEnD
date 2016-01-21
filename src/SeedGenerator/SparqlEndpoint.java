/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Semih
 */
public class SparqlEndpoint {

    static String connectionUrl = "jdbc:mysql://localhost/crawler?" + "user=root&password=62217769";
    static Connection con ;

    public String dataSetName;
    public String endpointUrl;
    public int triples;
    public String source;

    public SparqlEndpoint() {
        try {
            connectionUrl = getConnectionString();
            Class.forName("com.mysql.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception ex) {

        }
    }

    private static String getConnectionString() {
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
    public static void insertNewEndpoint(String datasetName, String endpointurl, String source, int triples) {
        try {
             connectionUrl = getConnectionString();
            Class.forName("com.mysql.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(connectionUrl);
            
            if (endpointurl.endsWith("/")) {
                endpointurl = endpointurl.substring(0, endpointurl.length() - 1);
            }
            String SQL = "SELECT * FROM endpoints where endpointUrl='" + endpointurl + "' and source='"+source+"'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (!rs.next()) {
                String SQLi = "INSERT INTO endpoints (datasetName,endpointUrl,source,triples) VALUES (?,?,?,?);";
                PreparedStatement pstmt
                        = con.prepareStatement(SQLi);
                pstmt.setString(1, datasetName);
                pstmt.setString(2, endpointurl);
                pstmt.setString(3, source);
                pstmt.setInt(4, triples);
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void updateEndpoint(String datasetName, String endpointurl, String source, String subject, int triples) {
        try {
               connectionUrl = getConnectionString();
            Class.forName("com.mysql.jdbc.Driver");//.newInstance();
            con = DriverManager.getConnection(connectionUrl);
            
            if (endpointurl.endsWith("/")) {
                endpointurl = endpointurl.substring(0, endpointurl.length() - 1);
            }
            String SQL = "SELECT * FROM endpoints where endpointUrl='" + endpointurl + "' and source LIKE '%"+source+"%'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                String SQLi = "UPDATE endpoints SET subject = ?, datasetName = ?, triples = ? where id = ?";
                PreparedStatement pstmt
                        = con.prepareStatement(SQLi);
                pstmt.setString(1, subject.split("/")[subject.split("/").length-1]);
                pstmt.setString(2, datasetName);
                pstmt.setInt(3, triples);
                pstmt.setInt(4, rs.getInt("id"));
                pstmt.executeUpdate();
                pstmt.close();
            }
             else {
                String SQLi = "INSERT INTO endpoints (datasetName,endpointUrl,source,triples,subject) VALUES (?,?,?,?,?);";
                PreparedStatement pstmt
                        = con.prepareStatement(SQLi);
                pstmt.setString(1, datasetName);
                pstmt.setString(2, endpointurl);
                pstmt.setString(3, source);
                pstmt.setInt(4, triples);
                pstmt.setString(5, subject);
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
