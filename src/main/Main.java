package main;

import Business.TranslateBusiness;
import Business.TranslateBusiness2;
import Business.TranslateBusiness3;

public class Main {

	//private static TranslateBusiness2 translateBusiness = new TranslateBusiness2();
	private static TranslateBusiness3 translateBusiness = new TranslateBusiness3();

	// �������
	public static void main(String[] args) throws Exception {

		String fromFile = "C:\\Users\\Administrator\\Desktop\\����\\����\\src.txt";
		String toFile = "C:\\Users\\Administrator\\Desktop\\����\\����\\result.txt";
		
		translateBusiness.translateFromFileToFile(fromFile, toFile, "en", "zh");
		
	}

}
