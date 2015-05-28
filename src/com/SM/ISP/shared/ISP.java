package com.SM.ISP.shared;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.*;

/**
 * @author mannymengs
 * This class simulates an ISP entry in the database.
 */

public class ISP implements IsSerializable, Serializable //implements Comparable<ISP>
{
	protected static final ArrayList<String> topics = new ArrayList<String>();
	
	private int ID;
	private String sf, sl, ct, cf, cl, o1, o2, oA, oC, oS, oZ, an, ce, cp, a, r;
	private String t;
	private String y;
	
	//required for RPC
	public ISP(){}
	
	/**
	 * Constructor for ISP object
	 * @param id the ID number of the ISP (use of this data is tbd by future administrator)
	 * @param year the graduation year of the student (to be added into the database table)
	 * @param topic the topic of the ISP
	 * @param studentFirst the first name of the student
	 * @param studentLast the last name of the student
	 * @param contactTitle the title of the contact (Mr., Dr., Prof., etc.)
	 * @param contactFirst the first name of the contact
	 * @param contactLast the last name of the contact
	 * @param orgLine1 the name of the first organization with which the student is involved
	 * @param orgLine2 the name of the second organization with which the student is involved
	 * @param orgAddress the address of the primary organization
	 * @param orgCity the city in which the primary organization is located
	 * @param orgState the state in which the primary organization is located
	 * @param orgZip the zip code of the primary organization
	 * @param advisorName the name of the advisor
	 * @param contactEmail the email of the advisor
	 * @param contactPhone the phone number of the advisor
	 * @param abstraction the abstract written by the student
	 * @param reflection the reflection written by the student
	 */
	public ISP(int id, String year, String topic, String studentFirst, String studentLast, 
			String contactTitle, String contactFirst, String contactLast, 
			String orgLine1, String orgLine2, 
			String orgAddress, String orgCity, String orgState, String orgZip,
			String advisorName, String contactEmail, String contactPhone,
			String abstraction, String reflection)
	{
		ID = id;
		y = year;
		t = topic;
		sf = studentFirst;
		sl = studentLast;
		ct = contactTitle;
		cf = contactFirst;
		cl = contactLast;
		o1 = orgLine1;
		o2 = orgLine2;
		oA = orgAddress;
		oC = orgCity;
		oS = orgState;
		oZ = orgZip;
		an = advisorName;
		ce = contactEmail;
		cp = contactPhone;
		a = abstraction;
		r = reflection;
		if(!topics.contains(t))
		{
			topics.add(t);
			Collections.sort(topics);
		}
	}
	
	/**
	 * This method formats the ISP entry into a string which can be displayed on the page
	 * @return display the display string holding the formatted ISP entry
	 */
	public String toString()
	{
		String display = "";
		display += "Student: " + sf + " " + sl + "\n";
		display += "Topic: " + t + "\n";
		display += "Primary Organization: " + o1 + "\n";
		if(o2 != null || o2.length() > 0)
			display += "Secondary Organization: " + o2 + "\n";
		display += "Address: " + oA + "\n" + oC + ", " + oS + " " + oZ + "\n";
		display += "On-Campus Advisor: " + an + "\n";
		display += "Off-Campus Advisor: " + ct + " " + cf + " " + cl + "\n";
		display += "Off-Campus Advisor Email: " + ce + "\n";
		display += "Off-Campus Advisor Phone #: " + cp + "\n";
		display += "Abstract:\n" + a + "\n";
		display += "Reflection:\n" + r + "\n";
		return display;
	}

	/**
	 * This method returns the ID number of the ISP
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * This method sets the ID number of the ISP
	 * @param ID the ID to set
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * This method returns the graduation year of the student who did the ISP
	 * @return the y
	 */
	public String getYear() {
		return y;
	}

	/**
	 * This method sets the year of the ISP
	 * @param y the y to set
	 */
	public void setYear(String y) {
		this.y = y;
	}

	/**
	 * This method returns the student's first name
	 * @return the sf
	 */
	public String getSf() {
		return sf;
	}

	/**
	 * This method sets the students first name
	 * @param sf the sf to set
	 */
	public void setSf(String sf) {
		this.sf = sf;
	}

	/**
	 * @return the sl
	 */
	public String getSl() {
		return sl;
	}

	/**
	 * @param sl the sl to set
	 */
	public void setSl(String sl) {
		this.sl = sl;
	}

	/**
	 * @return the ct
	 */
	public String getCt() {
		return ct;
	}

	/**
	 * @param ct the ct to set
	 */
	public void setCt(String ct) {
		this.ct = ct;
	}

	/**
	 * @return the cf
	 */
	public String getCf() {
		return cf;
	}

	/**
	 * @param cf the cf to set
	 */
	public void setCf(String cf) {
		this.cf = cf;
	}

	/**
	 * @return the cl
	 */
	public String getCl() {
		return cl;
	}

	/**
	 * @param cl the cl to set
	 */
	public void setCl(String cl) {
		this.cl = cl;
	}

	/**
	 * @return the o1
	 */
	public String getO1() {
		return o1;
	}

	/**
	 * @param o1 the o1 to set
	 */
	public void setO1(String o1) {
		this.o1 = o1;
	}

	/**
	 * @return the o2
	 */
	public String getO2() {
		return o2;
	}

	/**
	 * @param o2 the o2 to set
	 */
	public void setO2(String o2) {
		this.o2 = o2;
	}

	/**
	 * @return the oA
	 */
	public String getoA() {
		return oA;
	}

	/**
	 * @param oA the oA to set
	 */
	public void setoA(String oA) {
		this.oA = oA;
	}

	/**
	 * @return the oC
	 */
	public String getoC() {
		return oC;
	}

	/**
	 * @param oC the oC to set
	 */
	public void setoC(String oC) {
		this.oC = oC;
	}

	/**
	 * @return the oS
	 */
	public String getoS() {
		return oS;
	}

	/**
	 * @param oS the oS to set
	 */
	public void setoS(String oS) {
		this.oS = oS;
	}

	/**
	 * @return the oZ
	 */
	public String getoZ() {
		return oZ;
	}

	/**
	 * @param oZ the oZ to set
	 */
	public void setoZ(String oZ) {
		this.oZ = oZ;
	}

	/**
	 * @return the an
	 */
	public String getAn() {
		return an;
	}

	/**
	 * @param an the an to set
	 */
	public void setAn(String an) {
		this.an = an;
	}

	/**
	 * @return the ce
	 */
	public String getCe() {
		return ce;
	}

	/**
	 * @param ce the ce to set
	 */
	public void setCe(String ce) {
		this.ce = ce;
	}

	/**
	 * @return the cp
	 */
	public String getCp() {
		return cp;
	}

	/**
	 * @param cp the cp to set
	 */
	public void setCp(String cp) {
		this.cp = cp;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(String a) {
		this.a = a;
	}

	/**
	 * @return the r
	 */
	public String getR() {
		return r;
	}

	/**
	 * @param r the r to set
	 */
	public void setR(String r) {
		this.r = r;
	}

	/**
	 * @return the t
	 */
	public String getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(String t) {
		this.t = t;
	}

	/**
	 * @return the topics
	 */
	public static ArrayList<String> getTopics() {
		return topics;
	}
	
}
