#!/bin/bash

middleman build
rm -Rf ../../public/*
cp -R build/* ../../public
mv ../../public/*.html ../../src/gratefulplace/templates
mv ../../public/users ../../src/gratefulplace/templates
