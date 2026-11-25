#!/usr/bin/env bash
set -euo pipefail

# Render locally with a separately installed PlantUML JAR; no network requests.
# Usage: JAVA_BIN=/path/to/java ./render-uml.sh /absolute/path/plantuml.jar
# Or set PLANTUML_JAR=/absolute/path/plantuml.jar and run ./render-uml.sh.
if (( $# > 1 )); then
  printf 'Usage: %s [/absolute/path/plantuml.jar]\n' "$0" >&2
  exit 2
fi

uml_jar="${1:-${PLANTUML_JAR:-}}"
java_command="${JAVA_BIN:-java}"
case "$uml_jar" in
  /*) ;;
  *) printf 'Provide an absolute PlantUML JAR path as an argument or PLANTUML_JAR.\n' >&2; exit 2 ;;
esac
if [[ ! -f "$uml_jar" || ! -r "$uml_jar" ]]; then
  printf 'Cannot read PlantUML JAR: %s\n' "$uml_jar" >&2
  exit 2
fi
if ! command -v "$java_command" >/dev/null 2>&1; then
  printf 'Java executable not found: %s\n' "$java_command" >&2
  exit 2
fi

diagram_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd -- "$diagram_dir"
sources=( ./*.puml )
if [[ ! -f "${sources[0]}" ]]; then
  printf 'No PlantUML sources found in %s\n' "$diagram_dir" >&2
  exit 2
fi

"$java_command" -Djava.awt.headless=true -DPLANTUML_LIMIT_SIZE=16384 \
  -jar "$uml_jar" -charset UTF-8 -failfast2 -tsvg "${sources[@]}"
"$java_command" -Djava.awt.headless=true -DPLANTUML_LIMIT_SIZE=16384 \
  -jar "$uml_jar" -charset UTF-8 -failfast2 -tpng "${sources[@]}"
printf 'Rendered %s UML sources to SVG and PNG in %s\n' "${#sources[@]}" "$diagram_dir"
