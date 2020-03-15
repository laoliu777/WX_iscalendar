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

import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class cancelCheckin
 */
@WebServlet("/checkin/cancelCheckin")
public class cancelCheckin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	//private PreparedStatement prestmt1 = null;
	private PreparedStatement prestmt2 = null;
	//private ResultSet rs1 = null;
	private ResultSet rs2 = null;
	private String sql = null;
	//private String sql1 = null;
	private String sql2 = null;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cancelCheckin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致
		
		String checkin_id=request.getParameter("checkin_id"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();
	        
	    //sql1="select checkin_id from checkinrecord WHERE id =? ; ";
	    sql="DELETE FROM checkinrecord WHERE checkin_id =? and date(checkin_date)=current_date() ;  ";
	    sql2="select rn from" + 
	    		"(" + 
	    		"select min(checkin_date),max(checkin_date) as maxenddate,(datediff(max(checkin_date),min(checkin_date))+1) as rn from " + 
	    		"(" + 
	    		"select *,((select count(1) from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr2 where dr2.checkin_date <= dr1.checkin_date)" + 
	    		" - day(dr1.checkin_date)) as rownum from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr1 " + 
	    		")z group by rownum " + 
	    		")p where date(p.maxenddate)=current_date();";
	      
	    try
	    {
	    	/*
	    	int checkin_id=0;
	    	prestmt1 = con.prepareStatement(sql1);
			prestmt1.setString(1, id);
			rs1 = prestmt1.executeQuery();
			if(rs1.next())
			{
				checkin_id=rs1.getInt("checkin_id");
			}
			*/
			
	    	prestmt = con.prepareStatement(sql);
	        prestmt.setString(1, checkin_id);
	        prestmt.execute();
			//out.print("ok");
	        
	        prestmt2 = con.prepareStatement(sql2);
			prestmt2.setString(1, checkin_id);
			prestmt2.setString(2, checkin_id);
			rs2 = prestmt2.executeQuery();	
			//rs.getString("rn")!=null||
			if(rs2.next()) {
				jsonobj.put("stick_days", rs2.getString("rn")); //当前连续打卡天数
				//stick_days=rs.getInt("rn");
			}
			else {
				jsonobj.put("stick_days", 0); //当前连续打卡天数
			}
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
	         //DBUtil.close(prestmt1);
	    	 if(prestmt2!=null)
	    		 DBUtil.close(prestmt2);
	         //DBUtil.close(rs1);
	    	 if(rs2!=null)
	    		 DBUtil.close(rs2);
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
