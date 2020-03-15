package user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class saveUser
 */
@WebServlet("/user/saveUser")
public class saveUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private PreparedStatement prestmt1 = null;
	private ResultSet rs = null;
	private String sql1 = null;
	private String sql2 = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public saveUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致
		String code=request.getParameter("code"); 
		//String username=request.getParameter("username"); 
	    JSONObject jsonobj = new JSONObject(); 

	    //开始获取openid
	    JSONObject jsonObject = null;
    	OutputStreamWriter urlout = null;
        StringBuffer buffer = new StringBuffer();

        try {
	    //1.连接部分
            URL url = new URL("https://api.weixin.qq.com/sns/jscode2session");
            // http协议传输
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  		
	    //2.传入参数部分
            // 得到请求的输出流对象  
            urlout = new OutputStreamWriter(httpUrlConn.getOutputStream(),"UTF-8");  
            // 把数据写入请求的Body
            urlout.write("appid=wx362a2203222dc677&secret=6e671520a379fff31c83c1b1f3393510&js_code="+code+"&grant_type=authorization_code"); 
            //参数形式跟在地址栏的一样
            urlout.flush();  
            urlout.close(); 
	    //3.获取数据
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return jsonObject.toString();
        //JSONObject jsonObj = new JSONObject(jsonStr);
        //获取用户的唯一标识
	 	String openid = (String) jsonObject.get("openid");
	 	//获取会话秘钥
	 	String session_key = (String) jsonObject.get("session_key");
        //int errcode = jsonObject.getInt("errcode");
        //String request = (String) jsonObj.get("request");
		jsonobj.put("openid", openid);  
		jsonobj.put("session_key", session_key);  
	    
	    
//开始查一下openid有没有注册过了
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    //JSONArray jsonarray = new JSONArray();  
	    con=DBUtil.CreateConn();
		//sql="insert into user (id,username,created_at) values (?,?,now());";
		sql1="select count(*) from `user` where id=? ;";
		sql2="insert into `user` (id,created_at) values (?,now());";

	      try
	        {
	    	  	prestmt = con.prepareStatement(sql1);
				prestmt.setString(1, openid);
				rs = prestmt.executeQuery();
				if(rs.next())
				{
					//没有的话给他注册！
					if(rs.getInt(1)==0) {
						prestmt1 = con.prepareStatement(sql2);
			        	prestmt1.setString(1, openid);
			        	//prestmt.setString(2, username);
			        	prestmt1.execute();
						//jsonobj.put("birthday", rs.getString("birthday"));  
					}								    	
				}        	
				//out.print("ok");
	        }
	        catch(Exception ex)
	        {
	        	//out.print("no");
	        	ex.printStackTrace();
	        }
	        finally
	        {   
	        	out.print(jsonobj);
	        	if(prestmt!=null)
	        		DBUtil.close(prestmt);
	        	if(prestmt1!=null)
	        		DBUtil.close(prestmt1);
	        	if(rs!=null)
	        		DBUtil.close(rs);
	        	DBUtil.close(con);
	        }	  	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
