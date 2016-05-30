package hudson.plugins.travelo;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import org.kohsuke.stapler.DataBoundConstructor;

import org.yaml.snakeyaml.Yaml;


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
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        InputStream input;
        boolean ret=true;
        PrintStream logger = listener.getLogger();
        
        FilePath ws=build.getWorkspace();

        try
        {
            input = new FileInputStream(new File(ws+"/.travis.yml"));
        }
        catch(FileNotFoundException e)
        {
            //TODO: afegit info error
            logger.println("ERROR: .travis.yaml NOT found");
            return false;
        }
   
        Yaml yaml = new Yaml();
        Map<String, Object> travis = (Map<String, Object>) yaml.load(input);
        Map<String, Object> matrix = (Map<String, Object>) travis.get("matrix");
        ArrayList<Map<String, String>> include = (ArrayList<Map<String, String>>) matrix.get("include");
        
        // Loop through include list
	for (int i = 0; i < include.size(); i++) {
	    Map<String, String> job = include.get(i);
            
	    //System.out.println("Element: " + job);
            logger.println(" == JOB "+i+" ==");
            logger.println("env:" + job.get("env"));
            logger.println("script:" + job.get("script"));
                    
            //build.
            
            logger.println();
        }
                
        return ret;
    }
    
    @Extension
    public static class Descriptor extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return FreeStyleProject.class.isAssignableFrom(jobType);
        }

        @Override
        public String getDisplayName() {
            return "execute traveloci task";
        }
    }
}
