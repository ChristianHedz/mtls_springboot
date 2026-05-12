#!/usr/bin/env bash
#
# generate-pki.sh — Genera la PKI completa para el ejercicio de mTLS:
#   1. Una CA raíz local
#   2. Keystore + truststore del Provider, firmado por la CA
#   3. Keystore + truststore del Consumer, firmado por la CA
#   4. Keystore para Postman, firmado por la CA
#
# Uso:
#   bash generate-pki.sh
#
# Resultado: archivos .p12 / .crt en el directorio actual, listos para copiar
# a src/main/resources/keystore/ de cada proyecto e importar en Postman.

set -euo pipefail

# -------------------------------------------------------------------------
# Parámetros (cambia STOREPASS si quieres password distinto, mismo en todo)
# -------------------------------------------------------------------------
STOREPASS="changeit"
KEYPASS="changeit"
VALIDITY_CA=3650           # 10 años para la CA
VALIDITY_CERT=825          # límite Apple/Chrome para certs TLS
KEYALG_CA="RSA"
KEYSIZE_CA=4096
KEYALG="RSA"
KEYSIZE=2048
SIGALG="SHA256withRSA"

ORG="Capacitacion"
COUNTRY="MX"
SAN_EXT="san=dns:localhost,ip:127.0.0.1"

CA_ALIAS="mtls-ca"
CA_KEYSTORE="ca.p12"
CA_CERT="ca.crt"

# -------------------------------------------------------------------------
# Limpieza previa para que el script sea idempotente
# -------------------------------------------------------------------------
echo "[+] Limpiando artefactos previos..."
rm -f ca.p12 ca.crt \
      provider-keystore.p12 provider-truststore.p12 provider.csr provider.crt \
      consumer-keystore.p12 consumer-truststore.p12 consumer.csr consumer.crt \
      postman-keystore.p12 postman.csr postman.crt

# -------------------------------------------------------------------------
# 1. CA RAÍZ
# -------------------------------------------------------------------------
echo "[+] Generando CA raíz ($CA_ALIAS)..."
keytool -genkeypair -alias "$CA_ALIAS" \
  -keyalg "$KEYALG_CA" -keysize "$KEYSIZE_CA" -sigalg "$SIGALG" \
  -validity "$VALIDITY_CA" \
  -dname "CN=mTLS-Local-CA, O=$ORG, C=$COUNTRY" \
  -ext "bc:c" \
  -keystore "$CA_KEYSTORE" -storetype PKCS12 \
  -storepass "$STOREPASS" -keypass "$KEYPASS"

echo "[+] Exportando cert público de la CA -> $CA_CERT"
keytool -exportcert -alias "$CA_ALIAS" -rfc \
  -keystore "$CA_KEYSTORE" -storepass "$STOREPASS" \
  -file "$CA_CERT"

# -------------------------------------------------------------------------
# Función auxiliar: genera keystore firmado por la CA
#   $1 = alias / nombre lógico (provider | consumer | postman)
#   $2 = CN del cert
# -------------------------------------------------------------------------
generate_signed_keystore() {
  local NAME="$1"
  local CN="$2"
  local KS="${NAME}-keystore.p12"
  local CSR="${NAME}.csr"
  local CRT="${NAME}.crt"

  echo "[+] [$NAME] Generando par de claves..."
  keytool -genkeypair -alias "$NAME" \
    -keyalg "$KEYALG" -keysize "$KEYSIZE" -sigalg "$SIGALG" \
    -validity "$VALIDITY_CERT" \
    -dname "CN=$CN, O=$ORG, C=$COUNTRY" \
    -ext "$SAN_EXT" \
    -keystore "$KS" -storetype PKCS12 \
    -storepass "$STOREPASS" -keypass "$KEYPASS"

  echo "[+] [$NAME] Generando CSR..."
  keytool -certreq -alias "$NAME" \
    -keystore "$KS" -storepass "$STOREPASS" \
    -ext "$SAN_EXT" \
    -file "$CSR"

  echo "[+] [$NAME] Firmando CSR con la CA..."
  keytool -gencert -alias "$CA_ALIAS" \
    -keystore "$CA_KEYSTORE" -storepass "$STOREPASS" \
    -infile "$CSR" -outfile "$CRT" \
    -validity "$VALIDITY_CERT" \
    -ext "$SAN_EXT" \
    -ext "ku=digitalSignature,keyEncipherment" \
    -ext "eku=serverAuth,clientAuth" \
    -rfc

  echo "[+] [$NAME] Importando CA en su keystore (para construir cadena)..."
  keytool -importcert -alias "$CA_ALIAS" -file "$CA_CERT" -noprompt \
    -keystore "$KS" -storepass "$STOREPASS"

  echo "[+] [$NAME] Importando cert firmado (reemplaza el self-signed inicial)..."
  keytool -importcert -alias "$NAME" -file "$CRT" -noprompt \
    -keystore "$KS" -storepass "$STOREPASS"
}

# -------------------------------------------------------------------------
# Función auxiliar: genera truststore con la CA
#   $1 = nombre lógico
# -------------------------------------------------------------------------
generate_truststore() {
  local NAME="$1"
  local TS="${NAME}-truststore.p12"

  echo "[+] [$NAME] Generando truststore (solo CA)..."
  keytool -importcert -alias "$CA_ALIAS" -file "$CA_CERT" -noprompt \
    -keystore "$TS" -storetype PKCS12 -storepass "$STOREPASS"
}

# -------------------------------------------------------------------------
# 2. PROVIDER
# -------------------------------------------------------------------------
generate_signed_keystore "provider" "provider-service"
generate_truststore     "provider"

# -------------------------------------------------------------------------
# 3. CONSUMER
# -------------------------------------------------------------------------
generate_signed_keystore "consumer" "consumer-service"
generate_truststore     "consumer"

# -------------------------------------------------------------------------
# 4. POSTMAN
# -------------------------------------------------------------------------
generate_signed_keystore "postman" "postman-client"

# -------------------------------------------------------------------------
# 5. Resumen y verificación
# -------------------------------------------------------------------------
echo ""
echo "=========================================================="
echo "  PKI generada correctamente en: $(pwd)"
echo "=========================================================="
echo ""
echo "Artefactos:"
ls -la *.p12 *.crt
echo ""
echo "Verificación rápida (debe verse 'Certificate chain length: 2'):"
echo ""
keytool -list -v -keystore provider-keystore.p12 -storepass "$STOREPASS" -alias provider \
  | grep -E "Alias name|Owner|Issuer|Certificate chain length|SubjectAlternativeName" || true
echo ""
echo "Próximo paso:"
echo "  cp provider-keystore.p12 provider-truststore.p12 ../users-provider/src/main/resources/keystore/"
echo "  cp consumer-keystore.p12 consumer-truststore.p12 ../users-consumer/src/main/resources/keystore/"
echo "  Importar ca.crt y postman-keystore.p12 en Postman → Settings → Certificates"
