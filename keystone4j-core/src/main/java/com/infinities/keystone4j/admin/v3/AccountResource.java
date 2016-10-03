/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.infinities.keystone4j.admin.v3;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantAction;
import com.infinities.keystone4j.assignment.controller.action.project.CreateProjectAction;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.controller.action.user.CreateUserAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.AuthV3Wrapper;
import com.infinities.keystone4j.model.identity.CreateUserParam;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.wrapper.CreateUserParamWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UserParamWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountResource {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AccountResource.class);
	private final AuthController authController;
	private final UserV3Controller userController;
	// private final Properties ecsProperties = new Properties();
	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;
	private final IdentityApi identityApi;
	// private static final String CONFIG_FOLDER = "conf";
	// private static final String ECS_PROPERTIES_FILE = "ecs.conf";
	// private final AWSCredentials credentials;
	private final String roleid;


	@Inject
	public AccountResource(AuthController authController, UserV3Controller userController, AssignmentApi assignmentApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, IdentityApi identityApi) throws Exception {
		this.authController = authController;
		this.userController = userController;
		this.assignmentApi = assignmentApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.identityApi = identityApi;
		// String configFileLocation = CONFIG_FOLDER + File.separator +
		// ECS_PROPERTIES_FILE;
		// try {
		// logger.debug("config file: {}", configFileLocation);
		// ecsProperties.load(new FileInputStream(configFileLocation));
		// } catch (FileNotFoundException e) {
		// String message = configFileLocation + " file is not found";
		// logger.warn(message, e);
		// throw new RuntimeException(message, e);
		// } catch (IOException e) {
		// String message = "Error while reading" + configFileLocation +
		// " file";
		// logger.warn(message, e);
		// throw new RuntimeException(message, e);
		// }
		// credentials = new
		// BasicAWSCredentials(ecsProperties.getProperty("ecs.accesskey"),
		// ecsProperties.getProperty("ecs.secretkey"));

		Hints hints = new Hints();
		hints.addFilter("name", "_member_");
		List<Role> refs = assignmentApi.listRoles(hints);
		roleid = refs.iterator().next().getId();
	}

	@POST
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response authenticateForToken(AuthV3Wrapper authWrapper) throws Exception {
		return authController.authenticateForToken(authWrapper.getAuth());
	}

	@GET
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response validateToken() throws Exception {
		return authController.validateToken();
	}

	@POST
	@Path("/users/{userid}/password")
	public Response changePassword(@PathParam("userid") String userid, UserParamWrapper userWrapper) throws Exception {
		userController.changePassword(userid, userWrapper.getUser());
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@POST
	@JsonView(Views.Advance.class)
	@Path("/users")
	public synchronized Response createUser(@Context ContainerRequestContext context, CreateUserParamWrapper userWrapper)
			throws Exception {
		Project project = createProject(context, userWrapper.getUser());
		userWrapper.getUser().setDomainId("default");
		userWrapper.getUser().setDefaultProjectId(project.getId());
		userWrapper.getUser().getUser().setDefaultProject(project);
		userWrapper.getUser().getUser().setEnabled(true);
		MemberWrapper<User> ret = createUser(context, userWrapper.getUser());
		String userid = ret.getRef().getId();
		String projectid = project.getId();
		createGrant(context, roleid, userid, projectid);

		// ContainerDefinition container = new ContainerDefinition();
		// container.withName(ecsProperties.getProperty("container.containername"));
		// container.withMemoryReservation(Integer.parseInt(ecsProperties.getProperty("container.memoryreservation")));
		// container.withImage(ecsProperties.getProperty("container.image"));
		// container.withEssential(true);
		// String command = ecsProperties.getProperty("container.command");
		// String commands[] = command.split(",");
		// container.withCommand(commands);
		// List<ContainerDefinition> containers = new
		// ArrayList<ContainerDefinition>();
		// containers.add(container);
		// String taskName = ecsProperties.getProperty("container.taskname");
		// String networkmode =
		// ecsProperties.getProperty("container.networkmode");

		// TaskDefinition taskDefinition = registerTask(credentials, taskName,
		// networkmode, containers);
		// ContainerDefinition containerDefinition =
		// taskDefinition.getContainerDefinitions().iterator().next();
		// PortMapping portMapping =
		// containerDefinition.getPortMappings().iterator().next();
		// Integer port = portMapping.getHostPort();
		// String clusterName =
		// ecsProperties.getProperty("container.clustername");
		// Task task = runTask(credentials, clusterName,
		// taskDefinition.getTaskDefinitionArn());
		// String publicIp = getTask(credentials, task);
		// User user = new User();
		// user.setPort(port);
		// user.setIp(publicIp);
		// user.setId(ret.getRef().getId());
		// ret = updateUser(context, user.getId(), user);

		return Response.status(Status.CREATED).entity(ret).build();
	}

	private MemberWrapper<Role> createGrant(@Context ContainerRequestContext context, String roleid, String userid,
			String projectid) throws Exception {
		CreateGrantAction action = new CreateGrantAction(assignmentApi, identityApi, tokenProviderApi, policyApi, roleid,
				userid, null, null, projectid);
		return action.execute(context);
	}

	private Project createProject(@Context ContainerRequestContext context, CreateUserParam user) throws Exception {
		Project project = new Project();
		project.setDomainId("default");
		project.setDescription("project for " + user.getName());
		project.setName(user.getName());
		project.setEnabled(true);

		CreateProjectAction action = new CreateProjectAction(assignmentApi, tokenProviderApi, policyApi, project);
		MemberWrapper<Project> projectWrapper = action.execute(context);
		project = projectWrapper.getRef();
		return project;
	}

	private MemberWrapper<User> createUser(@Context ContainerRequestContext context, CreateUserParam user) throws Exception {
		this.identityApi.setAssignmentApi(assignmentApi);
		CreateUserAction createUser = new CreateUserAction(identityApi, tokenProviderApi, policyApi, user.getUser());
		MemberWrapper<User> ret = createUser.execute(context);
		return ret;
	}

	// private MemberWrapper<User> updateUser(@Context ContainerRequestContext
	// context, String userid, User user)
	// throws Exception {
	// this.identityApi.setAssignmentApi(assignmentApi);
	// UpdateUserAction createUser = new UpdateUserAction(identityApi,
	// tokenProviderApi, policyApi, userid, user);
	// MemberWrapper<User> ret = createUser.execute(context);
	// return ret;
	// }

	// private String getTask(AWSCredentials credential, Task task) {
	// AmazonECSClient ecs = getAmazonECSClient(credential);
	//
	// DescribeContainerInstancesRequest request = new
	// DescribeContainerInstancesRequest();
	// request.withCluster(task.getClusterArn());
	// List<String> ids = new ArrayList<String>();
	// ids.add(task.getContainerInstanceArn());
	// request.withContainerInstances(ids);
	// for (ContainerInstance container :
	// ecs.describeContainerInstances(request).getContainerInstances()) {
	// DescribeInstancesRequest insRequest = new DescribeInstancesRequest();
	// List<String> vmids = new ArrayList<String>();
	// String instanceId = container.getEc2InstanceId();
	// vmids.add(instanceId);
	// insRequest.setInstanceIds(vmids);
	// for (Reservation res :
	// getAmazonEC2Client(credential).describeInstances(insRequest).getReservations())
	// {
	// for (Instance ins : res.getInstances()) {
	// return ins.getPublicIpAddress();
	// }
	// }
	// }
	// throw new IllegalStateException("get task public ip failed");
	// }

	// private Task runTask(AWSCredentials credential, String cluster, String
	// taskDefinition) {
	// AmazonECSClient ecs = getAmazonECSClient(credential);
	// logger.debug("cluster:{}, taskDefinition:{}", new Object[] { cluster,
	// taskDefinition });
	// RunTaskRequest request = new RunTaskRequest();
	// request.withCluster(cluster);
	// request.withCount(1);
	// request.withTaskDefinition(taskDefinition);
	// RunTaskResult result = ecs.runTask(request);
	// List<Failure> fails = result.getFailures();
	// if (fails.size() > 0) {
	// throw new IllegalArgumentException(fails.get(0).getReason());
	// }
	// List<Task> rets = result.getTasks();
	// for (Task task : rets) {
	// return task;
	// }
	// throw new IllegalStateException("run task failed");
	// }

	// private TaskDefinition registerTask(AWSCredentials credential, String
	// taskName, String networkMode,
	// List<ContainerDefinition> containerDefinitions) {
	// if (containerDefinitions == null || containerDefinitions.size() == 0) {
	// throw new IllegalArgumentException("Invalid Container Definition");
	// }
	//
	// Integer hostPort = getHostPort(credential);
	// List<PortMapping> portMappings = new ArrayList<PortMapping>();
	// PortMapping portMapping = new PortMapping();
	// portMapping.withContainerPort(Integer.parseInt(ecsProperties.getProperty("container.port")));
	// portMapping.withProtocol(ecsProperties.getProperty("container.protocol"));
	// portMapping.withHostPort(hostPort);
	// portMappings.add(portMapping);
	//
	// for (ContainerDefinition container : containerDefinitions) {
	// container.withPortMappings(portMappings);
	// }
	//
	// RegisterTaskDefinitionRequest request = new
	// RegisterTaskDefinitionRequest();
	// request.withFamily(taskName);
	// request.withNetworkMode(networkMode);
	// request.withContainerDefinitions(containerDefinitions);
	// AmazonECSClient ecs = getAmazonECSClient(credential);
	// RegisterTaskDefinitionResult response =
	// ecs.registerTaskDefinition(request);
	// return response.getTaskDefinition();
	// }

	// private Integer getHostPort(AWSCredentials credential) {
	// List<Integer> usedPorts = new ArrayList<Integer>();
	// List<com.amazonaws.services.ecs.model.Task> tasks =
	// listTasks(credential);
	// for (com.amazonaws.services.ecs.model.Task task : tasks) {
	// for (Container container : task.getContainers()) {
	// for (NetworkBinding bind : container.getNetworkBindings()) {
	// usedPorts.add(bind.getHostPort());
	// }
	// }
	// }
	//
	// int portMin = Integer.parseInt(ecsProperties.getProperty("port.min"));
	// int portMax = Integer.parseInt(ecsProperties.getProperty("port.max"));
	// int hostport = portMin;
	// for (int num = portMin; num <= portMax; num++) {
	// boolean used = false;
	// for (int i = 0; i < usedPorts.size(); i++) {
	// if (num == usedPorts.get(i)) {
	// used = true;
	// break;
	// }
	// }
	// if (!used) {
	// hostport = num;
	// break;
	// }
	// }
	// return hostport;
	// }

	// private List<Task> listTasks(AWSCredentials credential) {
	// List<Cluster> clusters = listClusters(credential);
	// List<Task> rets = new ArrayList<>();
	// AmazonECSClient ecs = getAmazonECSClient(credential);
	// for (Cluster cluster : clusters) {
	// ListTasksRequest request = new ListTasksRequest();
	// request.setCluster(cluster.getClusterName());
	// ListTasksResult result = ecs.listTasks(request);
	// List<String> taskList = result.getTaskArns();
	// for (String arn : taskList) {
	// DescribeTasksRequest taskRequest = new DescribeTasksRequest();
	// taskRequest.setCluster(cluster.getClusterName());
	// List<String> ids = new ArrayList<String>();
	// ids.add(getName(arn));
	// taskRequest.setTasks(ids);
	// List<com.amazonaws.services.ecs.model.Task> tasks =
	// ecs.describeTasks(taskRequest).getTasks();
	// rets.addAll(tasks);
	// }
	// }
	// return rets;
	// }

	// private List<Cluster> listClusters(AWSCredentials credential) {
	// AmazonECSClient ecs = getAmazonECSClient(credential);
	// ListClustersResult result = ecs.listClusters();
	// List<String> clusters = result.getClusterArns();
	// List<Cluster> rets = new ArrayList<>();
	// for (String arn : clusters) {
	// String name = getName(arn);
	// logger.debug("cluster:{}", arn);
	// DescribeClustersRequest desRequest = new DescribeClustersRequest();
	// List<String> ids = new ArrayList<String>();
	// ids.add(name);
	// desRequest.setClusters(ids);
	// List<com.amazonaws.services.ecs.model.Cluster> clusterList =
	// ecs.describeClusters(desRequest).getClusters();
	// rets.addAll(clusterList);
	// }
	// return rets;
	// }

	// private String getName(String arn) {
	// return arn.split("/")[1];
	// }

	// private AmazonECSClient getAmazonECSClient(AWSCredentials credential) {
	// AmazonECSClient ecs = new AmazonECSClient(credential);
	// ecs.setEndpoint(ecsProperties.getProperty("ecs.endpoint"));
	// return ecs;
	// }

	// private AmazonEC2Client getAmazonEC2Client(AWSCredentials credential) {
	// AmazonEC2Client ecs = new AmazonEC2Client(credential);
	// ecs.setEndpoint(ecsProperties.getProperty("ec2.endpoint"));
	// return ecs;
	// }
}
