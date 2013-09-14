package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
	private String name;
	//private String sex;
	private String password;
	//private String email;
	public User() {
		super();
	}
	public User(String name,String password) {
		super();
		setName(name);
		setPassword(password);
	}
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass()==this.getClass()){
			User user=(User)obj;
			if(this.name.equals(user.name)){
				return true;
			}//Check if there exist user that has same username
		}
		return false;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	@Override
	/*public String toString() {
		return name+","+sex+","+password+","+email+"\n";
	}*/
	public String toString() {
		return name+","+password+"\n";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 

	
	/**
		Check if the User already exist
	 **/
	static User check(User user){
		User userChecked=null;
		//Get registered info
		boolean flag=false;//if registered
		List<User> list=new ArrayList<User>();//Store all accounts			
		String infomation="";//Store the content in login.txt
		FileInputStream file=null;
		try {
			file = new FileInputStream("login.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] content=new byte[20];				
		while(true){
			int num=0;
			try {
				num = file.read(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(num==-1){
				break;
			}
			String temp=new String(content,0,num);
			infomation+=temp;
		}
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
		if(infomation.length()==0)
		{
		}
		else
		{String[] accounts=(infomation.substring(0, infomation.length()-1)).split("\n");
		User[] account=new User[accounts.length];
		for(int i=0;i<accounts.length;i++){
			String[] info=accounts[i].split(",");
			account[i]=new User(info[0],info[1]);
		list.add(account[i]);
		}				
		for(User human:list){
			if(user.getName().equals(human.getName())){
				userChecked=human;
			}//Based on username
		}
		
		}//all users info
		
		return userChecked;
	}
	/**
	 * User Test
	 */
	public static void main(String[] args){
		List<User> list=new ArrayList<User>();
		User user=new User("ggg","ggg");
		System.out.println("Search Result:"+User.check(user));
	}
}