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
 * Servlet implementation class createDiary
 */
@WebServlet("/diary/createDiary")
public class createDiary extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private PreparedStatement prestmt1 = null;
	private ResultSet rs = null;
	//private ResultSet rs1 = null;
	private String sql = null;
	private String sql1 = null;
	private String sql2 = null;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createDiary() {
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
		String picture=request.getParameter("picture"); 
		String content=request.getParameter("content"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    con=DBUtil.CreateConn();

	    sql="SELECT * FROM diary WHERE user_id = ? and date(date) = current_date(); ";
		sql1="insert into diary (user_id,picture,content,date) values (?,?,?,now());";
		sql2="update diary set picture=? and content=? where user_id =? and date(date) = current_date();  ";
	       
	      try
	        {
	    	  	prestmt = con.prepareStatement(sql);
				prestmt.setString(1, user_id);
				rs = prestmt.executeQuery();
				if(rs.next())
				{
					prestmt1 = con.prepareStatement(sql2);
		        	prestmt1.setString(1, picture);
		        	prestmt1.setString(2, content);
		        	prestmt1.setString(3, user_id);		        
		        	prestmt1.execute();
					out.print("ok");
				}
				else {
					prestmt1 = con.prepareStatement(sql1);
					prestmt1.setString(1, user_id);
		        	prestmt1.setString(2, picture);
		        	prestmt1.setString(3, content);
		        	prestmt1.execute();
					out.print("ok");
				}
	        	
	        }
	        catch(Exception ex)
	        {
	        	out.print("no");
	        	ex.printStackTrace();
	        }
	        finally
	        {   
	        	if(rs!=null)
	        		DBUtil.close(rs);
	        	if(prestmt!=null)
	        		DBUtil.close(prestmt);
		        //DBUtil.close(rs1);
	        	if(prestmt1!=null)
	        		DBUtil.close(prestmt1);
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
