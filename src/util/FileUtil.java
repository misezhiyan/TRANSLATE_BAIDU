package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FileUtil {

	public static void writeIntoFileWithDir(String fileRealPath, String content) throws Exception {
		File file = new File(fileRealPath);
		File file_package = file.getParentFile();
		if (!file_package.exists())
			file_package.mkdirs();
		writeIntoFile(fileRealPath, content);
	}

	public static void appendToFile(String fileRealPath, String content) throws Exception {

		String file_content = fileReadToString(fileRealPath);

		// file_content += "\r\n";
		// file_content += "\r\n";
		file_content += content;

		writeIntoFile(fileRealPath, file_content);
	}

	public static String fileReadToString(String fileRealPath) throws Exception {
		
		File file = new File(fileRealPath);
		if (!file.exists())
			throw new Exception("文件不存在");

		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(filecontent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}

		String encoding = EncodingUtil.getFileEncodingType(fileRealPath);
		
		String result = new String(filecontent, encoding);
		return result;
	}

	public static boolean writeIntoFile(String filePath, String fileName, String fileType, String content)
			throws Exception {

		if (StringUtil.isEmpty(filePath) || StringUtil.isEmpty(fileName) || StringUtil.isEmpty(fileType))
			throw new Exception("can not be null");

		String fileFullName = fileName.trim() + "." + fileType.trim();
		return writeIntoFile(filePath, fileFullName, content);
	}

	private static boolean writeIntoFile(String filePath, String fileFullName, String content) throws Exception {

		if (StringUtil.isEmpty(filePath) || StringUtil.isEmpty(fileFullName))
			throw new Exception("can not be null");
		String fileRealPath = filePath.trim() + fileFullName.trim();
		File file_package = new File(filePath);
		if (!file_package.exists())
			file_package.mkdirs();

		return writeIntoFile(fileRealPath, content);
	}

	/*
	 * ***** // 编码格式问题 private static boolean writeIntoFile(String fileRealPath,
	 * String content) throws Exception { if (empty(fileRealPath)) throw new
	 * Exception("can not be null");
	 * 
	 * File file = new File(fileRealPath); if (!file.exists()) {
	 * file.createNewFile(); } FileWriter fileWriter = new FileWriter(file); try {
	 * fileWriter.write(content); fileWriter.flush(); } catch (Exception e) {
	 * e.printStackTrace(); } finally { fileWriter.close(); }
	 * 
	 * return true; }
	 */
	private static boolean writeIntoFile(String fileRealPath, String content) throws Exception {
		if (StringUtil.isEmpty(fileRealPath))
			throw new Exception("can not be null");

		File file = new File(fileRealPath);

		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileRealPath), "UTF-8");
		out.write(content);
		out.flush();
		out.close();

		return true;
	}

}
