FROM php:5.6-apache

# Active mod_rewrite pour les URL propres
RUN a2enmod rewrite

# Active le parsing des fichiers .php3
RUN echo 'AddType application/x-httpd-php .php3' > /etc/apache2/conf-available/php3.conf \
    && echo 'DirectoryIndex index.php3 index.php index.html' >> /etc/apache2/conf-available/php3.conf \
    && a2enconf php3

# Répare les dépôts Debian Stretch obsolètes en pointant vers l'archive officielle
RUN sed -i 's/deb.debian.org/archive.debian.org/g' /etc/apt/sources.list \
    && sed -i 's/security.debian.org/archive.debian.org/g' /etc/apt/sources.list \
    && sed -i '/stretch-updates/d' /etc/apt/sources.list

# Option d'acquisition pour ignorer la vérification des clés GPG expirées
RUN echo "Acquire::Check-Valid-Until \"false\";" > /etc/apt/apt.conf.d/99no-check-valid-until

# Installe la bibliothèque avec le flag d'autorisation, puis installe les extensions PHP
RUN apt-get update && apt-get install -y --allow-unauthenticated libzip-dev \
    && docker-php-ext-install mysqli pdo_mysql zip

# Active les balises courtes <?
RUN echo "short_open_tag = On" > /usr/local/etc/php/conf.d/short-tags.ini