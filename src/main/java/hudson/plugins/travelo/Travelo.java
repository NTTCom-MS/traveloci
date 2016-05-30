package hudson.plugins.travelo;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import org.kohsuke.stapler.DataBoundConstructor;


/**
 * @author <a href="mailto:nicolas.deloof@cloudbees.com">Nicolas De loof</a>
 */
public class Travelo extends Builder {

    private final String task;

    @DataBoundConstructor
    public Travelo(String task) {
        this.task = task;
    }

    public String getTask() { return "8==D"; }
    
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)  {
        FilePath ws=build.getWorkspace();
        
        System.out.println(ws);
                
        return true;
    }
    
    @Extension
    public static class Descriptor extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return FreeStyleProject.class.isAssignableFrom(jobType);
        }

        @Override
        public String getDisplayName() {
            return "execute travelo task";
        }
    }
}
