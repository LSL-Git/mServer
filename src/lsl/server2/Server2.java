package lsl.server2;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import lsl.base.Base64Image;
import lsl.utils.FileUtil;
import lsl.utils.ServerUtil;

public class Server2 {
	
	private final int port = 54321;
	private String filepath;
	private File file;
	void start() {
		Socket socket = null;
		try {
			// ����server socket����
			ServerSocket socket2 = new ServerSocket(port);
			while (true) {			
				
				initFile();
				
				socket = socket2.accept();
				System.out.println("����socket����");
			
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream(),"utf-8"));
				String str = br.readLine();
				System.out.println("�յ���Ϣ��" + str);
					
				if ("request_download".equals(str))
					new ServerUtil().StoC(file,filepath,socket);
				if ("request_commit".equals(str))
					new ServerUtil().CtoS(socket);
				
		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�ļ�·�������ļ���Ϣ
	 */
	public void initFile() {
		
		filepath = new FileUtil().getFilePath();
		file = new File(filepath);
		System.out.println("�������ļ���\n·����" + filepath);
		System.out.println("�ļ�����" + file.getName());
		System.out.println("�ļ����ȣ�" + file.length());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ��������
		new Server2().start();
	}

}
