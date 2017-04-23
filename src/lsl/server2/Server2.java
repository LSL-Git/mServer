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
			// 创建server socket对象
			ServerSocket socket2 = new ServerSocket(port);
			while (true) {			
				
				initFile();
				
				socket = socket2.accept();
				System.out.println("建立socket连接");
			
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream(),"utf-8"));
				String str = br.readLine();
				System.out.println("收到信息：" + str);
					
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
	 * 获取文件路径，和文件信息
	 */
	public void initFile() {
		
		filepath = new FileUtil().getFilePath();
		file = new File(filepath);
		System.out.println("待发送文件：\n路径：" + filepath);
		System.out.println("文件名：" + file.getName());
		System.out.println("文件长度：" + file.length());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 启动服务
		new Server2().start();
	}

}
