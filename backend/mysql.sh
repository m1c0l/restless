#!/bin/bash

config='config.py'
db_info=($(grep 'SQLALCHEMY_DATABASE_URI' $(dirname ${BASH_SOURCE[0]})/$config |
          sed 's;.*mysql://;;' |
          awk -F "[:@/']" '{print $1; print $2; print $4;}'))

user=${db_info[0]}
pass=${db_info[1]}
db=${db_info[2]}

mysql $db -u $user -p$pass
