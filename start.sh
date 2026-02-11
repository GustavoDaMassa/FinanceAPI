#!/bin/bash
set -e

IMAGE_TAG="${1:-latest}"

if [ ! -f .env ]; then
    echo "ERROR: .env file not found. Create it with the required secrets."
    exit 1
fi

source .env
export IMAGE_TAG

echo "=== Deploying FinanceAPI (tag: $IMAGE_TAG) ==="

echo ">> Pulling latest images..."
docker compose -f docker-compose.prod.yml pull finance-api nginx

echo ">> Starting services..."
docker compose -f docker-compose.prod.yml up -d

echo ">> Pruning old images..."
docker image prune -f

echo "=== Deploy complete ==="
docker compose -f docker-compose.prod.yml ps
