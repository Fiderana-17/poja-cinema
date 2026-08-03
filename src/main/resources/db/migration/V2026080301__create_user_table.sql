CREATE TABLE "user" (
                        id UUID PRIMARY KEY,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        birthdate DATE NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        role VARCHAR(50) NOT NULL
);