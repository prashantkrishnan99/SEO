package seo;

public class PageIndexer 
{
	    String uUId;		
		int wordCount;		
		double totalFactor;		
		int titleRank;
		String description;
		String title;
		
		
		public String getuUId() {
			return uUId;
		}
		public void setuUId(String uUId) {
			this.uUId = uUId;
		}

		
		public int getWordCount() {
			return wordCount;
		}
		public void setWordCount(int wordCount) {
			this.wordCount = wordCount;
		}

		
		public double getTotalFactor() {
			return totalFactor;
		}
		public void setTotalFactor(double totalFactor) {
			this.totalFactor = totalFactor;
		}

		
		public int getTitleRank() {
			return titleRank;
		}
		public void setTitleRank(int titleRank) {
			this.titleRank = titleRank;
		}

		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}		
}

