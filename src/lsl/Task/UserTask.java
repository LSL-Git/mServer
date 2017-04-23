package lsl.Task;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import lsl.base.Base64Image;
import lsl.utils.DbUtil;

import org.json.JSONObject;

/**
 * 用户操纵相关业务
 * @author M1308_000
 *
 */
public class UserTask {
	
	/**
	 *  注册保存用户信息业务
	 * @param json
	 * @return
	 */
	public static String RegisterTask(JSONObject json) {
		
		String RESULT = "NO";
		try {
			// 用户名
			String user_name = json.getString("name");
			// 用户密码
			String user_psw = json.getString("psw");
			// 用户电话
			String user_tel = json.getString("tel");
			// 用户邮箱
			String user_mail = json.getString("email");
			// 经过base64加密的图片信息
			String imgStr = json.getString("img");

			// 或取当地时间，并格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			String imgName = sdf.format(new Date());
			
			String Img_Name = user_name + imgName;
			// 解密图片并保持本地
			Base64Image.GenerateImage(imgStr, "H:\\Images\\用户头像\\User\\"
					+ Img_Name + ".jpg");
			
			DbUtil.getInstance();
			// 操作数据库保存信息
			RESULT = DbUtil.SaveUserData(user_name, user_psw, user_tel, user_mail, 0, Img_Name, "100%");
			
		} catch (Exception e) {
			System.err.println("RegisterTask：信息保存出错->" + e.getMessage());
		}
		
		return RESULT;
	}

	/**
	 * 用户登录业务
	 * @param json
	 * @return
	 */
	public static String LoginTask(JSONObject json) {
		
		String RESULT = "Login_Fail";
		try {
			// 解析客户端发送的用户名和密码
			String name = json.getString("login_name");
			String psw = json.getString("login_psw");
			
			DbUtil.getInstance();
			// 获取数据库用户对应密码
			RESULT = DbUtil.Login(name);
			// 如果请求密码和数据库检索密码相同
			if (RESULT.equals(psw))
				RESULT = "Login_Success";
			
		} catch (Exception e) {
			System.err.println("LoginTask: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * 用户信息查询业务
	 * 跟据用户名查询用户信息，并返回给客户端
	 * @param json
	 * @param dos
	 * @param socket
	 * @return
	 */
	public static String QueryTask(JSONObject json, DataOutputStream dos, Socket socket) {
		String RESULT = "Query_Fail";
		
		try {
			// 解读请求信息
			String name = json.getString("query_name");
			
			DbUtil.getInstance();
			// 获取用户信息
			JSONObject js = DbUtil.QueryUserAll(name);
			// 将信息反馈给客户端
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			dos.writeUTF(js.toString());
			RESULT = "Query_Success";
			dos.flush();
			dos.close();
			
		} catch (Exception e) {
			System.err.println("QueryTask: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	
	
	
}
