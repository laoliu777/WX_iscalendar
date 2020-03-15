package anniversary;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import servlet.DBUtil;

/**
 * Servlet implementation class getAnniversaryByID
 */
@WebServlet("/anniversary/getAnniversaryByID")
public class getAnniversaryByID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private PreparedStatement prestmt = null;
	private ResultSet rs = null;
	private String sql = null;       
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getAnniversaryByID() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");//编码必须和页面编码一致

		String id=request.getParameter("id"); 
		
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
	    PrintWriter out = response.getWriter();
	    JSONObject jsonobj = new JSONObject(); 
	    con=DBUtil.CreateConn();

	    sql="SELECT *,abs(timestampdiff(day,now(),anniversary)) as restday " + 
	    		"FROM anniversary WHERE id = ? and state=1 ";
	   
	    try
	    {
	    	prestmt = con.prepareStatement(sql);
			prestmt.setString(1, id);
			rs = prestmt.executeQuery();
			while(rs.next())
			{
		    	 jsonobj.put("anniversary", rs.getString("anniversary"));  
		    	 jsonobj.put("anniversary_name", rs.getString("anniversary_name"));  
		    	 jsonobj.put("anniversary_description", rs.getString("anniversary_description"));  
		    	 jsonobj.put("anniversary_type", rs.getString("anniversary_type"));  
		    	 jsonobj.put("icon_url", rs.getString("icon_url"));  
		    	 jsonobj.put("background", rs.getString("background"));  
		    	 jsonobj.put("restday", rs.getString("restday"));  
		    	 jsonobj.put("created_at", rs.getString("created_at"));  
		    	 
		    	 SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		    	 String clidate = rs.getString("anniversary");
		    	 SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		    	 Date date = null;
		    	 //try {
		    	     date = ft.parse(clidate);
		    	 //} catch (ParseException e) {
		    	     //e.printStackTrace();
		    	 //}
		    	 //java.util.Date date=sdf.parse(dstr);
		    	 Calendar cToday = Calendar.getInstance(); // 存今天
		    	 Calendar cBirth = Calendar.getInstance(); // 存生日
		    	 cBirth.setTime(date); // 设置生日
		    	 cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
		    	 Integer days;
		    	 if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
		    	     // 生日已经过了，要算明年的了
		    	     days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
		    	     days += cBirth.get(Calendar.DAY_OF_YEAR);
		    	 } else {
		    	     // 生日还没过
		    	     days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
		    	 }
		    	 // 输出结果
		    	 jsonobj.put("to_next_anniversary", days);  
		    	 /*
		    	 if (days == 0) {
		    	     System.out.println("今天生日");
		    	 } else {
		    	     System.out.println("距离生日还有：" + days + "天");
		    	 }
		    	 */
			}
			
	      }
	      catch(Exception ex)
	      {
	        ex.printStackTrace();
	      }
	      finally
	      {
	        out.print(jsonobj);
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
