package diary;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class getDiaryByUserIDandDate
 */
@WebServlet("/diary/getDiaryByUserIDandDate")
public class getDiaryByUserIDandDate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;       
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getDiaryByUserIDandDate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致

		String user_id=request.getParameter("user_id"); 
		String this_date=request.getParameter("this_date"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
        //JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT * FROM diary WHERE user_id = ? and  date=?; ";
	   
	    try
	    {
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, user_id);
			prestmt.setString(2, this_date);
			rs = prestmt.executeQuery();
			if(rs.next())
			{
				jsonobj.put("diarystate", 1);  
		    	jsonobj.put("picture", rs.getString("picture"));  
		    	jsonobj.put("content", rs.getString("content"));  
				
				//jsonarray.put(jsonobj); 
			}
			else {
				jsonobj.put("diarystate", 0);  
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
