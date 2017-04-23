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
 * 接收来自客户端的图片
 * @author M1308_000
 *
 */
public class Server {

	private final int port = 54321;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("服务器启动。。。");
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
			System.out.println("服务器异常" + e.getMessage());
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
				// 读取客户端数据
				System.out.println("客户端数据已连接");
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
				
				// 处理客户端数据
				// 将socket接收的数据还原JSONObject
				JSONObject json = new JSONObject(strinputstream);
				// 从客户端获取操作类型
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
				// op = 1 表示收到的客户端信息为注册信息， 
				// op = 2 表示收到的客户端的数据为检索信息
				// op = 3 表示查询用户所有信息。 
				
				// 当用户进行注册操作时		
				case 1: // 待返回值
						String isSuccess = UserTask.RegisterTask(json);
						
						if(isSuccess.equals("Register_Success")) {
							System.out.println("注册完成！");
						} else {
							System.out.println("注册失败！");
						}				
						// 返回客户端注册结果信息
						BackToUser(isSuccess, dos, json, socket);
						System.out.println("注册结果已反馈用户");
						break;
						
				// 当执行登录操作时
				case 2:
						String isLgSuccess = "Login_Fail";
						isLgSuccess = UserTask.LoginTask(json);
						if(isLgSuccess.equals("Login_Success"))
							System.out.println("登录成功");
						// 返回客户端登录结果信息
						BackToUser(isLgSuccess, dos, json, socket);						
						break;
						
				// 当执行查询操作时	
				case 3:
						if(UserTask.QueryTask(json, dos, socket).equals("Query_Success"))
							System.out.println("查询成功");
						else
							System.out.println("查询失败");						
						break;
				}
				
			}catch (Exception e) {
				System.out.println("服务器运行异常:" + e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("服务器finally异常" + e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * 用户请求后，将相关信息返回客户端
	 * @param BackStr
	 * @param dos
	 * @param json
	 * @param socket
	 */
	private void BackToUser(String BackStr, DataOutputStream dos, JSONObject json, Socket socket) {
		
		try {// 向客户端回复信息  json对象
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
