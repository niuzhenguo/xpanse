# xpanse

Xpanse is an Open Source project allowing to easily implement native managed 
service on any cloud service provider.

Xpanse unleash your cloud services by removing vendor lock-in and lock out. 
It standardizes and exposes cloud service providers core services, meaning 
that your xpanse service is portable (multi-cloud) on any cloud topology and 
provider. It also avoids tight coupling of your service to other cloud service 
provider services.

## APIs (core services)

Xpanse interacts directly with the fundamental APIs used by the cloud service 
provider to create managed service:

* **identity** dealing with access, users, groups, roles, ...
* **computing** abstracts the manipulation of virtual machines
* **storage** abstracts the manipulation of storage volumes
* **vpc** abstracts the manipulation of network devices
* **billing** registers the business model in the cloud provider billing system
* ...

## Configuration Language

A managed service is described using Open Services Cloud Configuration Language 
(OCL).

OCL is a json descriptor of a managed service, describing the expected final 
state of your service, interacting with the fundamental APIs:

```yaml
# The version of the OCL
version: 2.0
# The category of the service.
category: middleware
# The Service provided by the ISV, the name will be shown on the console as a service.
name: Kafka-cluster
# The version of the service, if the end-user want to select the version when they want to deploy the service.
serviceVersion: v3.3.2
# For the users may have more than one service, the @namespace can be used to separate the clusters.
description: This is an ehanced Kafka cluster services by ISV-A.
namespace: ISV-A
# Icon for the service.
icon: |
  data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAACRAQMAAAAPc4+9AAAAAXNSR0IB2cksfwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAZQTFRF+/v7Hh8gVD0A0wAAAcVJREFUeJzNlc1twzAMhSX44KNH0CgaTd6gK3kUd4McDVTwq/hjiUyaIkV7qNA2/QCFIh+ppxB+svLNEqqBGTC0ANugBOwmCGDCFOAwIWGDOoqoODtN2BdL6wxD9NMTO9tXPa1PqL5M30W5p8lm5vNcF0t7ahSrVguqNqmMokRW4YQucVjBCBWH1Z2g3WDlW2skoYU+2x8JOtGedBF3k2iXMO0j16iUiI6gxzPdQhnU/s2G9pCO57QY2r6hvjPbKJHq7DRTRXT60avtuTRdbrFJI3mSZhNOqYjVbd99YyK1QKWzEqSWrE0k07U60uPaelflMzaaeu1KBuurHSsn572I1KWy2joX5ZBfWbS/VEt50H5P6aL4JxTuyJ/+QCNPX4PWF3Q8Xe1eF9FsLdD2VaOnaP2hWvs+zI58/7i3vH3nRFtDZpyTUNaZkON5XnBNsp8lrmDMrpvBr+b6pUl+4XbkQdndqnzYGzfuJm1JmIWimIbe6dndd/bk7gVce/cJdo3uIeLJl7+I2xTnPek67mjtDeppE7b03Ov+kSfDe3JweW53njxeGfXkaz28VeYd86+af/H8a7hgJKaebILaFzakLfxyfQLTxVB6K1K9KQAAAABJRU5ErkJggg==
# Reserved for CSP, aws,azure,ali,huawei and ...
cloudServiceProvider:
  name: huawei
  regions:
    - name: eu-west-101
      area: Europe
    - name: cn-north-4
      area: Asia China
billing:
  # The business model(`flat`, `exponential`, ...)
  model: flat
  # The rental period (`daily`, `weekly`, `monthly`, `yearly`)
  period: monthly
  # The billing currency (`euro`, `usd`, ...)
  currency: euro
# The flavor of the service, the @category/@name/@version/@flavor can locate the specific service to be deployed.
flavors:
  - name: 1-zookeeper-with-3-worker-nodes-normal
    # The fixed price during the period (the price applied one shot whatever is the service use)
    fixedPrice: 40
    # Properties for the service, which can be used by the deployment.
    property:
      worker_nodes_count: 3
      flavor_id: c7.large.4
  - name: 1-zookeeper-with-3-worker-nodes-performance
    # The fixed price during the period (the price applied one shot whatever is the service use)
    fixedPrice: 60
    # Properties for the service, which can be used by the deployment.
    property:
      worker_nodes_count: 3
      flavor_id: c7.xlarge.4
  - name: 1-zookeeper-with-5-worker-nodes-normal
    # The fixed price during the period (the price applied one shot whatever is the service use)
    fixedPrice: 60
    # Properties for the service, which can be used by the deployment.
    property:
      worker_nodes_count: 5
      flavor_id: c7.large.4
  - name: 1-zookeeper-with-5-worker-nodes-performance
    # The fixed price during the period (the price applied one shot whatever is the service use)
    fixedPrice: 80
    # Properties for the service, which can be used by the deployment.
    property:
      worker_nodes_count: 5
      flavor_id: c7.xlarge.4
deployment:
  # kind, Supported values are terraform, pulumi, crossplane.
  kind: terraform
  # Context for deployment: the context including some kind of parameters for the deployment, such as fix_env, fix_variable, env, variable, env_env, env_variable.
  # - fix_env: Values for variable of this type are defined by the managed service provider in the OCL template. Runtime will inject it to deployer as environment variables. This variable is not visible to the end user.
  # - fix_variable: Values for variable of this type are defined by the managed service provider in the OCL template. Runtime will inject it to deployer as usual variables. This variable is not visible to the end user.
  # - env: Value for a variable of this type can be provided by end user. If marked as mandatory then end user must provide value to this variable. If marked as optional and if end user does not provided it, then the fallback value to this variable is read by runtime (it can read from other sources, e.g., OS env variables). This variable is injected as a environment variable to the deployer.
  # - variable: Value for a variable of this type can be provided by end user. . If marked as mandatory then end user must provide value to this variable. If marked as optional and if end user does not provided it, then the fallback value to this variable is read by runtime (it can read from other sources, e.g., OS env variables). This variable is injected as a regular variable to the deployer.
  # - env_env: Value to this variable is read by runtime (it can read from other sources, e.g., OS env variables) and injected as a environment variable to the deployer. End user cannot see or change this variable.
  # - env_variable: Value to this variable is read by runtime (it can read from other sources, e.g., OS env variables) and injected as a regular variable to the deployer. End user cannot see or change this variable.
  # The parameters will be used to generate the API of the managed service.
  context:
    - name: HW_ACCESS_KEY
      description: Huawei cloud access key.
      kind: env
      type: string
      mandatory: true
    - name: HW_SECRET_KEY
      description: Huawei cloud secret key.
      kind: env
      type: string
      mandatory: true
    - name: admin_passwd
      description: The admin password of all nodes in the Kafka cluster. If the value is empty, will create a random password.
      kind: variable
      type: string
      mandatory: false
      validator: minLength=8|maxLength=16|pattern=^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$
    - name: vpc_name
      description: The vpc name of all nodes in the Kafka cluster. If the value is empty, will use the example value to find or create VPC.
      kind: variable
      type: string
      example: "kafka-vpc-default"
      mandatory: false
    - name: subnet_name
      description: The sub network name of all nodes in the Kafka cluster. If the value is empty, will use the example value to find or create subnet.
      kind: variable
      type: string
      example: "kafka-subnet-default"
      mandatory: false
    - name: secgroup_name
      description: The security group name of all nodes in the Kafka cluster. If the value is empty, will use the example value to find or create security group.
      kind: variable
      type: string
      example: "kafka-secgroup-default"
      mandatory: false
  deployer: |
    variable "flavor_flavor_id" {
      type        = string
      default     = "c7.large.2"
      description = "The flavor_id of all nodes in the Kafka cluster."
    }

    variable "flavor_worker_nodes_count" {
      type        = string
      default     = 3
      description = "The worker nodes count in the Kafka cluster."
    }

    variable "admin_passwd" {
      type        = string
      default     = ""
      description = "The root password of all nodes in the Kafka cluster."
    }

    variable "vpc_name" {
      type        = string
      default     = "kafka-vpc-default"
      description = "The vpc name of all nodes in the Kafka cluster."
    }

    variable "subnet_name" {
      type        = string
      default     = "kafka-subnet-default"
      description = "The subnet name of all nodes in the Kafka cluster."
    }

    variable "secgroup_name" {
      type        = string
      default     = "kafka-secgroup-default"
      description = "The security group name of all nodes in the Kafka cluster."
    }

    data "huaweicloud_vpcs" "existing" {
      name = var.vpc_name
    }

    data "huaweicloud_vpc_subnets" "existing" {
      name = var.subnet_name
    }

    data "huaweicloud_networking_secgroups" "existing" {
      name = var.secgroup_name
    }

    locals {
      admin_passwd = var.admin_passwd == "" ? random_password.password.result : var.admin_passwd
      vpc_id       = length(data.huaweicloud_vpcs.existing.vpcs) > 0 ? data.huaweicloud_vpcs.existing.vpcs[0].id : huaweicloud_vpc.new[0].id
      subnet_id    = length(data.huaweicloud_vpc_subnets.existing.subnets)> 0 ? data.huaweicloud_vpc_subnets.existing.subnets[0].id : huaweicloud_vpc_subnet.new[0].id
      secgroup_id  = length(data.huaweicloud_networking_secgroups.existing.security_groups) > 0 ? data.huaweicloud_networking_secgroups.existing.security_groups[0].id : huaweicloud_networking_secgroup.new[0].id
    }

    resource "huaweicloud_vpc" "new" {
      count = length(data.huaweicloud_vpcs.existing.vpcs) == 0 ? 1 : 0
      name  = var.vpc_name
      cidr  = "192.168.0.0/16"
    }

    resource "huaweicloud_vpc_subnet" "new" {
      count      = length(data.huaweicloud_vpcs.existing.vpcs) == 0 ? 1 : 0
      vpc_id     = local.vpc_id
      name       = var.subnet_name
      cidr       = "192.168.10.0/24"
      gateway_ip = "192.168.10.1"
    }

    resource "huaweicloud_networking_secgroup" "new" {
      count       = length(data.huaweicloud_networking_secgroups.existing.security_groups) == 0 ? 1 : 0
      name        = var.secgroup_name
      description = "Kafka cluster security group"
    }

    resource "huaweicloud_networking_secgroup_rule" "secgroup_rule_0" {
      count             = length(data.huaweicloud_networking_secgroups.existing.security_groups) == 0 ? 1 : 0
      direction         = "ingress"
      ethertype         = "IPv4"
      protocol          = "tcp"
      port_range_min    = 22
      port_range_max    = 22
      remote_ip_prefix  = "121.37.117.211/32"
      security_group_id = local.secgroup_id
    }

    resource "huaweicloud_networking_secgroup_rule" "secgroup_rule_1" {
      count             = length(data.huaweicloud_networking_secgroups.existing.security_groups) == 0 ? 1 : 0
      direction         = "ingress"
      ethertype         = "IPv4"
      protocol          = "tcp"
      port_range_min    = 2181
      port_range_max    = 2181
      remote_ip_prefix  = "121.37.117.211/32"
      security_group_id = local.secgroup_id
    }

    resource "huaweicloud_networking_secgroup_rule" "secgroup_rule_2" {
      count             = length(data.huaweicloud_networking_secgroups.existing.security_groups) == 0 ? 1 : 0
      direction         = "ingress"
      ethertype         = "IPv4"
      protocol          = "tcp"
      port_range_min    = 9092
      port_range_max    = 9093
      remote_ip_prefix  = "121.37.117.211/32"
      security_group_id = local.secgroup_id
    }

    data "huaweicloud_availability_zones" "osc-az" {}

    resource "random_id" "new" {
      byte_length = 4
    }

    resource "random_password" "password" {
      length           = 12
      upper            = true
      lower            = true
      numeric          = true
      special          = true
      min_special      = 1
      override_special = "#%@"
    }

    resource "huaweicloud_compute_keypair" "keypair" {
      name = "keypair-kafka-${random_id.new.hex}"
    }

    data "huaweicloud_images_image" "image" {
      name        = "Kafka-v3.3.2_Ubuntu-20.04"
      most_recent = true
    }

    resource "huaweicloud_compute_instance" "zookeeper" {
      availability_zone  = data.huaweicloud_availability_zones.osc-az.names[0]
      name               = "kafka-zookeeper-${random_id.new.hex}"
      flavor_id          = var.flavor_flavor_id
      security_group_ids = [ local.secgroup_id ]
      image_id           = data.huaweicloud_images_image.image.id
      key_pair           = huaweicloud_compute_keypair.keypair.name
      network {
        uuid = local.subnet_id
      }
      user_data = <<EOF
        #!bin/bash
        echo root:${local.admin_passwd} | sudo chpasswd
        sudo systemctl start docker
        sudo systemctl enable docker
        sudo docker run -d --name zookeeper-server --privileged=true -p 2181:2181 -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:3.8.1
      EOF
    }

    resource "huaweicloud_compute_instance" "kafka-broker" {
      count              = var.flavor_worker_nodes_count
      availability_zone  = data.huaweicloud_availability_zones.osc-az.names[0]
      name               = "kafka-broker-${random_id.new.hex}-${count.index}"
      flavor_id          = var.flavor_flavor_id
      security_group_ids = [ local.secgroup_id ]
      image_id           = data.huaweicloud_images_image.image.id
      key_pair           = huaweicloud_compute_keypair.keypair.name
      network {
        uuid = local.subnet_id
      }
      user_data = <<EOF
        #!bin/bash
        echo root:${local.admin_passwd} | sudo chpasswd
        sudo systemctl start docker
        sudo systemctl enable docker
        private_ip=$(ifconfig | grep -A1 "eth0" | grep 'inet' | awk -F ' ' ' {print $2}'|awk ' {print $1}')
        sudo docker run -d --name kafka-server --restart always -p 9092:9092 -p 9093:9093  -e KAFKA_BROKER_ID=${count.index}  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://$private_ip:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=${huaweicloud_compute_instance.zookeeper.access_ip_v4}:2181 bitnami/kafka:3.3.2
      EOF
      depends_on = [
        huaweicloud_compute_instance.zookeeper
      ]
    }

    output "zookeeper_server" {
      value = "${huaweicloud_compute_instance.zookeeper.access_ip_v4}:2181"
    }

    output "admin_passwd" {
      value = var.admin_passwd == "" ? nonsensitive(local.admin_passwd) : local.admin_passwd
    }
```

