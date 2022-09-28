package com.GCash_GGivesScripts;
import org.testng.annotations.Test;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.utility.JIRAResult_Update;

public class JIRATest {

	@Test()
	public void jirs() throws UnirestException {
		JIRAResult_Update.updateTaskStatus("PP-1","41"); //11-Backlog, 21-Dev, 31- In Progress, 41-Done
		JIRAResult_Update.updateTaskComment("PP-1","Test Passed hence closing the task");
	}
}
