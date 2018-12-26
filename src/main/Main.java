package main;

import Business.TranslateBusiness;

public class Main {

	//private static TranslateBusiness2 translateBusiness = new TranslateBusiness2();
	private static TranslateBusiness translateBusiness = new TranslateBusiness();

	// 翻译入口
	public static void main(String[] args) throws Exception {

		String fromFile = "C:\\Users\\Administrator\\Desktop\\工作\\翻译\\src.txt";
		String toFile = "C:\\Users\\Administrator\\Desktop\\工作\\翻译\\result.txt";
		
		translateBusiness.translateFromFileToFile(fromFile, toFile, "en", "zh");
		
	}

}
