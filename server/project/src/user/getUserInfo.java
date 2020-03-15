package user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class getUserInfo
 */
@WebServlet("/user/getUserInfo")
public class getUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致

		String id=request.getParameter("id"); 
		//System.out.println(id);
		
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
        //JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT * FROM `user` WHERE id=? ; ";
	   
	    try
	    {	    	
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, id);
			rs = prestmt.executeQuery();
			if(rs.next())
			{
				if(rs.getString("birthday")!=null)
					jsonobj.put("birthday", rs.getString("birthday"));  
				else
					jsonobj.put("birthday", "");  
		    	jsonobj.put("phone", rs.getString("phone"));  
		    	
				//jsonarray.put(jsonobj); 
			}
			
	      }
	      catch(Exception ex)
	      {
	        ex.printStackTrace();
	      }
	      finally
	      {
	        //out.print(jsonarray);
		    out.print(jsonobj);
        	if(rs!=null)
        		DBUtil.close(rs);
        	if(prestmt!=null)
        		DBUtil.close(prestmt);
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
