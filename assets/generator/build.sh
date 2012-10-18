#!/bin/bash

middleman build
rm -Rf ../../public/*
rm -Rf ../../src/gratefulplace/templates/*
cp -R build/* ../../public
mv ../../public/*.html ../../src/gratefulplace/templates
mv ../../public/users ../../src/gratefulplace/templates
