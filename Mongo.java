package mongo.extract;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mysql.jdbc.PreparedStatement;



import java.util.Date;


public class Mongo {

	public static void main(String[] args) throws Exception
	{
		MongoClient mongo = new MongoClient("130.65.133.212", 27017);		  
        
        
         Connection con=null; 
    	   
         
    	 String URL ="jdbc:mysql://localhost/cmpe283";
    	 String username= "root" ;
    	 String password = "admin";
    	 
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			con=(Connection) DriverManager.getConnection(URL,username,password);
		
			
			
		} catch (	Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	FiveMinutes five = new FiveMinutes(mongo,con);
    	five.start();
    	
    	Hour hour = new Hour(mongo,con);
    	hour.start();
    	
    	TwentyFour twentyfour = new TwentyFour(mongo, con);
    	twentyfour.start();

    	

		
	}
}


class FiveMinutes extends Thread
{	MongoClient mongo ;
Connection con;
	FiveMinutes(MongoClient mongo,Connection con)
	{
		this.mongo= mongo;		  
        
        
        this.con=con; 
	}

	public void run() 
	{	PreparedStatement pt =null;
		try {
			System.out.println("Five Minutes Called");
	        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));  
	        Calendar cal=null;
	        DB db = mongo.getDB("finalcmpe283");
	        float cpu1 = 0.0f;
	        long mem1=0;
	        float ioread1=0.0f;
	        float iowrite1=0.0f;
	        float network1 = 0.0f;
	        int t1=0;
	        
	        float cpu2 = 0.0f;
	        long mem2=0;
	        float ioread2=0.0f;
	        float iowrite2=0.0f;
	        float network2 = 0.0f; 
	        
	        float cpu3 = 0.0f;
	        long mem3=0;
	        float ioread3=0.0f;
	        float iowrite3=0.0f;
	        float network3 = 0.0f; 
	        
