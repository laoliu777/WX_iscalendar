package anniversary;

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
 * Servlet implementation class getAnniversariesAllByUser
 */
@WebServlet("/anniversary/getAnniversariesAllByUser")
public class getAnniversariesAllByUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getAnniversariesAllByUser() {
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
		
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
        JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT *,"
	    		+ "DATEDIFF(CURDATE(),anniversary) as distday "
	    		+ " FROM anniversary WHERE user_id = ? and state=1; ";
	    
	    try
	    {
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, user_id);
			rs = prestmt.executeQuery();
			while(rs.next())
			{
				jsonobj.put("id", rs.getString("id"));  
				jsonobj.put("anniversary", rs.getString("anniversary"));  
		    	jsonobj.put("anniversary_name", rs.getString("anniversary_name"));  
		    	jsonobj.put("anniversary_description", rs.getString("anniversary_description"));  
		    	jsonobj.put("anniversary_type", rs.getString("anniversary_type"));  
		    	jsonobj.put("icon_url", rs.getString("icon_url"));  
		    	jsonobj.put("background", rs.getString("background"));  
		    	jsonobj.put("created_at", rs.getString("created_at"));  
		    	jsonobj.put("distday", rs.getString("distday"));  
		    	//jsonobj.put("distmonth", rs.getString("distmonth"));  
		    	//jsonobj.put("distyear", rs.getString("distyear"));  
				
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
	        DBUtil.close(rs);
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
