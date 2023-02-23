/**New file created to controll and manage user login input**/

package org.jboss.as.quickstarts.kitchensink.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ExternalContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.logging.Logger;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;

@Named
@RequestScoped

public class MemberLogin{
	
	@Inject
	private Logger log;

    @Inject 
    private MemberRepository repository;

    @Inject
    private FacesContext facesContext;
    
    @Inject 
    private MemberController memberController;
    
    //String variables to record user input
    private String username;
    private String password;

    //Click determines which messages field to show
    private boolean click = false;
    
    public void authenticate() throws Exception {
        try {
            //Render register button's messages field false so general message does not show beside register button
        	memberController.setClick(false);
            //Render Login button's messages field true general message shows beside Login button
        	click = true;
            //Determine if username input is in database
            Member member = repository.findByUsername(username);
            if(member!=null && password.equals(member.getPassword()))//if username input is in database, verify if password is equal/correct
            {
                //Username is valid, password matches, directs user to JSON database page
            	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/rest/members");
            }else{
                //Username valid but password does not match, indicate message "Invalid username or password"
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null);
                facesContext.addMessage(null, m);
            }
        } catch (Exception e) {
            //Username invalid and password does not match, indicate message "Invalid username or password"
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null);
            facesContext.addMessage(null, m);
        }
    }
    
    //getters & setters
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
    public boolean getClick() {
    	return click;
    }
    
    public void setClick(boolean click) {
    	this.click = click;
    }
}