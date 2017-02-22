package run.bingo.project;

import server.bingo.project.SenderObject;

public class TestTask implements Runnable {

	// monitor thread
	public TestTask() {
		System.out.println("[!] TestTask has been created.");
	}
	
	@Override
	public void run() {
		System.out.println("[!] Monitor thread has begun.");
		while(true) {
			try {
				SenderObject.getInstance().printAllList();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
