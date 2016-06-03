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
import java.util.List;
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

    public String getTask() { return this.task; }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
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
        
        
        List<TraveloSubJob> subjobs = new ArrayList<TraveloSubJob>();
        
        // Loop through include list
	for (int i = 0; i < include.size(); i++) {
            subjobs.add(new TraveloSubJob(include.get(i)));
        }
        
        if(include.size()!=subjobs.size())
        {
            logger.println("assertion FAILED: include.size()!=subjobs.size()");
            return false;
        }
        
        for (int i = 0; i < subjobs.size(); i++) {
            if(!subjobs.get(i).launchjob(build, launcher, listener))
            {
                logger.println("ERROR: failed to launch job "+i);
                logger.println(include.get(i).toString());
            }
        }
        
        
        boolean childsrunning=false;
        do
        {
            childsrunning=false;
            for (int i = 0; i < subjobs.size(); i++) {
                TraveloSubJob subjob=subjobs.get(i);
                
                subjob.flushOutput();
                
                if(subjob.isRunning())
                    childsrunning=true;
                else
                    if(subjob.getReturnCode()!=0)
                        ret=false;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                logger.println("ERROR: failed to sleep: ");
                logger.println(ex.toString());
            }
        }
        while(childsrunning);
        
        for (int i = 0; i < subjobs.size(); i++) {
            logger.println(" == JOB "+i+" ==");
            logger.println(" Command :"+subjobs.get(i).getLastCommand());
            logger.println(" return code: "+subjobs.get(i).getReturnCode());
            logger.println(" Job status: "+(subjobs.get(i).getReturnCode()==0?"SUCCESS":"FAILED"));
            logger.println();
        }
        
        for (int i = 0; i < subjobs.size(); i++) {
            logger.println(" #### JOB "+i+" ####");
            logger.println(" OUTPUT: ");
            logger.println();
            logger.println(subjobs.get(i).getOutput());
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
