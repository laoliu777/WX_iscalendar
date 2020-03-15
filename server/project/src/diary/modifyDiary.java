package diary;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.DBUtil;

/**
 * Servlet implementation class modifyDiary
 */
@WebServlet("/diary/modifyDiary")
public class modifyDiary extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private String sql = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modifyDiary() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致
		
		String picture=request.getParameter("picture"); 
		String content=request.getParameter("content"); 
		String id=request.getParameter("id"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    con=DBUtil.CreateConn();
	        
	    sql="update diary set picture=? and content=? where id =? ;  ";
	      
	    try
	    {
	    	prestmt = con.prepareStatement(sql);
	        prestmt.setString(1, picture);
	        prestmt.setString(2, content);
	        prestmt.setString(3, id);
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
