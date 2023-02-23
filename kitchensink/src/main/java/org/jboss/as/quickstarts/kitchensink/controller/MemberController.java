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
    private MemberLogin memberLogin;
    
    @Produces
    @Named
    private Member newMember;
    
    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }
    
    @NotNull
    private String passwordVal;
    private boolean click = false;
    
    public void register() throws Exception {
    	try {
        	memberLogin.setClick(false);
        	click = true;
            memberRegistration.register(newMember);
        	if (!passwordVal.equals(newMember.getPassword())) {
            	FacesContext.getCurrentInstance().addMessage("reg:passwordVal", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password does not match"));
            	throw new Exception("Password Violation");
        	}
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
        	log.info("here");
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


