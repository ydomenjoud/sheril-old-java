FROM php:5.6-apache

# Active le parsing des fichiers .php3
RUN echo 'AddType application/x-httpd-php .php3' > /etc/apache2/conf-available/php3.conf \
    && echo 'DirectoryIndex index.php3 index.php index.html' >> /etc/apache2/conf-available/php3.conf \
    && a2enconf php3

# Active les extensions MySQL de PHP 5
RUN docker-php-ext-install mysqli pdo_mysql
