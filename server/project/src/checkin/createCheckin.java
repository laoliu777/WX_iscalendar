package checkin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class createCheckin
 */
@WebServlet("/checkin/createCheckin")
public class createCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createCheckin() {
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
		String checkin_name=request.getParameter("checkin_name"); 
		String checkin_description=request.getParameter("checkin_description"); 
		String icon_url=request.getParameter("icon_url"); 
		String background=request.getParameter("background"); 
		//String stick_days=request.getParameter("stick_days"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    con=DBUtil.CreateConn();

		sql="insert into checkin (user_id,checkin_name,checkin_description,icon_url,background,created_at)"
				+ " values (?,?,?,?,?,now());";
	       
	      try
	        {
	        	prestmt = con.prepareStatement(sql);
	        	prestmt.setString(1, user_id);
	        	prestmt.setString(2, checkin_name);
	        	prestmt.setString(3, checkin_description);
	        	prestmt.setString(4, icon_url);
	        	prestmt.setString(5, background);
	        	//prestmt.setString(6, stick_days);
	        	prestmt.execute();
				out.print("ok");
	        }
	        catch(Exception ex)
	        {
	        	out.print("no");
	        	ex.printStackTrace();
	        }
	        finally
	        {   
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
