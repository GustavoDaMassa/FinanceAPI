#!/bin/bash
# =============================================================================
# Retry terraform apply until instance is created (OCI Out of Capacity workaround)
# Usage: ./retry-apply.sh [interval_seconds]
# =============================================================================

INTERVAL="${1:-60}"
ATTEMPT=0

echo "=== OCI Instance Retry Script ==="
echo "Interval: ${INTERVAL}s | Started: $(date)"
echo "Press Ctrl+C to stop"
echo "================================="

while true; do
    ATTEMPT=$((ATTEMPT + 1))
    echo ""
    echo "[Attempt #${ATTEMPT}] $(date)"

    OUTPUT=$(terraform apply -auto-approve 2>&1)

    if echo "$OUTPUT" | grep -q "Apply complete"; then
        echo ""
        echo "========================================="
        echo "SUCCESS! Instance created on attempt #${ATTEMPT}"
        echo "========================================="
        echo "$OUTPUT" | grep -E "(Apply complete|instance_public_ip|ssh_command)"
        exit 0
    fi

    if echo "$OUTPUT" | grep -q "Out of host capacity"; then
        echo "  Out of capacity. Retrying in ${INTERVAL}s..."
    else
        echo "  Unexpected error:"
        echo "$OUTPUT" | grep "Error:" | head -3
        echo "  Retrying in ${INTERVAL}s..."
    fi

    sleep "$INTERVAL"
done
