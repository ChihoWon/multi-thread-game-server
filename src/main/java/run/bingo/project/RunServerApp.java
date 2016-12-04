package run.bingo.project;


import server.bingo.project.ServerTask;
/**
 * Created by incognito on 2016-12-02.
 */
public class RunServerApp {

    public static void main(String[] args) {

        System.out.println("Welcome :D");
        ServerTask task = ServerTask.getInstance();
        Thread thread = new Thread(task);
        thread.start();

    }

}
