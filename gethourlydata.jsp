<%@page import="java.sql.*" %>
    <%@page import="java.util.*" %>
    <%@page import="org.json.JSONObject" %>
<%@ page import="java.io.*,java.util.*,java.util.Date ,java.util.Calendar,java.text.SimpleDateFormat,java.text.DateFormat" %>
<%
    Connection con= null;
    try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cmpe283","root","admin");
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        List<JSONObject> VM01 = new LinkedList<JSONObject>();
        List<JSONObject> VM02 = new LinkedList<JSONObject>();
        List<JSONObject> VM03 = new LinkedList<JSONObject>();
        
        JSONObject responseObj = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.HOUR, -24);
        
        String query1 = "SELECT * from onehour where vmname='T09_VM01_Lab3'and timeinserted>'"+dateFormat.format(cal.getTime())+"'";
        String query2 = "SELECT * from onehour where vmname='T09_VM02_Lab4'and timeinserted>'"+dateFormat.format(cal.getTime())+"'";
        String query3 = "SELECT * from onehour where vmname='T09_VM03_Lab4'and timeinserted>'"+dateFormat.format(cal.getTime())+"'";
       //String query3 = "SELECT cpu,,mem,iowrite,network,vmname from fiveminutes where vmname='T09_VM03_Lab4' and timeinserted>'"+cal.getTime()+"'";
        
        PreparedStatement pstm1= con.prepareStatement(query1);
        PreparedStatement pstm2= con.prepareStatement(query2);
        PreparedStatement pstm3= con.prepareStatement(query3);
        
        rs1 = pstm1.executeQuery();
        rs2 = pstm2.executeQuery();
        rs3 = pstm3.executeQuery();
        
        JSONObject empObj = null;
        JSONObject vm2 = null;
        JSONObject vm3 = null;
        
        while (rs1.next()) {
            String vmname = rs1.getString("vmname");
            float cpu = rs1.getFloat("cpu");
            float mem = rs1.getFloat("mem");
            float ioreadwrite = rs1.getFloat("iowrite");
            float network =rs1.getFloat("network");
            
            empObj = new JSONObject();
            empObj.put("vmname", vmname);
            empObj.put("cpu", cpu);
            empObj.put("mem", mem);
            empObj.put("ioreadwrite", ioreadwrite);
            empObj.put("network", network);
            VM01.add(empObj);
        }
        while (rs2.next()) {
            String vmname = rs2.getString("vmname");
            float cpu = rs2.getFloat("cpu");
            float mem = rs2.getFloat("mem");
            float ioreadwrite = rs2.getFloat("iowrite");
            float network =rs2.getFloat("network");
            
            empObj = new JSONObject();
            empObj.put("vmname", vmname);
            empObj.put("cpu", cpu);
            empObj.put("mem", mem);
            empObj.put("ioreadwrite", ioreadwrite);
            empObj.put("network", network);
            VM02.add(empObj);
        }
        while (rs3.next()) {
            String vmname = rs3.getString("vmname");
            float cpu = rs3.getFloat("cpu");
            float mem = rs3.getFloat("mem");
            float ioreadwrite = rs3.getFloat("iowrite");
            float network =rs3.getFloat("network");
            
            empObj = new JSONObject();
            empObj.put("vmname", vmname);
            empObj.put("cpu", cpu);
            empObj.put("mem", mem);
            empObj.put("ioreadwrite", ioreadwrite);
            empObj.put("network", network);
            VM03.add(empObj);
        }
        responseObj.put("VM01", VM01);
        responseObj.put("VM02",VM02);
        responseObj.put("VM03",VM03);
        out.print(responseObj.toString());
    }catch(Exception e){
        e.printStackTrace();
    }finally{
        if(con != null){
            try{
            	con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
 %>