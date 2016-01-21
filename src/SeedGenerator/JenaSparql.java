/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * @author Simon Jupp
 * @date 11/09/2013 Functional Genomics Group EMBL-EBI
 *
 * Example of querying the Gene Expression Atlas SPARQL endpoint from Java using
 * the Jena API (http://jena.apache.org)
 *
 */
public class JenaSparql {
  //static String sparqlEndpoint;// = "http://www.ebi.ac.uk/rdf/services/atlas/sparql";

    // get expression values for uniprot acc Q16850
//  String sparqlQuery = "" +
//  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//  "PREFIX atlasterms: <http://rdf.ebi.ac.uk/terms/atlas/>" +
//  "SELECT distinct ?expressionValue ?pvalue \n" +
//  "WHERE { \n" +
//  "?value rdfs:label ?expressionValue . \n" +
//  "?value atlasterms:pValue ?pvalue . \n" +
//  "?value atlasterms:isMeasurementOf ?probe . \n" +
//  "?probe atlasterms:dbXref ?uniprotAccession .\n" +
//  "} \n" +
//  "ORDER BY ASC(?pvalue)";
//   String sparqlQuery = "" +
//  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
//"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
//"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
//"PREFIX dcterms: <http://purl.org/dc/terms/>"+
//"PREFIX obo: <http://purl.obolibrary.org/obo/>"+
//"PREFIX sio: <http://semanticscience.org/resource/>"+
//"PREFIX efo: <http://www.ebi.ac.uk/efo/>"+
//"PREFIX atlas: <http://rdf.ebi.ac.uk/resource/atlas/>"+
//"PREFIX atlasterms: <http://rdf.ebi.ac.uk/terms/atlas/>"+
//
//"SELECT DISTINCT ?experiment ?description WHERE {"+
//"?experiment a atlasterms:Experiment ."+
//"?experiment dcterms:description ?description"+
//"}";
    private JenaSparql() {
        //    sparqlEndpoint = url;
    }
    public static int queryTimeOut = 30000;

    public static boolean isSparqlEndpoint(String sparqlEndpoint) {
        String sparqlQuery2 = "select distinct ?Concept where {[] a ?Concept} LIMIT 10";
        String sparqlQuery1 = "select ?s where {?s ?p ?o} LIMIT 10";
        String sparqlQuery = "ASK  {?x ?g ?v}";
        
        // create the Jena query using the ARQ syntax (has additional support for SPARQL federated queries)

        Query query2 = QueryFactory.create(sparqlQuery, Syntax.syntaxARQ);
        Query query1 = QueryFactory.create(sparqlQuery, Syntax.defaultQuerySyntax);
        Query query3 = QueryFactory.create(sparqlQuery, Syntax.defaultSyntax);
        Query query = QueryFactory.create(sparqlQuery, Syntax.syntaxSPARQL);
        Query query4 = QueryFactory.create(sparqlQuery, Syntax.syntaxSPARQL_10);

//     we want to bind the ?uniprotAccession variable in the query
//     to the URI for Q16850 which is http://purl.uniprot.org/uniprot/Q16850
//    QuerySolutionMap querySolutionMap = new QuerySolutionMap();
//    querySolutionMap.add("uniprotAccession", new ResourceImpl("http://purl.uniprot.org/uniprot/Q16850"));
//    ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString(query.toString(),);
//    ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString(query.toString(), querySolutionMap);
//     QueryEngineHTTP httpQuery = new QueryEngineHTTP(sparqlEndpoint,parameterizedSparqlString.asQuery());
        try {
            QueryEngineHTTP httpQuery = new QueryEngineHTTP(sparqlEndpoint, query);

            httpQuery.setTimeout(queryTimeOut);
            // execute a Select query
            boolean results = httpQuery.execAsk();
            //ResultSet results = httpQuery.execSelect();
            return true;

//            while (results.hasNext()) {
//                QuerySolution solution = results.next();
//                return true;
            // System.out.println( solution.toString());
//      // get the value of the variables in the select clause
//      String expressionValue = solution.get("expressionValue").asLiteral().getLexicalForm();
//      String pValue = solution.get("pvalue").asLiteral().getLexicalForm();
            // print the output to stdout
//      System.out.println(expressionValue + "\t" + pValue);
        } catch (Exception ex) {
            System.out.println(sparqlEndpoint);
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static String getSparqlXMLResult(String sparqlEndpoint, String sparqlQuery) {
        Query query;
        QueryEngineHTTP httpQuery;
        ResultSet results;
        try {

            try {
                query = QueryFactory.create(sparqlQuery, Syntax.syntaxARQ);
            } catch (Exception ex) {
                System.out.println("Exception (Query Generation): " + ex.getMessage());
                return null;//"Exception (Query Generation): " + ex.getMessage();
            }

            try {
                httpQuery = new QueryEngineHTTP(sparqlEndpoint, sparqlQuery);
                httpQuery.setTimeout(queryTimeOut);
                results = httpQuery.execSelect();
                String resultString = com.hp.hpl.jena.query.ResultSetFormatter.asXMLString(results);
                return resultString;

            } catch (com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP ex) {
                System.out.println("Unknown Host: " + ex.getMessage());
            } catch (Exception ex) {
                Thread.sleep(1000);
                System.out.println("Exception (Query Execution): " + ex.getMessage());
            }
            return null;
        } catch (Exception ex) {

            System.out.println("Exception (Result Parse)" + ex.getMessage());
            return null;
        }
    }

    public static String getSparqlXMLResult(String sparqlEndpoint, String sparqlQuery, int offset, int limit) {
        Query query;
        QueryEngineHTTP httpQuery;
        ResultSet results;
        sparqlQuery = sparqlQuery + " OFFSET " + offset + " LIMIT " + limit;
        try {

            try {
                query = QueryFactory.create(sparqlQuery, Syntax.syntaxARQ);
            } catch (Exception ex) {
                System.out.println("Exception (Query Generation): " + ex.getMessage());
                return null;//"Exception (Query Generation): " + ex.getMessage();
            }

            try {
                httpQuery = new QueryEngineHTTP(sparqlEndpoint, sparqlQuery);
                httpQuery.setTimeout(queryTimeOut);
                results = httpQuery.execSelect();
                String resultString = com.hp.hpl.jena.query.ResultSetFormatter.asXMLString(results);
                return resultString;

            } catch (com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP ex) {
                System.out.println("Unknown Host: " + ex.getMessage());
            } catch (Exception ex) {
                Thread.sleep(1000);
                System.out.println("Exception (Query Execution): " + ex.getMessage());
            }
            return null;
        } catch (Exception ex) {

            System.out.println("Exception (Result Parse)" + ex.getMessage());
            return null;
        }
    }

    public static Resource getSparqlRDFResult(String sparqlEndpoint, String sparqlQuery) {
        Query query;
        QueryEngineHTTP httpQuery;
        ResultSet results;
        try {

            try {
                query = QueryFactory.create(sparqlQuery, Syntax.syntaxARQ);
            } catch (Exception ex) {
                //return "Exception (Query Generation): " + ex.getMessage();
            }

            try {
                httpQuery = new QueryEngineHTTP(sparqlEndpoint, sparqlQuery);
                httpQuery.setTimeout(queryTimeOut);
                results = httpQuery.execSelect();
                Resource result = com.hp.hpl.jena.query.ResultSetFormatter.asRDF(null, results);

                return result;
            } catch (Exception ex) {
                //return "Exception (Query Execution): " + ex.getMessage();

            }

            //while (results.hasNext()) {
            //    QuerySolution solution = results.next();
            //    return true;
            // System.out.println( solution.toString());
//      // get the value of the variables in the select clause
//      String expressionValue = solution.get("expressionValue").asLiteral().getLexicalForm();
//      String pValue = solution.get("pvalue").asLiteral().getLexicalForm();
            // print the output to stdout
//      System.out.println(expressionValue + "\t" + pValue);
            //        }
        } catch (Exception ex) {
            //return "Exception (Result Parse): " + ex.getMessage();
        }
        return null;
//        return false;
    }

    /**
     * @return the timeOut
     */
    public int getTimeOut() {
        return queryTimeOut;
    }

    /**
     * @param timeOut the timeOut to set
     */
    public void setTimeOut(int queryTimeOut) {
        this.queryTimeOut = queryTimeOut;
    }

}
