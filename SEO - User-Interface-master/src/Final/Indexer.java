package practice;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

class Similarity {
	public static double CosineSimilarity(IndexDoc d1,IndexDoc d2) {
        double cosinesimilarity;
        try {
            cosinesimilarity = (d1.vector.dotProduct(d2.vector))
                    / (d1.vector.getNorm() * d2.vector.getNorm());
        } catch (Exception e) {
            return 0.0;
        }
        return cosinesimilarity;
    }

}

class IndexOpener {
	 public static IndexReader GetIndexReader() throws IOException {
	        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File(Configuration.INDEX_DIRECTORY)));
	        return indexReader;
	    }

	    public static Integer TotalDocumentInIndex() throws IOException
	    {
	        Integer maxDoc = GetIndexReader().maxDoc();
	        GetIndexReader().close();
	        return maxDoc;
	    }

}


public class Indexer {

	 private final File sourceDirectory;
	    private final File indexDirectory;
	    private static String fieldName;

	    public Indexer() {
	        this.sourceDirectory = new File(Configuration.SOURCE_DIRECTORY_TO_INDEX);
	        this.indexDirectory = new File(Configuration.INDEX_DIRECTORY);
	        fieldName = Configuration.FIELD_CONTENT;
	    }
	    public void index() throws CorruptIndexException,
	            LockObtainFailedException, IOException {
	        Directory dir = FSDirectory.open(indexDirectory);
	        Analyzer analyzer = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET);  // using stop words
	        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

	        if (indexDirectory.exists()) {
	            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	        } else {
	            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	        }

	        IndexWriter writer = new IndexWriter(dir, iwc);
	        for (File f : sourceDirectory.listFiles()) {
	            Document doc = new Document();
	            FieldType fieldType = new FieldType();
	            fieldType.setIndexed(true);
	            fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
	            fieldType.setStored(true);
	            fieldType.setStoreTermVectors(true);
	            fieldType.setTokenized(true);
	            Field contentField = new Field(fieldName, getAllText(f), fieldType);
	            //
	            try {
	            Field pathField = new StringField("path", f.toString(), Field.Store.YES);
	  	      doc.add(pathField);
	            }catch (Exception e) {
					// TODO: handle exception
				}
	            //
	            doc.add(contentField);
	            
	            writer.addDocument(doc);
	        }
	        writer.close();
	    }
	    public String getAllText(File f) throws FileNotFoundException, IOException {
	        String textFileContent = "";

	        for (byte line : Files.readAllBytes(Paths.get(f.getAbsolutePath()))) {
	            textFileContent += line;
	        }
	        return textFileContent;
	    }
	
}
