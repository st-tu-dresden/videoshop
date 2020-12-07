
##### General Settings
variable "project_id" {
  type = string
  default = "composite-stage-264608"
}

variable "credentials" {
  type = string
  default = "./My_First_Project-e4d3803bda87.json"
}

variable "region" {
  type = string
  default = "europe-north1-a"
}

variable "zone" {
  type = string
  default = "europe-north1-a"
}

variable "image" {
  type = string
  default = "ubuntu-os-cloud/ubuntu-1804-lts"
}
variable "tags" {
  type = list(string)
  default = ["http-server", "https-server"]
}
variable "connection_type" {
  type = string
  default = "ssh"
}

variable "connection_timeout" {
  type = string
  default = "500s"
}

variable "network" {
  type = string
  default = "default"
}

variable "user" {
  type = string
  default = "inception"
}

###### Jenkins Instance Settings

variable "jenkins_name" {
  type = string
  default = "jenkins-instance"
}
variable "jenkins_machine_type" {
  type = string
  default = "n1-standard-1"
}


###### DEV Instance Settings

variable "dev_instance_name" {
  type = string
  default = "ubuntu-dev"
}
variable "dev_machine_type" {
  type = string
  default = "n1-standard-1"
}

###### PROD Instance Settings

variable "prod_instance_name" {
  type = string
  default = "ubuntu-prod"
}
variable "prod_machine_type" {
  type = string
  default = "n1-standard-1"
}


#Network Settings
variable "ip_addr_jenkins" {
  type = string
  default = "35.217.19.13"
}

variable "ip_addr_dev_instance" {
  type = string
  default = "35.217.57.162"
}

variable "ip_addr_prod_instance" {
  type = string
  default = "35.217.5.220"
}

#SSH Keys Settings
variable "ssh_key_public" {
  type = string
  default = "/home/inception/.ssh/gcp-ubuntu-pipeline.pub"
}

variable "ssh_key_private" {
  type = string
  default = "/home/inception/.ssh/gcp-ubuntu-pipeline"
}

