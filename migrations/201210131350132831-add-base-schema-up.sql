CREATE TABLE posts (
id serial PRIMARY KEY,
content text,
created_at timestamp with time zone NOT NULL DEFAULT current_timestamp,
author varchar
);
