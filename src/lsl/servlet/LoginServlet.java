package lsl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json;charset=UTF-8");
		String reqMessage, respMessage;
		
		JSONArray reqObject = null;
		JSONArray respObject = null;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			br.close();
			
			reqMessage = sb.toString();
			System.out.println("请求报文：" + reqMessage);
			
			reqObject = new JSONArray(reqMessage);
			
			respObject = new JSONArray().put(new JSONObject().put("user_name", "lsl"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			respMessage = respObject == null ? "" : respObject.toString();
			System.out.println("返回报文：" + respMessage);
			PrintWriter pw = resp.getWriter();
			pw.write(respMessage);
			pw.flush();
			pw.close();
		}
	}
	
	

}
