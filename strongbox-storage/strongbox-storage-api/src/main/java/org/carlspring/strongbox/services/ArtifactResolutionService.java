package org.carlspring.strongbox.services;

import org.carlspring.strongbox.storage.resolvers.LocationResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author mtodorov
 */
public interface ArtifactResolutionService
{

    InputStream getInputStream(String storageId,
                               String repositoryId,
                               String artifactPath)
            throws IOException;

    OutputStream getOutputStream(String storageId,
                                 String repositoryId,
                                 String artifactPath)
                    throws IOException;

    Map<String, LocationResolver> getResolvers();

}
