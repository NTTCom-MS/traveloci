package hudson.plugins.travelo;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    @SuppressWarnings("SleepWhileInLoop")
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        InputStream input;
        boolean ret=true;
        Proc child=null;
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
            
            Long startTime = System.currentTimeMillis();
                    
            //build
            try {
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                StreamBuildListener sbl = new StreamBuildListener(baos);
                
                ArgumentListBuilder args = new ArgumentListBuilder();
                args.add("/bin/bash");
                args.add("-c");
                args.add(job.get("script"));
                

                child = launcher.decorateFor(build.getBuiltOn()).launch()
                  .cmds(args).envs(job.get("env")).stdout(sbl).stderr(baos).pwd(build.getWorkspace()).start();
                
                while (child.isAlive()) {
                    baos.flush();
                    String s = baos.toString();
                    baos.reset();

                    listener.getLogger().print(s);
                    listener.getLogger().flush();
                    
                    Thread.sleep(2);
                }
                
                ret = child.join() == 0;
            }
            catch(IOException e) {
                logger.println("IOExcetion - WTF?");
                e.printStackTrace(logger);
                ret=false;
                if(child!=null)
                {
                    try {
                            child.kill();
                    } catch (IOException ex) {
                        logger.println("inception IOExcetion");
                    } catch (InterruptedException ex) {
                        logger.println("1 exception throws an exception, 2 exceptions throws an exception, 3 exceptions...");
                    }
                }

            }
            catch(InterruptedException e) {
                logger.println("InterruptedException - user aboeted?");
                e.printStackTrace(logger);
                ret=false;
                if(child!=null)
                {
                    try {
                            child.kill();
                    } catch (IOException ex) {
                        logger.println("inception IOExcetion");
                    } catch (InterruptedException ex) {
                        logger.println("1 exception throws an exception, 2 exceptions throws an exception, 3 exceptions...");
                    }
                }
            }
                    
                    
            Long endTime = System.currentTimeMillis();        
            
            logger.println("Total time spent: "+(endTime-startTime)+" ms");
            
            logger.println();
            logger.flush();
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
