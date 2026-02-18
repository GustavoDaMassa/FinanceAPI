# =============================================================================
# FinanceAPI Terraform Variables — Oracle Cloud
# =============================================================================

# =============================================================================
# OCI Provider Authentication
# =============================================================================

variable "oci_region" {
  description = "OCI region"
  type        = string
  default     = "sa-saopaulo-1"
}

variable "oci_tenancy_ocid" {
  description = "OCID of the OCI tenancy"
  type        = string
}

variable "oci_user_ocid" {
  description = "OCID of the OCI user"
  type        = string
}

variable "oci_fingerprint" {
  description = "Fingerprint of the OCI API signing key"
  type        = string
}

variable "oci_private_key_path" {
  description = "Path to the OCI API private key (.pem)"
  type        = string
  default     = "~/.oci/oci_api_key.pem"
}

variable "compartment_ocid" {
  description = "OCID of the compartment to create resources in"
  type        = string
}

# =============================================================================
# SSH Access
# =============================================================================

variable "ssh_public_key_path" {
  description = "Path to the SSH public key for instance access"
  type        = string
  default     = "~/.ssh/id_ed25519.pub"
}

# =============================================================================
# Compute Instance
# =============================================================================

variable "instance_ocpus" {
  description = "Number of OCPUs for the instance (Always Free: up to 4)"
  type        = number
  default     = 2
}

variable "instance_memory_gb" {
  description = "Memory in GB for the instance (Always Free: up to 24)"
  type        = number
  default     = 8
}

variable "boot_volume_size_gb" {
  description = "Boot volume size in GB (Always Free: up to 200)"
  type        = number
  default     = 50
}
