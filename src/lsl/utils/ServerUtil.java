package lsl.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ServerUtil {
	private static int num = 0;
	
	/**
	 *  响应客户端请求，将图片从服务器发送至客户端
	 * @param file
	 * @param filePaht
	 * @param socket
	 */

	public void StoC(File file, String filePaht, Socket socket) {
		
		try {
	
			// 创建数据输入流对象
			DataInputStream fis = new DataInputStream(
					new BufferedInputStream(new FileInputStream(filePaht)));
			// 创建数据输出流对象
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			// 将文件名和文件长度发给客户端
			dos.writeUTF(file.getName());
			dos.flush();
			dos.writeLong(file.length());
			dos.flush();
			
			int bufferSize = 8192;
			byte [] buf = new byte[bufferSize];
			// 将文件发送给客户端
			while (true) {
				int read = 0;
				if (fis != null) {
					read = fis.read(buf);
				}
				
				if (read == -1) {
					break;
				}
				
				dos.write(buf, 0, read);
			}
			dos.flush();
			fis.close();
			// 注意关闭socket连接。不然会导致客户端等待，直至超时。会导致数据不完整
			socket.close();
			System.out.println("数据传输完成");	
		} catch (Exception e) {
			System.out.println("Err:" + e.getMessage());
		}
	}

	/**
	 * 接收来自客户端的图片
	 * @param socket
	 */
	public void CtoS(Socket socket) {
		
		byte [] inputByte = null;
		int len = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try {
			try {
				// 图片保存路径
				File file = new File("H:\\Images\\j" + num + ".png");
				dis = new DataInputStream(socket.getInputStream());
				fos = new FileOutputStream(file);
				num++;
				
				inputByte = new byte[1024];

				
				System.out.println("开始接收数据。。。");
				while ((len = dis.read(inputByte, 0, inputByte.length)) > 0) {
					System.out.println(len);
					fos.write(inputByte, 0, len);
					fos.flush();
				}
				System.out.println("完成接收！");
			} finally {
				if (fos != null)
					fos.close();
				if (dis != null)
					dis.close();
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	}
}
