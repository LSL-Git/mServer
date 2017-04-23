package lsl.utils;

import java.io.File;
import java.util.Random;

public class FileUtil {
	/**
	 * �����ȡҪ���͸��ͻ��˵�ͼƬ·��
	 * @return
	 */
	public String getFilePath() {
		Random r = new Random();
		int i = r.nextInt(3);
		int j = r.nextInt(20);
		String filepath = "H:\\Images\\�����\\";
		String file = "";
		switch (i) {
			case 0: file = "atm\\atm"; break;
			case 1: file = "����\\����"; break;
			case 2: file = "��վ\\��վ"; break;
		}
		return filepath + file + j +".jpg";
	}
	

	/**
	 * ����·���������ļ����ڵ������ļ��к��ļ�
	 * @param path
	 */
	public void traverseFolder2(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("�ļ����ǿյ�!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("�ļ���:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        System.out.println("�ļ�:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("�ļ�������!");
        }
    }
}
