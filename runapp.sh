#!/bin/sh

echo "Waiting for PostgreSQL to be ready..."
until nc -z db 5432; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

echo "PostgreSQL is up. Starting the app..."
exec java -jar app.jar