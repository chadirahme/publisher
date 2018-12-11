package db;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import java.io.*;
import org.apache.log4j.Logger;
import com.sun.rowset.CachedRowSetImpl;

public class DBHandler {

	private Connection con = null;
	Properties props = new Properties();
    public static Runtime runtime=Runtime.getRuntime();
    private Logger logger = Logger.getLogger(DBHandler.class);
	private int nColumns=0;
	private StringWriter sw = null;
	DataSource ds = null;
	
	public DBHandler(){
		createPool();
	}
	
	
	public void createPool(String dataSource){
		try{
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			ds = (DataSource)envCtx.lookup(dataSource);
		}
		catch(Exception e) {
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("| Services Monitor | [DBManager.connect(1)]  ClassNotFoundException:  "+sw.toString());
		}
	}
	
	public void createPool(){
		try{
			Context ctx = new InitialContext();
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			//ds = (DataSource)envCtx.lookup("jdbc/iqbal_hba");
			 ds = (DataSource)envCtx.lookup("PublisherConnectionPool");
		}
		catch(Exception e) {
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("| Services Monitor | [DBManager.connect(1)]  ClassNotFoundException:  "+sw.toString());
		}
	}
	
	public void connect() {		
		try{			
			if (ds != null) {
		        con = ds.getConnection();
			}								
		}
		catch(SQLException sqlEx)
		{
			sw = new StringWriter();
			sqlEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in DBHandler---connect-->"+sw.toString());
		}
		catch(Exception e) {
			sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("| Services Monitor | [DBManager.connect(1)]  ClassNotFoundException:  "+sw.toString());
		}
	}

	public void disconnect(){
		try{
			con.close();		
		}catch(SQLException ex){
			sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("| Services Monitor | [DBManager.disconnect]  SQLException: "+sw.toString());
		}
	}

	public void Commit(){
			try{
				con.commit();
			}catch(SQLException ex){
				sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				logger.error("| Services Monitor | [DBManager.disconnect]  SQLException: "+sw.toString());
			}
	}

	public Connection getConnection(){
		return con;
	}

	public String escapeString(String theString)
	    {
	        StringBuffer str = new StringBuffer();

	        theString = nullToEmpty(theString).trim();

	        int len = (theString != null) ? theString.length() : 0;
	        for (int i = 0; i < len; i++)
	        {
	            char ch = theString.charAt(i);
	            switch (ch)
	            {
	                case '<':
	                {
	                    str.append("&lt;");
	                    break;
	                }
	                case '>':
	                {
	                    str.append("&gt;");
	                    break;
	                }
	                case '&':
	                {
	                    str.append("&amp;");
	                    break;
	                }
	                case '"':
	                {
	                    str.append("&quot;");
	                    break;
	                }
	                case '\'':
	                {
	                    str.append("&apos;");
	                    break;
	                }
	                case '\r':
	                case '\n':
	                {
	                    str.append("&#");
	                    str.append(Integer.toString(ch));
	                    str.append(';');
	                    break;
	                }
	                // else, default append char
	                default:
	                {
	                    str.append(ch);
	                }
	            }
	        }
	        return str.toString();
	    }

	   public String nullToEmpty(Object obj)
		    {
		        if (obj == null)
		        {
		            return "";
		        }
		        else
		        {
		            return obj.toString();
		        }
		    }

