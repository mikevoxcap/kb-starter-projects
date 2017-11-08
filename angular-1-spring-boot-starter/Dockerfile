FROM java:6
MAINTAINER Michael Hoffman <mhoffman@nvisia.com>
ARG PROJECT_NAME
ARG PROJECT_VERSION

# We'll assume that whatever spring profile is chosen will expose this port
EXPOSE 8080

# The build script splits the application into:
#     a fat layer with dependencies that don't often change
#     and a skinny layer with things that change every build
# This allows this Dockerfile to build new images that usually only
# have a new skinny layer, which can significantly reduce image storage needs 
COPY build/docker/fat-layer/ /app/
COPY build/docker/skinny-layer/ /app/

# Copy the ARGs into environment variables and set the entrypoint to echo these values before running
ENV PROJECT_NAME ${PROJECT_NAME:-unknown}
ENV PROJECT_VERSION ${PROJECT_VERSION:-unknown}
ENTRYPOINT echo "running: ${PROJECT_NAME}" \
	&& echo "version: ${PROJECT_VERSION}" \
	&& echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}" \
	&& /app/${PROJECT_NAME}/bin/${PROJECT_NAME}
