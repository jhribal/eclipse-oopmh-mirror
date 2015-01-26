package org.eclipse.oomph.setup.git.impl;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

@SuppressWarnings("restriction")
public class GitCloneTaskImplTest {
	private GitCloneTaskImpl cloneTask = new GitCloneTaskImpl();
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	@Ignore("Works only locally for given Git URL and authentiction enabled")
	public void testGitCloneTask() throws Exception {
		String newRemoteURI = "https://de.abg.reichert.joerg@googlemail.com@github.com/joergreichert/JRScalaPlayground";
		cloneTask.setCheckoutBranch("master");
		cloneTask.setLocation(folder.getRoot().getAbsolutePath() + "/local_clone");
		cloneTask.setRemoteURI(newRemoteURI);
		ResourceSetImpl rs = new ResourceSetImpl();
		URIConverter uriConverter = rs.getURIConverter();
		SetupPrompter prompter = SetupPrompter.CANCEL;
		Trigger trigger = Trigger.MANUAL;
		SetupContext setupContext = SetupContext.create();
		Stream stream = SetupFactory.eINSTANCE.createStream();
		stream.setName("master");
		SetupTaskContext context = new SetupTaskPerformer(uriConverter,
				prompter, trigger, setupContext, stream);
		cloneTask.isNeeded(context);
		cloneTask.perform(context);
	}
}
