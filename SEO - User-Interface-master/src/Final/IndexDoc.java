package practice;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;


class TermInitializer {
	private Map<String,Integer> terms;
    Integer totalNoOfDocumentInIndex;
    IndexReader indexReader;
    
    public TermInitializer() throws IOException
    {    
        terms= new HashMap<>();
        indexReader = IndexOpener.GetIndexReader();
        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex();
    }
        
    public void initterms() throws IOException
    {
        int pos = 0;
        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
            Terms vector = indexReader.getTermVector(docId, Configuration.FIELD_CONTENT);
            TermsEnum termsEnum = null;
            termsEnum = vector.iterator(termsEnum);
            BytesRef text = null;
            while ((text = termsEnum.next()) != null) {
                String term = text.utf8ToString();
                terms.put(term, pos++);
            }
        }       
        
        //Update postition
        pos = 0;
        for(Entry s: terms.entrySet())
        {        
            System.out.println(s.getKey());
            s.setValue(pos++);
        }
    }
    
    public Map<String,Integer> getterms() {
        return terms;
    }
	
}


class  Configuration {
	  public static final String SOURCE_DIRECTORY_TO_INDEX = "C:/COURSES/CS454/workspace/crawler/files";
	    public static final String INDEX_DIRECTORY = "C:/COURSES/CS454/workspace/cc";
	    public static final String FIELD_CONTENT = "contents"; // name of the field to index

}

public class IndexDoc {
	
	public Map terms;
    public OpenMapRealVector vector;
    
    public IndexDoc(Map terms) {
        this.terms = terms;
        this.vector = new OpenMapRealVector(terms.size());        
    }

    public void setEntry(String term, int freq) {
        if (terms.containsKey(term)) {
        	//
            int pos =  (int) terms.get(term);
            vector.setEntry(pos, (double) freq);
        }
    }

    public void normalize() {
        double sum = vector.getL1Norm();
        vector = (OpenMapRealVector) vector.mapDivide(sum);
    }

    @Override
    public String toString() {
        RealVectorFormat formatter = new RealVectorFormat();
        return formatter.format(vector);
    }

}
