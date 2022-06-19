## RMI Client - Server Network ##
RMI Server was provided by teacher and it is located in "server" directory. RMI client is located in "client" directory.
To launch this application correctly, follow these instructions.
1. Firstly, you have to run server application. You can do this from a command line with a command  `mvn clean compile exec:java -D exec.args="4000"`
from server directory, or just click on file `start.cmd` in your server directory.
2. Secondly, you have to run client application. Do this from a command line with a command `mvn clean compile exec:java -D exec.args="127.0.0.1 4000"` from client directory.
3. You are now connected to the server and can use the following commands:
    + `exit`(you will be disconnected from server)
    + `Login & password`(create user account with login and password)
    + `echo`(test massage)
    + `ping`(test connection)
4. Commands `login & password, echo` have parameters, so application will ask you about them, when you input command.
