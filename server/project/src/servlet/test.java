package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import net.sf.json.JSONObject;


/**
 * Servlet implementation class test
 */
@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String url="https://www.tianqiapi.com/api/?version=v1&city=南京";
		/*
		PrintWriter out = null;  	        
		BufferedReader in = null;  	        
		StringBuilder result = new StringBuilder(); 	        
		try {  	            
			URL reqUrl = new URL(url);  	            
			// 建立连接	            
			URLConnection conn = reqUrl.openConnection();  	            	            
			//设置请求头	            
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"); 	//          
			conn.setRequestProperty("Connection", "Keep-Alive");//保持长连接	            
			conn.setDoOutput(true); //设置为true才可以使用conn.getOutputStream().write()	            
			conn.setDoInput(true); //才可以使用conn.getInputStream().read();	            	            
			//写入参数	            
			out = new PrintWriter(conn.getOutputStream()); 	            
			//设置参数，可以直接写&参数，也可以直接传入拼接好的	            
			//out.print(params);	            
			// flush输出流的缓冲  	            
			out.flush();  	            	            
			// 定义BufferedReader输入流来读取URL的响应  	            
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));  	            String line;  	            
			while ((line = in.readLine()) != null) {  	                
				result.append(line);  	            
				}  	        
			} catch (Exception e) {  	            
				e.printStackTrace();  	        
			}finally {// 使用finally块来关闭输出流、输入流  	            
				try {  	                
					if (out != null) {  	                    
						out.close();  	                
					}  	                
					if (in != null) {  	                    
						in.close();  	                
					}  	            
				} catch (IOException ex) {  	                
					ex.printStackTrace();  	            
					}  	        
				}  	       
			//return result.toString();       
			System.out.println(result.toString());
*/
		//String url="https://www.tianqiapi.com/api/?version=v1&city=南京";
    	JSONObject jsonObject = null;
    	OutputStreamWriter urlout = null;
        StringBuffer buffer = new StringBuffer();

        try {

	    //1.连接部分
            URL url = new URL("https://www.tianqiapi.com/api/");
            // http协议传输
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  		
	    //2.传入参数部分
            // 得到请求的输出流对象  
            urlout = new OutputStreamWriter(httpUrlConn.getOutputStream(),"UTF-8");  
            // 把数据写入请求的Body
            urlout.write("version=" + "v1" + "&city=" + "南京"); //参数形式跟在地址栏的一样
            urlout.flush();  
            urlout.close(); 
         
	    //3.获取数据
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return jsonObject.toString();
        //JSONObject jsonObj = new JSONObject(jsonStr);
        int errcode = jsonObject.getInt("errcode");
        //String request = (String) jsonObj.get("request");
		System.out.println(errcode);



	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
