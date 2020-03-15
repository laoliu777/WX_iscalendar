package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import servlet.DBUtil;

/**
 * Servlet implementation class getOverviewOfToday
 */
@WebServlet("/user/getOverviewOfToday")
public class getOverviewOfToday extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt1 = null;
	private PreparedStatement prestmt2 = null;
	private ResultSet rs1 = null;
	private ResultSet rs2 = null;
	private String sql1 = null;
	private String sql2 = null;
	private String sql3 = null;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getOverviewOfToday() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致

		String user_id=request.getParameter("id"); 
		String this_date=request.getParameter("this_date"); 

		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();
	       
	    /*
	    Date d = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("MM dd");	
	    String datenow = sdf.format(d);
	    String day=getWeekOfDate(d);
	    jsonobj.put("date", datenow);  
	    jsonobj.put("day", day);  
	    */

	    //当日纪念数
	    //sql1="select count(*) from anniversary as a WHERE a.user_id =? and date(a.anniversary) = current_date() ; ";
	    sql1="select count(*) from anniversary WHERE user_id =? and anniversary= ? ; ";
	    //当日打卡数
	    //sql2="SELECT count(*) FROM checkinrecord cd, checkin c WHERE c.user_id =? and cd.checkin_id = c.id and " + 
	    //		"date(cd.checkin_date) = CURRENT_DATE() ; ";
	    sql2="SELECT count(*) FROM checkinrecord cd, checkin c WHERE c.user_id =? and cd.checkin_id = c.id and " + 
	    		"date(cd.checkin_date) =? and c.state=1; ";	    
	    //日记？
	    //sql3="select content from diary as d WHERE d.user_id =? and date(d.date) = current_date() ; ";

	    try
	    {
	    	prestmt1 = con.prepareStatement(sql1);
			prestmt1.setString(1, user_id);
			prestmt1.setString(2, this_date);
			rs1 = prestmt1.executeQuery();
			if(rs1.next())
			{
				jsonobj.put("anniversarycount", rs1.getInt(1));  
				
			}
			prestmt2 = con.prepareStatement(sql2);
			prestmt2.setString(1, user_id);
			prestmt2.setString(2, this_date);
			rs2 = prestmt2.executeQuery();
			if(rs2.next())
			{
				jsonobj.put("checkincount", rs2.getInt(1));  			
			}
			/*
			prestmt = con.prepareStatement(sql3);
			prestmt.setString(1, user_id);
			rs = prestmt.executeQuery();
			while(rs.next())
			{
				jsonobj.put("diarycontent", rs.getString("content"));  			
			}
			*/
	      }
	      catch(Exception ex)
	      {
	        ex.printStackTrace();
	      }
	      finally
	      {
	        out.print(jsonobj);
	        DBUtil.close(rs1);
	        DBUtil.close(rs2);
	        DBUtil.close(prestmt1);
	        DBUtil.close(prestmt2);
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

	public String getWeekOfDate(Date date) {
	    String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	    if (w < 0)
	        w = 0;
	    return weekDays[w];
	}
	
}