	        while(true)
	        {	
				cal=Calendar.getInstance(TimeZone.getDefault());
	        	System.out.println(cal.getTime());
	        cal.add(Calendar.SECOND, -300);
	        System.out.println(cal.getTime());
	        
	        
	        Date dateGMT=cal.getTime(); 
	       // BasicDBObject query1 = new BasicDBObject("@timestamp", new BasicDBObject("$gt", dateGMT));
	        
	        BasicDBObject query1 = new BasicDBObject("vmname","T09_VM01_Lab3").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query2 = new BasicDBObject("vmname","T09_VM02_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query3 = new BasicDBObject("vmname","T09_VM03_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        DBCollection coll = db.getCollection("finalmetric");        
	        
	        DBCursor cursor1 = coll.find(query1);
	        DBCursor cursor2 = coll.find(query2);
	        DBCursor cursor3 = coll.find(query3);
	        

	        System.out.println(cursor1.count());
	        
			while(cursor1.hasNext()) {
				 DBObject getdata=cursor1.next();
				
				cpu1=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu1;
				mem1=Long.parseLong(getdata.get("mem_used_kb").toString())+mem1;
				ioread1=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread1;
				iowrite1=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite1;
				network1=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network1;
				
					}
			while(cursor2.hasNext()) {
				 DBObject getdata=cursor2.next();
				
				cpu2=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu2;
				mem2=Long.parseLong(getdata.get("mem_used_kb").toString())+mem2;
				ioread2=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread2;
				iowrite2=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite2;
				network2=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network2;
					}
			while(cursor3.hasNext()) {
				 DBObject getdata=cursor3.next();
				
				cpu3=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu3;
				mem3=Long.parseLong(getdata.get("mem_used_kb").toString())+mem3;
				ioread3=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread3;
				iowrite3=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite3;
				network3=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network3;
					}
					
			cal=null;
			cal=Calendar.getInstance(TimeZone.getDefault());
			String sql1 = "INSERT into FiveMinutes(vmname,cpu,mem,ioread,iowrite,network,timeinserted) values(?,?,?,?,?,?,?)";
			pt=  (PreparedStatement) con.prepareStatement(sql1);
			if(cursor1.count()>0)
			{
				pt.setString(1, "T09_VM01_Lab3");
				pt.setDouble(2,	cpu1/cursor1.count());
				pt.setFloat(3, mem1/cursor1.count());
				pt.setFloat(4, ioread1/cursor1.count());
				pt.setFloat(5, iowrite1/cursor1.count());
				pt.setFloat(6, network1/cursor1.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor2.count()>0)
			{
				pt.setString(1, "T09_VM02_Lab4");
				pt.setDouble(2,	cpu2/cursor2.count());
				pt.setFloat(3, mem2/cursor2.count());
				pt.setFloat(4, ioread2/cursor2.count());
				pt.setFloat(5, iowrite2/cursor2.count());
				pt.setFloat(6, network2/cursor2.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor3.count()>0)
			{
				pt.setString(1, "T09_VM03_Lab4");
				pt.setDouble(2,	cpu3/cursor3.count());
				pt.setFloat(3, mem3/cursor3.count());
				pt.setFloat(4, ioread3/cursor3.count());
				pt.setFloat(5, iowrite3/cursor3.count());
				pt.setFloat(6, network3/cursor3.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
		
			
			
			pt.executeBatch();
			
			cpu1=cpu2=cpu3 = 0.0f;
			mem1=mem2=mem3=0;
			ioread1=ioread2=ioread3=0.0f;
			iowrite1=iowrite2=iowrite3=0.0f;
			network1=network2=network3=0.0f;
			Thread.sleep(300000);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


class Hour extends Thread
{	MongoClient mongo ;
Connection con;
Hour(MongoClient mongo,Connection con)
	{
		this.mongo= mongo;		  
        
        
        this.con=con; 
	}

	public void run() 
	{	PreparedStatement pt =null;
		try {
			System.out.println("Hour  Called");
	        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));  
	        Calendar cal=null;
	        DB db = mongo.getDB("finalcmpe283");
	        float cpu1 = 0.0f;
	        long mem1=0;
	        float ioread1=0.0f;
	        float iowrite1=0.0f;
	        float network1 = 0.0f;
	        
	        float cpu2 = 0.0f;
	        long mem2=0;
	        float ioread2=0.0f;
	        float iowrite2=0.0f;
	        float network2 = 0.0f; 
	        
	        float cpu3 = 0.0f;
	        long mem3=0;
	        float ioread3=0.0f;
	        float iowrite3=0.0f;
	        float network3 = 0.0f; 
	        
	        while(true)
	        {	
				cal=Calendar.getInstance(TimeZone.getDefault());
	        	System.out.println(cal.getTime());
	        cal.add(Calendar.HOUR, -1);
	        System.out.println(cal.getTime());
	        ;
	        
	        Date dateGMT=cal.getTime(); 
	       // BasicDBObject query1 = new BasicDBObject("@timestamp", new BasicDBObject("$gt", dateGMT));
	        
	        BasicDBObject query1 = new BasicDBObject("vmname","T09_VM01_Lab3").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query2 = new BasicDBObject("vmname","T09_VM02_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query3 = new BasicDBObject("vmname","T09_VM03_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        DBCollection coll = db.getCollection("finalmetric");        
	        
	        DBCursor cursor1 = coll.find(query1);
	        DBCursor cursor2 = coll.find(query2);
	        DBCursor cursor3 = coll.find(query3);
	        
	        System.out.println(cursor1.count());
	        
			while(cursor1.hasNext()) {
				 DBObject getdata=cursor1.next();
				
				cpu1=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu1;
				mem1=Long.parseLong(getdata.get("mem_used_kb").toString())+mem1;
				ioread1=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread1;
				iowrite1=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite1;
				network1=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network1;
					}
			while(cursor2.hasNext()) {
				 DBObject getdata=cursor2.next();
				
				cpu2=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu2;
				mem2=Long.parseLong(getdata.get("mem_used_kb").toString())+mem2;
				ioread2=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread2;
				iowrite2=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite2;
				network2=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network2;
					}
			while(cursor3.hasNext()) {
				 DBObject getdata=cursor3.next();
				
				cpu3=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu3;
				mem3=Long.parseLong(getdata.get("mem_used_kb").toString())+mem3;
				ioread3=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread3;
				iowrite3=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite3;
				network3=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network3;
					}
			cal=null;
			cal=Calendar.getInstance(TimeZone.getDefault());
			String sql1 = "INSERT into onehour(vmname,cpu,mem,ioread,iowrite,network,timeinserted) values(?,?,?,?,?,?,?)";
			pt=  (PreparedStatement) con.prepareStatement(sql1);
			if(cursor1.count()>0)
			{	cal=Calendar.getInstance(TimeZone.getDefault());
				pt.setString(1, "T09_VM01_Lab3");
				pt.setDouble(2,	cpu1/cursor1.count());
				pt.setFloat(3, mem1/cursor1.count());
				pt.setFloat(4, ioread1/cursor1.count());
				pt.setFloat(5, iowrite1/cursor1.count());
				pt.setFloat(6, network1/cursor1.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor2.count()>0)
			{	cal=Calendar.getInstance(TimeZone.getDefault());
				pt.setString(1, "T09_VM02_Lab4");
				pt.setFloat(2,	cpu2/cursor2.count());
				pt.setFloat(3, mem2/cursor2.count());
				pt.setFloat(4, ioread2/cursor2.count());
				pt.setFloat(5, iowrite2/cursor2.count());
				pt.setFloat(6, network2/cursor2.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor3.count()>0)
			{	cal=Calendar.getInstance(TimeZone.getDefault());
				pt.setString(1, "T09_VM03_Lab4");
				pt.setDouble(2,	cpu3/cursor3.count());
				pt.setFloat(3, mem3/cursor3.count());
				pt.setFloat(4, ioread3/cursor3.count());
				pt.setFloat(5, iowrite3/cursor3.count());
				pt.setFloat(6, network3/cursor3.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
		
			
			
			pt.executeBatch();
			
			cpu1=cpu2=cpu3 = 0.0f;
			mem1=mem2=mem3=0;
			ioread1=ioread2=ioread3=0.0f;
			iowrite1=iowrite2=iowrite3=0.0f;
			network1=network2=network3=0.0f;
			Thread.sleep(3600000);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


class TwentyFour extends Thread
{	MongoClient mongo ;
Connection con;
TwentyFour(MongoClient mongo,Connection con)
	{
		this.mongo= mongo;		  
        
        
        this.con=con; 
	}

	public void run() 
	{	PreparedStatement pt =null;
		try {
			System.out.println("TwentyFour  Called");
	        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));  
	        Calendar cal=null;
	        DB db = mongo.getDB("finalcmpe283");
	        float cpu1 = 0.0f;
	        long mem1=0;
	        float ioread1=0.0f;
	        float iowrite1=0.0f;
	        float network1 = 0.0f;
	        
	        float cpu2 = 0.0f;
	        long mem2=0;
	        float ioread2=0.0f;
	        float iowrite2=0.0f;
	        float network2 = 0.0f; 
	        
	        float cpu3 = 0.0f;
	        long mem3=0;
	        float ioread3=0.0f;
	        float iowrite3=0.0f;
	        float network3 = 0.0f; 
	        
	        while(true)
	        {	
				cal=Calendar.getInstance(TimeZone.getDefault());
	        	System.out.println(cal.getTime());
	        cal.add(Calendar.DAY_OF_MONTH, -7);
	        System.out.println(cal.getTime());
	        ;
	        
	        Date dateGMT=cal.getTime(); 
	       // BasicDBObject query1 = new BasicDBObject("@timestamp", new BasicDBObject("$gt", dateGMT));
	        
	        BasicDBObject query1 = new BasicDBObject("vmname","T09_VM01_Lab3").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query2 = new BasicDBObject("vmname","T09_VM02_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        BasicDBObject query3 = new BasicDBObject("vmname","T09_VM03_Lab4").append("@timestamp", new BasicDBObject("$gt", dateGMT));
	        DBCollection coll = db.getCollection("finalmetric");        
	        
	        DBCursor cursor1 = coll.find(query1);
	        DBCursor cursor2 = coll.find(query2);
	        DBCursor cursor3 = coll.find(query3);
	        
	        System.out.println(cursor1.count());
	        
			while(cursor1.hasNext()) {
				 DBObject getdata=cursor1.next();
				
				cpu1=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu1;
				mem1=Long.parseLong(getdata.get("mem_used_kb").toString())+mem1;
				ioread1=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread1;
				iowrite1=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite1;
				network1=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network1;
					}
			while(cursor2.hasNext()) {
				 DBObject getdata=cursor2.next();
				
				cpu2=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu2;
				mem2=Long.parseLong(getdata.get("mem_used_kb").toString())+mem2;
				ioread2=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread2;
				iowrite2=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite2;
				network2=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network2;
					}
			while(cursor3.hasNext()) {
				 DBObject getdata=cursor3.next();
				
				cpu3=Float.parseFloat(getdata.get("cpu_usage_perc").toString())+cpu3;
				mem3=Long.parseLong(getdata.get("mem_used_kb").toString())+mem3;
				ioread3=Float.parseFloat(getdata.get("io_read_byte").toString())+ioread3;
				iowrite3=Float.parseFloat(getdata.get("io_write_byte").toString())+iowrite3;
				network3=Float.parseFloat(getdata.get("nw_tx_kbps").toString())+network3;
					}
			cal=null;
			cal=Calendar.getInstance(TimeZone.getDefault());
			String sql1 = "INSERT into 24hour(vmname,cpu,mem,ioread,iowrite,network,timeinserted) values(?,?,?,?,?,?,?)";
			pt=  (PreparedStatement) con.prepareStatement(sql1);
			if(cursor1.count()>0)
			{
				pt.setString(1, "T09_VM01_Lab3");
				pt.setDouble(2,	cpu1/cursor1.count());
				pt.setFloat(3, mem1/cursor1.count());
				pt.setFloat(4, ioread1/cursor1.count());
				pt.setFloat(5, iowrite1/cursor1.count());
				pt.setFloat(6, network1/cursor1.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor2.count()>0)
			{
				pt.setString(1, "T09_VM02_Lab4");
				pt.setFloat(2,	cpu2/cursor2.count());
				pt.setFloat(3, mem2/cursor2.count());
				pt.setFloat(4, ioread2/cursor2.count());
				pt.setFloat(5, iowrite2/cursor2.count());
				pt.setFloat(6, network2/cursor2.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
			if(cursor3.count()>0)
			{
				pt.setString(1, "T09_VM03_Lab4");
				pt.setDouble(2,	cpu3/cursor3.count());
				pt.setFloat(3, mem3/cursor3.count());
				pt.setFloat(4, ioread3/cursor3.count());
				pt.setFloat(5, iowrite3/cursor3.count());
				pt.setFloat(6, network3/cursor3.count());
				pt.setTimestamp(7,new Timestamp(new Date().getTime()));
				pt.addBatch();
			}
		
			
			
			pt.executeBatch();
			
			cpu1=cpu2=cpu3 = 0.0f;
			mem1=mem2=mem3=0;
			ioread1=ioread2=ioread3=0.0f;
			iowrite1=iowrite2=iowrite3=0.0f;
			network1=network2=network3=0.0f;
			Thread.sleep(86400000);
	        }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
