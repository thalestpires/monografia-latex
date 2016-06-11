def synchronize_tasks(process_instance_id)
    read_human_tasks(process_instance_id).each do |task|
        next if BpmIntegration::HumanTaskIssue.where(human_task_id:task.id).first
        task_definition = BpmIntegration::TaskDefinition.by_task_instance(task.taskDefinitionKey,
            task.processDefinitionId).first
        issue = build_issue_from_task(task, task_definition)
        next if issue.nil?
    
        issue.human_task_issue = build_human_task_issue(task, task_definition)
    
        issue.custom_fields = custom_values_from_task_form_data(task, task_definition)

        issue.save(validate: false)
    end
end