# Home Server Setup — FinanceAPI

Documentação da migração da infraestrutura AWS para um servidor doméstico.

---

## Contexto

A infraestrutura anterior rodava na AWS (EC2 t3.micro + RDS MySQL db.t3.micro) com custo de ~$24/mês (~R$140/mês). A decisão de migrar para um servidor doméstico foi motivada pela redução de custo e pelo controle total da infraestrutura.

---

## Hardware do Servidor

| Componente | Especificação |
|---|---|
| CPU | Intel Core i5-3330, 4 cores, 3.0GHz |
| RAM | 8GB (2× 4GB) |
| Disco | 500GB HDD |
| Sistema | Ubuntu 24.04.4 LTS (instalação limpa, dedicado) |

---

## Problema de Rede — CGNAT

A conexão residencial da Claro/NET usa CGNAT (Carrier-Grade NAT), o que impede exposição direta de portas ao exterior. A solução adotada foi o **Cloudflare Tunnel**, que cria uma conexão de saída do servidor para a Cloudflare sem necessidade de abrir portas no roteador.

---

## Domínio

- Registrado em **registro.br**: `financeapi.com.br` (~R$40/ano)
- Nameservers apontados para a Cloudflare
- Estrutura de subdomínios:

| Subdomínio | Destino |
|---|---|
| `financeapi.com.br` | Frontend (Vercel) |
| `www.financeapi.com.br` | Frontend (Vercel) |
| `api.financeapi.com.br` | FinanceAPI (home server) |
| `ssh.financeapi.com.br` | SSH (home server) |

---

## Instalação do Docker

```bash
sudo apt update && sudo apt install -y ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg
echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu noble stable" | sudo tee /etc/apt/sources.list.d/docker.list
sudo apt update && sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo usermod -aG docker $USER && newgrp docker
```

---

## Cloudflare Tunnel

### Instalação

```bash
curl -L https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64.deb -o cloudflared.deb
sudo dpkg -i cloudflared.deb
```

### Configuração

```bash
# Autenticar com a conta Cloudflare
cloudflared tunnel login

# Criar tunnel
cloudflared tunnel create servidor-casa

# Criar registros DNS
cloudflared tunnel route dns servidor-casa api.financeapi.com.br
cloudflared tunnel route dns servidor-casa ssh.financeapi.com.br
```

### Arquivo de configuração `/etc/cloudflared/config.yml`

```yaml
tunnel: 125b8ea7-eb3d-4082-8e05-ed39ce44bba9
credentials-file: /etc/cloudflared/125b8ea7-eb3d-4082-8e05-ed39ce44bba9.json

ingress:
  - hostname: api.financeapi.com.br
    service: http://localhost:80
  - hostname: www.api.financeapi.com.br
    service: http://localhost:80
  - hostname: ssh.financeapi.com.br
    service: ssh://localhost:22
  - service: http_status:404
```

### Instalar como serviço

```bash
sudo mkdir -p /etc/cloudflared
sudo cp ~/.cloudflared/config.yml /etc/cloudflared/
sudo cp ~/.cloudflared/<tunnel-id>.json /etc/cloudflared/
sudo sed -i 's|/home/gustavo/.cloudflared/|/etc/cloudflared/|g' /etc/cloudflared/config.yml
sudo cloudflared service install
sudo systemctl enable cloudflared
sudo systemctl start cloudflared
```

---

## Estrutura do Servidor

```
~/servidor/
└── financeapi/         # git clone do repositório
    ├── nginx/
    │   └── nginx.prod.conf
    ├── docker-compose.prod.yml
    └── .env            # não vai para o git
```

---

## Arquivo `.env`

```env
DATABASE_PASSWORD=...
JWT_SECRET=...
MASTER_KEY=...
PLUGGY_CLIENT_ID=...
PLUGGY_CLIENT_SECRET=...
IMAGE_TAG=latest
CORS_ALLOWED_ORIGINS=https://financeapi.com.br
SPRING_PROFILES_ACTIVE=prod
```

---

## Nginx (`nginx/nginx.prod.conf`)

SSL é gerenciado pela Cloudflare — o Nginx escuta apenas HTTP na porta 80.

```nginx
server {
    listen 80;
    server_name api.financeapi.com.br;

    location / {
        proxy_pass http://finance-api:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## Deploy — Watchtower

O Watchtower monitora o Docker Hub e atualiza os containers automaticamente quando uma nova imagem é publicada.

```yaml
watchtower:
  image: containrrr/watchtower
  container_name: finance-watchtower
  restart: unless-stopped
  volumes:
    - /var/run/docker.sock:/var/run/docker.sock
  command: --interval 30 finance-api
```

### Fluxo de deploy

```
git push → main
    ↓
GitHub Actions (build + test + push Docker Hub)
    ↓
gustavodamassa/finance-api:latest atualizado
    ↓
Watchtower detecta (~30s)
    ↓
pull + restart automático
```

---

## Acesso SSH Remoto

Via Cloudflare Tunnel, sem abrir portas no roteador:

```bash
ssh gustavo@192.168.0.244          # rede local
ssh -o "ProxyCommand cloudflared access ssh --hostname ssh.financeapi.com.br" gustavo@ssh.financeapi.com.br  # remoto
```

---

## Subir os containers

```bash
cd ~/servidor/financeapi
docker compose -f docker-compose.prod.yml up -d
```

---

## Alterações feitas no CI/CD

- Removido job `deploy` (Oracle Cloud) — substituído pelo Watchtower
- Removida configuração Kafka Upstash SASL/SSL do perfil `prod` — API usa Kafka local

---

## Custo comparativo

| Infraestrutura | Custo/mês |
|---|---|
| AWS (EC2 + RDS) | ~$24 (~R$140) |
| Home server (energia) | ~R$55–70 |
| Domínio financeapi.com.br | ~R$3,30 (R$40/ano) |
| **Total home server** | **~R$60–75/mês** |
