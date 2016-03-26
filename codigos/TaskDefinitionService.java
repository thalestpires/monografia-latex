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

}