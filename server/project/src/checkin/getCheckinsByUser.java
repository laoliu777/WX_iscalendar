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
 * Servlet implementation class getCheckinsByUser
 */
@WebServlet("/checkin/getCheckinsByUser")
public class getCheckinsByUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private PreparedStatement prestmt1 = null;
	private ResultSet rs = null;
	private ResultSet rs1 = null;
	private String sql = null;     
	private String sql1 = null; 	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getCheckinsByUser() {
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
        JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT c.*,cd.checkin_date FROM checkin c,checkinrecord cd "
	    		+ "WHERE c.user_id =? and c.state=1 and c.id=cd.checkin_id "
	    		+ "and date(cd.checkin_date) = ? ;";
	    sql1="select rn from" + 
	    		"(" + 
	    		"select min(checkin_date),max(checkin_date) as maxenddate,(datediff(max(checkin_date),min(checkin_date))+1) as rn from " + 
	    		"(" + 
	    		"select *,((select count(1) from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr2 where dr2.checkin_date <= dr1.checkin_date)" + 
	    		" - day(dr1.checkin_date)) as rownum from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr1 " + 
	    		")z group by rownum " + 
	    		")p where date(p.maxenddate)=? ;";
	    
	    try
	    {
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, user_id);
			prestmt.setString(2, this_date);
			rs = prestmt.executeQuery();
			while(rs.next())
			{
				jsonobj.put("id", rs.getString("id"));  
				jsonobj.put("checkin_name", rs.getString("checkin_name"));  
				jsonobj.put("checkin_description", rs.getString("checkin_description"));  
				jsonobj.put("icon_url", rs.getString("icon_url"));  
				jsonobj.put("background", rs.getString("background"));  
				//jsonobj.put("stick_days", rs.getString("stick_days"));  
				jsonobj.put("created_at", rs.getString("created_at"));  
				/*
				if(rs.getString("is_checkin")!=null) {
					jsonobj.put("is_checkin", 1); //今天已打卡
				}
				else {
					jsonobj.put("is_checkin", 0); //当前连续打卡天数
				}
				*/
				jsonobj.put("checkin_date", rs.getString("checkin_date"));  
				
				prestmt1 = con.prepareStatement(sql1);
				prestmt1.setString(1, rs.getString("id"));
				prestmt1.setString(2, rs.getString("id"));
				prestmt1.setString(3, this_date);
				rs1 = prestmt1.executeQuery();
				//||rs1.next()
				//String stick_days=rs1.getString("rn");
				if(rs1.next()) {
					jsonobj.put("stick_days", rs1.getString("rn")); //当前连续打卡天数
					//stick_days=rs.getInt("rn");
				}
				else {
					jsonobj.put("stick_days", 1); //当前连续打卡天数
				}
				
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
	        if(rs1!=null)
	        	DBUtil.close(rs1);
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
