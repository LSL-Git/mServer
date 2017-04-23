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
	 *  ��Ӧ�ͻ������󣬽�ͼƬ�ӷ������������ͻ���
	 * @param file
	 * @param filePaht
	 * @param socket
	 */

	public void StoC(File file, String filePaht, Socket socket) {
		
		try {
	
			// ������������������
			DataInputStream fis = new DataInputStream(
					new BufferedInputStream(new FileInputStream(filePaht)));
			// �����������������
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			// ���ļ������ļ����ȷ����ͻ���
			dos.writeUTF(file.getName());
			dos.flush();
			dos.writeLong(file.length());
			dos.flush();
			
			int bufferSize = 8192;
			byte [] buf = new byte[bufferSize];
			// ���ļ����͸��ͻ���
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
			// ע��ر�socket���ӡ���Ȼ�ᵼ�¿ͻ��˵ȴ���ֱ����ʱ���ᵼ�����ݲ�����
			socket.close();
			System.out.println("���ݴ������");	
		} catch (Exception e) {
			System.out.println("Err:" + e.getMessage());
		}
	}

	/**
	 * �������Կͻ��˵�ͼƬ
	 * @param socket
	 */
	public void CtoS(Socket socket) {
		
		byte [] inputByte = null;
		int len = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try {
			try {
				// ͼƬ����·��
				File file = new File("H:\\Images\\j" + num + ".png");
				dis = new DataInputStream(socket.getInputStream());
				fos = new FileOutputStream(file);
				num++;
				
				inputByte = new byte[1024];

				
				System.out.println("��ʼ�������ݡ�����");
				while ((len = dis.read(inputByte, 0, inputByte.length)) > 0) {
					System.out.println(len);
					fos.write(inputByte, 0, len);
					fos.flush();
				}
				System.out.println("��ɽ��գ�");
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
