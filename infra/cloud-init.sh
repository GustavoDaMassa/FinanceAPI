#!/bin/bash
# =============================================================================
# FinanceAPI Cloud-Init Script
# Ubuntu 22.04 aarch64 — Oracle Cloud Always Free
# =============================================================================

set -e

# Update system packages
apt update && apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com | sh
systemctl enable docker
systemctl start docker

# Add ubuntu user to docker group
usermod -aG docker ubuntu

# Install Docker Compose v2 plugin (ARM64)
mkdir -p /usr/local/lib/docker/cli-plugins
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-aarch64 \
  -o /usr/local/lib/docker/cli-plugins/docker-compose
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

# Open iptables for HTTP and HTTPS (OCI Ubuntu blocks by default)
iptables -I INPUT 6 -m state --state NEW -p tcp --dport 80 -j ACCEPT
iptables -I INPUT 6 -m state --state NEW -p tcp --dport 443 -j ACCEPT

# Persist iptables rules across reboots
DEBIAN_FRONTEND=noninteractive apt install -y iptables-persistent
netfilter-persistent save

# Create application directory
mkdir -p /opt/financeapi/nginx
chown -R ubuntu:ubuntu /opt/financeapi

# Log completion
echo "cloud-init completed at $(date)" >> /var/log/cloud-init-custom.log
