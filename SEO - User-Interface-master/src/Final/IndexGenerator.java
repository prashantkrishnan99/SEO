package practice;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
public class IndexGenerator {
	 IndexDoc[] docVector;
	    private Map allterms;
	    Integer totalNoOfDocumentInIndex;
	    IndexReader indexReader;
	    
	    public IndexGenerator() throws IOException
	    {
	        allterms = new HashMap<>();
	        indexReader = IndexOpener.GetIndexReader();
	        totalNoOfDocumentInIndex = IndexOpener.TotalDocumentInIndex();
	        docVector = new IndexDoc[totalNoOfDocumentInIndex];
	    }
	    
	    public void GetAllTerms() throws IOException
	    {
	        TermInitializer allTerms = new TermInitializer();
	        allTerms.initterms();
	        allterms = allTerms.getterms();
	    }
	    
	    public IndexDoc[] GetDocumentVectors() throws IOException {
	        for (int docId = 0; docId < totalNoOfDocumentInIndex; docId++) {
	            Terms vector = indexReader.getTermVector(docId, Configuration.FIELD_CONTENT);
	            TermsEnum termsEnum = null;
	            termsEnum = vector.iterator(termsEnum);
	            BytesRef text = null;            
	            docVector[docId] = new IndexDoc(allterms);            
	            while ((text = termsEnum.next()) != null) {
	                String term = text.utf8ToString();
	                int freq = (int) termsEnum.totalTermFreq();
	                docVector[docId].setEntry(term, freq);
	            }
	            docVector[docId].normalize();
	        }        
	        indexReader.close();
	        return docVector;
	    }

}
