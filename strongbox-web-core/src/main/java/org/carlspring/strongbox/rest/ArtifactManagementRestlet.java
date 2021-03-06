package org.carlspring.strongbox.rest;

import org.apache.maven.artifact.Artifact;
import org.carlspring.strongbox.annotations.ArtifactExistenceState;
import org.carlspring.strongbox.annotations.ArtifactResource;
import org.carlspring.strongbox.annotations.ArtifactResourceMapper;
import org.carlspring.maven.commons.util.ArtifactUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Martin Todorov
 */
@Component
@Path("/manage/artifact")
@Deprecated
public class ArtifactManagementRestlet
        extends BaseRestlet
{

    private static final Logger logger = LoggerFactory.getLogger(ArtifactManagementRestlet.class);


    @POST
    @Path("/{repositoryId}/state/{state}/length/{length}/{path:.*}")
    @Consumes(MediaType.TEXT_PLAIN)
    public String addArtifact(@PathParam("repositoryId") String repositoryId,
                              @PathParam("state") String state,
                              @PathParam("length") long length,
                              @PathParam("path") String artifactPath)
            throws IOException
    {
        if (!ArtifactUtils.isArtifact(artifactPath))
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Artifact artifact = ArtifactUtils.convertPathToArtifact(artifactPath);

        ArtifactExistenceState existenceState = ArtifactExistenceState.valueOf(state);

        final ArtifactResource artifactResource = ArtifactResourceMapper.getArtifactResourceInstance(repositoryId,
                                                                                                     artifact,
                                                                                                     length,
                                                                                                     existenceState);

        ArtifactResourceMapper.addResource(artifactResource);

        final String msg = "Added artifact " + artifact.toString() +
                           " with state " + state +
                           " and length " + length +
                           " to repository " + repositoryId + ".";

        logger.debug(msg);

        return msg;
    }

    @DELETE
    @Path("{repositoryId}/{path:.*}")
    public void delete(@PathParam("repositoryId") String repositoryId,
                       @PathParam("path") String path)
            throws IOException
    {
        if (!ArtifactUtils.isArtifact(path))
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Artifact artifact = ArtifactUtils.convertPathToArtifact(path);
        ArtifactResourceMapper.removeResources(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());

        logger.debug("Removed artifact " + artifact.toString() + ".");
    }

}
