package qa114.utility;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.awaitility.Awaitility.*;

import org.awaitility.Durations;
import org.awaitility.pollinterval.FibonacciPollInterval;
import org.hamcrest.Matchers;
import org.testfx.api.FxRobot;
import org.testfx.service.support.Capture;
import org.testfx.util.DebugUtils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import qa114.runapplication.NewRegistrationAdultTest;
import org.apache.log4j.BasicConfigurator;  
import org.apache.log4j.LogManager;  
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.assertj.core.internal.bytebuddy.matcher.VisibilityMatcher;
import org.testfx.util.WaitForAsyncUtils;


/**
 * 
 * @author Neeharika.Garg
 *@References https://www.codota.com/code/java/classes/org.awaitility.Awaitility
 */
public class WaitsUtil {
	private static final Logger logger = LogManager.getLogger(WaitsUtil.class);  

	Node node;
	FxRobot robot;

	public WaitsUtil(FxRobot robot) {
		this.robot=robot;
	}

	public <T extends Node> T lookupById( String controlId) {
		 try {
				with()
				.pollInSameThread()
				.pollDelay(1,  TimeUnit.SECONDS)
				.await()
				.atMost(60, TimeUnit.SECONDS)
			.until(() -> robot.lookup(controlId).query() != null);
	  }catch(Exception e)
		{logger.error(e.getMessage());
		Rectangle2D r=new Rectangle2D(0, 0, 600, 700);
		Capture c=robot.capture(r);
		c.getImage();
		}
		return robot.lookup(controlId).query();

	}


	public void clickNodeAssert(String id)
	{	try {
		node=lookupById(id);
		robot.clickOn(node);
		assertNotNull(node," "+id+" is not present");
	}catch(Exception e)
	{logger.error(e.getMessage());
	Rectangle2D r=new Rectangle2D(0, 0, 600, 700);
	Capture c=robot.capture(r);
	c.getImage();


	}

	}



	public <T extends Node> TextField lookupByIdTextField( String controlId,FxRobot robot) {
				try {
			with()
			.dontCatchUncaughtExceptions()
			.await()
			.pollDelay(2,  TimeUnit.SECONDS)
			.atMost(60, TimeUnit.SECONDS) // Read config 
			.until(() -> (robot.lookup(controlId).queryAs(TextField.class))!= null);
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			Rectangle2D r=new Rectangle2D(0, 0, 600, 700);
			Capture c=robot.capture(r);
			c.getImage();


		}

		return robot.lookup(controlId).queryAs(TextField.class);

	}


	public <T extends Node> Button lookupByIdButton( String controlId,FxRobot robot) {
		try {
			
			with().pollInSameThread()
			.await()
			.atMost(60, TimeUnit.SECONDS)
			.until(() -> (robot.lookup(controlId).queryAs(Button.class))!= null);
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			Rectangle2D r=new Rectangle2D(0, 0, 600, 700);
			Capture c=robot.capture(r);
			c.getImage();


		}

		return robot.lookup(controlId).queryAs(Button.class);

	}


}