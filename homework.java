import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;



public class inference {
	
	static int NO_OF_QUERIES;
	static int NO_OF_CLAUSES;
	static  Predicate_Object QUERIES[];
	static Hashtable<String, Predicates> predicates= new Hashtable<String, Predicates>() ;
	static HashSet<String> goals = new HashSet<String>();
	
	public static void main(String args[])
	{
		String filename=args[0];
		readFile(filename);
		String path1="output.txt";
		PrintWriter output=null;
		try {
			output=new PrintWriter(path1);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
		
		for(int i=0;i<QUERIES.length;i++)
		{
			
			if(ASK(QUERIES[i]))
			{
				output.println("TRUE");
				     
			}
			else
			{
				output.println("FALSE");
				            
			}
		}
		output.close();
		
	}
	private static boolean ASK(Predicate_Object predicate_Object) {
		// TODO Auto-generated method stub
		boolean answer;
		answer = backChainOR(predicate_Object);
		
		return answer;
	}





	private static boolean backChainOR(Predicate_Object predicate_Object) {
		// TODO Auto-generated method stub
		
		if(goals.contains(predicate_Object.toString()))
			return false;
		else
			goals.add(predicate_Object.toString());
		
		Predicates temp = predicates.get(predicate_Object.name);
		if(temp==null)
		{
			return false;
		}
		String facts_object[]=predicate_Object.name_of_parameters;
		for(String t[]:temp.facts)
		{
			if(Arrays.equals(facts_object, t))
			{
				if(goals.contains(predicate_Object.toString()))
					goals.remove(predicate_Object.toString());
				return true;
			}	
			
		}
		
		List<Predicate_Object> substitution = new ArrayList<Predicate_Object>();
		String object_parameters[]=predicate_Object.getName_of_parameters();
		
		for(String t[]:temp.facts)
		{
			//if(Arrays.equals(facts_object, t))
			
			// variable constant
			boolean flag_to_check=true;
			for(int k=0;k<t.length;k++)
			{
				
			if(!isVariable(object_parameters[k]))
				{
					if(!object_parameters[k].equals(t[k]))
					{
						// checking if constants r same or no
						flag_to_check=false;
						break;
					}
				}
						
			}
			if(flag_to_check)
				substitution.add(new Predicate_Object(predicate_Object.name,t.length,t));
			
		}
		for(Predicate_Object p : substitution){
			if(backChainOR(p))
				return true;
		}
		
		for(Predicate_Object [] p :temp.rules )
		{
			if(backChainAND(p,predicate_Object))
				return true;
		}
		return false;
	}

	private static Predicate_Object TransferTheParameters(Predicate_Object main_object, Predicate_Object k,
			Predicate_Object predicate_Object) {
		// TODO Auto-generated method stub
		Predicate_Object ret = new Predicate_Object(k);
		String main_object_param[]=main_object.getName_of_parameters();
		String k_param[]=ret.getName_of_parameters();
		String predicate_object_param[]=predicate_Object.getName_of_parameters();
		for(int i=0;i<main_object_param.length;i++)
		{
			String temp=main_object_param[i];
			if(!isVariable(temp))
				continue;
			for(int j=0;j<k_param.length;j++)
			{
				if(k_param[j].equals(temp)&& !isVariable(predicate_object_param[i]))
				{
					k_param[j]=predicate_object_param[i];
				}
			}
		}
		return ret;
		
	}




	private static boolean isVariable(String temp) {
		// TODO Auto-generated method stub
		
		char a = temp.charAt(0);
		if(a>= 'a' && a<='z')
			return true;
		return false;
	}

	private static boolean backChainAND(Predicate_Object[] p, Predicate_Object predicate_Object) {
		// TODO Auto-generated method stub
			
			for(int i=1;i<p.length;i++)
			{
				Predicate_Object substituted = TransferTheParameters(p[0],p[i],predicate_Object);
				if(!backChainOR(substituted))
					return false;
			}
		return true;
	}





	private static void readFile(String filename) {
		// TODO Auto-generated method stub
	  	  String line = null;
	 	  FileReader fileReader;
		try {
			fileReader = new FileReader(filename);
			BufferedReader bufferedReader =new BufferedReader(fileReader);
			line = bufferedReader.readLine();
			NO_OF_QUERIES=Integer.parseInt(line);
			QUERIES=new Predicate_Object[NO_OF_QUERIES];
			
			for(int i=0;i<NO_OF_QUERIES;i++)
			{
				QUERIES[i]= new Predicate_Object();
				line=bufferedReader.readLine();
				
				QUERIES[i].setName(findTheName(line));
				QUERIES[i].setNo_of_parameters(findTheNoOfParameter(line));
				line=line.substring(line.indexOf("(")+1,line.indexOf(")"));
				line=line.replaceAll("\\s+", "");
				QUERIES[i].setName_of_parameters(line.split("\\,"));
				
			}
			
			
			NO_OF_CLAUSES=Integer.parseInt(bufferedReader.readLine());
			
			for(int i=0;i<NO_OF_CLAUSES;i++)
			{
				Predicates temp = new Predicates();
				line=bufferedReader.readLine();
				          
				String name;
				int no_of_param;
				String line2[],line3;
				int flag=0;
				if(!line.contains("=>"))
				{
					 name=findTheName(line);
					 
					 no_of_param=findTheNoOfParameter(line);
					 line=line.substring(line.indexOf("(")+1,line.indexOf(")"));
					 line=line.replaceAll("\\s+","");
					 line2=line.split("\\,");
					 flag=1;
					
					 
				}
				else
				{
					//line 3 is for after implication sign
					String line1[]=line.split("\\s+");
					name=findTheName(line1[line1.length-1]);
					no_of_param=findTheNoOfParameter(line1[line1.length-1]);
					line=line.replaceAll("\\s+","");
					line3=line.substring(line.indexOf("=>")+2);
					
					line=line.substring(0,line.indexOf("=>"));
					line=line3+"^"+line;
					line2=line.split("\\^");
					flag=2;
					
				}
				
				
				
				if(!predicates.containsKey(name))
				{
					temp.setName(name);
					temp.setNo_of_parameters(no_of_param);
					if(flag==1)
					temp.setFacts(line2);
					else
						temp.setRules(line2);
					predicates.put(name, temp);
				}
					
				else
				{
					Predicates pp=predicates.get(name);
					if(line2.length!=0)
					if(flag==1)
						pp.setFacts(line2);
						else
							pp.setRules(line2);
					
				}
				
				
			}
	     
	        bufferedReader.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
		
	}

	public static int findTheNoOfParameter(String line) {
		// TODO Auto-generated method stub
		String t=line.substring(line.indexOf("("), line.indexOf(")"));
		
		return t.split(",").length;
	}

	public static String findTheName(String line) {
		// TODO Auto-generated method stub
		String t[]=line.split("\\(");
		return t[0];
	}
}


class Predicates 
{
	private String name;
	private int no_of_parameters;
	ArrayList<Predicate_Object[]> rules = new ArrayList<Predicate_Object[]>();
	ArrayList<String[]> facts = new ArrayList<String[]> ();
	
	public Predicates() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void setRules(String[] l) {
		
		Predicate_Object po[]=new Predicate_Object[l.length];
		
		for(int i=0;i<l.length;i++)
		{
			po[i]=new Predicate_Object();
			po[i].setName(inference.findTheName(l[i]));
		
			po[i].setNo_of_parameters(inference.findTheNoOfParameter(l[i]));
			l[i]=l[i].substring(l[i].indexOf("(")+1,l[i].indexOf(")"));
			po[i].setName_of_parameters(l[i].split("\\,"));
			
		}
		rules.add(po);
		
	}

	public void setFacts(String[] l) {
		facts.add(l);
	}

	public int getNo_of_parameters() {
		return no_of_parameters;
	}
	
	public void setNo_of_parameters(int no_of_parameters) {
		this.no_of_parameters = no_of_parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
	}
	
	public int hashCode(){
        
        int hashcode = 0;
        hashcode = 10*20;
        
        hashcode += name.hashCode();
        return hashcode;
	}
	
	@Override
	public boolean equals(Object d){ 
	    if (!(d instanceof Predicates)) {
	        return false;
	    }
	   Predicates pd = (Predicates) d;
	   
	    return this.getName().equals(pd.getName());
	}
	
}

 class Predicate_Object {
	
	String name;
	int no_of_parameters;
	String name_of_parameters[];
	List<HashMap<String, String>> unification = new ArrayList<HashMap<String, String>>();
	
	public Predicate_Object(Predicate_Object p) {
		super();
		this.name = p.name;
		this.no_of_parameters = p.no_of_parameters;
		this.name_of_parameters = p.name_of_parameters.clone();
	}

	public Predicate_Object() {
		// TODO Auto-generated constructor stub
	}

	

	public Predicate_Object(String name2, int length, String[] t) {
		// TODO Auto-generated constructor stub
		
		this.name = name2;
		this.no_of_parameters = length;
		this.name_of_parameters = t.clone();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNo_of_parameters() {
		return no_of_parameters;
	}
	public void setNo_of_parameters(int no_of_parameters) {
		this.no_of_parameters = no_of_parameters;
	}
	public String[] getName_of_parameters() {
		return name_of_parameters;
	}
	public void setName_of_parameters(String[] name_of_parameters) {
		this.name_of_parameters = name_of_parameters;
	}
	
	public String toString()
	{
		
		return name+" "+Arrays.toString(name_of_parameters);
		
	}
	

}