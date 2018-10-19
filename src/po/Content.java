package po;

import util.StringUtil;

public class Content {

	private String from = null;
	private String to = null;

	private Paragraph[] paragraph_arr = null;

	public Paragraph[] getParagraph_arr() {
		return paragraph_arr;
	}

	public void setParagraph_arr(Paragraph[] paragraph_arr) {
		// TODO
		this.paragraph_arr = paragraph_arr;
	}

	public String getContent() {

		String content = "";

		for (Paragraph paragraph : paragraph_arr) {
			content += paragraph.getParagraph();
		}

		return content;
	}
	
	public String getContent_trans() {
		
		String content = "";
		
		for (Paragraph paragraph : paragraph_arr) {
			content += paragraph.getParagraph_trans();
		}
		
		return content;
	}

	public void setContent(String content) throws Exception {

		String[] paragraphs = content.split("\r\n");
		int length = paragraphs.length;
		paragraph_arr = new Paragraph[length];

		for (int i = 0; i < length; i++) {
			Paragraph paragraph = new Paragraph();
			String paragraph_str = paragraphs[i];

			//if("".equals(paragraph_str))continue;
			
			paragraph.setFrom(from);
			paragraph.setTo(to);
			if (StringUtil.isBlankEmpty(paragraph_str)) {
				paragraph.setEmpty(true);
			}

			paragraph.setParagraph(paragraph_str);
			paragraph_arr[i] = paragraph;
		}

	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
