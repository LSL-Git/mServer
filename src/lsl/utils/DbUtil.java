package lsl.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import lsl.base.Base64Image;
import lsl.values.Values;
/**
 * ���ݿ���ز���
 * @author M1308_000
 *
 */
public class DbUtil {
	
	private static DbUtil instance = null;
	private static Connection connection;
	private static Statement statement;
	
	// ����ģʽ
	public static DbUtil getInstance() {
		if(null == instance) {
			instance = new DbUtil();
		}
		return instance;
	}

	/**
	 * �������ݿ�
	 * ��Ҫ���� mysql-connector-java-5.1.26-bin.jar
	 */
	private DbUtil() {
		try {
			//ָ����������
			Class.forName(Values.NAME);
			// ��ȡ����
			connection = DriverManager.getConnection(Values.URL, Values.USER, Values.PASSWORD);
			
			if(!connection.isClosed()) {
				System.out.println("Db:���ݿ����ӳɹ�...");	
			}
			statement = (Statement)connection.createStatement();
			initDb(statement);
			
		} catch (Exception e) {
			System.err.println("DB������ʧ�� ��|" + e.getMessage());
		} 
	}

	
	/**
	 * �����ʼ��
	 */
	private void initDb(Statement statement) {
		
		try {
			// �����ѱ�ǩ�������Ϣ��
			if (!statement.execute(Values.CR_FINISH_TB)) {	
				System.out.println("�ѱ�ǩ�������Ϣ�����ɹ�");
			}
		} catch (Exception e) {}
		
		try {
			// �����û���Ϣ��
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("�û���Ϣ�����ɹ�");
			}
		} catch (Exception e) {}
		
		try {
			// ��������Ա��Ϣ��
			if (!statement.execute(Values.CR_MANAGER_TB)) {	
				System.out.println("����Ա��Ϣ�����ɹ�");
			}
		} catch (Exception e) {}
		
	}

	
	/**
	 * ����ͻ���Ϣ
	 * @param name �û���
	 * @param psw  ����
	 * @param tel  �绰����
	 * @param email ����
	 * @param integral	����
	 * @param icon_path	ͷ�񱣴�·��
	 * @param task	����������
	 * @return
	 */
	public static String SaveUserData(String name, String psw, String tel, String email, 
			int integral, String icon_name, String task) {
		
		String RESULT = "Register_Fail";
		// �����û���Ϣ��SQL���
		String SQL_Insert = "insert into " + Values.USER_TB + " values (0,'" + name + "','" + psw + "','" + tel 
		+ "','" + email + "'," + integral + ",'" + icon_name + "','" + task + "')";
		// ��ѯ�û��Ƿ����
		String SQL_Query_Uname = "select " + Values.USER_NAME + " from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		
		try {
			// �������ݿ��Ƿ���ڴ��û���
			ResultSet qname = statement.executeQuery(SQL_Query_Uname);
			String rename = "";
			
			while (qname.next()) {
				rename = qname.getString(1);
			}
			
			if (rename.equals(name)) {
				RESULT = "User_Exist";
			} else {
				// �����û���Ϣ
				int row = statement.executeUpdate(SQL_Insert);
				System.out.println("�Ѹ��£�" + row + "��");
				RESULT = "Register_Success";
			}

			qname.close();
//			statement.close();
//			connection.close();
			
		} catch (Exception e) {
			System.err.println("SaveUserData: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * ���û�����ѯ�û����롣������
	 * @param name
	 * @return
	 */
	public static String Login(String name) {
		String RESULT = "User_Not_Exist";
		// ��ѯ�����SQL���
		String SQL_Query = "select " + Values.USER_PSW + " from " + 
		Values.USER_TB +" where " + Values.USER_NAME + " = '" + name +"'";
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query);
			while (ResultStr.next()) {
				// ��ȡ�û�
				RESULT = ResultStr.getString(1);
			}
			
			ResultStr.close();
//			statement.close();
//			connection.close();
			
		} catch (SQLException e) {
			System.err.println("Login: Err->" + e.getMessage());
		}
		
		return RESULT;
	}
	
	/**
	 * ͨ���û�����ѯ�û�������Ϣ,����json�����û�����Ա������
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// ����ͷ��ͼƬ·��
		String ImgPath = "H:\\Images\\�û�ͷ��\\User\\"; 
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = null;
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query_All);
			while (ResultStr.next()) {
				map.put("user_name", ResultStr.getString(2));
				map.put("user_psw", ResultStr.getString(3));				
				map.put("user_tel", ResultStr.getString(4));
				map.put("user_mail", ResultStr.getString(5));
				map.put("user_integral", ResultStr.getString(6));
				// ��ȡͷ���ļ�
				ImgPath += ResultStr.getString(7) + ".jpg";
				// ����ͷ���ļ�
				String Img = Base64Image.getImageStr(ImgPath);
				map.put("user_icon", Img);
				
				map.put("task_completion", ResultStr.getString(8));
				
				// ���û���Ϣ��װ��json������
				json = new JSONObject(map);
				
				ResultStr.close();
				
			}
		} catch (Exception e) {
			System.err.println("QueryUserAll: Err->" + e.getMessage());
		}
		// ����json����
		return json;
	}

	
	
	
}
