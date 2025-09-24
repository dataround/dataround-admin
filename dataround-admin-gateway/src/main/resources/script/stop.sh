#!/usr/bin/env bash

pid=`ps aux |grep "dataround-gateway" |grep -v grep |awk '{print $2}'`

if [ -z "$pid" ]; then
  echo "dataround-gateway is not running."
  exit 0
fi

echo "Attempting to gracefully shut down dataround-gateway (pid $pid)."
kill $pid

# Wait for a maximum of 10 seconds for the process to terminate
for i in {1..10}; do
  if ! ps -p $pid > /dev/null; then
    echo "dataround-gateway shut down gracefully."
    exit 0
  fi
  sleep 1
done

# If the process is still running, force kill it
if ps -p $pid > /dev/null; then
  echo "Graceful shutdown failed. Forcing shutdown with kill -9."
  kill -9 $pid
  sleep 1 # Give it a moment to die
fi

if ! ps -p $pid > /dev/null; then
  echo "dataround-gateway has been stopped."
else
  echo "Failed to stop dataround-gateway (pid $pid). Please check manually."
fi
