#!/bin/sh
set -eu

if [ "$#" -ne 1 ]; then
    echo "Usage: sh scripts/verify-reports.sh <generated-report-directory>" >&2
    exit 2
fi

output=$1
root=$(CDPATH= cd "$(dirname "$0")/.." && pwd)

if [ ! -d "$output" ]; then
    echo "Report directory does not exist: $output" >&2
    exit 1
fi

count=$(find "$output" -maxdepth 1 -type f -name '*.md' | wc -l | tr -d '[:space:]')
if [ "$count" -ne 32 ]; then
    echo "Expected 32 Markdown reports, found $count in $output." >&2
    exit 1
fi

for expected in "$root"/reports/*.md; do
    filename=$(basename "$expected")
    if [ ! -s "$output/$filename" ]; then
        echo "Missing or empty report: $filename" >&2
        exit 1
    fi
    if ! cmp -s "$expected" "$output/$filename"; then
        echo "Generated report differs from the committed baseline: $filename" >&2
        diff -u "$expected" "$output/$filename" >&2
        exit 1
    fi
done

echo "Verified all 32 reports: filenames and contents match the committed baseline."