       /**
        * 
        * @param String query
        * @return CachedRowSetImpl
        * @author Movses Kiredjian
        * @since January 2009
        * @category Executes a sql select query and returns the result in a cachedRowSet
        * @exception SQLException
        * 
        */
	   public CachedRowSetImpl executeNonQuery(String query)
		{
		   logger.info("Executing this query-->"+query);
			CachedRowSetImpl crs = null;
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				connect();
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				crs.populate(rs);
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		rs.close();
			    		stmt.close();
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			    	}
			    }
			    return crs;	
			}
		}
	   
	   public CachedRowSetImpl executeNonQueryWithoutLogs(String query)
		{
			CachedRowSetImpl crs = null;
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				connect();
				
				
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				crs.populate(rs);
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQueryWithoutLogs-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQueryWithoutLogs-->"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeNonQueryWithoutLogs-->"+sw.toString());
			    	}
			    }
			    return crs;	
			}
		}
	   
	  
	   public long getUsedMemory()
	   {
		   return runtime.totalMemory()-runtime.freeMemory();
	   }
	   
		public StringBuffer getHeaders(ResultSet rs)
		{
			StringBuffer columnNames=new StringBuffer();
			try
			{
				rs.beforeFirst();
			    ResultSetMetaData rsmd=rs.getMetaData();
			    this.nColumns=rsmd.getColumnCount();
			   for(int i=1;i<=nColumns;i++)
			    {
				 columnNames.append(rsmd.getColumnLabel(i));
				 columnNames.append(',');
				}
			   columnNames.deleteCharAt(columnNames.length()-1);
			   rs.beforeFirst();
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("| Services Monitor | [ExportToCSV.getHeaders]  SQLException:  "+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("| Services Monitor | [ExportToCSV.getHeaders]  GeneralException:  "+sw.toString());
			}
			
			return columnNames;
		}
	   
	   
	   
	   /**
        * 
        * @param String query
        * @return CachedRowSetImpl
        * @author Movses Kiredjian
        * @since January 2009
        * @category Executes a sql select query and returns the result in a cachedRowSet
        * @exception SQLException
        * 
        */
	   public ResultSet executeNonQueryRS(String query)
		{
		   logger.info("Executing this query-->"+query);
			CachedRowSetImpl crs = null;
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				connect();
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				stmt.setFetchSize(Integer.MAX_VALUE);
				rs = stmt.executeQuery(query);
				rs.setFetchSize(Integer.MAX_VALUE);
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQueryRS-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQueryRS-->"+sw.toString());
			}
			
			    return rs;	
			
		}
	   
	   public int executeUpdateQuery(String query)
		{
		   logger.info("Executing this query-->"+query);
			CachedRowSetImpl crs = null;
			ResultSet rs;
			Statement stmt = null;
			int uId=0;
			try
			{
				connect();
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				uId=stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
				
				rs = stmt.getGeneratedKeys();
				crs.populate(rs);
			    if (crs.next()) {
			        uId = crs.getInt(1);
			    } 
   
			}
			catch(SQLException sqlEx) 
			{
				uId=-1;
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			}
			catch(Exception gEx)
			{				
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			}
			finally
			{
				if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
						sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			    	}
			    }
			    return uId;	
			}
		}
	   
	   public int executeUpdateQueryWithLogs(String query)
		{
 		    logger.info("Executing this query-->"+query);
			CachedRowSetImpl crs = null;
			ResultSet rs;
			Statement stmt = null;
			int uId=0;
			try
			{
				connect();
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
				
				rs = stmt.getGeneratedKeys();
				crs.populate(rs);
			    if (crs.next()) {
			        uId = crs.getInt(1);
			    } 
			    
			}
			catch(SQLException sqlEx)
			{
				uId=-1;
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
						sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
			    	}
			    }
			    return uId;	
			}
		}
	   
	   public int executeUpdateQueryWithoutKeys(String query)
		{
		   logger.info("Executing this query-->"+query);
			CachedRowSetImpl crs = null;
			ResultSet rs;
			Statement stmt = null;
			int uId=0;
			try
			{
				connect();
				crs = new CachedRowSetImpl();
				stmt = con.createStatement();
				uId = stmt.executeUpdate(query);			    
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQueryWithoutKeys-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeUpdateQueryWithoutKeys-->"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeUpdateQueryWithoutKeys-->"+sw.toString());
			    	}
			    }
			    return uId;	
			}
		}
	   
	   public boolean callProcedure(String proName,String[]parameters)
	   {
		   
		   boolean executedSuccesfully=false;
		   try
			{
				connect();
				CallableStatement cs;
				StringBuffer parmlist=new StringBuffer("(");
		        for(int i=0;i<parameters.length;i++)
		        {
		        	parmlist.append("?");
		        	parmlist.append(",");
		        }
		        parmlist.deleteCharAt(parmlist.length()-1);
		        parmlist.append(")");
				
		        logger.info("calling "+proName);
		        if(parameters!=null&&parameters.length>0)
		        {
		        	 cs = con.prepareCall("{call "+proName+parmlist.toString()+"}");
		        	for(int i=0;i<parameters.length;i++)
		        	{
		        		cs.setString((i+1), parameters[i]);
		        		//System.out.println( parameters[i]);
		        	}
		        }
		        else
		        {
		        	  cs = con.prepareCall("{call "+proName+"}");
		        }
		        
		        executedSuccesfully=cs.execute();		       		        
			}
		   
		   catch(SQLException sqlEx)
			{
			   sw = new StringWriter();
			   sqlEx.printStackTrace(new PrintWriter(sw));
			   logger.error("callProcedure SQL Exception "+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
			logger.error("callProcedure Exception"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("callProcedure "+sw.toString());
			    	}
			    }
			    return executedSuccesfully;
			}
		   
		   
	   }
	   
	   public int[] executeBatchQuery(Object []queries)
		{
			Statement stmt = null;
			int []updateCounts=null;
			//System.out.println("queries length is "+queries.length);
			try
			{
				connect();
				stmt = con.createStatement();
				for(int i=0;i<queries.length;i++)
				{
					stmt.addBatch(queries[i].toString());
//					logger.info("Executing this query-->"+queries[i]);
				}
				
				updateCounts=stmt.executeBatch();
				con.commit();
			    
			}
			catch(SQLException sqlEx)
			{
				con.rollback();
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
			    	}
			    }
			    return updateCounts;	
			}
		}
	   
	   public static String format(String str)
		{
			String formatted=str;
			CharSequence comma = ",";
			CharSequence dash = "-";
			if(str.contains(comma))
			formatted=str.replace(comma, dash);
			return formatted;
		} 
	   
}