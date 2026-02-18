# Deploy FinanceAPI na AWS (EC2 + RDS)

## Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                         AWS Cloud                            │
│  ┌─────────────────┐         ┌─────────────────────────┐   │
│  │   RDS PostgreSQL │◄───────│        EC2 Instance      │   │
│  │   (db.t3.micro)  │        │  ┌───────────────────┐  │   │
│  └─────────────────┘         │  │  Docker Compose   │  │   │
│                               │  │  - Kafka          │  │   │
│                               │  │  - Zookeeper      │  │   │
│                               │  │  - FinanceAPI     │  │   │
│                               │  └───────────────────┘  │   │
│                               └─────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Custos Estimados (Free Tier - 12 meses)

| Serviço | Free Tier | Depois |
|---------|-----------|--------|
| EC2 t2.micro | 750h/mês grátis | ~$8/mês |
| RDS db.t3.micro | 750h/mês grátis | ~$13/mês |
| Storage | 30GB grátis | ~$0.10/GB |

---

## Passo 1: Criar Conta AWS

1. Acesse https://aws.amazon.com/free
2. Clique em "Create a Free Account"
3. Preencha email e dados pessoais
4. **Cartão de crédito**: Necessário (não será cobrado no Free Tier)
5. Verificação por telefone
6. Selecione plano "Basic Support - Free"

---

## Passo 2: Instalar e Configurar AWS CLI

### Linux (Ubuntu/Debian)
```bash
# Instalar AWS CLI v2
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Verificar instalação
aws --version
```

### Criar Access Keys
1. AWS Console → Seu nome (canto superior direito) → Security Credentials
2. Access keys → Create access key
3. Selecione "Command Line Interface (CLI)"
4. Salve o Access Key ID e Secret Access Key

### Configurar CLI
```bash
aws configure
# AWS Access Key ID: [sua_access_key]
# AWS Secret Access Key: [sua_secret_key]
# Default region name: sa-east-1
# Default output format: json
```

---

## Passo 3: Criar Security Groups

### Security Group para RDS
```bash
# Criar VPC (se não existir default)
VPC_ID=$(aws ec2 describe-vpcs --filters "Name=isDefault,Values=true" --query "Vpcs[0].VpcId" --output text)

# Criar Security Group para RDS
aws ec2 create-security-group \
  --group-name financeapi-rds-sg \
  --description "Security group for FinanceAPI RDS" \
  --vpc-id $VPC_ID

# Obter ID do Security Group
RDS_SG_ID=$(aws ec2 describe-security-groups --group-names financeapi-rds-sg --query "SecurityGroups[0].GroupId" --output text)
echo "RDS Security Group ID: $RDS_SG_ID"
```

### Security Group para EC2
```bash
# Criar Security Group para EC2
aws ec2 create-security-group \
  --group-name financeapi-ec2-sg \
  --description "Security group for FinanceAPI EC2" \
  --vpc-id $VPC_ID

EC2_SG_ID=$(aws ec2 describe-security-groups --group-names financeapi-ec2-sg --query "SecurityGroups[0].GroupId" --output text)
echo "EC2 Security Group ID: $EC2_SG_ID"

# Liberar SSH (porta 22)
aws ec2 authorize-security-group-ingress \
  --group-id $EC2_SG_ID \
  --protocol tcp \
  --port 22 \
  --cidr 0.0.0.0/0

# Liberar HTTP (porta 8080)
aws ec2 authorize-security-group-ingress \
  --group-id $EC2_SG_ID \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0

# Liberar RDS apenas do EC2 Security Group
aws ec2 authorize-security-group-ingress \
  --group-id $RDS_SG_ID \
  --protocol tcp \
  --port 5432 \
  --source-group $EC2_SG_ID
```

---

## Passo 4: Criar RDS PostgreSQL

```bash
# Criar subnet group (necessário para RDS)
SUBNETS=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" --query "Subnets[*].SubnetId" --output text | tr '\t' ',')

aws rds create-db-subnet-group \
  --db-subnet-group-name financeapi-subnet-group \
  --db-subnet-group-description "Subnet group for FinanceAPI" \
  --subnet-ids $(echo $SUBNETS | tr ',' ' ')

# Criar instância RDS
aws rds create-db-instance \
  --db-instance-identifier financeapi-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 15 \
  --master-username postgres \
  --master-user-password SuaSenhaSegura123! \
  --allocated-storage 20 \
  --db-name financeapi \
  --vpc-security-group-ids $RDS_SG_ID \
  --db-subnet-group-name financeapi-subnet-group \
  --publicly-accessible \
  --no-multi-az \
  --storage-type gp2

# Aguardar RDS ficar disponível (pode levar 5-10 minutos)
aws rds wait db-instance-available --db-instance-identifier financeapi-db

# Obter endpoint do RDS
RDS_ENDPOINT=$(aws rds describe-db-instances --db-instance-identifier financeapi-db --query "DBInstances[0].Endpoint.Address" --output text)
echo "RDS Endpoint: $RDS_ENDPOINT"
```

