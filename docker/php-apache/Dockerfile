FROM php:8.3-apache

RUN docker-php-ext-install pdo pdo_mysql

RUN a2enmod headers rewrite

RUN apt-get update && apt-get install -y libzip-dev \
    && docker-php-ext-install zip

COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

WORKDIR /var/www/html/openMinds
