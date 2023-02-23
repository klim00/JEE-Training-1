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
package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.model.Member;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.logging.Logger;
import org.jboss.as.quickstarts.kitchensink.controller.MemberController;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;
    
    @Inject 
    MemberController memberController;
    
    @Inject
    private EntityManager em;

    @Inject
    private Event<Member> memberEventSrc;

    public void register(Member member) throws Exception {
        //If and only if retyped password matches password, member is verified to go through registration process.
    	if (memberController.getPasswordVal().equals(member.getPassword())) {
    		log.info("Registering " + member.getUsername());
            em.persist(member);
            memberEventSrc.fire(member);
    	}
    }
}
