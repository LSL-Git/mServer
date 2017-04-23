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
 * �û��������ҵ��
 * @author M1308_000
 *
 */
public class UserTask {
	
	/**
	 *  ע�ᱣ���û���Ϣҵ��
	 * @param json
	 * @return
	 */
	public static String RegisterTask(JSONObject json) {
		
		String RESULT = "NO";
		try {
			// �û���
			String user_name = json.getString("name");
			// �û�����
			String user_psw = json.getString("psw");
			// �û��绰
			String user_tel = json.getString("tel");
			// �û�����
			String user_mail = json.getString("email");
			// ����base64���ܵ�ͼƬ��Ϣ
			String imgStr = json.getString("img");

			// ��ȡ����ʱ�䣬����ʽ��
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			String imgName = sdf.format(new Date());
			
			String Img_Name = user_name + imgName;
			// ����ͼƬ�����ֱ���
			Base64Image.GenerateImage(imgStr, "H:\\Images\\�û�ͷ��\\User\\"
					+ Img_Name + ".jpg");
			
			DbUtil.getInstance();
			// �������ݿⱣ����Ϣ
			RESULT = DbUtil.SaveUserData(user_name, user_psw, user_tel, user_mail, 0, Img_Name, "100%");
			
		} catch (Exception e) {
			System.err.println("RegisterTask����Ϣ�������->" + e.getMessage());
		}
		
		return RESULT;
	}

	/**
	 * �û���¼ҵ��
	 * @param json
	 * @return
	 */
	public static String LoginTask(JSONObject json) {
		
		String RESULT = "Login_Fail";
		try {
			// �����ͻ��˷��͵��û���������
			String name = json.getString("login_name");
			String psw = json.getString("login_psw");
			
			DbUtil.getInstance();
			// ��ȡ���ݿ��û���Ӧ����
			RESULT = DbUtil.Login(name);
			// ���������������ݿ����������ͬ
			if (RESULT.equals(psw))
				RESULT = "Login_Success";
			
		} catch (Exception e) {
			System.err.println("LoginTask: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * �û���Ϣ��ѯҵ��
	 * �����û�����ѯ�û���Ϣ�������ظ��ͻ���
	 * @param json
	 * @param dos
	 * @param socket
	 * @return
	 */
	public static String QueryTask(JSONObject json, DataOutputStream dos, Socket socket) {
		String RESULT = "Query_Fail";
		
		try {
			// ���������Ϣ
			String name = json.getString("query_name");
			
			DbUtil.getInstance();
			// ��ȡ�û���Ϣ
			JSONObject js = DbUtil.QueryUserAll(name);
			// ����Ϣ�������ͻ���
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
