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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "searchEngines")
public class SearchEngines {

    @XmlElement(name = "searchEngine", type = SearchEngine.class)
    private List<SearchEngine> searchEngines = new ArrayList<SearchEngine>();

    public SearchEngines() {
    }

    public SearchEngines(List<SearchEngine> selist) {
        this.searchEngines = selist;
    }

    public List getSearchEngines() {
        return searchEngines;
    }

    public void setSearchEngines(List selist) {
        this.searchEngines = selist;
    }
}
