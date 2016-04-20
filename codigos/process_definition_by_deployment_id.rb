def self.process_definition_by_deployment_id(deployment_id)
    process = get(
      "/repository/process-definitions",
      query: { deploymentId: deployment_id },
      basic_auth: @@auth
    )["data"].first
    BpmProcessDefinition.new(process)
end