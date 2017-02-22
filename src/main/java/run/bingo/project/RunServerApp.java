package run.bingo.project;


import server.bingo.project.ServerTask;
/**
 * Created by incognito on 2016-12-02.
 */
public class RunServerApp {

    public static void main(String[] args) {

        System.out.println("Welcome :D");
        // server task thread
        ServerTask task = ServerTask.getInstance();
        Thread thread = new Thread(task);
        thread.start();
        
        // monitor thread (placeholder of logback)
        Thread testThread = new Thread(new TestTask());
        testThread.start();
        
//        UserVO user1 = new UserVO("abcd", "1234");
//        UserDAO.getInstance().insert(user1);
//        
//        UserVO user2 = new UserVO("efgh", "5678");
//        UserDAO.getInstance().insert(user2);
        
    }

}