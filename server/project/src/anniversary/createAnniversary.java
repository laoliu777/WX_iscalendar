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
 * Servlet implementation class createAnniversary
 */
@WebServlet("/anniversary/createAnniversary")
public class createAnniversary extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createAnniversary() {
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
		String anniversary_name=request.getParameter("anniversary_name"); 
		String anniversary_description=request.getParameter("anniversary_description"); 
		String anniversary_type=request.getParameter("anniversary_type"); 
		String icon_url=request.getParameter("icon_url"); 
		String background=request.getParameter("background"); 
		String anniversary=request.getParameter("anniversary"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    con=DBUtil.CreateConn();

		sql="insert into anniversary (user_id,anniversary_name,anniversary_description,anniversary_type,icon_url,background,anniversary,created_at)"
				+ " values (?,?,?,?,?,?,?,now());";
	       
	      try
	        {
	        	prestmt = con.prepareStatement(sql);
	        	prestmt.setString(1, user_id);
	        	prestmt.setString(2, anniversary_name);
	        	prestmt.setString(3, anniversary_description);
	        	prestmt.setString(4, anniversary_type);
	        	prestmt.setString(5, icon_url);
	        	prestmt.setString(6, background);
	        	prestmt.setString(7, anniversary);
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
