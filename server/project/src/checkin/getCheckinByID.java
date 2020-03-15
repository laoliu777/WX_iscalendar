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
 * Servlet implementation class getCheckinByID
 */
@WebServlet("/checkin/getCheckinByID")
public class getCheckinByID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt3 = null;
	private PreparedStatement prestmt1 = null;
	private PreparedStatement prestmt2 = null;
	private ResultSet rs3 = null;
	private ResultSet rs1 = null;
	private ResultSet rs2 = null;
	private String sql3 = null;    
	private String sql1 = null;   
	private String sql2 = null;   

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getCheckinByID() {
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
        //JSONArray jsonarray = new JSONArray();  
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql1="SELECT count(*) as totalcheckinday"
	    		+ " FROM checkinrecord WHERE checkin_id=? ; ";
	    sql2="select rn from" + 
	    		"(" + 
	    		"select min(checkin_date),max(checkin_date) as maxenddate,(datediff(max(checkin_date),min(checkin_date))+1) as rn from " + 
	    		"(" + 
	    		"select *,((select count(1) from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr2 where dr2.checkin_date <= dr1.checkin_date)" + 
	    		" - day(dr1.checkin_date)) as rownum from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr1 " + 
	    		")z group by rownum " + 
	    		")p where date(p.maxenddate)=current_date();";
	    sql3="SELECT checkin_name,checkin_description,icon_url,background,created_at,"
	    		+ "abs(timestampdiff(day,now(),created_at)) as historyday "//创建后的历史总天数
	    		+ " FROM checkin WHERE id = ?; ";
	   
	    try
	    {
	    	int totalcheckinday=0;
	    	prestmt1 = con.prepareStatement(sql1);
			prestmt1.setString(1, checkin_id);
			rs1 = prestmt1.executeQuery();
			while(rs1.next())
			{
		    	jsonobj.put("totalcheckinday", rs1.getString("totalcheckinday")); //共计打卡天数
		    	totalcheckinday=rs1.getInt("totalcheckinday");
			}
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
	    	prestmt3 = con.prepareStatement(sql3);
			prestmt3.setString(1, checkin_id);
			rs3 = prestmt3.executeQuery();
			while(rs3.next())
			{
		    	jsonobj.put("checkin_name", rs3.getString("checkin_name"));  
		    	jsonobj.put("checkin_description", rs3.getString("checkin_description"));  
		    	jsonobj.put("icon_url", rs3.getString("icon_url"));  
		    	jsonobj.put("background", rs3.getString("background"));  
		    	//jsonobj.put("stick_days", rs.getString("stick_days"));  
		    	jsonobj.put("created_at", rs3.getString("created_at"));  
		    	jsonobj.put("historyday", rs3.getString("historyday"));  
		    	jsonobj.put("missday", (rs3.getInt("historyday")-totalcheckinday));  

			}
			
	      }
	      catch(Exception ex)
	      {
	        ex.printStackTrace();
	      }
	      finally
	      {
		    out.print(jsonobj);
	        DBUtil.close(rs1);
        	if(rs2!=null)
        		DBUtil.close(rs2);
        	if(rs3!=null)
        		DBUtil.close(rs3);
	        DBUtil.close(prestmt1);
        	if(prestmt2!=null)
        		DBUtil.close(prestmt2);
        	if(prestmt3!=null)
        		DBUtil.close(prestmt3);
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
