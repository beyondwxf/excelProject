import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		
		sss();
//		
	}
	
	public static void sss (){
		String str="2016-01-13 10:09:24";
//		String regEx="[^0-9]"; 
//		Pattern p = Pattern.compile(regEx); 
//		Matcher m = p.matcher(str); 
//		String str=m.replaceAll("").trim().toString().substring(0,8);
		
		
//		String reg = "\\D+(\\d+)$";    //提取字符串末尾的数字：封妖塔守卫71 == >> 71  
		String reg = "^\\d+";
		String s = "2055555asdasww88dasd000";  
		Pattern p2 = Pattern.compile(reg);  
		Matcher m2 = p2.matcher(s);  
//		int historyHighestLevel = 1;
		if(m2.find()){  
//		    historyHighestLevel = Integer.parseInt(m2.group(0));
		    System.out.println(m2.group(0));  // 组提取字符串  
		}
	}
}
