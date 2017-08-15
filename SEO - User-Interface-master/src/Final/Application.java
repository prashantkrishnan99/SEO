package practice;
import java.io.IOException;
import org.apache.lucene.store.LockObtainFailedException;

public class Application {

	public static void main(String[] args) throws LockObtainFailedException, IOException {
		// TODO Auto-generated method stub
		Similarity();

	}
	 public static void Similarity() throws LockObtainFailedException, IOException
	    {
	       Indexer index = new Indexer();
	       index.index();
	       IndexGenerator j = new IndexGenerator();
	       j.GetAllTerms();       
	       IndexDoc[] docVector = j.GetDocumentVectors(); // getting document vectors
	       for(int i = 0; i < docVector.length; i++)
	       {
	           double cs = Similarity.CosineSimilarity(docVector[1], docVector[i]);
	           System.out.println("Cosine Similarity Score between document 0 and "+i+"  = " + cs);
	       }    
	    }

}
