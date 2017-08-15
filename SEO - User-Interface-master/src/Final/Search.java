package Final;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

/** Simple command-line based search demo. */

public class Search {
  private Search() {}
     
  static float tf = 1;
  static float idf = 0;
  private float tfidf_score;
     
  static float[] tfidf = null;
  
  public void score(IndexReader reader,String field,String term) throws IOException 
     { 
         TFIDFSimilarity tfidfSIM = new DefaultSimilarity();
         Bits liveDocs = MultiFields.getLiveDocs(reader);
         TermsEnum termEnum = MultiFields.getTerms(reader, field).iterator(null);
         BytesRef bytesRef;
         while ((bytesRef = termEnum.next()) != null) 
         {           
           if(bytesRef.utf8ToString().trim() == term.trim())
           {                  
              if (termEnum.seekExact(bytesRef)) 
              {
                 idf = tfidfSIM.idf(termEnum.docFreq(), reader.numDocs());
                 DocsEnum docsEnum = termEnum.docs(liveDocs, null);
                 if (docsEnum != null) 
                 {
                    int doc; 
                    while((doc = docsEnum.nextDoc())!=DocIdSetIterator.NO_MORE_DOCS) 
                     {
                         tf = tfidfSIM.tf(docsEnum.freq());
                         tfidf_score = tf*idf; 
                         System.out.println(" -tfidf_score- " + tfidf_score);
                     }
                 } 
             } 
           }
        } 
     }

  public static void main(String[] args) throws Exception
  {
	    String index = "index";
	    String field = "contents1";
	    String queries = null;
	    int repeat = 0;
	    boolean raw = false;
	    String queryString = null;
	    int hitsPerPage = 10;  
	  
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0])))
    {
      System.out.println("Please enter a valid command, something like -query or -index");
      System.exit(0);
    }

    
    for(int i = 0;i < args.length;i++)
    {
      if ("-index".equals(args[i]))
      {
        index = args[i+1];
        i++;
      }
      else if ("-query".equals(args[i]))
      {
        queryString = args[i+1];
        i++;
      }
    }

    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("C:/index/nn")));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer();

    Search sl=new Search();
    sl.score(reader, field, queryString);
       
    BufferedReader in = null;
    if (queries != null) {
      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
    } else {
      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }
    QueryParser parser = new QueryParser(field, analyzer);
    while (true)
    {
      if (queries == null && queryString == null)
      { 
        System.out.println("Enter the query: ");
      }

      String line = queryString != null ? queryString : in.readLine();

      if (line == null || line.length() == -1) {
        break;
      }

      line = line.trim();
      if (line.length() == 0) 
      {
        break;
      }
      
      Query query = parser.parse(line);
      System.out.println("Searching for: " + query.toString(field));
            
      if (repeat > 0)
      {
        Date start = new Date();
        for (int i = 0; i < repeat; i++)
        {
          searcher.search(query, 100);
        }
        Date end = new Date();
        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
      }

      paging(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

      if (queryString != null)
      {
        break;
      }
    }
    reader.close();
  }

   public static void paging(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = results.totalHits;
    System.out.println("There are" +numTotalHits+ " total number of matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        hits = searcher.search(query, numTotalHits).scoreDocs;
      }
      
      end = Math.min(hits.length, start + hitsPerPage);
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
          System.out.println((i+1) + ". " + path);
          String title = doc.get("title");
          if (title != null) {
            System.out.println("   Title: " + doc.get("title"));
          }
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
                  
      }

      if (!interactive || end == 0) {
        break;
      }
      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
    }
  }
}

