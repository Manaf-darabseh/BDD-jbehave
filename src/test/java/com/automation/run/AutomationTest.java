package com.automation.run;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.PendingStepStrategy;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.junit.experimental.categories.Category;
import org.springframework.context.ApplicationContext;

import com.automation.tools.drivers.DriversResources;
import com.automation.utils.UtilProperties;



/**
 * Class which provides the link between the JBehave's executor framework
 * (called Embedder) and the textual stories.
 */

// @RunWith(AnnotatedEmbedderRunner.class)
@Category(com.automation.run.AutomationTest.class)

public class AutomationTest extends JUnitStories {
	public static String OS = System.getProperty("os.name");

	PendingStepStrategy pendingStepStrategy = new FailingUponPendingStep();
	
	ContextView contextView = new LocalFrameContextView().sized(640, 80).located(10, 10);
	SeleniumContext seleniumContext = new SeleniumContext();

	StoryReporterBuilder reporterBuilder = new StoryReporterBuilder().withFailureTrace(true)
			.withFailureTraceCompression(true).withDefaultFormats();

	@Override
	public Configuration configuration() {
		return new SeleniumConfiguration().useSeleniumContext(seleniumContext)
				.usePendingStepStrategy(pendingStepStrategy)
				.useStoryControls(new StoryControls().doResetStateBeforeScenario(true))
				.useStoryLoader(new LoadFromClasspath(AutomationTest.class)).useStoryReporterBuilder(reporterBuilder);
	}

	public static ApplicationContext context = null;

	@Override
	public InjectableStepsFactory stepsFactory() {
		context = new SpringApplicationContextFactory("steps.xml").createApplicationContext();
		return new SpringStepsFactory(configuration(), context);
	}

	@Override
	protected List<String> storyPaths() {

		String storiesPath = null;
		String URL = codeLocationFromClass(this.getClass()).getFile().toString();
		try {
			storiesPath = URLDecoder.decode(URL, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new StoryFinder().findPaths(storiesPath,
				asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);

	}

	

	

	public void startStories(Embedder embedder) {
		try {
			embedder.runStoriesAsPaths(storyPaths());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			embedder.generateCrossReference();
		}
	}

	@Override
	public void run() {
		System.out.print(OS);
		Embedder embedder = configuredEmbedder();
		embedder.useMetaFilters(getMetaData());
		embedder.embedderControls().useStoryTimeoutInSecs(600);
		DateFormat df = new SimpleDateFormat("ddMMyyHHmm");
		Calendar calobj = Calendar.getInstance();
		System.out.println(df.format(calobj.getTime()));
		
		embedder.systemProperties().setProperty("browser", "chrome");
		embedder.systemProperties().setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + DriversResources.getDriverFilename());

		startStories(embedder);

	}

	
	public List<String> getMetaData() {
		String metaData = System.getProperty("metaData");
		if (metaData == null) {
			metaData = UtilProperties.getInstance().getProperty("metaData");
		}
		ArrayList<String> metaDataList = new ArrayList<String>();
		if (metaData != null && !metaData.isEmpty()) {
			metaDataList.addAll(asList(metaData.split(",")));
		}
		metaDataList.add("-skip");
		return metaDataList;
	}

	public static void skipScenariosList(Embedder embedder) {

		ArrayList<Scenario> skippedScenarioByStroy = null;
		ArrayList<Scenario> skipScenarios = new ArrayList<Scenario>();

		StoryManager storyManager = embedder.storyManager();
		List<String> stories = new StoryFinder().findPaths(codeLocationFromClass(AutomationTest.class).getFile(),
				asList("**/" + System.getProperty("storyFilter", "*") + ".story"), null);
		for (String storyPath : stories) {
			Story story = storyManager.storyOfPath(storyPath);

			if (new MetaFilter("skip").allow(story.getMeta())) {
				skippedScenarioByStroy = new ArrayList<Scenario>();

				for (Scenario scenario : story.getScenarios()) {
					// scenario also inherits meta from story
					Meta inherited = scenario.getMeta().inheritFrom(story.getMeta());
					if (new MetaFilter("+skip").allow(inherited)) {

						skippedScenarioByStroy.add(scenario);
						System.out.println("this is skipped:" + scenario.getTitle());
						skipScenarios.add(scenario);

					}
				}
			}
		}

	}

}