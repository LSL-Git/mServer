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
 * 数据库相关操作
 * @author M1308_000
 *
 */
public class DbUtil {
	
	private static DbUtil instance = null;
	private static Connection connection;
	private static Statement statement;
	
	// 单例模式
	public static DbUtil getInstance() {
		if(null == instance) {
			instance = new DbUtil();
		}
		return instance;
	}

	/**
	 * 链接数据库
	 * 需要配置 mysql-connector-java-5.1.26-bin.jar
	 */
	private DbUtil() {
		try {
			//指定链接类型
			Class.forName(Values.NAME);
			// 获取连接
			connection = DriverManager.getConnection(Values.URL, Values.USER, Values.PASSWORD);
			
			if(!connection.isClosed()) {
				System.out.println("Db:数据库链接成功...");	
			}
			statement = (Statement)connection.createStatement();
			initDb(statement);
			
		} catch (Exception e) {
			System.err.println("DB：链接失败 ！|" + e.getMessage());
		} 
	}

	
	/**
	 * 建表初始化
	 */
	private void initDb(Statement statement) {
		
		try {
			// 创建已标签话完成信息表
			if (!statement.execute(Values.CR_FINISH_TB)) {	
				System.out.println("已标签话完成信息表创建成功");
			}
		} catch (Exception e) {}
		
		try {
			// 创建用户信息表
			if (!statement.execute(Values.CR_USER_TB)) { 
				System.out.println("用户信息表创建成功");
			}
		} catch (Exception e) {}
		
		try {
			// 创建管理员信息表
			if (!statement.execute(Values.CR_MANAGER_TB)) {	
				System.out.println("管理员信息表创建成功");
			}
		} catch (Exception e) {}
		
	}

	
	/**
	 * 保存客户信息
	 * @param name 用户名
	 * @param psw  密码
	 * @param tel  电话号码
	 * @param email 邮箱
	 * @param integral	积分
	 * @param icon_path	头像保存路径
	 * @param task	任务完成情况
	 * @return
	 */
	public static String SaveUserData(String name, String psw, String tel, String email, 
			int integral, String icon_name, String task) {
		
		String RESULT = "Register_Fail";
		// 插入用户信息的SQL语句
		String SQL_Insert = "insert into " + Values.USER_TB + " values (0,'" + name + "','" + psw + "','" + tel 
		+ "','" + email + "'," + integral + ",'" + icon_name + "','" + task + "')";
		// 查询用户是否存在
		String SQL_Query_Uname = "select " + Values.USER_NAME + " from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		
		try {
			// 检索数据库是否存在此用户名
			ResultSet qname = statement.executeQuery(SQL_Query_Uname);
			String rename = "";
			
			while (qname.next()) {
				rename = qname.getString(1);
			}
			
			if (rename.equals(name)) {
				RESULT = "User_Exist";
			} else {
				// 保存用户信息
				int row = statement.executeUpdate(SQL_Insert);
				System.out.println("已更新：" + row + "条");
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
	 * 以用户名查询用户密码。并返回
	 * @param name
	 * @return
	 */
	public static String Login(String name) {
		String RESULT = "User_Not_Exist";
		// 查询密码的SQL语句
		String SQL_Query = "select " + Values.USER_PSW + " from " + 
		Values.USER_TB +" where " + Values.USER_NAME + " = '" + name +"'";
		
		try {
			ResultSet ResultStr = statement.executeQuery(SQL_Query);
			while (ResultStr.next()) {
				// 获取用户
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
	 * 通过用户名查询用户所有信息,返回json对象，用户管理员都可用
	 * @param name
	 * @return
	 */
	public static JSONObject QueryUserAll(String name) {
		
		String SQL_Query_All = "select * from " + Values.USER_TB + " where " + Values.USER_NAME + " = '" + name + "'";
		// 本地头像图片路径
		String ImgPath = "H:\\Images\\用户头像\\User\\"; 
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
				// 获取头像文件
				ImgPath += ResultStr.getString(7) + ".jpg";
				// 加密头像文件
				String Img = Base64Image.getImageStr(ImgPath);
				map.put("user_icon", Img);
				
				map.put("task_completion", ResultStr.getString(8));
				
				// 将用户信息封装到json对象中
				json = new JSONObject(map);
				
				ResultStr.close();
				
			}
		} catch (Exception e) {
			System.err.println("QueryUserAll: Err->" + e.getMessage());
		}
		// 返回json对象
		return json;
	}

	
	
	
}