---

## Passo 5: Criar Key Pair para EC2

```bash
# Criar key pair
aws ec2 create-key-pair \
  --key-name financeapi-key \
  --query "KeyMaterial" \
  --output text > ~/.ssh/financeapi-key.pem

# Ajustar permissões
chmod 400 ~/.ssh/financeapi-key.pem
```

---

## Passo 6: Criar EC2 Instance

```bash
# Obter AMI mais recente do Ubuntu 22.04
AMI_ID=$(aws ec2 describe-images \
  --owners 099720109477 \
  --filters "Name=name,Values=ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*" \
  --query "Images | sort_by(@, &CreationDate) | [-1].ImageId" \
  --output text)

# Criar EC2 (t2.medium para Kafka - t2.micro não tem RAM suficiente)
aws ec2 run-instances \
  --image-id $AMI_ID \
  --instance-type t2.medium \
  --key-name financeapi-key \
  --security-group-ids $EC2_SG_ID \
  --block-device-mappings "[{\"DeviceName\":\"/dev/sda1\",\"Ebs\":{\"VolumeSize\":30}}]" \
  --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=financeapi-server}]" \
  --count 1

# Obter Instance ID
INSTANCE_ID=$(aws ec2 describe-instances \
  --filters "Name=tag:Name,Values=financeapi-server" "Name=instance-state-name,Values=running,pending" \
  --query "Reservations[0].Instances[0].InstanceId" \
  --output text)

# Aguardar instância ficar pronta
aws ec2 wait instance-running --instance-ids $INSTANCE_ID

# Obter IP público
EC2_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID --query "Reservations[0].Instances[0].PublicIpAddress" --output text)
echo "EC2 Public IP: $EC2_IP"
```

**Nota**: t2.medium não está no Free Tier (~$0.046/h). Alternativa: usar t3.small (~$0.02/h).

---

## Passo 7: Configurar EC2

### Conectar via SSH
```bash
ssh -i ~/.ssh/financeapi-key.pem ubuntu@$EC2_IP
```

### Instalar Docker e Docker Compose
```bash
# Atualizar sistema
sudo apt update && sudo apt upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Relogar para aplicar grupo docker
exit
```

### Reconectar e verificar
```bash
ssh -i ~/.ssh/financeapi-key.pem ubuntu@$EC2_IP
docker --version
docker-compose --version
```

---

## Passo 8: Deploy da Aplicação

### Clonar repositório no EC2
```bash
git clone https://github.com/GustavoDaMassa/FinanceAPI.git
cd FinanceAPI
```

### Criar arquivo de ambiente
```bash
cat > .env << 'EOF'
# Database (RDS)
DB_HOST=SEU_RDS_ENDPOINT
DB_NAME=financeapi
DB_USER=postgres
DB_PASSWORD=SuaSenhaSegura123!

# JWT
JWT_SECRET=SuaChaveJWTSegura404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000

# Master Key
MASTER_KEY=SuaMasterKeySegura123!
EOF
```

### Usar docker-compose de produção
```bash
# O arquivo docker-compose.prod.yaml já foi criado
docker-compose -f docker-compose.prod.yaml up -d --build
```

### Verificar logs
```bash
docker logs finance-api -f
```

---

## Passo 9: Testar API

```bash
# Health check
curl http://$EC2_IP:8080/actuator/health

# GraphiQL (navegador)
# http://$EC2_IP:8080/graphiql
```

---

## Passo 10: Configurar Domínio (Opcional)

Para ter um domínio como `api.seusite.com`:

1. Registre um domínio (Namecheap, GoDaddy, etc.)
2. No Route 53 ou DNS do registrador, crie um registro A apontando para o IP do EC2
3. Configure HTTPS com Let's Encrypt

---

## Comandos Úteis

```bash
# Ver logs
docker logs finance-api -f

# Reiniciar serviços
docker-compose -f docker-compose.prod.yaml restart

# Parar tudo
docker-compose -f docker-compose.prod.yaml down

# Rebuild e reiniciar
docker-compose -f docker-compose.prod.yaml up -d --build

# Ver uso de recursos
docker stats
```

---

## Limpeza (para não ser cobrado)

```bash
# Terminar EC2
aws ec2 terminate-instances --instance-ids $INSTANCE_ID

# Deletar RDS
aws rds delete-db-instance --db-instance-identifier financeapi-db --skip-final-snapshot

# Deletar Security Groups (após EC2 e RDS serem deletados)
aws ec2 delete-security-group --group-id $EC2_SG_ID
aws ec2 delete-security-group --group-id $RDS_SG_ID

# Deletar Key Pair
aws ec2 delete-key-pair --key-name financeapi-key
rm ~/.ssh/financeapi-key.pem
```
