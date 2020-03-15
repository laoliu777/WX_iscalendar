package checkin;

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
 * Servlet implementation class getMonthCheckin
 */
@WebServlet("/checkin/getMonthCheckin")
public class getMonthCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getMonthCheckin() {
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
		String this_month=request.getParameter("this_month"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
        JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT checkin_id,checkin_date from checkinrecord where DATE_FORMAT( checkin_date, '%Y-%m' ) = ? "
	    		+ "and checkin_id in (select id from checkin where user_id=? ); ";
	   
	    try
	    {	    	
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, this_month);
			prestmt.setString(2, user_id);
			rs = prestmt.executeQuery();
			while(rs.next())
			{
		    	jsonobj.put("checkin_date", rs.getString("checkin_date"));  
		    	jsonobj.put("checkin_id", rs.getString("checkin_id"));  

				jsonarray.put(jsonobj); 
				jsonobj=new JSONObject(); 
			}
			
	      }
	      catch(Exception ex)
	      {
	        ex.printStackTrace();
	      }
	      finally
	      {
	        out.print(jsonarray);
		    //out.print(jsonobj);
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
