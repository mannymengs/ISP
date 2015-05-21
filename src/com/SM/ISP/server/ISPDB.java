
package com.SM.ISP.server;
import com.SM.ISP.shared.ISP;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author mannymengs
 * This class represents an ISP database with methods to manipulate and organize the ISP objects in the database
 */
public class ISPDB 
{
	private static final String connectionUrl = "jdbc:mysql://localhost:3306/LocalISPDB";
	private static final String connectionUser = "mannymengs";
	private static final String connectionPassword = "mannymengs";
	private static final String table = "mytable";
	private ArrayList<ISP> db = new ArrayList<ISP>();
	
	/**
	 * Constructor for ISPDB object 
	 */
	public ISPDB()
	{
		Connection con = null;
		Statement s = null;
		ResultSet rs = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);//connect to the database
			s = con.createStatement();
			rs = s.executeQuery("SELECT * FROM " + table);//pull every column from the data table
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		finally
		{
			//close resources
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (s != null) s.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}
		try 
		{
			//add every row to the array list of ISP objects
			while(rs.next())
			{
				db.add(new ISP(rs.getInt("id"), "1645", rs.getString("topic"), 
						rs.getString("student_first_name"), rs.getString("student_last_name"), 
						rs.getString("contact_title"), rs.getString("contact_first_name"), rs.getString("contact_last_name"), 
						rs.getString("org_line_1"), rs.getString("org_line_2"), rs.getString("org_address"), 
						rs.getString("org_city"), rs.getString("org_state"), rs.getString("org_zip"), 
						rs.getString("advisor_name"), rs.getString("contact_email"), rs.getString("contact_phone"), 
						rs.getString("abstract"), rs.getString("reflection")));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace(System.err);
		}
	}

	/**
	 * This method returns the array list of ISP objects in the database
	 * @return db the array list containing the projects
	 */
	public ArrayList<ISP> getDB()
	{
		return db;
	}

	/**
	 * This method sorts the array list of ISPs by first name
	 * @return byFirst the sorted array list of ISPs
	 */
	public ArrayList<ISP> byFirst()
	{
		//array list of string names formatted firstlast in the same order as the original database
		ArrayList<String> names = new ArrayList<String>();
		for(ISP p : db)
		{
			names.add(p.getSf() + p.getSl());
		}

		ArrayList<String> firstLast = new ArrayList<String>();//holds firstlast to be sorted
		ArrayList<ISP> byFirst = new ArrayList<ISP>();//to be filled with ordered ISP projects

		//fill firstLast with firstnamelastname
		for(int i = 0; i < db.size(); i++)
		{
			firstLast.add(db.get(i).getSf() + db.get(i).getSl());
		}
		Collections.sort(firstLast);//sort firstLast alphabetically

		//fill ordered array list by finding index of corresponding ordered name in original unordered database 
		//and adding ISP object from database at given index to ordered array list of ISP objects
		for(int i = 0; i < firstLast.size(); i++)
		{
			byFirst.add(db.get(names.indexOf(firstLast.get(i))));
		}

		db = byFirst;
		return db;
	}

	/**
	 * This method sorts the array list of ISPs by last name
	 * @return byLast the sorted array list of ISPs
	 */
	public ArrayList<ISP> byLast()
	{
		//array list of string names formatted lastfirst in the same order as the original database
		ArrayList<String> names = new ArrayList<String>();
		for(ISP p : db)
		{
			names.add(p.getSl() + p.getSf());
		}

		ArrayList<String> lastFirst = new ArrayList<String>();//holds lastfirst to be sorted
		ArrayList<ISP> byLast = new ArrayList<ISP>();//to be filled with ordered ISP projects

		//fill lastFirst with lastnamefirstname
		for(int i = 0; i < db.size(); i++)
		{
			lastFirst.add(db.get(i).getSl() + db.get(i).getSf());
		}
		Collections.sort(lastFirst);//sort firstLast alphabetically

		//fill ordered array list by finding index of corresponding ordered name in original unordered database 
		//and adding ISP object from database at given index to ordered array list of ISP objects
		for(int i = 0; i < lastFirst.size(); i++)
		{
			byLast.add(db.get(names.indexOf(lastFirst.get(i))));
		}

		db = byLast;
		return db;
	}

	public ArrayList<ISP> byTopic()
	{
		return db;
	}

	public ArrayList<ISP> byYear()
	{
		return db;
	}

	public ArrayList<ISP> search(String searchText)
	{
		ArrayList<ISP> containing = new ArrayList<ISP>();
		byFirst();
		
		for(ISP p : db)
		{
			if(p.toString().contains(searchText))
			{
				containing.add(p);
			}
		}
		return containing;
	}

	public void add(ISP p)
	{
		db.add(p);
	}
}
