#!/bin/bash

#add a new migration
touch "migrations/`date +%Y%m%d%s`-$1-up.sql"
touch "migrations/`date +%Y%m%d%s`-$1-down.sql"
