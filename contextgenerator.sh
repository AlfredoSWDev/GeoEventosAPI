#!/bin/bash
# Guarda este archivo como contextgenerator.sh en la raíz de tu proyecto
# Dale permisos de ejecución con: chmod +x contextgenerator.sh
# Ejecútalo con: ./contextgenerator.sh

OUTPUT="API_context.txt"

echo "Generando estructura del proyecto (tree)..."
echo "=== ESTRUCTURA DEL PROYECTO ===" > "$OUTPUT"

# Intentamos usar el comando 'tree' si está instalado, sino usamos 'find'
if command -v tree &> /dev/null; then
    tree -I 'build|.gradle|.idea|node_modules|.git' -a >> "$OUTPUT"
else
    find . -type d -not -path '*/\.*' -not -path '*/build*' -not -path '*/node_modules*' | sort >> "$OUTPUT"
fi

echo -e "\n=== CONTENIDO DE LOS ARCHIVOS ===" >> "$OUTPUT"

# Buscar archivos clave de KMP (Kotlin, Gradle scripts, TOML, Properties, HTML, CSS, Markdown)
find . -type f \( -name "*.java" -o -name "*.gradle" -o -name "*.toml" -o -name "*.properties" -o -name "*.html" -o -name "*.css" -o -name "*.md" \) \
    -not -path '*/\.*' \
    -not -path '*/build/*' \
    -not -path '*/node_modules/*' \
    | sort | while read -r file; do

    echo "Agregando: $file"
    echo -e "\n\n========================================================" >> "$OUTPUT"
    echo "📄 ARCHIVO: $file" >> "$OUTPUT"
    echo "========================================================" >> "$OUTPUT"
    cat "$file" >> "$OUTPUT"
done

echo "¡Completado! Todo el contexto se ha guardado en: $OUTPUT"