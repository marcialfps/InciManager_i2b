package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniovi.entities.Incident;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.KafkaService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentController {
	
	@Autowired
	private AgentsService agentsService;
	
	@Autowired
	private IncidentsService incidentsService;
	
	@Autowired
	private KafkaService kafkaService;
	
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	@ResponseBody
	public String createIncident(@RequestBody Incident incident) throws Exception {
		if (!agentsService.existsAgent(incident.getAgent())) {
			throw new AgentNotFoundException();
		}

		agentsService.addAgent(incident.getAgent());
		incidentsService.addNewIncident(incident);
		kafkaService.sendToKafka(incident);
		return "Incident correctly sent!";
	}
	
	@RequestMapping(value="/incident/create", method=RequestMethod.GET)
	public String createIncident() throws Exception {
		return "chatroom.html";
	}

}
