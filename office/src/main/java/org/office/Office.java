package org.office;

import java.util.Scanner;

public class Office {

    public static void main(String[] args) {
        Service service = new Service("jdbc:h2:.\\Office");

        Option opt=Option.AddDepartment;
        Scanner sc = new Scanner(System.in);
        
        while(!opt.equals(Option.EXIT)){
            System.out.println("Введите число:");
            for(Option o: Option.values()) System.out.println(o.getText());
            opt=Option.values()[sc.nextInt()];    
            opt.action(service);
        }        
    }
}
