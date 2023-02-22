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
    
    private String username;
    private String password;
    
    public void authenticate() throws Exception {
        try {
        	log.info("function running");
            Member member = repository.findByUsername(username);
            log.info("."+password.equals(member.getPassword()));
            if(member!=null && password.equals(member.getPassword()))
            {
            	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/rest/members");
            }else{
                FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null);
                facesContext.addMessage(null, m);
            }
        } catch (Exception e) {
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null);
            facesContext.addMessage(null, m);
        }
    }
    
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
}