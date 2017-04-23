package lsl.test;

import java.net.Socket;
import java.util.*;
import java.util.Random;
import org.json.*;
import java.io.*;

import lsl.base.Base64Image;
import lsl.utils.DbUtil;
import lsl.utils.FileUtil;

public class test2 {

	public static final String ip = "192.168.1.100";
	public static final int port = 54321;
	
	public static String register(String name, String img_path, int psw) {
		// 将图片的信息转化为base64编码	
		String imgStr = Base64Image.getImageStr(img_path);
		String isRegSuccess = "";
		while (true){
			Socket socket = null;
			try {
				//创建一个流套接字并将其连接到指定主机上的指定端口号 
				socket = new Socket(ip, port);
				System.out.println("连接已经建立");
				// 向服务器端发送数据 
				Map<String, String> map = new HashMap<String,String>();
				// 类型
//				map.put("type", "login");
//				map.put("login_name",name);
//				map.put("login_psw",psw + "");
				
				map.put("type", "register");
				map.put("name", name);
				map.put("tel","18877645454");
				map.put("email", "45745@adj.com");
				map.put("img",imgStr);
				map.put("psw", psw + "");
				
//				map.put("type", "query");
//				map.put("query_name", name);
				
				
				// 将json转化为String类型    

				JSONObject json = new JSONObject(map);
				String jsonStr = "";
				jsonStr = json.toString();
				
				// 将String转化为byte[]  
				byte[] jsonByte = jsonStr.getBytes();
				DataOutputStream ops = null;
				ops = new DataOutputStream(socket.getOutputStream());
				System.out.println("发送的数据长度为：" + jsonByte.length);
				ops.write(jsonByte);
				ops.flush();
				System.out.println("传输数据完毕");
				
				socket.shutdownOutput();
				
				
				// 读取服务器端数据    
				DataInputStream input = null;
				String strinput = "";
				input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				strinput = input.readUTF();
				System.out.println("返回信息为："+ strinput);
				JSONObject js = new JSONObject(strinput);
				System.out.println(js.getString("Back_Msg"));
				isRegSuccess = js.getString("Back_Msg");
				
				// 如接收到 "OK" 则断开连接  


				if (js != null) {
					System.out.println("客户端将关闭连接");
					Thread.sleep(500);
					break;
				}
				
			} catch (Exception e) {
				System.out.println("conn time out  Err->" + e.getMessage());
				break;
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						socket = null;
						System.out.println("客户端 finally 异常：" + e.getMessage());
						
					}
				}
			}
		}
		return isRegSuccess;
	}
	
	
	public static void main(String [] args) {
		DbUtil.getInstance();
		//DbUtil.SaveUserData("lsl", "123", "18877543725", "1748359995@qq.com", 100, "H:\\Images\\icon.png", "100%");
		//String r = DbUtil.Login("ls");
		//System.out.println(r);
		
//		new DbUtil(); 
		//new FileUtil().traverseFolder2("H:\\Images\\"); // 娓氬灝鍩勯弬鍥︽婢剁懓鍞寸�锟�		
		register("lsl", "H:/Images/icon.png", 123);
		
		//DbUtil.QueryUserAll("lsl");
		

	}
}
