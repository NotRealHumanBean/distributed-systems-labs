package lpi.server.rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


class CommandHandler{
    private IServer proxy; //object class of IServer
    private String loginId; //login id

    private Timer timer; //object class of Timer

    CommandHandler(IServer proxy){ //constructor of class CommandHandler
        this.proxy = proxy;
    }

    void main() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  //input buffer stream from user

        boolean ifLoop = true;
        while (ifLoop) { //loop for entering command
            System.out.println("\nEnter the command: ");
            String command = br.readLine(); //read command of user

            switch (command){
                case "exit": //complete client execution
                    ifLoop = false;
                    try{
                        proxy.exit(loginId); //call the method exit of class IServer
                        timer.cancel(); //cancel thread of timer
                    } catch (IOException ignored) {}
                    break;

                case "ping": //command ping

                    try{
                        proxy.ping(); //call the method ping of class IServer
                        System.out.println(" Ping successful.");
                    } catch(RemoteException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case "echo": //command echo

                    System.out.print(" Enter the echo text: ");
                    String echoText = br.readLine(); //read echo text from user

                    try{
                        String echoResponse = proxy.echo(echoText); //call the method echo of class IServer
                        System.out.println("Received from server: " + echoResponse);
                    } catch(RemoteException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;

                case "login": //command login

                    System.out.print(" Enter the login: ");
                    String loginName = br.readLine(); //read login from user
                    System.out.print(" Enter the password: ");
                    String loginPass = br.readLine(); //read password from user

                    try{
                        String loginResponse = proxy.login(loginName, loginPass); //call the method login of class IServer
                        if (loginResponse != null){ //successful login
                            System.out.println(" Successful login");
                            this.loginId = loginResponse;

                            ReceiveTimer receive = new ReceiveTimer(this.proxy, this.loginId); //create object of class ReceiveTimer
                            timer = new Timer(true); //init timer daemon
                            timer.scheduleAtFixedRate(receive, 0, 2000); //set frequency  for thread
                        }
                    } catch(RemoteException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
            }
        }
    }
}

class ReceiveTimer extends TimerTask  {
    private IServer proxy;//object class of IServer
    private String loginId; //login id
    public static IServer.FileInfo[] recFile = new IServer.FileInfo[25]; //list of object IServer.FileInfo
    public static IServer.Message[] recMsg = new IServer.Message[50]; //list of object IServer.Message
    private static ArrayList<String> current = new ArrayList<>(); //array list of current user
    private static ArrayList <String> old = new ArrayList<>(); //array list of old user
    private static int count = 0;
    ReceiveTimer(IServer proxy, String loginId){ //constructor
        this.proxy = proxy;
        this.loginId = loginId;
    }
    @Override
    public void run() throws NullPointerException { //run method of timer
        //check new or log out user
        String[] listUsers = null;
        try{
            listUsers = proxy.listUsers(loginId);
        } catch(RemoteException ex) {
            System.out.println("This command requires login first.");
            System.out.println(ex.getMessage());
        }
        if (count == 0){ //record users to array list on first iteration
            assert listUsers != null;
            for(String i: listUsers){
                current.add(i);
                old.add(i);
            }
        } else { //second and subsequent iterations
            current.clear();
            assert listUsers != null;
            Collections.addAll(current, listUsers);

            if(current.size() > old.size()){ //new user
                current.removeAll(old);
                System.out.println("The user " + current.get(0) + " connected to the server.");
                current.clear(); //clear array list
                old.clear();
                for(String i: listUsers){
                    current.add(i);
                    old.add(i);
                }
            } else if (current.size() < old.size()){ //logout user
                old.removeAll(current);
                System.out.println("The user " + old.get(0) + " disconnected from server ");
                old.clear(); //clear array list
                current.clear();
                for(String i: listUsers) {
                    current.add(i);
                    old.add(i);
                }
            }
        }
        if (count == 0) //detect first iteration
            count++;
    }
}