## OCL loading

Xpanse provides different options to generate and provision OCL:

* REST API on the xpanse runtime
* CLI allowing to directly interact with xpanse via command line
* language frontend (SDL) for Java, Python, ...

## Orchestrator & binding

OCL descriptor is an abstract description of the final managed service state. 
It's generic enough to work with any cloud service provider.

Xpanse runtime embeds an orchestrator responsible to delegate the services 
management to plugins.

Each plugin is dedicated to handle a cloud provider infrastructure and do 
actions required to actually deal with the services' lifecycle:

1. to bind OCL to the concrete cloud provider internal APIs
2. to generate the graph of actions required to reach the final expected state, 
specifically for a target cloud provider

## Runtime

Xpanse runtime is the overall component running on the cloud provider.

The runtime embeds and run together:

1. the orchestrator with the different bindings
2. the OCL loader and parser
3. the frontends (REST API, ...)

## Database

The default database attached to the runtime is the H2 in-memory database. 
The same can be replaced with other production ready database systems by 
replacing the configurations mentioned below and by adding relevant maven 
dependencies.

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto= update
```

### Build and Package

First, you can build the whole xpanse project, including all modules 
(orchestrator, OCL, runtime, plugins, etc), simply with:

```shell
$ mvn clean install
```

### Run

By default, the application will not activate any plugins. They must be 
activated via spring profiles. Also ensure that only one plugin is active at a 
time.

* for Huawei Cloud:

```shell
$ cd runtime/target
$ java -jar xpanse-runtime-1.0.0-SNAPSHOT.jar -Dspring.profiles.active=huaweicloud
```

* for Openstack:

```shell
$ cd runtime/target
$ java -jar xpanse-runtime-1.0.0-SNAPSHOT.jar -Dspring.profiles.active=openstack
```

By default, the runtime is built in "exploded mode". Additionally, you can also 
build a Docker image adding `-Ddocker.skip=false` as build argument:

```shell
$ cd runtime
$ mvn clean install -Ddocker.skip=false
```

We can start xpanse runtime with a specific plugin by passing the plugin name 
in the profile name. For example to start huaweicloud

```shell
$ docker run -e "SPRING_PROFILES_ACTIVE=huaweicloud" --name my-xpanse-runtime xpanse
```

### Static Code Analysis using CheckStyle

This project using `CheckStyle` framework to perform static code analysis. The 
configuration can be found in [CheckStyle](checkstyle.xml). The framework also 
checks the code format in accordance to `Google Java Format`.

The same file can also be imported in IDE CheckStyle plugins to get the 
analysis results directly in IDE and also to perform code formatting directly 
in IDE.

The framework is added as a maven plugin and is executed by default as part of 
the `verify` phase. Any violations will result in build failure.

### License/Copyright Configuration

All files in the repository must contain a license header in the format 
mentioned in [License Header](license.header).

The static code analysis framework will also validate if the license exists in 
the specified format.
