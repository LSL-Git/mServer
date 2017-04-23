package lsl.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Test {

	private static DataOutputStream dos;
	private static FileInputStream fis;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("ÊäÈë1£¬»ò0");
		int i = scan.nextInt();
		int len = 0;
		if (i == 1) {
			Socket socket = null;
			try {
				try {
					socket = new Socket("192.168.1.101",54321);
					dos = new DataOutputStream(socket.getOutputStream());
					File file = new File("H:\\icon.png");
					fis = new FileInputStream(file);
					byte[] sendBytes = new byte[1024];
					while ((len = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
						dos.write(sendBytes, 0, len);
						dos.flush();
					}
				} finally {
					if (dos != null)
						dos.close();
					if (fis != null)
						fis.close();
					if (socket != null)
						socket.close();
				}
				
//				
//				Image[] img_arr = new Image[40];
//				BufferedImage image = ImageIO.read(file);
//				img_arr[0] = image;
//				
//				
//				ImageIO.write(image, "png", new File("H:\\Images\\i.png"));
//				//dos.write(d);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
