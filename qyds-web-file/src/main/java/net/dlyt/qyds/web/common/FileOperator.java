package net.dlyt.qyds.web.common;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作的底层操作类
 */
public class FileOperator {
	public FileOperator() {
	}

	public static boolean reNameFile(String oldName, String newName) {
		boolean jdg = true;
		try {
			File oldFile = new File(oldName);
			File newFile = new File(newName);
			oldFile.renameTo(newFile);
		} catch (Exception e) {
			e.printStackTrace();
			jdg = false;
		}
		return jdg;
	}

	/**
	 * 将原文件拷贝到目标文件
	 * 
	 * @param orgFile
	 *            file
	 * @param targetPath
	 * @return boolean
	 */
	public static boolean upLoadFile(File orgFile, String targetPath) {
		String fileName = getFileName(orgFile.getName());
		boolean flag = true;
		try {
			targetPath = targetPath + File.separator + fileName;
			if (!isFileExist(targetPath)) {
				createFolders(targetPath);
			}
			FileInputStream stream = new FileInputStream(orgFile);// 把文件读入
			OutputStream bos = new FileOutputStream(targetPath);// 建立一个上传文件的输出流
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			bos.close();
			stream.close();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 将原文件保存到目标文件
	 * 
	 * @param file
	 *            file
	 * @param path
	 * @return boolean
	 * @throws IOException
	 */
	public static void saveMultipartFile(MultipartFile file, String path)
			throws IOException {
		if (file == null || file.getSize() <= 0) {
			return;
		}
		File file1 = new File(path);

		if (!file1.getParentFile().exists()) {
			file1.getParentFile().mkdirs();
		}
		FileOutputStream out = new FileOutputStream(path);
		FileCopyUtils.copy(file.getInputStream(), out);
	}

//	/**
//	 * 将原文件保存到目标文件
//	 *
//	 * @param file
//	 * @param path
//	 * @return boolean
//	 * @throws IOException
//	 */
//	public static void saveMultipartFileCompress(MultipartFile file, String path)
//			throws IOException {
//		if (file == null || file.getSize() <= 0) {
//			return;
//		}
//		FileOutputStream out = new FileOutputStream(path);
//		FileCopyUtils.copy(file.getInputStream(), out);
//		CommonUtil.compressPic(path, 700);
//	}

	/**
	 * 将原文件拷贝到目标文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @return boolean
	 */
	public static boolean copyFile(String sourceFile, String targetFile) {
		String path = sourceFile;
		String target = targetFile;
		boolean flag = true;
		try {
			File file = new File(path);
			FileInputStream stream = new FileInputStream(file);// 把文件读入
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStream bos = new FileOutputStream(target);// 建立一个上传文件的输出流
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件写入服务器
			}
			bos.close();
			stream.close();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 文件夹下所有文件夹和文件的拷贝
	 *
	 * @param source
	 * @param target
	 */
	public static void copyDirectiory(String source, String target) {
		String file1 = target;
		String file2 = source;
		try {
			(new File(file1)).mkdirs();
			File[] file = (new File(file2)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					FileInputStream input = new FileInputStream(file[i]);
					FileOutputStream output = new FileOutputStream(file1 + "/"
							+ file[i].getName());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (file[i].isDirectory()) {
					copyDirectiory(file2 + "/" + file[i].getName(), file1 + "/"
							+ file[i].getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Object> fileWithPath = new ArrayList<Object>();

	private void getFileWithPath(String target) {
		String file2 = target;
		try {
			File[] file = (new File(file2)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					fileWithPath
							.add(file2 + File.separator + file[i].getName());
				}
				if (file[i].isDirectory()) {
					this.getAllFileWithPath(file2 + File.separator
							+ file[i].getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件路径取得该文件路径下的所有文件
	 *
	 * @param target
	 *            文件路径
	 * @return List 返回的文件路径list
	 */
	public List<Object> getAllFileWithPath(String target) {
		this.getFileWithPath(target);
		return this.fileWithPath;
	}

	/**
	 * 删除文件夹，如果该文件夹下有子文件或者文件夹，则全部删除
	 *
	 * @param path
	 *            要删除的文件夹
	 * @return boolean
	 */
	public static boolean delFoldsWithChilds(String path) {
		int files = 0;
		File file = new File(path);
		File[] tmp = file.listFiles();
		if (tmp == null) {
			files = 0;
		} else {
			files = tmp.length;
		}
		for (int i = 0; i < files; i++) {
			FileOperator.delFoldsWithChilds(tmp[i].getAbsolutePath());
		}
		boolean ret = FileOperator.deleteFolder(path);
		return ret;
	}

	/**
	 * 判断文件或者文件夹是否存在
	 *
	 * @param folderName
	 *            文件或者文件夹的绝对路径
	 * @return boolean
	 */
	public static boolean isFileExist(String folderName) {
		File file = new File(folderName);
		boolean returnBoolean = file.exists();
		return returnBoolean;
	}

	/**
	 * 这里的路径格式必须是：c:\tmp\tmp\ 或者是c:\tmp\tmp
	 *
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolders(String path) {
		return (new File(path)).mkdirs();
	}

	/**
	 * 取得文件名称（带后缀）
	 *
	 * @param filePath
	 *            文件路径（包括文件名称）
	 * @return String 文件名称
	 */
	public static String getFileName(String filePath) {
		String fileName = "";
		String tmpFilePath = filePath;
		int winIndex = tmpFilePath.lastIndexOf("\\");
		int linuxIndex = tmpFilePath.lastIndexOf("/");
		if (winIndex != -1)
			fileName = tmpFilePath.substring(winIndex + 1).trim();
		if (linuxIndex != -1)
			fileName = tmpFilePath.substring(linuxIndex + 1).trim();
		return fileName;
	}

	/**
	 * 得到文件的后缀
	 *
	 * @param fileName
	 *            文件名称
	 * @return String 后缀名称
	 */
	public static String getSuffix(String fileName) {
		String returnSuffix = "";
		String tmp = "";
		try {
			int index = fileName.lastIndexOf(".");
			if (index == -1) {
				tmp = "";
			} else
				tmp = fileName.substring(index + 1, fileName.length());
		} catch (Exception e) {
			tmp = "";
		}
		returnSuffix = tmp;
		return returnSuffix;
	}

	/**
	 * 递归创建文件
	 *
	 * @param path
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	private static boolean createFolds(String path) {
		boolean ret = false;
		String child = path;

		if (!FileOperator.isFileExist(path)) {
			int i = path.lastIndexOf(File.separator);
			String pathTmp = path.substring(0, i);
			child = pathTmp;
			FileOperator.createFolds(pathTmp);
			ret = FileOperator.createFolder(child);
		} else {
			return true;
		}
		return ret;
	}

	/**
	 * 将文件的路径格式转换为标准的文件路径格式
	 *
	 * @param inputPath
	 *            原文件路径
	 * @return String 转换后的文件路径
	 */
	public static String toStanderds(String inputPath) {
		String rtp = "";
		/**
		 * 这是使用正则表达式进行替换 先把所有的路径格式替换为linux下的，会出现多个连接的情况
		 */
		String pathChar = "/";
		String pathCharLin = "/";
		String pathCharWin = "\\";
		// char[] mychar = path.toCharArray();
		if (pathCharLin.equalsIgnoreCase(File.separator)) {
			pathChar = "/";
		}
		if (pathCharWin.equalsIgnoreCase(File.separator)) {
			pathChar = "\\\\";
		}
		rtp = FileOperator.replaceString("\\\\+|/+", inputPath, "/");
		rtp = FileOperator.replaceString("/+", rtp, pathChar);
		/**
		 * 这是使用正常的循环进行替换
		 */
		/***********************************************************************
		 * / String path = inputPath; char pathChar = '/'; char pathCharLin =
		 * '/'; char pathCharWin = '\\'; char[] mychar = path.toCharArray();
		 * if(String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharWin; }
		 * if(String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharLin; } for(int i = 0;i <mychar.length;i++) {
		 * if(mychar[i] == pathCharWin || mychar[i] == pathCharLin) { mychar[i]
		 * = pathChar; } if(mychar[i] != pathCharLin && mychar[i] !=
		 * pathCharWin) rtp += String.valueOf(mychar[i]); if(i <mychar.length-1)
		 * { if(mychar[i] == pathChar && mychar[i+1] != pathChar && mychar[i+1]
		 * != pathCharWin && mychar[i+1] != pathCharLin) { rtp +=
		 * String.valueOf(mychar[i]); } } } /
		 **********************************************************************/
		return rtp;
	}

	/**
	 * 将路径转换为linux路径－也可使用为将http的相对路径进行转换
	 *
	 * @param inputPath
	 * @return String
	 */
	public static String toLinuxStanderds(String inputPath) {
		String rtp = "";
		/**
		 * 这是使用正则表达式进行替换
		 */
		rtp = FileOperator.replaceString("\\\\+|/+", inputPath, "/");
		rtp = FileOperator.replaceString("/+", rtp, "/");
		/**
		 * 这是使用正常的循环进行替换
		 */
		/***********************************************************************
		 * / String path = inputPath; char pathChar = '/'; char pathCharLin =
		 * '/'; char pathCharWin = '\\'; char[] mychar = path.toCharArray();
		 * if(String.valueOf((pathCharWin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharWin; }
		 * if(String.valueOf((pathCharLin)).equalsIgnoreCase(File.separator)) {
		 * pathChar = pathCharLin; } pathChar = '/'; for(int i = 0;i
		 * <mychar.length;i++) { if(mychar[i] == pathCharWin || mychar[i] ==
		 * pathCharLin) { mychar[i] = pathChar; } if(mychar[i] != pathCharLin &&
		 * mychar[i] != pathCharWin) rtp += String.valueOf(mychar[i]); if(i
		 * <mychar.length-1) { if(mychar[i] == pathChar && mychar[i+1] !=
		 * pathChar && mychar[i+1] != pathCharWin && mychar[i+1] != pathCharLin)
		 * { rtp += String.valueOf(mychar[i]); } } } /
		 **********************************************************************/
		return rtp;
	}

	/**
	 * 在已经存在的路径下创建文件夹
	 *
	 * @param path
	 * @return boolean
	 */
	public static boolean createFolder(String path/* ,String folderName */) {
		// String fPath = path + File.separator + folderName;
		File file = new File(path);
		boolean returnBoolean = file.mkdir();
		return returnBoolean;
	}

	/**
	 * 删除文件夹，当该文件夹下有文件或者文件夹的时候不能删除该文件夹
	 *
	 * @param path
	 * @return boolean
	 */
	public static boolean deleteFolder(String path/* ,String folderName */) {
		// String fPath = path + File.separator + folderName;
		File file = new File(path);
		boolean returnBoolean = file.delete();
		return returnBoolean;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		boolean returnBoolean = file.delete();
		return returnBoolean;
	}

	/**
	 * 创建文件或者文件夹
	 *
	 * @param path
	 * @param fileName
	 * @return boolean
	 */
	public static boolean createFile(String path, String fileName) {
		String fPath = path + File.separator + fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean createFile(String fileName) {
		String fPath = fileName;
		File file = new File(fPath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 替换函数
	 *
	 * @param pattern
	 *            正则表达式
	 * @param inputStr
	 *            要替换的字符串
	 * @param replaceStr
	 *            要被替换的字符串
	 * @return String 替换之后的结果
	 */
	public static String replaceString(String pattern, String inputStr,
			String replaceStr) {
		java.util.regex.Pattern p = null; // 正则表达式
		java.util.regex.Matcher m = null; // 操作的字符串
		String value = "";
		try {// ['%\"|\\\\]校验非法字符.'"|\正则表达式
				// ^[0-9]*[1-9][0-9]*$
				// "['%\"|\n\t\\\\]"
				// 校验是否全部是空格：[^ ]
			p = java.util.regex.Pattern.compile(pattern);
			m = p.matcher(inputStr);
			value = m.replaceAll(replaceStr);
			m = p.matcher(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void removeFile(String path) {
		removeFile(new File(path));
	}

	public static void removeFile(File path) {
		if (path.isDirectory()) {
			File[] child = path.listFiles();
			if (child != null && child.length != 0) {
				for (int i = 0; i < child.length; i++) {
					removeFile(child[i]);
					child[i].delete();
				}
			}
		}
		path.delete();
	}

	public static String getAllFile(String path, String picName,
			String relative_html_path) {

		File[] file = (new File(path)).listFiles();
		String fileName = "";
		for (int i = 0; i < file.length; i++) {
			fileName = ((File) file[i]).getName();
			if (fileName.indexOf(picName) > -1) {
				return relative_html_path + fileName;
			}

		}
		return "";
	}

	public static void decoderBase64File(String base64Code, String filePath, String fileName)
			throws Exception {

		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		FileOutputStream out = new FileOutputStream(filePath.concat(fileName));
		out.write(buffer);
		out.flush();
		out.close();
	}

	public static byte[] file2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] s) {
		// FileOperator.copyFile("c:/StaffInfo-20071106.csv",
		// "c:/login/StaffInfo-20071106.csv");
		// FileOperator.deleteFile("c:/link1.jpg");
		FileOperator a = new FileOperator();
		List<?> list = a.getAllFileWithPath("D:\\Helix Server\\Content");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("---:"
					+ FileOperator.getSuffix(FileOperator.toLinuxStanderds(list
							.get(i).toString()
							.replaceAll("D:\\\\Helix Server\\\\Content", ""))));
		}
	}

}