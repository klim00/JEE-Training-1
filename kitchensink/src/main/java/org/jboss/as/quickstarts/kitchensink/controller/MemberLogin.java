/**New file created to control and manage user login input**/

package org.jboss.as.quickstarts.kitchensink.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ExternalContext;
//import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.logging.Logger;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ejb.Stateless;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;

@Named
@Stateless
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
    @NotNull
    @NotEmpty
    private String username;
    
    @NotNull
    @NotEmpty
    private String password;

    //Click determines which messages field to show
    private boolean click = false;
    
    //Flag for member authentication 
    private boolean isMember = false;
    
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
            	isMember = true;
            	log.info("."+(click && isMember));
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
    
    public boolean isMember() {
    	return isMember;
    }
    
    public void setIsMember(boolean isMember) {
    	this.isMember = isMember;
    }
}