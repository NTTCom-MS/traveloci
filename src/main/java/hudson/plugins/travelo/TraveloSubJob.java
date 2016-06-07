/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.travelo;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.StreamBuildListener;
import hudson.util.ArgumentListBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraveloSubJob {

    public Proc getChild() {
        return child;
    }

    public void setChild(Proc child) {
        this.child = child;
        
        if(this.child!=null)
            this.running=true;
    }

    public ByteArrayOutputStream getBAOS() {
        return baos;
    }

    public void setBAOS(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    public StreamBuildListener getSBL() {
        return sbl;
    }

    public void setSBL(StreamBuildListener sbl) {
        this.sbl = sbl;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isRunning() {
        return this.running;
    }
    
    public boolean haveBeenStarted() {
        return this.started;
    }

    public Integer getReturnCode() {
        return returncode;
    }

    public void setReturnCode(Integer returncode) {
        this.returncode = returncode;
    }
    
    public TraveloSubJob(Map<String, String> job)
    {
        this.job=job;
        this.output=new String();
        this.baos=new ByteArrayOutputStream();
        this.sbl=new StreamBuildListener(this.baos);
    }
    
    private Proc child = null;
    private ByteArrayOutputStream baos = null;
    private StreamBuildListener sbl = null;
    private String output = null;
    private boolean running = false;
    private boolean started = false;
    private Integer returncode = 999;
    private String exception = null;
    private String lastcommand = null;
    private Map<String, String> job=null;
    
    public String getLastCommand() 
    {
        return this.lastcommand;
    }
    
    protected boolean launchjob(AbstractBuild build, Launcher launcher, BuildListener listener)
    {
        if(this.child!=null)
            return false;
        
        this.started=true;

        Pattern pattern = Pattern.compile("([a-zA-Z0-9_]*)=\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(this.job.get("env"));
        EnvVars envvars;

        try {
            envvars=build.getEnvironment(listener);
            envvars.putAll(build.getBuildVariables());

            while (matcher.find()) {
                envvars.put(matcher.group(1), matcher.group(2));
            }

        } catch (IOException e) {
            this.child=null;
            this.running=false;
            this.returncode=666;
            this.exception=this.exception.concat(e.toString());
            return false;
        } catch (InterruptedException e) {
            this.child=null;
            this.running=false;
            this.returncode=666;
            this.exception=this.exception.concat(e.toString());
            return false;
        }
        
        //build
        try {

            ArgumentListBuilder args = new ArgumentListBuilder();
            args.add("/bin/bash");
            args.add("-c");
            args.add(job.get("script"));
            
            this.lastcommand=args.toString();

            //job.get("env")
            child = launcher.decorateFor(build.getBuiltOn()).launch()
              .cmds(args).envs(envvars.descendingMap()).stdout(sbl)
              .stderr(baos).pwd(build.getWorkspace()).start();
           
            this.running=true;
            return true;
        }
        catch(IOException e) {
            
            this.running=false;
            this.returncode=666;
            this.exception=this.exception.concat(e.toString());

            if(child!=null)
            {
                try {
                    child.kill();
                } catch (IOException e2) {
                    this.exception=this.exception.concat(e2.toString());
                } catch (InterruptedException e2) {
                    this.exception=this.exception.concat(e2.toString());
                }
            }
        }
        
        this.child=null;
        return false;
    }
    
    protected void flushOutput()
    {
        if(this.running)
        {
            try 
            {
                if(this.child.isAlive())
                {
                    this.baos.flush();
                    this.output=this.output.concat(baos.toString());
                    this.baos.reset();
                }
                else
                {
                    this.running=false;
                    this.returncode=this.child.join();
                }
            } catch (IOException ex) {
                this.running=false;
                this.returncode=666;
                this.exception=this.exception.concat(ex.toString());
            } catch (InterruptedException ex) {
                this.running=false;
                this.returncode=666;
                this.exception=this.exception.concat(ex.toString());
            }   
        }      
    }
    
}
