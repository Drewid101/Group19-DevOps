#!/bin/sh
set -eu

log=$(mktemp)
trap 'rm -f "$log"' EXIT

if docker compose "$@" run --rm --no-deps app 127.0.0.1:1 0 > "$log" 2>&1; then
    cat "$log"
    echo "Expected an unreachable database to fail the app, but it succeeded." >&2
    exit 1
fi

cat "$log"
if ! grep -F "Could not connect to 127.0.0.1:1/world after 5 attempts." "$log" > /dev/null; then
    echo "Container failed for an unexpected reason, not exhausted database retries." >&2
    exit 1
fi

echo "Verified exhausted database retries produce a failed container run."
