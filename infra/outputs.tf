# =============================================================================
# FinanceAPI Terraform Outputs
# =============================================================================

output "instance_public_ip" {
  description = "Public IP address of the compute instance"
  value       = data.oci_core_vnic.financeapi.public_ip_address
}

output "instance_id" {
  description = "OCID of the compute instance"
  value       = oci_core_instance.financeapi.id
}

output "ssh_command" {
  description = "SSH command to connect to the instance"
  value       = "ssh ubuntu@${data.oci_core_vnic.financeapi.public_ip_address}"
}

output "vcn_id" {
  description = "OCID of the VCN"
  value       = oci_core_vcn.financeapi.id
}
