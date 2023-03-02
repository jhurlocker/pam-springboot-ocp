**Follow the instructions below to deploy on OpenShift**

1. Create a project in OpenShift and login to the cluster from a terminal window.

2. Switch to the project you just created in OpenShift (oc project <BAMOE_PROJECT>).

3. Build the eforms-model project, eforms-kjar project, and then the eforms-service project (mvn clean install).

4. Copy the eforms-service/target/eforms-service-1.0-SNAPSHOT.jar to ocp-image/root/opt/spring-service

5. In a terminal change directory to ocp-image and run the following build commands:
```
   oc new-build --binary --strategy=docker --name openshift-kie-springboot  
   oc start-build openshift-kie-springboot --from-dir=. --follow
```
6. Once the build finishes create a new application in the project:
```
   oc new-app openshift-kie-springboot
```
7. Exposed the route:
```
   oc expose service/openshift-kie-springboot --port=8090
```

For reference see:  
https://access.redhat.com/documentation/pa-in/red_hat_process_automation_manager/7.13/html/integrating_red_hat_process_automation_manager_with_other_products_and_components/openshift-springboot-proc_business-applications

**Important URLs**

* KIE Server Info  
  http://OPENSHIFT-KIE-SPRINGBOOT-ROUTE/rest/server  
* KIE Server List of Created Containers  
  http://OPENSHIFT-KIE-SPRINGBOOT-ROUTE/rest/server/containers  
* Swagger UI  
  http://OPENSHIFT-KIE-SPRINGBOOT-ROUTE/rest/api-docs?url=http://OPENSHIFT-KIE-SPRINGBOOT-ROUTE/rest/swagger.json  

**Send a JSON request to start a process**
1. Go to the swagger URL and under the section titled Process Instances expand the below sub section.
   POST /server/containers/{containerId}/processes/{processId}/instances
2. Click the Try it Out button in the right hand corner of the screen
3. Set containerId to:
   ```
   eforms-kjar-container1
   ```
4. Set processId to:
   ```
   moh_process
   ```
5. Make sure the content type is application/json and set the body to:
   ```
   {"model":
      {"Model":{
           "firstName":"John",
           "lastName":"Hurlocker"
            }
      }
   }
   ```
6. Click the Execute button
7. Make sure the request was succesful (a 201 response is good).
8. Open the kie-springboot pod in the OpenShift web console and scroll to the end of the log file.
9. If the process executed you should see the following in the log.
   ```
   -- IN PRINT NAME TASK --
   -- FIRST NAME -- John
   -- LAST NAME -- Hurlocker
   -- YOUR FULLNAME IS -- John Hurlocker
   ```

**Postman collection**  
See MOH_BAMOE.postman_collection.json to send requests to the KIE server through Postman (https://www.postman.com/)