package com.redminebpm.integration.service.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.rest.common.api.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskDefinitionService <T extends PvmActivity> {

	@Autowired
	protected RepositoryService repositoryService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/repository/task-definitions/{processDefinitionId}", method = RequestMethod.GET, produces = "application/json")
	public DataResponse getTaskDefinitions(@PathVariable String processDefinitionId, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		
		ReadOnlyProcessDefinition processDefinition = 
				((RepositoryServiceImpl)repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);

		List<TaskDefinition> allActivities = new ArrayList<TaskDefinition>();
		if (processDefinition != null) {
			List<T> activities = (List<T>) processDefinition.getActivities();
			addActivities(activities, allActivities);
		}
		
		dataResponse.setData(allActivities);
		return dataResponse;
	}
	
	@SuppressWarnings("unchecked")
	private void addActivities(List<T> activities, List<TaskDefinition> taskDefinitions) {
		for (T activity : activities) {
			if (! activity.getActivities().isEmpty()) {
				addActivities((List<T>)activity.getActivities(), taskDefinitions);
			} else {
				ActivityBehavior activityBehavior = ((ActivityImpl)activity).getActivityBehavior();
				if (activityBehavior instanceof UserTaskActivityBehavior) {
					TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityBehavior).getTaskDefinition();
					taskDefinitions.add(taskDefinition);
				}
			}
		}
	}
}