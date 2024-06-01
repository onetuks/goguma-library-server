#!/bin/bash

network_ids=$(docker network ls -q)

for id in $network_ids; do
  # shellcheck disable=SC2086
  network_info=$(docker network inspect -f '{{.Name}} {{.Containers}}' $id)

  if [[ $network_info == *"map[]"* ]]; then
    # shellcheck disable=SC2086
    echo "Removing network: $(echo $network_info | cut -d' ' -f1)"
    # shellcheck disable=SC2086
    docker network rm $id
  fi
done