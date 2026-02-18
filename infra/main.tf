# =============================================================================
# FinanceAPI Oracle Cloud Infrastructure (Always Free Tier)
# =============================================================================
# This Terraform configuration creates:
# - VCN with public subnet, internet gateway, route table, and security list
# - Compute instance VM.Standard.A1.Flex (ARM64, 4 OCPU, 24GB RAM)
# - Cloud-init provisioning (Docker, Docker Compose, iptables)
# =============================================================================

terraform {
  required_version = ">= 1.0"
  required_providers {
    oci = {
      source  = "oracle/oci"
      version = "~> 5.0"
    }
  }
}

provider "oci" {
  region           = var.oci_region
  tenancy_ocid     = var.oci_tenancy_ocid
  user_ocid        = var.oci_user_ocid
  fingerprint      = var.oci_fingerprint
  private_key_path = var.oci_private_key_path
}

# =============================================================================
# Networking — VCN, Internet Gateway, Route Table, Security List, Subnet
# =============================================================================

resource "oci_core_vcn" "financeapi" {
  compartment_id = var.compartment_ocid
  cidr_blocks    = ["10.0.0.0/16"]
  display_name   = "financeapi-vcn"
  dns_label      = "financeapi"
}

resource "oci_core_internet_gateway" "financeapi" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.financeapi.id
  display_name   = "financeapi-igw"
  enabled        = true
}

resource "oci_core_route_table" "financeapi" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.financeapi.id
  display_name   = "financeapi-rt"

  route_rules {
    destination       = "0.0.0.0/0"
    destination_type  = "CIDR_BLOCK"
    network_entity_id = oci_core_internet_gateway.financeapi.id
  }
}

resource "oci_core_security_list" "financeapi" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.financeapi.id
  display_name   = "financeapi-sl"

  # SSH
  ingress_security_rules {
    protocol    = "6" # TCP
    source      = "0.0.0.0/0"
    description = "SSH"
    tcp_options {
      min = 22
      max = 22
    }
  }

  # HTTP
  ingress_security_rules {
    protocol    = "6"
    source      = "0.0.0.0/0"
    description = "HTTP"
    tcp_options {
      min = 80
      max = 80
    }
  }

  # HTTPS
  ingress_security_rules {
    protocol    = "6"
    source      = "0.0.0.0/0"
    description = "HTTPS"
    tcp_options {
      min = 443
      max = 443
    }
  }

  # Egress: all traffic
  egress_security_rules {
    protocol    = "all"
    destination = "0.0.0.0/0"
    description = "Allow all outbound"
  }
}

resource "oci_core_subnet" "financeapi" {
  compartment_id             = var.compartment_ocid
  vcn_id                     = oci_core_vcn.financeapi.id
  cidr_block                 = "10.0.1.0/24"
  display_name               = "financeapi-subnet"
  dns_label                  = "subnet1"
  route_table_id             = oci_core_route_table.financeapi.id
  security_list_ids          = [oci_core_security_list.financeapi.id]
  prohibit_public_ip_on_vnic = false
}

# =============================================================================
# Compute Instance — VM.Standard.A1.Flex (Always Free)
# =============================================================================

resource "oci_core_instance" "financeapi" {
  compartment_id      = var.compartment_ocid
  availability_domain = data.oci_identity_availability_domains.ads.availability_domains[0].name
  display_name        = "financeapi-server"
  shape               = "VM.Standard.A1.Flex"

  shape_config {
    ocpus         = var.instance_ocpus
    memory_in_gbs = var.instance_memory_gb
  }

  source_details {
    source_type             = "image"
    source_id               = data.oci_core_images.ubuntu.images[0].id
    boot_volume_size_in_gbs = var.boot_volume_size_gb
  }

  create_vnic_details {
    subnet_id        = oci_core_subnet.financeapi.id
    assign_public_ip = true
    display_name     = "financeapi-vnic"
  }

  metadata = {
    ssh_authorized_keys = file(var.ssh_public_key_path)
    user_data           = base64encode(file("${path.module}/cloud-init.sh"))
  }

  freeform_tags = {
    Application = "FinanceAPI"
  }
}

# =============================================================================
# Data Sources
# =============================================================================

data "oci_identity_availability_domains" "ads" {
  compartment_id = var.oci_tenancy_ocid
}

data "oci_core_images" "ubuntu" {
  compartment_id           = var.compartment_ocid
  operating_system         = "Canonical Ubuntu"
  operating_system_version = "22.04"
  shape                    = "VM.Standard.A1.Flex"
  sort_by                  = "TIMECREATED"
  sort_order               = "DESC"
}

data "oci_core_vnic_attachments" "financeapi" {
  compartment_id = var.compartment_ocid
  instance_id    = oci_core_instance.financeapi.id
}

data "oci_core_vnic" "financeapi" {
  vnic_id = data.oci_core_vnic_attachments.financeapi.vnic_attachments[0].vnic_id
}
