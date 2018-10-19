package po;

public class Sentense {

	private String source_sentense;
//	private SentenseAnalyzed[] needToTranslate;
//	private SentenseAnalyzed[] noTranslate;

	public void setSentense(String sentense) {
		source_sentense = sentense;
//		analyzeSentense(sentense);
	}

	public String getSentense() {
		
		return source_sentense;
	}
	
	public String getSentense_trans() {
		
		return source_sentense + "\r\n";
	}

//	private void analyzeSentense(String sentense) {
//		// 获取闭合域, 并判断闭合域是否需要翻译
//		
//	}
	
	
	
}
