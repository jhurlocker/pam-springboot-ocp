Follow the instructions below to deploy on OpenShift

1. Create a project in OpenShift and login to the cluster from a terminal window.

2. Switch to the project you just created in OpenShift (oc project <BAMOE_PROJECT>).

3. Build the eforms-service project (mvn clean install).

4. Copy the eforms-service/target/eforms-service-1.0-SNAPSHOT.jar to ocp-image/root/opt/spring-service

5. In a terminal change directory to ocp-image and run the following build commands:

oc new-build --binary --strategy=docker --name openshift-kie-springboot

oc start-build openshift-kie-springboot --from-dir=. --follow

6. Once the build finishes create a new application in the project:

oc new-app openshift-kie-springboot

7. Exposed the route:

oc expose service/openshift-kie-springboot --port=8090


For reference see:

https://access.redhat.com/documentation/pa-in/red_hat_process_automation_manager/7.13/html/integrating_red_hat_process_automation_manager_with_other_products_and_components/openshift-springboot-proc_business-applications
