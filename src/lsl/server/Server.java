package lsl.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lsl.Task.UserTask;
import lsl.base.Base64Image;

import org.json.JSONObject;

/**
 * �������Կͻ��˵�ͼƬ
 * @author M1308_000
 *
 */
public class Server {

	private final int port = 54321;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("����������������");
		Server server = new Server();
		server.init();
	}
	
	public void init() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket client = serverSocket.accept();
				
				new HandlerThread(client);
			}
		} catch (Exception e) {
			System.out.println("�������쳣" + e.getMessage());
		}
	}


	private class HandlerThread implements Runnable {
		
		private Socket socket;
		public HandlerThread(Socket socket) {
			this.socket = socket;
			new Thread(this).start();
		}

		@Override
		public void run() {
			
			try {
				// ��ȡ�ͻ�������
				System.out.println("�ͻ�������������");
				DataInputStream dis = null;
				DataOutputStream dos = null;
				String strinputstream = "";
				dis = new DataInputStream(socket.getInputStream());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] b = new byte[2 * 1024];
				int n;
				while ((n = dis.read(b)) != -1) {
					baos.write(b, 0, n);
				}
				strinputstream = new String(baos.toByteArray());
				
				//socket.shutdownOutput();
				baos.close();
				
				// ����ͻ�������
				// ��socket���յ����ݻ�ԭJSONObject
				JSONObject json = new JSONObject(strinputstream);
				// �ӿͻ��˻�ȡ��������
				String type = json.getString("type");
				System.out.println("type:" + type + "   msg:" + json);
				
				int OP = 0;
				if (type.equals("register"))
					OP = 1;
				else if(type.equals("login"))
					OP = 2;
				else if(type.equals("query"))
					OP = 3;
				
				switch (OP) {
				// op = 1 ��ʾ�յ��Ŀͻ�����ϢΪע����Ϣ�� 
				// op = 2 ��ʾ�յ��Ŀͻ��˵�����Ϊ������Ϣ
				// op = 3 ��ʾ��ѯ�û�������Ϣ�� 
				
				// ���û�����ע�����ʱ		
				case 1: // ������ֵ
						String isSuccess = UserTask.RegisterTask(json);
						
						if(isSuccess.equals("Register_Success")) {
							System.out.println("ע����ɣ�");
						} else {
							System.out.println("ע��ʧ�ܣ�");
						}				
						// ���ؿͻ���ע������Ϣ
						BackToUser(isSuccess, dos, json, socket);
						System.out.println("ע�����ѷ����û�");
						break;
						
				// ��ִ�е�¼����ʱ
				case 2:
						String isLgSuccess = "Login_Fail";
						isLgSuccess = UserTask.LoginTask(json);
						if(isLgSuccess.equals("Login_Success"))
							System.out.println("��¼�ɹ�");
						// ���ؿͻ��˵�¼�����Ϣ
						BackToUser(isLgSuccess, dos, json, socket);						
						break;
						
				// ��ִ�в�ѯ����ʱ	
				case 3:
						if(UserTask.QueryTask(json, dos, socket).equals("Query_Success"))
							System.out.println("��ѯ�ɹ�");
						else
							System.out.println("��ѯʧ��");						
						break;
				}
				
			}catch (Exception e) {
				System.out.println("�����������쳣:" + e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("������finally�쳣" + e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * �û�����󣬽������Ϣ���ؿͻ���
	 * @param BackStr
	 * @param dos
	 * @param json
	 * @param socket
	 */
	private void BackToUser(String BackStr, DataOutputStream dos, JSONObject json, Socket socket) {
		
		try {// ��ͻ��˻ظ���Ϣ  json����
			Map<String, String> map = new HashMap<String, String>();
			map.put("Back_Msg", BackStr);
			json = new JSONObject(map);
			String jsonStr = json.toString();
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			dos.writeUTF(jsonStr);
			dos.flush();
			dos.close();
		} catch (Exception e) {
			System.err.println("BackToUser: Err->" + e.getMessage());
		}
		
	}
	
	
}
