import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//import org.junit.runner.Runwith;
@RunWith(Suite.class)
@SuiteClasses({requestTest.class,RqstTrainTest.class,ElevSubSysTest.class,SchedulerTest.class})
public class TestAll {

}
