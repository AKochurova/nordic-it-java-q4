package sql.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Palindrome {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str1 = br.readLine();

        System.out.println(fun(str1));


    }

    public static boolean fun(String s) {


        if (s.charAt(0) != s.charAt(s.length() - 1)) {
            return false;
        }
        fun(s.substring(1, s.length() - 2));

        return true;


    }
}
