#!/bin/bash

WORKER_LOG="worker.log"

echo "啟動 Worker..."
./gradlew bootRun > "$WORKER_LOG" 2>&1 &
WORKER_PID=$!
echo "Worker PID: $WORKER_PID"

trap "echo '停止 Worker...'; kill $WORKER_PID 2>/dev/null; rm -f \"$WORKER_LOG\"; exit" EXIT INT TERM

echo "等待 Worker 啟動..."
for i in {1..30}; do
    if grep -q "Started TemporalApplication" "$WORKER_LOG" 2>/dev/null; then
        echo "Worker 已啟動"
        break
    fi
    sleep 1
done
echo ""
