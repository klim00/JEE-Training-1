/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;
import java.util.logging.Logger;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.jboss.as.quickstarts.kitchensink.rest.MemberResourceRESTService;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class MemberController {

	@Inject 
	Logger log;
	
    @Inject
    private FacesContext facesContext;

    @Inject
    private MemberRegistration memberRegistration;
    
    @Inject
    private MemberResourceRESTService MemReRService;
    @Inject
    private MemberLogin memberLogin;
    
    @Produces
    @Named
    private Member newMember;
    
    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }
    
    //String variables to record user input
    @NotNull(message="Retype password for validation")
    private String passwordVal;

    //Click determines which messages field to show
    private boolean click = false;
    
    public void register() throws Exception {
    	try {
            //Render Logic button's messages field false so general message does not show beside Logic button
        	memberLogin.setClick(false);
        	
        	//Check and return error message if email exists in database
            if(MemReRService.emailAlreadyExists(newMember.getEmail())) {
            	FacesContext.getCurrentInstance().addMessage("reg:email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already exists!"));
            }
            
            //Check and return error message if username exists in database
            if(MemReRService.usernameAlreadyExists(newMember.getUsername())) {
            	FacesContext.getCurrentInstance().addMessage("reg:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists!"));
            }
            
            memberRegistration.register(newMember);
            
            // check if retyped password matches (retyped password implemented to ensure user inputs the password intended since input cannot be directly viewed by user)
        	if (!passwordVal.equals(newMember.getPassword())) {
                //Lets user know retyped password does not match
            	FacesContext.getCurrentInstance().addMessage("reg:passwordVal", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password does not match"));
        	}
        	else {
        		//Render Register button's messages field true general message shows beside Register button
            	click = true;
	            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
	            facesContext.addMessage(null, m);
	            initNewMember();
        	}
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
        
    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

    //getters & setters
    public String getPasswordVal() {
        return passwordVal;
    }

    public void setPasswordVal(String passwordVal){
        this.passwordVal = passwordVal;
    }
    
    public boolean getClick() {
    	return click;
    }
    
    public void setClick(boolean click) {
    	this.click = click;
    }
}


