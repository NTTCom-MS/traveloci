/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.travelo;

import hudson.Proc;
import hudson.model.StreamBuildListener;
import java.io.ByteArrayOutputStream;

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
        return running;
    }

    public Integer getReturnCode() {
        return returncode;
    }

    public void setReturnCode(Integer returncode) {
        this.returncode = returncode;
    }
    
    public TraveloSubJob()
    {
        this.output=new String();
        this.baos=new ByteArrayOutputStream();
        this.sbl=new StreamBuildListener(this.baos);
    }
    
    Proc child = null;
    ByteArrayOutputStream baos = null;
    StreamBuildListener sbl = null;
    String output = null;
    boolean running = false;
    Integer returncode = null;
    
}
