# ==== CONFIGURE =====
# Use a Node 16 base image
FROM node:16-alpine
# Set the working directory to /app inside the container
WORKDIR /app
# Copy app files
COPY . .
# ==== BUILD =====
# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
ENV NODE_ENV development
EXPOSE 3000
# Build the app
RUN npm install
# ==== RUN =======
# Set the env to "production"
# Expose the port on which the app will be running (3000 is the default that `serve` uses)
# Start the app
CMD [ "npm", "start" ]