FROM node:alpine

# Copy package.json and package-lock.json to the image
# We need them to build the dependencies for our project

RUN npm i npm@latest -g
COPY package.json package.json
COPY package-lock.json package-lock.json

# Run `npm ci` to install project dependencies
RUN npm ci

# Install npm dependencies including axios and bootstrap
RUN npm install axios bootstrap

# install react-leaflet and leaflet
RUN npm install react-leaflet leaflet

# Set environment variables
ENV CI=true
ENV PORT=3000

# Copy the entire project directory to the image
COPY . .

# Default command to start the app
CMD [ "npm", "start" ]
