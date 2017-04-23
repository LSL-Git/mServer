package lsl.utils;

import java.io.File;
import java.util.Random;

public class FileUtil {
	/**
	 * 随机获取要发送给客户端的图片路径
	 * @return
	 */
	public String getFilePath() {
		Random r = new Random();
		int i = r.nextInt(3);
		int j = r.nextInt(20);
		String filepath = "H:\\Images\\已完成\\";
		String file = "";
		switch (i) {
			case 0: file = "atm\\atm"; break;
			case 1: file = "餐厅\\餐厅"; break;
			case 2: file = "车站\\车站"; break;
		}
		return filepath + file + j +".jpg";
	}
	

	/**
	 * 根据路径，便利文件夹内的所用文件夹和文件
	 * @param path
	 */
	public void traverseFolder2(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }
}
