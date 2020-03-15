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
 * Servlet implementation class checkin
 */
@WebServlet("/checkin/checkin")
public class checkin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private PreparedStatement prestmt1 = null;
	private PreparedStatement prestmt2 = null;
	private ResultSet rs = null;
	private ResultSet rs2 = null;
	private String sql1 = null;
	private String sql2 = null;
	private String sql3 = null;
	private String sql4 = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkin() {
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

	    //先判断今天这项有没有打过卡
	    sql1="select count(*) from checkinrecord where checkin_id=? and date(checkin_date) = current_date()";
	    //没有，插入
	    sql2="insert into checkinrecord (checkin_id,checkin_date) values (?,now());";
	    //有，更新
	    sql3="update checkinrecord set checkin_date=now() where checkin_id=? and date(checkin_date) = current_date();";
	    //查询连续天数
	    sql4="select rn from" + 
	    		"(" + 
	    		"select min(checkin_date),max(checkin_date) as maxenddate,(datediff(max(checkin_date),min(checkin_date))+1) as rn from " + 
	    		"(" + 
	    		"select *,((select count(1) from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr2 where dr2.checkin_date <= dr1.checkin_date)" + 
	    		" - day(dr1.checkin_date)) as rownum from (select checkin_date from checkinrecord where checkin_id=? group by checkin_date)dr1 " + 
	    		")z group by rownum " + 
	    		")p where date(p.maxenddate)=current_date();";
	    
	      try
	        {
	        	prestmt = con.prepareStatement(sql1);
	        	prestmt.setString(1, checkin_id);
				rs = prestmt.executeQuery();
				if(rs.next()) {
					if(rs.getInt(1)>0) {
						prestmt1 = con.prepareStatement(sql3);
			        	prestmt1.setString(1, checkin_id);
			        	prestmt1.execute();
						//out.print("ok");
					}
					else {
						prestmt1 = con.prepareStatement(sql2);
			        	prestmt1.setString(1, checkin_id);
			        	prestmt1.execute();
						//out.print("ok");
					}
				}
				else {
		        	//out.print("no");
				}
				//返回连续天数	
				prestmt2 = con.prepareStatement(sql4);
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
	        	if(prestmt1!=null)
	        		DBUtil.close(prestmt1);
	        	if(prestmt2!=null)
	        		DBUtil.close(prestmt2);
	        	if(rs!=null)
	        		DBUtil.close(rs);
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
