package lsl.values;

public class Values {
	public static final String URL = "jdbc:mysql://127.0.0.1/resource_management_system?characterEncoding=utf8";
	public static final String NAME = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "123";
	
	// *******************用户信息*************************
	public static final String CR_USER_TB = "create table user_tb(user_id int auto_increment primary key," +
																"user_name varchar(20) not null," +
																"user_psw varchar(10) not null," +
																"user_tel varchar(12) not null," +
																"user_mail varchar(20)," +
																"user_integral int," +
																"icon_name varchar(40)," +
																"task_completion varchar(4)," +
																"unique(user_name))";
	public static final String USER_TB = "user_tb";
	public static final String USER_NAME = "user_name";
	public static final String USER_PSW = "user_psw";
	public static final String USER_TEL = "user_tel";
	public static final String USER_EMIL = "user_mail";
	// *******************用户信息*************************
	
	// 管理员信息
	public static final String CR_MANAGER_TB = "create table manager_tb(manager_id int auto_increment primary key," +
																"manager_name varchar(10) not null," +
																"manager_psw varchar(10) not null," +
																"manager_tel int not null," +
																"manager_mail text," +
																"icon_name varchar(40))";
	// 已标签化完成
	public static final String CR_FINISH_TB = "create table finish_tb(id int auto_increment primary key," +
																"img_name varchar(10) not null," +
																"img_lab_name varchar(10) not null," +
																"img_filepath varchar(30) not null," +
																"finish_time varchar(10))";
	
}
