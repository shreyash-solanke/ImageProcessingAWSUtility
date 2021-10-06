package io.shreyash.imageaws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.amazonaws.services.ec2.AmazonEC2Client;

import com.mysql.jdbc.Driver;

public class Ec2Processing {
	
	private AmazonEC2Client ec2Client;

	public Ec2Processing() {
		ec2Client = new AmazonEC2Client();
		
	}
	
	public void createConnection() {
		
		
		
	}
	
	public void updateDatabase(String uploadedImg, String srcImg, long jobId) {
	
		 Connection conn = null;
		 Statement stmt = null;

	       try
	       {
	    	   //creating connection
	           String url = "jdbc:mysql://35.154.225.58:3306/test";
	           Class.forName ("com.mysql.jdbc.Driver");
	           conn = DriverManager.getConnection (url,"root","Assign2root");
	           System.out.println ("Database connection established");
	           
	           //update query
	           stmt = conn.createStatement();
	           String sql = "UPDATE transformed_images "+
	        		   		"SET flag = 1, transformed_img_name = '"+uploadedImg+"'"+
	        		   		" WHERE job_id = "+jobId+ " AND src_img_name = "
	        		   		+"'"+srcImg+"'";
	           
	           stmt.executeUpdate(sql);
	           
	       }
	       catch (Exception e)
	       {
	           e.printStackTrace();

	       }
	       finally
	       {
	           if (conn != null)
	           {
	               try
	               {
	                   conn.close ();
	                   System.out.println ("Database connection terminated");
	               }
	               catch (Exception e) { /* ignore close errors */ }
	           }
	       }
		
	}
	
}
