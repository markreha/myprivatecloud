FROM node:10-alpine
ENV PORT 8080

WORKDIR /usr/src/app
COPY . /defaultapp
RUN chmod 755 /defaultapp/startup.sh

RUN npm install -g nodemon
RUN npm install

ENTRYPOINT ["/defaultapp/startup.sh"]



