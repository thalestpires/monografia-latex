def synchronize_single_process(deployment_id)
    Delayed::Worker.logger.info "#{self.class} - Sincronizando apenas process_definition - deploymentId: #{deployment_id}"
    process = BpmProcessDefinitionService.process_definition_by_deployment_id(deployment_id)
    save_process_definition(process)
  rescue => e
    Delayed::Worker.logger.error "#{self.class} - Ocorreu um erro ao realizar cadastro do processo - deploymentId: #{deployment_id}"
    e.backtrace.each { |line| Delayed::Worker.logger.error line }
end