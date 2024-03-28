package uj.wmii.pwj.introduction;
public class HelloWorld {
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("No input parameters provided");
        }else {
            for (String arg : args)
                System.out.println(arg);
        }
    }
}
