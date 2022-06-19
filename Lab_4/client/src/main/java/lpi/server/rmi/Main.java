package lpi.server.rmi;

public class Main{
    public static void main(String[] args) {
        try {
            Connect client = new Connect(args);
            client.run(); 
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}