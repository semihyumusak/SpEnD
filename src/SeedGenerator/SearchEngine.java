/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeedGenerator;

/**
 *
 * @author Semih
 */
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchEngine{
 
	String name;
        String excludedWords;
        String baseUrl;
        String queryTextBoxName;
        String submitButtonId;
        String submitButtonName;
        String defaultBrowser;
        String nextButtonIdentifier;
        String useUrlRedirection;
        int waitIntervalMs;
        
	public int getWaitIntervalMs() {
		return waitIntervalMs;
	}
 
	@XmlElement
	public void setWaitIntervalMs(int waitInterval) {
		this.waitIntervalMs = waitInterval;
	}
        
	public String getName() {
		return name;
	}
 
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
        
	public String getExcludedWords() {
		return excludedWords;
	}
 
	@XmlElement
	public void setExcludedWords(String excludedWords) {
		this.excludedWords = excludedWords;
	}
 
	public String getBaseUrl() {
		return baseUrl;
	}
 
	@XmlElement
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getQueryTextBoxName() {
		return queryTextBoxName;
	}
 
	@XmlElement
	public void setQueryTextBoxName(String queryTextBoxName) {
		this.queryTextBoxName = queryTextBoxName;
	}
	
	public String getSubmitButtonId() {
		return submitButtonId;
	}
 
	@XmlElement
	public void setSubmitButtonId(String submitButtonId) {
		this.submitButtonId = submitButtonId;
	}
	public String getSubmitButtonName() {
		return submitButtonName;
	}
 
	@XmlElement
	public void setSubmitButtonName(String submitButtonName) {
		this.submitButtonName = submitButtonName;
	}
	public String getDefaultBrowser() {
		return defaultBrowser;
	}
 
	@XmlElement
	public void setDefaultBrowser(String defaultBrowser) {
		this.defaultBrowser = defaultBrowser
                        ;
	}
	public String getNextButtonIdentifier() {
		return nextButtonIdentifier;
	}
 
	@XmlElement
	public void setNextButtonIdentifier(String nextButtonIdentifier) {
		this.nextButtonIdentifier= nextButtonIdentifier;
	}
	public String getUseUrlRedirection() {
		return useUrlRedirection;
	}
 
	@XmlElement
	public void setUseUrlRedirection(String useUrlRedirection) {
		this.useUrlRedirection= useUrlRedirection;
	}
}