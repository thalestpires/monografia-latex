def self.deploy_process(process_data)
post(
  '/repository/deployments',
  basic_auth: @@auth,
  multipart: true,
  query: {
    file: process_data
  }
)
end